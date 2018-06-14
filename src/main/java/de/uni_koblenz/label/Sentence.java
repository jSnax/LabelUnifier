package de.uni_koblenz.label;

import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.uni_koblenz.cluster.GrammaticalRelationBetweenWords;
import de.uni_koblenz.enums.PartOfSpeechTypes;
import de.uni_koblenz.enums.PhraseStructureTypes;
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
import edu.stanford.nlp.coref.data.Dictionaries.Person;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;
import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;

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
	
	public Sentence() {
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
		
		RelationName[] subjects= {RelationName.SUBJECT,RelationName.NOMINAL_SUBJECT};
		RelationName[] objects= {RelationName.DIRECT_OBJECT,RelationName.OBJECT};

        // check if root of dependency tree is a verb
    	if(root.getPartOfSpeech().getJwnlType()==POS.VERB) {
    		// set root word as Action
    		root.setRole(RoleLeopold.ACTION);
    		


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
    	/*
    	 * HANDLE SENTENCES WITH TO BE (COPULA)
    	 */
    		
	    }else if(!root.getGrammaticalRelationsByName(RelationName.COPULA).isEmpty()) {
	    	// set business object
	    	if(root.getPartOfSpeech().getJwnlType()==POS.NOUN) {
	    		root.setRole(RoleLeopold.BUSINESS_OBJECT);
	    	}
	    	for(GrammaticalRelationBetweenWords relation:root.getGrammaticalRelations()) {
	    		//set business object
	    		if(relation.getGrammaticalRelationName()==RelationName.COMPOUND_MODIFIER&&root.getPartOfSpeech().getJwnlType()==POS.NOUN){
    				relation.getTargetWord().setRole(RoleLeopold.BUSINESS_OBJECT);
    			//set subject
	    		} else if(Arrays.asList(subjects).contains(relation.getGrammaticalRelationName())) {
    				relation.getTargetWord().setRole(RoleLeopold.SUBJECT);
    				// set compounds of the main subject also as subject
    				for(GrammaticalRelationBetweenWords relation2:relation.getTargetWord().getGrammaticalRelationsByName(RelationName.COMPOUND_MODIFIER)) {
    					relation2.getTargetWord().setRole(RoleLeopold.SUBJECT);
    				}
    			//set action
    			}else if(relation.getGrammaticalRelationName()==RelationName.COPULA){
    				relation.getTargetWord().setRole(RoleLeopold.ACTION);
    				for(GrammaticalRelationBetweenWords relation2:relation.getTargetWord().getGrammaticalRelationsByName(RelationName.PHRASAL_VERB_PARTICLE)) {
    					relation2.getTargetWord().setRole(RoleLeopold.ACTION);
    				}
    			}
	    	}
	    	
	    	
	    	
	    // Message, if no method worked
    	}else {
    		System.out.println("NO METHOD WORKED TO FIND ROLE for: " + asCoreSentence.text());
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
	
		public Phrase toPhrase(PhraseStructure Structure, Realiser realiser, SPhraseSpec p, NLGFactory nlgFactory){
			
			
			Sentence tempSentence = new Sentence();
			tempSentence.setWordsarray(this.getWordsarray());
			Phrase result = new Phrase();
			PhraseStructureTypes tempType;
			String tempString = "";
			int j = 0;
			NPPhraseSpec object = new NPPhraseSpec(nlgFactory);
			NPPhraseSpec subject = new NPPhraseSpec(nlgFactory);
			VPPhraseSpec verb = new VPPhraseSpec(nlgFactory);
			
			for (int i = 0; i < Structure.getElements().size(); i++){
				j = 0;
				tempType = Structure.getElements().get(i);
				switch(tempType){
				case DETERMINER_DEFINITEARTICLE:
					if (Structure.getElements().get(j+1).getdeterminer()=="Object"){
						object.setDeterminer("the");
					}
					if (Structure.getElements().get(j+1).getdeterminer()=="Subject"){
						subject.setDeterminer("the");
					}
					break;
				case DETERMINER_INDEFINITEARTICLE:
					if (Structure.getElements().get(j+1).getdeterminer()=="Object"){
						object.setDeterminer("a");
					}
					if (Structure.getElements().get(j+1).getdeterminer()=="Subject"){
						subject.setDeterminer("a");
					}
					break;
					// Incomplete: What about adjectives in front of Object / Subject? Needs better solution
					// Probably just a while loop until either subject or object is found
				case NOUN_PLURAL_OBJECT:
					while (tempString == "" && j < tempSentence.getWordsarray().size()){
						//old: if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.NOUN && tempSentence.getWordsarray().get(j).getRole().name() == "BUSINESS_OBJECT"){
						if (this.POSofTempWord(j) == POS.NOUN && this.RoleOfTempWord(j) == "BUSINESS_OBJECT"){
							//old: tempString = tempSentence.getWordsarray().get(j).getBaseform();
							tempString = this.BaseOfTempWord(j);
						}
						j++;
					}
					object = nlgFactory.createNounPhrase(tempString);
					object.setPlural(true);
					p.setObject(object);
					tempString = "";
					break;
				case NOUN_PLURAL_SUBJECT:
					while (tempString == "" && j < tempSentence.getWordsarray().size()){
						//old:	if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.NOUN && tempSentence.getWordsarray().get(j).getRole().name() == "SUBJECT"){
						if (this.POSofTempWord(j) == POS.NOUN && this.RoleOfTempWord(j) == "SUBJECT"){
							//old: tempString = tempSentence.getWordsarray().get(j).getBaseform();
							tempString = this.BaseOfTempWord(j);
						}
						j++;
					}
					subject = nlgFactory.createNounPhrase(tempString);
					subject.setPlural(true);
					p.setSubject(subject);
					tempString = "";
					break;
				case NOUN_SINGULAR_OBJECT:
					while (tempString == "" && j < tempSentence.getWordsarray().size()){
						//old: if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.NOUN && tempSentence.getWordsarray().get(j).getRole().name() == "BUSINESS_OBJECT"){
						if (this.POSofTempWord(j) == POS.NOUN && this.RoleOfTempWord(j) == "BUSINESS_OBJECT"){
						//old:	tempString = tempSentence.getWordsarray().get(j).getBaseform();
							tempString = this.BaseOfTempWord(j);
						}
						j++;
					}
					object = nlgFactory.createNounPhrase(tempString);
					p.setObject(object);
					tempString = "";
					break;
				case NOUN_SINGULAR_SUBJECT:
					while (tempString == "" && j < tempSentence.getWordsarray().size()){
					//old:	if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.NOUN && tempSentence.getWordsarray().get(j).getRole().name() == "SUBJECT"){
						if (this.POSofTempWord(j) == POS.NOUN && this.RoleOfTempWord(j) == "SUBJECT"){
						//old:	tempString = tempSentence.getWordsarray().get(j).getBaseform();
						tempString = this.BaseOfTempWord(j);
						}
						j++;
					}
					subject = nlgFactory.createNounPhrase(tempString);
					p.setSubject(subject);
					tempString = "";
					break;
				case PUNCTUATION_COMMA:
					break;
				case PUNCTUATION_PERIOD:
					break;
				case PUNCTUATION_QUESTIONMARK:
					break;
				case VERB_BASE:
					while (tempString == "" && j < tempSentence.getWordsarray().size()){
					//old:	if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.VERB && tempSentence.getWordsarray().get(j).getRole().name() == "ACTION"){
						if (this.POSofTempWord(j) == POS.VERB && this.RoleOfTempWord(j) == "ACTION"){
						//old:	tempString = tempSentence.getWordsarray().get(j).getBaseform();
							tempString = this.BaseOfTempWord(j);
						}
						j++;
					}
					verb = nlgFactory.createVerbPhrase(tempString);
			        p.setVerb(verb);
					tempString = "";
					break;
				case VERB_IMPERATIVE:
					while (tempString == "" && j < tempSentence.getWordsarray().size()){
						//old: if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.VERB && tempSentence.getWordsarray().get(j).getRole().name() == "ACTION"){
						if (this.POSofTempWord(j) == POS.VERB && this.RoleOfTempWord(j) == "ACTION"){
						//old: 	tempString = tempSentence.getWordsarray().get(j).getBaseform();
						tempString = this.BaseOfTempWord(j);
						}
						j++;
					}
					verb = nlgFactory.createVerbPhrase(tempString);
					verb.setFeature(Feature.FORM,simplenlg.features.Form.IMPERATIVE);;
					p.setVerb(verb);
					tempString = "";
					break;
				case VERB_PRESENT_PARTICIPLE:
					while (tempString == "" && j < tempSentence.getWordsarray().size()){
						//old: if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.VERB && tempSentence.getWordsarray().get(j).getRole().name() == "ACTION"){
						if (this.POSofTempWord(j) == POS.VERB && this.RoleOfTempWord(j) == "ACTION"){
						//old:	tempString = tempSentence.getWordsarray().get(j).getBaseform();
							tempString = this.BaseOfTempWord(j);
						
						}
						j++;
					}
					verb = nlgFactory.createVerbPhrase(tempString);
					verb.setFeature(Feature.FORM, simplenlg.features.Form.PRESENT_PARTICIPLE);
					p.setVerb(verb);
					tempString = "";
					break;
				case VERB_PASSIVE:
					while (tempString == "" && j < tempSentence.getWordsarray().size()){
					//old:	if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.VERB && tempSentence.getWordsarray().get(j).getRole().name() == "ACTION"){
						if (this.POSofTempWord(j) == POS.VERB && this.RoleOfTempWord(j) == "ACTION"){
						//old:	tempString = tempSentence.getWordsarray().get(j).getBaseform();
						tempString = this.BaseOfTempWord(j);
						}
						j++;
					}
					verb = nlgFactory.createVerbPhrase(tempString);
					verb.setFeature(Feature.PASSIVE, true);
					p.setVerb(verb);
					tempString = "";
					break;
				case VERB_INDICATIVE:
					break;
				case VERB_SIMPLEPAST:
					break;
				case VERB_SIMPLEPRESENT:
					break;
				default:
					break;

				}
			}
			result.setFullContent(realiser.realiseSentence(p));
			List<String> wordList = new ArrayList<String>(Arrays.asList(result.getFullContent().split(" ")));
			for (int i = 0; i < wordList.size(); i++){
				if (wordList.get(i).contains(",") || wordList.get(i).contains(".")){
					wordList.set(i, wordList.get(i).substring(0, wordList.get(i).length()-1));
				}
			}
			result.setseparatedContent(wordList);
			return(result);
		}
		
//new methods for shortening
public POS POSofTempWord(int i){
    return(this.getWordsarray().get(i).getPartOfSpeech().getJwnlType());
         }

public String RoleOfTempWord(int i){
    return(this.getWordsarray().get(i).getRole().name());
         }

public String BaseOfTempWord(int i){
    return(this.getWordsarray().get(i).getBaseform());
         }		
	
}
