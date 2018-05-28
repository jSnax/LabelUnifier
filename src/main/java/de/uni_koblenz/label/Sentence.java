package de.uni_koblenz.label;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.uni_koblenz.cluster.GrammaticalRelationBetweenWords;
import de.uni_koblenz.enums.RelationName;
import de.uni_koblenz.enums.RoleLeopold;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.POS;

public class Sentence {
	
	private List<Word> wordsarray = new ArrayList<Word>();
	private CoreSentence asCoreSentence;
	private String contentAsString;
	
	public Sentence(CoreSentence sentence) throws JWNLException {
		contentAsString=sentence.text();
		asCoreSentence=sentence;
		createWordsArray();
	}
	
	public List<Word> getWordsarray() {
		return wordsarray;
	}



	public void setWordsarray(List<Word> wordsarray) {
		this.wordsarray = wordsarray;
	}



	public CoreSentence getAsCoreSentence() {
		return asCoreSentence;
	}



	public void setAsCoreSentence(CoreSentence asCoreSentence) {
		this.asCoreSentence = asCoreSentence;
	}



	public String getContentAsString() {
		return contentAsString;
	}



	public void setContentAsString(String contentAsString) {
		this.contentAsString = contentAsString;
	}



	/*
	 * method to convert a String into a Word-array
	 */ 
	public void createWordsArray() throws JWNLException{
		// create words
		List<CoreLabel> tokens = asCoreSentence.tokens();
		for (CoreLabel token:tokens) {
			//create words and put them in a temporary list (easier to work with index)
			wordsarray.add(new Word(token));
			
		}
		//create grammatical relations and add them
		SemanticGraph graph=asCoreSentence.dependencyParse();
		//go through all grammatical relations of the current sentence
	    for(SemanticGraphEdge edge:graph.edgeIterable()) {
	    	//get the Word equivalent of the tokens
	    	Word sourceWord=wordsarray.get(edge.getSource().index()-1);
	    	Word targetWord=wordsarray.get(edge.getTarget().index()-1);
	    	// get RelationName Enum from String
	    	RelationName relationName=null;
	    	try {
	    		relationName=RelationName.valueOf(edge.getRelation().getLongName().toUpperCase().replace(' ', '_'));
	    	}catch(IllegalArgumentException e){
	    		relationName=null;
	    	}finally {
	    		
	    	}
	    	//create GrammaticalRelation
	    	GrammaticalRelationBetweenWords grammaticalRelationTemp= new GrammaticalRelationBetweenWords(sourceWord,targetWord,relationName);
	    	//store grammatical Relation in both words
	    	sourceWord.getGrammaticalRelations().add(grammaticalRelationTemp);
	    	targetWord.getGrammaticalRelations().add(grammaticalRelationTemp);
	    }
	    //define Role
	    // check if OpenIE triple got created
	    
	    // get information from dependency tree root
	    if(asCoreSentence.coreMap().get(NaturalLogicAnnotations.RelationTriplesAnnotation.class).isEmpty()) {
	    	// get root as Word
    		Word root=wordsarray.get(asCoreSentence.dependencyParse().getFirstRoot().index()-1);


	        // check if root of dependency tree is a verb
	    	if(root.getPartOfSpeech().getJwnlType()==POS.VERB) {
	    		// set root word as Action
	    		root.setRole(RoleLeopold.ACTION);
	    		// set business object and subject
	    		RelationName[] subjects= {RelationName.SUBJECT,RelationName.NOMINAL_SUBJECT};
	    		RelationName[] objects= {RelationName.DIRECT_OBJECT,RelationName.OBJECT};
	    		for(GrammaticalRelationBetweenWords relation:root.getGrammaticalRelations()) {
	    			//set business object
	    			if(Arrays.asList(objects).contains(relation.getGrammaticalRelationName())) {
	    				relation.getTargetWord().setRole(RoleLeopold.BUSINESS_OBJECT);
	    			}
	    			//set subject
	    			if(Arrays.asList(subjects).contains(relation.getGrammaticalRelationName())) {
	    				relation.getTargetWord().setRole(null);
	    			}
	    		}
	    		
	    	// if root is no verb	
	    	}else {
	    		System.out.println("ROLE: NO VERB AT ROOT for: " + asCoreSentence.text());
	    	}
	    // get information from OpenIE triple
	    }else {
	    	for(CoreLabel tripletoken : asCoreSentence.coreMap().get(NaturalLogicAnnotations.RelationTriplesAnnotation.class).iterator().next().relation) {
				wordsarray.get(tripletoken.index()-1).setRole(RoleLeopold.ACTION);
			}
	    	for(CoreLabel tripletoken : asCoreSentence.coreMap().get(NaturalLogicAnnotations.RelationTriplesAnnotation.class).iterator().next().object) {
				wordsarray.get(tripletoken.index()-1).setRole(RoleLeopold.BUSINESS_OBJECT);
			}
	    	for(CoreLabel tripletoken : asCoreSentence.coreMap().get(NaturalLogicAnnotations.RelationTriplesAnnotation.class).iterator().next().subject) {
				wordsarray.get(tripletoken.index()-1).setRole(null);
			}

	    }
			
		
	}
	
	@Override
	public String toString() {
		String result="Sentence:\n";
		for (Word word:wordsarray) {
			result+=word.toString()+"\n";
			
		}
		return result;
	}
	
}
