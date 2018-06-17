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
		// stores Words that have to be removed like symbols or compounds
    	List<Word> toRemove = new ArrayList<Word>();
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
	    	/*
	    	 *  COMBINES COMPOUND WORDS TO ONE WORD
	    	 */
	    	if(relationName==RelationName.COMPOUND_MODIFIER||relationName==RelationName.PHRASAL_VERB_PARTICLE) {
	    		toRemove.add(targetWord);
	    		if(edge.getSource().index()>edge.getTarget().index()) {
	    			sourceWord.setBaseform(targetWord.getBaseform()+" "+sourceWord.getBaseform());
	    			sourceWord.setOriginalForm(targetWord.getOriginalForm()+" "+sourceWord.getOriginalForm());
	    		}else {
	    			sourceWord.setBaseform(sourceWord.getBaseform()+" "+targetWord.getBaseform());
	    			sourceWord.setOriginalForm(sourceWord.getOriginalForm()+" "+targetWord.getOriginalForm());
	    		}
	    	/*
	    	 *  Removing Punctuation
	    	 */
	    	}else if(relationName==RelationName.PUNCTUATION) {
	    		toRemove.add(targetWord);
	    		
	    	}else {
		    	//create GrammaticalRelation
		    	GrammaticalRelationBetweenWords grammaticalRelationTemp= new GrammaticalRelationBetweenWords(sourceWord,targetWord,relationName);
		    	//store grammatical Relation in both words
		    	sourceWord.getGrammaticalRelations().add(grammaticalRelationTemp);
		    	targetWord.getGrammaticalRelations().add(grammaticalRelationTemp);
		    }
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
    			
    			//set business object
    			if(Arrays.asList(objects).contains(relation.getGrammaticalRelationName())) {
    				relation.getTargetWord().setRole(RoleLeopold.BUSINESS_OBJECT);
    			}
    			//set subject
    			else if(Arrays.asList(subjects).contains(relation.getGrammaticalRelationName())) {
    				relation.getTargetWord().setRole(RoleLeopold.SUBJECT);
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
    			//set subject
	    		if(Arrays.asList(subjects).contains(relation.getGrammaticalRelationName())) {
    				relation.getTargetWord().setRole(RoleLeopold.SUBJECT);
    			//set action
    			}else if(relation.getGrammaticalRelationName()==RelationName.COPULA){
    				relation.getTargetWord().setRole(RoleLeopold.ACTION);
    			}
	    	}
	    	
	    	
	    	
	    // Message, if no method worked
    	}else {
    		System.out.println("NO METHOD WORKED TO FIND ROLE for: " + asCoreSentence.text());
    	}
    	
    	
    	/*
    	 *  IMPORTANT THIS HAS TO BE ON THE END OF THE CONSTRUCTOR (PreProcessing)
    	 */
	    // removing all unused WORDs from wordsarray like punctuation and compound words
    	// at this point because other methods rely on the fact that the index of corenlpsentence equals wordsarray 
	    wordsarray.removeAll(toRemove);
    		
		
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
			boolean passive = false;
			for (int i = 0; i < Structure.getElements().size(); i++){
				j = 0;
				boolean found = false;
				int iterator = i;
				tempType = Structure.getElements().get(i);
				switch(tempType){
				case DETERMINER_DEFINITEARTICLE:
					while (iterator < Structure.getElements().size() -1 && !found){
						if (Structure.getElements().get(iterator+1).getdeterminer()=="Object"){
							object.setDeterminer("the");
							found = true;
						}
						if (Structure.getElements().get(iterator+1).getdeterminer()=="Subject"){
							subject.setDeterminer("the");
							found = true;
						}
						iterator++;
					}
					break;
				case DETERMINER_INDEFINITEARTICLE:
					while (iterator < Structure.getElements().size() -1 && !found){
						if (Structure.getElements().get(iterator+1).getdeterminer()=="Object"){
							object.setDeterminer("a");
							found = true;
						}
						if (Structure.getElements().get(iterator+1).getdeterminer()=="Subject"){
							subject.setDeterminer("a");
							found = true;
						}
						iterator++;
					}
					break;
				case NOUN_PLURAL_OBJECT:
					while (tempString == "" && j < tempSentence.getWordsarray().size()){
						//old: if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.NOUN && tempSentence.getWordsarray().get(j).getRole().name() == "BUSINESS_OBJECT"){
						if (this.POSofTempWord(j) == "NNS" && this.RoleOfTempWord(j) == "BUSINESS_OBJECT"){
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
						if (this.POSofTempWord(j) == "NNS" && this.RoleOfTempWord(j) == "SUBJECT"){
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
						if (this.POSofTempWord(j) == "NN" && this.RoleOfTempWord(j) == "BUSINESS_OBJECT"){
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
						if (this.POSofTempWord(j) == "NN" && this.RoleOfTempWord(j) == "SUBJECT"){
						//old:	tempString = tempSentence.getWordsarray().get(j).getBaseform();
						tempString = this.BaseOfTempWord(j);
						}
						j++;
					}
					subject = nlgFactory.createNounPhrase(tempString);
					p.setSubject(subject);
					tempString = "";
					break;
				case PUNCTUATION_PERIOD:
					break;
				case PUNCTUATION_QUESTIONMARK:
					break;
				case VERB_BASE:
					while (tempString == "" && j < tempSentence.getWordsarray().size()){
					//old:	if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.VERB && tempSentence.getWordsarray().get(j).getRole().name() == "ACTION"){
						if (this.jwnlPOSofTempWord(j) == POS.VERB && this.RoleOfTempWord(j) == "ACTION"){
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
						if (this.jwnlPOSofTempWord(j) == POS.VERB && this.RoleOfTempWord(j) == "ACTION"){
						//old: 	tempString = tempSentence.getWordsarray().get(j).getBaseform();
						tempString = this.BaseOfTempWord(j);
						}
						j++;
					}
					verb = nlgFactory.createVerbPhrase(tempString);
					verb.setFeature(Feature.FORM,simplenlg.features.Form.IMPERATIVE);
					p.setVerb(verb);
					tempString = "";
					break;
				case VERB_PRESENT_PARTICIPLE:
					while (tempString == "" && j < tempSentence.getWordsarray().size()){
						//old: if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.VERB && tempSentence.getWordsarray().get(j).getRole().name() == "ACTION"){
						if (this.jwnlPOSofTempWord(j) == POS.VERB && this.RoleOfTempWord(j) == "ACTION"){
						//old:	tempString = tempSentence.getWordsarray().get(j).getBaseform();
							tempString = this.BaseOfTempWord(j);
						
						}
						j++;
					}
					verb = nlgFactory.createVerbPhrase(tempString);
					p.setFeature(Feature.FORM, simplenlg.features.Form.PRESENT_PARTICIPLE);
					p.setVerb(verb);
					tempString = "";
					break;
				case VERB_PASSIVE:
					while (tempString == "" && j < tempSentence.getWordsarray().size()){
					//old:	if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.VERB && tempSentence.getWordsarray().get(j).getRole().name() == "ACTION"){
						if (this.jwnlPOSofTempWord(j) == POS.VERB && this.RoleOfTempWord(j) == "ACTION"){
						//old:	tempString = tempSentence.getWordsarray().get(j).getBaseform();
						tempString = this.BaseOfTempWord(j);
						}
						j++;
					}
					verb = nlgFactory.createVerbPhrase(tempString);
					p.setFeature(Feature.PASSIVE, true);
					p.setVerb(verb);
					tempString = "";
					passive = true;
					break;
				case VERB_SIMPLEPAST:
					while (tempString == "" && j < tempSentence.getWordsarray().size()){
					//old:	if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.VERB && tempSentence.getWordsarray().get(j).getRole().name() == "ACTION"){
						if (this.jwnlPOSofTempWord(j) == POS.VERB && this.RoleOfTempWord(j) == "ACTION"){
						//old:	tempString = tempSentence.getWordsarray().get(j).getBaseform();
						tempString = this.BaseOfTempWord(j);
						}
						j++;
					}
					verb = nlgFactory.createVerbPhrase(tempString);
					p.setVerb(verb);
					p.setFeature(Feature.TENSE, Tense.PAST);
					tempString = "";
					break;
				case VERB_SIMPLEFUTURE:
					while (tempString == "" && j < tempSentence.getWordsarray().size()){
						if (this.jwnlPOSofTempWord(j) == POS.VERB && this.RoleOfTempWord(j) == "ACTION"){
						tempString = this.BaseOfTempWord(j);
						}
						j++;
					}
					p.setVerb(tempString);
					// Unlike other cases since future only works like this, else you get "will will"
					p.setFeature(Feature.TENSE, Tense.FUTURE);
					tempString = "";
					break;
				case ADJECTIVE_FOR_OBJECT:
					while (tempString == "" && j < tempSentence.getWordsarray().size()){
						if (this.jwnlPOSofTempWord(j) == POS.ADJECTIVE && this.RoleOfTempWord(j) == "OPTIONAL_INFORMATION_FRAGMENT"){
						tempString = this.BaseOfTempWord(j);
						}
						j++;
					}
					object.addModifier(tempString);
					break;
				case ADJECTIVE_FOR_SUBJECT:
					while (tempString == "" && j < tempSentence.getWordsarray().size()){
						if (this.jwnlPOSofTempWord(j) == POS.ADJECTIVE && this.RoleOfTempWord(j) == "OPTIONAL_INFORMATION_FRAGMENT"){
						tempString = this.BaseOfTempWord(j);
						}
						j++;
					}
					subject.addModifier(tempString);
					break;
				case ADVERB:
					while (tempString == "" && j < tempSentence.getWordsarray().size()){
						if (this.jwnlPOSofTempWord(j) == POS.ADVERB && this.RoleOfTempWord(j) == "OPTIONAL_INFORMATION_FRAGMENT"){
						tempString = this.BaseOfTempWord(j);
						}
						j++;
					}
					p.addModifier(tempString);
					break;
				default:
					break;

				}
			}
			if (passive){
				NPPhraseSpec switcher = new NPPhraseSpec(nlgFactory);
				switcher = subject;
				subject = object;
				object = switcher;
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
public String POSofTempWord(int i){
	return(this.getWordsarray().get(i).getPartOfSpeech().getShortType());
}
		
		
public POS jwnlPOSofTempWord(int i){
	return(this.getWordsarray().get(i).getPartOfSpeech().getJwnlType());
}

public String RoleOfTempWord(int i){
	return(this.getWordsarray().get(i).getRole().name());
}

public String BaseOfTempWord(int i){
	return(this.getWordsarray().get(i).getBaseform());
}		
	
}
