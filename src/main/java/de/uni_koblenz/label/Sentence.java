package de.uni_koblenz.label;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.uni_koblenz.cluster.GrammaticalRelationBetweenWords;
import de.uni_koblenz.enums.PartOfSpeechTypes;
import de.uni_koblenz.enums.RelationName;
import de.uni_koblenz.enums.RoleLeopold;
import de.uni_koblenz.phrase.Phrase;
import de.uni_koblenz.phrase.PhraseStructure;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.POS;
import simplenlg.framework.NLGFactory;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

public class Sentence {
	
	private List<Word> wordsarray = new ArrayList<Word>();
	private CoreSentence asCoreSentence;
	private String contentAsString;
	public Phrase toPhrase;
	
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
	 * method to create wordsarray from CoreSentence
	 */ 
	public void createWordsArray() throws JWNLException{
		// create words
		List<CoreLabel> tokens = asCoreSentence.tokens();
		for (CoreLabel token:tokens) {
			wordsarray.add(new Word(token));
			
		}
		/*
		 * create grammatical relations and add them
		 */
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
	    /*
	     * define Role
	     */

	    
	    // get information from dependency tree root
    	// get root as Word
		Word root=wordsarray.get(asCoreSentence.dependencyParse().getFirstRoot().index()-1);


        // check if root of dependency tree is a verb
    	if(root.getPartOfSpeech().getJwnlType()==POS.VERB) {
    		// set root word as Action
    		root.setRole(RoleLeopold.ACTION);
    		
    		RelationName[] subjects= {RelationName.SUBJECT,RelationName.NOMINAL_SUBJECT};
    		RelationName[] objects= {RelationName.DIRECT_OBJECT,RelationName.OBJECT};

    		for(GrammaticalRelationBetweenWords relation:root.getGrammaticalRelations()) {
    			// set PHRASAL_VERB_PARTICLE also as action e.g. "go down"
    			if(relation.getGrammaticalRelationName()==RelationName.PHRASAL_VERB_PARTICLE) {
    				relation.getTargetWord().setRole(RoleLeopold.ACTION);
    			}
    			//set business object
    			else if(Arrays.asList(objects).contains(relation.getGrammaticalRelationName())) {
    				relation.getTargetWord().setRole(RoleLeopold.BUSINESS_OBJECT);
    				// set compounds of the main object also as object
    				for(GrammaticalRelationBetweenWords relation2:relation.getTargetWord().getGrammaticalRelationsByName(RelationName.COMPOUND_MODIFIER)) {
        				relation2.getTargetWord().setRole(RoleLeopold.BUSINESS_OBJECT);				
    				}
    			}
    			//set subject
    			else if(Arrays.asList(subjects).contains(relation.getGrammaticalRelationName())) {
    				relation.getTargetWord().setRole(RoleLeopold.SUBJECT);
    				// set compounds of the main subject also as subject
    				for(GrammaticalRelationBetweenWords relation2:relation.getTargetWord().getGrammaticalRelationsByName(RelationName.COMPOUND_MODIFIER)) {
    					relation2.getTargetWord().setRole(RoleLeopold.SUBJECT);
    				}
    			}
    		}
    		
	    // if root is no verb	
	    // get information from OpenIE triple
	    }else {
    		String consoleoutput="ROLE: NO VERB AT ROOT for: " + asCoreSentence.text();
    		if(!(asCoreSentence.coreMap().get(NaturalLogicAnnotations.RelationTriplesAnnotation.class).isEmpty())) {
		    	for(CoreLabel tripletoken : asCoreSentence.coreMap().get(NaturalLogicAnnotations.RelationTriplesAnnotation.class).iterator().next().relation) {
					wordsarray.get(tripletoken.index()-1).setRole(RoleLeopold.ACTION);
				}
		    	for(CoreLabel tripletoken : asCoreSentence.coreMap().get(NaturalLogicAnnotations.RelationTriplesAnnotation.class).iterator().next().object) {
					wordsarray.get(tripletoken.index()-1).setRole(RoleLeopold.BUSINESS_OBJECT);
				}
		    	for(CoreLabel tripletoken : asCoreSentence.coreMap().get(NaturalLogicAnnotations.RelationTriplesAnnotation.class).iterator().next().subject) {
					wordsarray.get(tripletoken.index()-1).setRole(RoleLeopold.SUBJECT);
		    	}
    		}else {
    			consoleoutput+=" #ALTERNATIVE METHOD FAILED#";
    		}
    		System.out.println(consoleoutput);

	    }
			
		
	}
	// HAS TO BE CHANGED TO PHRASE STRUCTURE INSTEAD OF STRUCTLIST ULTIMATELY
	public Phrase toPhrase(List<PartOfSpeechTypes> StructList, Realiser realiser, SPhraseSpec p, NLGFactory nlgFactory){
		Phrase result = new Phrase();
		for (int i = 0; i < StructList.size(); i++){
			
		}
		return(null);
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
