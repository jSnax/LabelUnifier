package de.uni_koblenz.label;

import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.uni_koblenz.cluster.GrammaticalRelationBetweenWords;
import de.uni_koblenz.enums.PartOfSpeechTypes;
import de.uni_koblenz.enums.PhraseStructureTypes;
import de.uni_koblenz.enums.RelationName;
import de.uni_koblenz.enums.RoleLeopold;
import de.uni_koblenz.phrase.Phrase;
import de.uni_koblenz.phrase.PhraseStructure;
import de.uni_koblenz.phrase.PhraseStructureList;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
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
import simplenlg.realiser.english.Realiser;
import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
import java.util.Comparator;

public class Sentence implements java.io.Serializable{
	
	private List<Word> wordsarray = new ArrayList<Word>();
	private transient CoreSentence asCoreSentence;
	private String contentAsString;
	public ArrayList<Phrase> possiblePhrases;
	
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



	public List<Phrase> getPossiblePhrases() {
		return possiblePhrases;
	}

	public void setPossiblePhrases(ArrayList<Phrase> possiblePhrases) {
		this.possiblePhrases = possiblePhrases;
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
	 * method to check whether a sentence is given in passive form
	 */
	public static boolean isPassive(List<Word> w){

		for (int i = 0; i < w.size(); i++) {
			for (int j = 0; j < w.get(i).getGrammaticalRelations().size(); j++) {	
				if(w.get(i).getGrammaticalRelations().get(j).getGrammaticalRelationName()!=null) {
					
					if(w.get(i).getGrammaticalRelations().get(j).getGrammaticalRelationName().equals(RelationName.NOMINAL_PASSIVE_SUBJECT)
						|| w.get(i).getGrammaticalRelations().get(j).getGrammaticalRelationName().equals(RelationName.CONTROLLING_NOMINAL_PASSIVE_SUBJECT)) 
						return true;
					}
				}
			}
		return false;		
	}
	
	/*
	 * method to switch the subject with the business object if the sentence is in a passive form
	 */
	public static void passiveHandling(List<Word> w) {
	
		for(int i = 0; i < w.size(); i++) {
			
			if(isPassive(w)==true) {
			
				if((w.get(i).getPartOfSpeech()!=null)
						&& (w.get(i).getPartOfSpeech().getJwnlType()==POS.NOUN || w.get(i).getPartOfSpeech()==PartOfSpeechTypes.PERSONAL_PRONOUN)
						&& w.get(i).getRole().equals(RoleLeopold.BUSINESS_OBJECT)){

					if(i==1) {
						if(w.get(i-1).getOriginalForm().equals("by")) {
							w.get(i).setRole(RoleLeopold.SUBJECT); }
					
					}else if(i==2) {
						if(w.get(i-2).getOriginalForm().equals("by")|| w.get(i-1).getOriginalForm().equals("by")) {
							w.get(i).setRole(RoleLeopold.SUBJECT); }
					
					}else if(i>=3) {
						if(w.get(i-3).getOriginalForm().equals("by") || w.get(i-2).getOriginalForm().equals("by") || w.get(i-1).getOriginalForm().equals("by")){
							w.get(i).setRole(RoleLeopold.SUBJECT); }
						if(w.get(i-2).getRole().equals(RoleLeopold.SUBJECT) || w.get(i-1).getRole().equals(RoleLeopold.SUBJECT)) {
							w.get(i).setRole(RoleLeopold.OPTIONAL_INFORMATION_FRAGMENT); }
						
					}else{
						if(w.get(i).getOriginalForm().equals("by") && w.get(i+1).getPartOfSpeech().getJwnlType()==POS.NOUN) {
							w.get(i+1).setRole(RoleLeopold.SUBJECT);
						}else if(w.get(i).getOriginalForm().equals("by") && w.get(i+2).getPartOfSpeech().getJwnlType()==POS.NOUN) {
							w.get(i+1).setRole(RoleLeopold.SUBJECT); }
					}	
				}
			}
		}
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
	    		//TODO
	    		/*
	    		 *  Replace Temporary method that uses string manipulation with a more elegant solution.
	    		 */
	    		List<IndexedWord> tempVertices=new ArrayList<IndexedWord>(); 
	    		tempVertices.addAll(graph.getChildrenWithReln(edge.getSource(), edge.getRelation()));
	    		tempVertices.add(edge.getSource());
	    		System.out.println("unsorted");
	    		System.out.println(tempVertices);
	    		Collections.sort(tempVertices, new Comparator<IndexedWord>(){
					public int compare(IndexedWord indexword1, IndexedWord indexword2) {
						return indexword1.index()-indexword2.index();
					}
	    		});
	    		System.out.println("sorted");
	    		System.out.println(tempVertices);
	    		
	    		
	    		
	    		if(edge.getSource().index()>edge.getTarget().index()&&!sourceWord.getBaseform().contains(" ")) {
	    			sourceWord.setBaseform(targetWord.getBaseform()+" "+sourceWord.getBaseform());
	    			sourceWord.setOriginalForm(targetWord.getOriginalForm()+" "+sourceWord.getOriginalForm());
	    		}else if(edge.getSource().index()>edge.getTarget().index()) {
	    			sourceWord.setBaseform(sourceWord.getBaseform().substring(0, sourceWord.getBaseform().lastIndexOf(" ")+1)+targetWord.getBaseform()+" "+sourceWord.getBaseform().substring(sourceWord.getBaseform().lastIndexOf(" ")+1));
	    			sourceWord.setOriginalForm(sourceWord.getOriginalForm().substring(0, sourceWord.getOriginalForm().lastIndexOf(" ")+1)+targetWord.getOriginalForm()+" "+sourceWord.getOriginalForm().substring(sourceWord.getOriginalForm().lastIndexOf(" ")+1));
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
		
		RelationName[] subjects= {RelationName.SUBJECT,RelationName.NOMINAL_SUBJECT,RelationName.NOMINAL_PASSIVE_SUBJECT,RelationName.CONTROLLING_NOMINAL_PASSIVE_SUBJECT};
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
	    
	    //redefining role for passive sentences
	    passiveHandling(wordsarray);
    		
	}
	
	@Override
	public String toString() {
		String result="Sentence:\n";
		for (Word word:wordsarray) {
			result+=word.toString()+"\n";
			
		}
		return result;
	}
	
		public void toPhrase(PhraseStructureList StructureList, Realiser realiser, NLGFactory nlgFactory){
			// This function creates Phrases according to PhraseStructures, but only if they fit the Sentence
			Sentence tempSentence = new Sentence();
			tempSentence.setWordsarray(this.getWordsarray());
			ArrayList<Phrase> result = new ArrayList<Phrase>();
			PhraseStructureTypes tempType;
			List<PhraseStructure> allStructures = StructureList.getAllStructures();
			String tempString = "";
			int j = 0;
			boolean passive;
			boolean error;
			boolean prepositionFound;
			int counter = 0;
			boolean hasFittingStructure = false;
			int maximumStructureUsed = 0;
			// Creating lots of variables that will be used later in this function
			while ((counter < allStructures.size()) && (allStructures.get(counter).getElements().size() >= maximumStructureUsed)){
				int subjectPosition = 500;
				int objectPosition = 500;
				int prepObjectPosition = 500;
				// This while loop stops when either all possible PhraseStructures were tried out for the current Sentence
				// or when a fitting PhraseStructure was already found and all remaining PhraseStructures are of lesser size
				for (int usageCounter = 0; usageCounter < this.getWordsarray().size(); usageCounter++){
					this.getWordsarray().get(usageCounter).setAlreadyUsedForStructure(false);
				}
				// In toPhrase, words from a Sentence that are deemed mandatory for the currently tested PhraseStructure
				// are marked as "being used." This for-loop removes all those marks from the previous iteration 
				SPhraseSpec p = nlgFactory.createClause();
				NPPhraseSpec object = new NPPhraseSpec(nlgFactory);
				NPPhraseSpec subject = new NPPhraseSpec(nlgFactory);
				NPPhraseSpec prepositionalObject = new NPPhraseSpec(nlgFactory);
				VPPhraseSpec verb = new VPPhraseSpec(nlgFactory);
		        PPPhraseSpec preposition = nlgFactory.createPrepositionPhrase();
				PhraseStructure Structure = allStructures.get(counter);
				int i = 0;
				passive = false;
				error = false;
				prepositionFound = false;
				// Creating lots of variables that will be used later in this function
				while (i < Structure.getElements().size() && !error){ 
					j = 0;
					// j refers to the position of the word that's currently being looked at in the Sentence
					boolean found = false;
					// found refers to determiners, i.e. articles. It is false while the part of speech that the determiner refers to has not yet been identified
					int iterator = i;
					// iterator refers to the position of the PhraseStructureType that's currently being looked at in the PhraseStructure
					tempType = Structure.getElements().get(i);
					// Shorthandle variable for the current PhraseStructureType
					switch(tempType){
					// In this switch case, each PhraseStructureType is handled differently. Explanations for similiarily handled Types
					// are found in the first case they appear
					case DetDef:
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
						// This while-loop checks whether the Article is supposed to be attached to a Subject or an Object
						if (!found) error = true;
						break;
					case DetIndef:
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
						if (!found) error = true;
						break;
					case NPO:
						while (tempString == "" && j < tempSentence.getWordsarray().size()){
							//old: if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.NOUN && tempSentence.getWordsarray().get(j).getRole().name() == "BUSINESS_OBJECT"){
							if ((this.POSofTempWord(j) == "NNS" || this.POSofTempWord(j) == "NNPS") && this.RoleOfTempWord(j) == "BUSINESS_OBJECT" && this.getWordsarray().get(j).getAlreadyUsedForStructure() == false){
								//old: tempString = tempSentence.getWordsarray().get(j).getBaseform();
								tempString = this.BaseOfTempWord(j);
								objectPosition = j;
								this.getWordsarray().get(j).setAlreadyUsedForStructure(true);
							}
							j++;
						}
						// This while-loop checks whether any word left in the Sentence being looked at fulfills three conditions:
						// 1. It is a Noun in plural [NNS or NNPS, see PhraseStructureTypes for clarity]
						// 2.It's role is "Business Object"
						// 3. It was not already marked as used
						object = nlgFactory.createNounPhrase(tempString);
						object.setPlural(true);
						p.setObject(object);
						if (tempString == "") error = true;
						// Here, the needed variables for the SimpleNLG-engine are set. Also, if the while-loop above did not successfully 
						// find a word that matches the three criteria above, "error" is set to true which will skip to the next PhraseStructure
						break;
					case NPS:
						while (tempString == "" && j < tempSentence.getWordsarray().size()){
							//old:	if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.NOUN && tempSentence.getWordsarray().get(j).getRole().name() == "SUBJECT"){
							if ((this.POSofTempWord(j) == "NNS" || this.POSofTempWord(j) == "NNPS") && this.RoleOfTempWord(j) == "SUBJECT"  && this.getWordsarray().get(j).getAlreadyUsedForStructure() == false){
								//old: tempString = tempSentence.getWordsarray().get(j).getBaseform();
								tempString = this.BaseOfTempWord(j);
								subjectPosition = j;
								this.getWordsarray().get(j).setAlreadyUsedForStructure(true);
							}
							j++;
						}
						subject = nlgFactory.createNounPhrase(tempString);
						subject.setPlural(true);
						p.setSubject(subject);
						if (tempString == "") error = true;
						// See Noun Plural Object
						break;
					case NSO:
						while (tempString == "" && j < tempSentence.getWordsarray().size()){
							//old: if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.NOUN && tempSentence.getWordsarray().get(j).getRole().name() == "BUSINESS_OBJECT"){
							if ((this.POSofTempWord(j) == "NN" || this.POSofTempWord(j) == "NNP") && this.RoleOfTempWord(j) == "BUSINESS_OBJECT"  && this.getWordsarray().get(j).getAlreadyUsedForStructure() == false){
							//old:	tempString = tempSentence.getWordsarray().get(j).getBaseform();
								tempString = this.BaseOfTempWord(j);
								objectPosition = j;
								this.getWordsarray().get(j).setAlreadyUsedForStructure(true);
							}
							j++;
						}
						object = nlgFactory.createNounPhrase(tempString);
						p.setObject(object);
						if (tempString == "") error = true;
						// See Noun Plural Object
						break;
					case NSS:
						while (tempString == "" && j < tempSentence.getWordsarray().size()){
						//old:	if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.NOUN && tempSentence.getWordsarray().get(j).getRole().name() == "SUBJECT"){
							if ((this.POSofTempWord(j) == "NN" || this.POSofTempWord(j) == "NNP") && this.RoleOfTempWord(j) == "SUBJECT"  && this.getWordsarray().get(j).getAlreadyUsedForStructure() == false){
							//old:	tempString = tempSentence.getWordsarray().get(j).getBaseform();
							tempString = this.BaseOfTempWord(j);
							subjectPosition = j;
							this.getWordsarray().get(j).setAlreadyUsedForStructure(true);
							}
							j++;
						}
						subject = nlgFactory.createNounPhrase(tempString);
						p.setSubject(subject);
						if (tempString == "") error = true;
						// See Noun Plural Object
						break;
					case PP:
						// Since SimpleNLG creates Periods anyway, this PartOfSpeechType needs no code to function.
						break;
					case PQ:
						if (!(Structure.isProperSentence())) error = true;
						p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);	
						// SimpleNLG can't really handle questions that don't have a Subject, Verb AND Object. Therefore, error is set true if not all of those can be found in the given PhraseStructure
						//TODO: Maybe catch this while processing the PhraseStructure Input List?
					    break;
					case VB:
						while (tempString == "" && j < tempSentence.getWordsarray().size()){
						//old:	if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.VERB && tempSentence.getWordsarray().get(j).getRole().name() == "ACTION"){
							if (this.jwnlPOSofTempWord(j) == POS.VERB && this.RoleOfTempWord(j) == "ACTION"  && this.getWordsarray().get(j).getAlreadyUsedForStructure() == false){
							//old:	tempString = tempSentence.getWordsarray().get(j).getBaseform();
								tempString = this.BaseOfTempWord(j);
								this.getWordsarray().get(j).setAlreadyUsedForStructure(true);
							}
							j++;
						}
						// while-loop similiar to the one for nouns, just this time it is checked for Verb and Action.
						verb = nlgFactory.createVerbPhrase(tempString);
				        p.setVerb(verb);
				        if (tempString == "") error = true;
				        // Setting SimpleNLG variables for verbs and setting "error" true if no verb was found
						break;
					case VI:
						while (tempString == "" && j < tempSentence.getWordsarray().size()){
							//old: if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.VERB && tempSentence.getWordsarray().get(j).getRole().name() == "ACTION"){
							if (this.jwnlPOSofTempWord(j) == POS.VERB && this.RoleOfTempWord(j) == "ACTION"  && this.getWordsarray().get(j).getAlreadyUsedForStructure() == false){
							//old: 	tempString = tempSentence.getWordsarray().get(j).getBaseform();
							tempString = this.BaseOfTempWord(j);
							this.getWordsarray().get(j).setAlreadyUsedForStructure(true);
							}
							j++;
						}
						verb = nlgFactory.createVerbPhrase(tempString);
						verb.setFeature(Feature.FORM,simplenlg.features.Form.IMPERATIVE);
						p.setVerb(verb);
						if (tempString == "") error = true;
						// See Verb Base
						break;
					case VPrP:
						while (tempString == "" && j < tempSentence.getWordsarray().size()){
							//old: if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.VERB && tempSentence.getWordsarray().get(j).getRole().name() == "ACTION"){
							if (this.jwnlPOSofTempWord(j) == POS.VERB && this.RoleOfTempWord(j) == "ACTION"  && this.getWordsarray().get(j).getAlreadyUsedForStructure() == false){
							//old:	tempString = tempSentence.getWordsarray().get(j).getBaseform();
								tempString = this.BaseOfTempWord(j);
								this.getWordsarray().get(j).setAlreadyUsedForStructure(true);
							}
							j++;
						}
						verb = nlgFactory.createVerbPhrase(tempString);
						p.setFeature(Feature.FORM, simplenlg.features.Form.PRESENT_PARTICIPLE);
						p.setVerb(verb);
						if (tempString == "") error = true;
						// See Verb Base
						break;
					case VP:
						while (tempString == "" && j < tempSentence.getWordsarray().size()){
						//old:	if (tempSentence.getWordsarray().get(j).getPartOfSpeech().getJwnlType() == POS.VERB && tempSentence.getWordsarray().get(j).getRole().name() == "ACTION"){
							if (this.jwnlPOSofTempWord(j) == POS.VERB && this.RoleOfTempWord(j) == "ACTION"  && this.getWordsarray().get(j).getAlreadyUsedForStructure() == false){
							//old:	tempString = tempSentence.getWordsarray().get(j).getBaseform();
							tempString = this.BaseOfTempWord(j);
							this.getWordsarray().get(j).setAlreadyUsedForStructure(true);
							}
							j++;
						}
						verb = nlgFactory.createVerbPhrase(tempString);
						p.setFeature(Feature.PASSIVE, true);
						p.setVerb(verb);
						if (tempString == "") error = true;
						passive = true;
						// See Verb Base
						// Also, passive is set to true here since generating passive sentences requires some more work further below
						break;
					case VPP:
						while (tempString == "" && j < tempSentence.getWordsarray().size()){
							if (this.jwnlPOSofTempWord(j) == POS.VERB && this.RoleOfTempWord(j) == "ACTION"  && this.getWordsarray().get(j).getAlreadyUsedForStructure() == false){
							tempString = this.BaseOfTempWord(j);
							this.getWordsarray().get(j).setAlreadyUsedForStructure(true);
							}
							j++;
						}
						verb = nlgFactory.createVerbPhrase(tempString);
						p.setFeature(Feature.PASSIVE, true);
						p.setFeature(Feature.TENSE, Tense.PAST);
						p.setVerb(verb);
						if (tempString == "") error = true;
						passive = true;
						// See Verb Base
						// Also, passive is set to true here since generating passive sentences requires some more work further below
						break;
					case VSP:
						while (tempString == "" && j < tempSentence.getWordsarray().size()){
							if (this.jwnlPOSofTempWord(j) == POS.VERB && this.RoleOfTempWord(j) == "ACTION"  && this.getWordsarray().get(j).getAlreadyUsedForStructure() == false){
							tempString = this.BaseOfTempWord(j);
							this.getWordsarray().get(j).setAlreadyUsedForStructure(true);
							}
							j++;
						}
						verb = nlgFactory.createVerbPhrase(tempString);
						p.setVerb(verb);
						p.setFeature(Feature.TENSE, Tense.PAST);
						if (tempString == "") error = true;
						// See Verb Base
						break;
					case VSF:
						while (tempString == "" && j < tempSentence.getWordsarray().size()){
							if (this.jwnlPOSofTempWord(j) == POS.VERB && this.RoleOfTempWord(j) == "ACTION"  && this.getWordsarray().get(j).getAlreadyUsedForStructure() == false){
							tempString = this.BaseOfTempWord(j);
							this.getWordsarray().get(j).setAlreadyUsedForStructure(true);
							}
							j++;
						}
						p.setVerb(tempString);
						// Unlike other cases since future only works like this, else you get "will will"
						p.setFeature(Feature.TENSE, Tense.FUTURE);
						if (tempString == "") error = true;
						// With the exception of special handling for future, see Verb Base
						break;
					case AO:
						if (subjectPosition < objectPosition){
							if (prepObjectPosition < objectPosition){
								if (subjectPosition < prepObjectPosition){
									j = prepObjectPosition + 1;
								}
								else j = subjectPosition + 1;
							}
							else j = subjectPosition + 1;
							}
						else if (prepObjectPosition < objectPosition){
							j = prepObjectPosition + 1;
						}
						// For adjectives, it is necessary to filter whether they belong to subject, object or prepositional object
						// Hence, we only search for them starting from the last found noun and until the current noun
						while (tempString == "" && j < tempSentence.getWordsarray().size() && j < objectPosition){
							if (this.jwnlPOSofTempWord(j) == POS.ADJECTIVE && this.RoleOfTempWord(j) == "OPTIONAL_INFORMATION_FRAGMENT"  && this.getWordsarray().get(j).getAlreadyUsedForStructure() == false){
							tempString = this.BaseOfTempWord(j);
							this.getWordsarray().get(j).setAlreadyUsedForStructure(true);
							}
							j++;
						}
						// while-loop ends at position of the object here, searches for Adjective and Optional Information Fragment however
						object.addModifier(tempString);
						if (tempString == "") error = true;
						// See cases above
						break;
					case AS:
						if (objectPosition < subjectPosition){
							if (prepObjectPosition < subjectPosition){
								if (objectPosition < prepObjectPosition){
									j = prepObjectPosition + 1;
								}
								else j = objectPosition + 1;
							}
							else j = objectPosition + 1;
							}
						else if (prepObjectPosition < subjectPosition){
							j = prepObjectPosition + 1;
						}
						// See cases above
						while (tempString == "" && j < tempSentence.getWordsarray().size() && j < subjectPosition){
							if (this.jwnlPOSofTempWord(j) == POS.ADJECTIVE && this.RoleOfTempWord(j) == "OPTIONAL_INFORMATION_FRAGMENT"  && this.getWordsarray().get(j).getAlreadyUsedForStructure() == false){
							tempString = this.BaseOfTempWord(j);
							this.getWordsarray().get(j).setAlreadyUsedForStructure(true);
							}
							j++;
						}
						subject.addModifier(tempString);
						if (tempString == "") error = true;
						// See cases above
						break;
					case APO:
						if (subjectPosition < prepObjectPosition){
							if (objectPosition < prepObjectPosition){
								if (subjectPosition < objectPosition){
									j = objectPosition + 1;
								}
								else j = subjectPosition + 1;
							}
							else j = subjectPosition + 1;
							}
						else if (objectPosition < prepObjectPosition){
							j = objectPosition + 1;
						}
						// See cases above
						while (tempString == "" && j < tempSentence.getWordsarray().size() && j < prepObjectPosition){
							if (this.jwnlPOSofTempWord(j) == POS.ADJECTIVE && this.RoleOfTempWord(j) == "OPTIONAL_INFORMATION_FRAGMENT"  && this.getWordsarray().get(j).getAlreadyUsedForStructure() == false){
							tempString = this.BaseOfTempWord(j);
							this.getWordsarray().get(j).setAlreadyUsedForStructure(true);
							}
							j++;
						}
						prepositionalObject.addModifier(tempString);
						if (tempString == "") error = true;
						// See cases above
						break;
					case ADV:
						while (tempString == "" && j < tempSentence.getWordsarray().size()){
							if (this.jwnlPOSofTempWord(j) == POS.ADVERB && this.RoleOfTempWord(j) == "OPTIONAL_INFORMATION_FRAGMENT"  && this.getWordsarray().get(j).getAlreadyUsedForStructure() == false){
							tempString = this.BaseOfTempWord(j);
							this.getWordsarray().get(j).setAlreadyUsedForStructure(true);
							}
							j++;
						}
						// While-loop searches for Adverbs
						p.addModifier(tempString);
						if (tempString == "") error = true;
						// See cases above
						break;
					case Prep:
						while (tempString == "" && j < tempSentence.getWordsarray().size()){
							if (this.getWordsarray().get(j).getPartOfSpeech() == PartOfSpeechTypes.PREPOSITION_SUBORDINATING_CONJUNCTION && this.RoleOfTempWord(j) == "OPTIONAL_INFORMATION_FRAGMENT"  && this.getWordsarray().get(j).getAlreadyUsedForStructure() == false){
							tempString = this.BaseOfTempWord(j);
							this.getWordsarray().get(j).setAlreadyUsedForStructure(true);
							}
							j++;
						}
						preposition.setPreposition(tempString);
						prepositionFound = true;
						// Similiar to passive handling, if the Phrase is supposed to include a prepositional sentence, special handling is required further below
						if (tempString == "") error = true;
						// See cases above
						break;
					case POS:
						while (tempString == "" && j < tempSentence.getWordsarray().size()){
							if ((this.POSofTempWord(j) == "NN" || this.POSofTempWord(j) == "NNP") && this.RoleOfTempWord(j) == "OPTIONAL_INFORMATION_FRAGMENT"  && this.getWordsarray().get(j).getAlreadyUsedForStructure() == false){
							tempString = this.BaseOfTempWord(j);
							prepObjectPosition = j;
							this.getWordsarray().get(j).setAlreadyUsedForStructure(true);
							}
							j++;
						}
						prepositionalObject = nlgFactory.createNounPhrase(tempString);
						preposition.addComplement(prepositionalObject);
						if (tempString == "") error = true;
						// See Noun Plural Object
						break;
					case POP:
						while (tempString == "" && j < tempSentence.getWordsarray().size()){
							if ((this.POSofTempWord(j) == "NNS" || this.POSofTempWord(j) == "NNPS") && this.RoleOfTempWord(j) == "OPTIONAL_INFORMATION_FRAGMENT"  && this.getWordsarray().get(j).getAlreadyUsedForStructure() == false){
							tempString = this.BaseOfTempWord(j);
							prepObjectPosition = j;
							this.getWordsarray().get(j).setAlreadyUsedForStructure(true);
							}
							j++;
						}
						prepositionalObject = nlgFactory.createNounPhrase(tempString);
						prepositionalObject.setPlural(true);
						preposition.addComplement(prepositionalObject);
						if (tempString == "") error = true;
						// See Noun Plural Object
						break;
					default:
						break;
	
					}
					tempString = "";
					i++;
					// Resetting tempString as well as increasing the PhraseStructureTypes-Counter i
				}
				if (!error){
					// Only enter this section if the current PhraseStructure fits the Sentence
					if (passive){
						NPPhraseSpec switcher = new NPPhraseSpec(nlgFactory);
						switcher = subject;
						subject = object;
						object = switcher;
						// For whatever reason, SimpleNLG switches object and subject in passive sentences. So before they are created, object and subject
						// are swapped here so once they are swapped once more, they are in correct order again
					}
					hasFittingStructure = true;
					// boolean value needed for output
					Phrase currentPhrase = new Phrase();
					currentPhrase.setNoStructureFound(false);
					// If this section is reached, a fitting PhraseStructure has been found. 
					if (prepositionFound){
						p.addComplement(preposition);
						// Adding the prepositional sentence to the Phrase
					}
					currentPhrase.setFullContent(realiser.realiseSentence(p));
					// Creates the new Phrase by using SimpleNLG engine
					List<String> wordList = new ArrayList<String>(Arrays.asList(currentPhrase.getFullContent().split(" ")));
					for (int t = 0; t < wordList.size(); t++){
						if (wordList.get(t).contains(",") || wordList.get(t).contains(".")){
							wordList.set(t, wordList.get(t).substring(0, wordList.get(t).length()-1));
						}
					}
					// Splits the Phrase by words
					currentPhrase.setseparatedContent(wordList);
					// And stores it here
					currentPhrase.setUsedStructure(counter);
					currentPhrase.setUsedStructureSize(allStructures.get(counter).getElements().size());
					// Sets some variables that are needed for output
					maximumStructureUsed = allStructures.get(counter).getElements().size();
					// Sets the currently used Structure size as maximum structure size. This works since PhraseStructures are sorted by size first
					result.add(currentPhrase);
				}
				counter++;
				// Jump to next PhraseStructure 
			}
			if (!hasFittingStructure){
				// Enter this section only if no Structure was applicable to the sentence
				Phrase originalSentence = new Phrase();
				originalSentence.setFullContent(this.getContentAsString());
				List<String> wordList = new ArrayList<String>(Arrays.asList(originalSentence.getFullContent().split(" ")));
				for (int t = 0; t < wordList.size(); t++){
					if (wordList.get(t).contains(",") || wordList.get(t).contains(".")){
						wordList.set(t, wordList.get(t).substring(0, wordList.get(t).length()-1));
					}
				}
				originalSentence.setseparatedContent(wordList);
				originalSentence.setNoStructureFound(true);
				result.add(originalSentence);
				// In that case, create a Phrase that's identical to the original Sentence
				// Mind that "NoStructureFound" is set to true here in order to display to the user that this Phrase was not generated from a PhraseStructure
			}
			this.setPossiblePhrases(result);
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
