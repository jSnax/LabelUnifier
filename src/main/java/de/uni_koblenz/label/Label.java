package de.uni_koblenz.label;

import de.uni_koblenz.enums.PartOfSpeechTypes;
import de.uni_koblenz.enums.RoleLeopold;

import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import net.sf.extjwnl.JWNLException;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Label {
	
	private Word[] wordsarray;
	private CoreSentence labelAsCoreSentence;
	private String labelAsString;
	private SemanticGraph parsedLabel;
	private boolean isFinal;
	
	public Label() {
		
	}
	
	public Label(String labelAsString) {
		
	}
	
	
	public Word[] getWordsarray() {
		return wordsarray;
	}
	
	public void setWordsarray(Word[] wordsarray) {
		this.wordsarray = wordsarray;
	}
	
	public CoreSentence getLabelAsCoreSentence() {
		return labelAsCoreSentence;
	}
	
	public void setLabelAsCoreSentence(CoreSentence labelAsCoreSentence) {
		this.labelAsCoreSentence = labelAsCoreSentence;
	}
	
	public String getLabelAsString() {
		return labelAsString;
	}
	
	public void setLabelAsString(String labelAsString) {
		this.labelAsString = labelAsString;
	}
	
	public SemanticGraph getParsedLabel() {
		return parsedLabel;
	}
	
	public void setParsedLabel(SemanticGraph parsedLabel) {
		this.parsedLabel = parsedLabel;
	}
	
	public boolean isFinal() {
		return isFinal;
	}
	
	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}
	
	/*
	 * method to convert a String into a Word-array
	 */ 
	public Word[] createWordsArray(String input){
		
	String cleanedS = input.replaceAll("[^a-zA-Z]", " ").toLowerCase();
		
		String sa[] = cleanedS.split(" ");
		Word[] w = new Word[sa.length];

		for(int i=0; i < w.length; i++){
			w[i] = new Word(sa[i]);
		}
		
	return w;
	}
	
	/*
	 * method to tag a label into PartOfSpeechTypes
	 */
	public void tagLabel(String text) throws IOException, ClassNotFoundException, JWNLException {

		MaxentTagger maxentTagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");;
        String tag = maxentTagger.tagString(text);
        
        String[] eachTag = tag.split("\\s+");
        
        Word[] w = createWordsArray(text);

        System.out.println("Semantic Analysis:");
        
        for(int i = 0; i< eachTag.length; i++) {
        	
			w[i] = new Word();
			w[i].setOriginalForm(eachTag[i].split("_")[0]);
        	
        	if(eachTag[i].split("_")[1].equals("CC")) {
                w[i].setPartOfSpeech(PartOfSpeechTypes.COORDINATING_CONJUNCTION);
        	}else if(eachTag[i].split("_")[1].equals("CD")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.CARDINAL_NUMBER);
        	}else if(eachTag[i].split("_")[1].equals("DT")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.DETERMINER);
        	}else if(eachTag[i].split("_")[1].equals("EX")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.EXISTENTIAL_THERE);
        	}else if(eachTag[i].split("_")[1].equals("FW")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.FOREIGN_WORD);
        	}else if(eachTag[i].split("_")[1].equals("IN")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.PREPOSITION_SUBORDINATING_CONJUNCTION);
        	}else if(eachTag[i].split("_")[1].equals("JJ")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.ADJECTIVE);
        	}else if(eachTag[i].split("_")[1].equals("JJR")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.ADJECTIVE_COMPARATIVE);
        	}else if(eachTag[i].split("_")[1].equals("JJS")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.ADJECTIVE_SUPERLATIVE);
        	}else if(eachTag[i].split("_")[1].equals("NN")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.NOUN_SINGULAR_MASS);
        	}else if(eachTag[i].split("_")[1].equals("NNS")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.NOUN_PLURAL);
        	}else if(eachTag[i].split("_")[1].equals("NNP")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.PROPER_NOUN_SINGULAR);
        	}else if(eachTag[i].split("_")[1].equals("NNPS")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.PROPER_NOUN_PLURAL);
        	}else if(eachTag[i].split("_")[1].equals("PDT")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.PREDETERMINER);		
        	}else if(eachTag[i].split("_")[1].equals("NP")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.NOUN_PHRASE);
        	}else if(eachTag[i].split("_")[1].equals("PP")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.PREPOSITIONAL_PHRASE);
        	}else if(eachTag[i].split("_")[1].equals("VP")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.VERB_PHRASE);
        	}else if(eachTag[i].split("_")[1].equals("PRP")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.PERSONAL_PRONOUN);
        	}else if(eachTag[i].split("_")[1].equals("RB")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.ADVERB);
        	}else if(eachTag[i].split("_")[1].equals("RBR")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.ADVERB_COMPARATIVE);
        	}else if(eachTag[i].split("_")[1].equals("RBS")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.ADJECTIVE_SUPERLATIVE);
        	}else if(eachTag[i].split("_")[1].equals("RP")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.PARTICLE);
        	}else if(eachTag[i].split("_")[1].equals("SYM")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.SYMBOL);
        	}else if(eachTag[i].split("_")[1].equals("TO")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.TO);
        	}else if(eachTag[i].split("_")[1].equals("UH")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.INTERJECTION);
        	}else if(eachTag[i].split("_")[1].equals("VB")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.VERB_BASE);
        	}else if(eachTag[i].split("_")[1].equals("VBD")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.VERB_PAST);
        	}else if(eachTag[i].split("_")[1].equals("VBG")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.VERB_GERUND_OR_PRESENT_PARTICIPLE);
        	}else if(eachTag[i].split("_")[1].equals("VBN")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.VERB_PAST_PARTICIPLE);
        	}else if(eachTag[i].split("_")[1].equals("VBP")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.VERB_NON_3RD_PERSON_SINGULAR_PRESENT);
        	}else if(eachTag[i].split("_")[1].equals("VBZ")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.VERB_3RD_PERSON_SINGULAR_PRESENT);
        	}else if(eachTag[i].split("_")[1].equals("WDT")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.WH_DETERMINER);
        	}else if(eachTag[i].split("_")[1].equals("WP")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.WH_PRONOUN);
        	}else if(eachTag[i].split("_")[1].equals("WRB")){
        		w[i].setPartOfSpeech(PartOfSpeechTypes.WH_ADVERB);
        	}else {
        		w[i].setPartOfSpeech(null);
        	}
        	Word.stemWord(w[i]);
 
        	System.out.println(i+1 +": " + w[i].getPartOfSpeech() + ": " + 
        			w[i].getOriginalForm() + " -> " + 
        			w[i].getBaseform());

        }
	}
	
	
	public static void defineRole(String input) {
		
		RoleLeopold action = RoleLeopold.ACTION;
		RoleLeopold businessObject = RoleLeopold.BUSINESS_OBJECT;
		RoleLeopold optInfFra = RoleLeopold.OPTIONAL_INFORMATION_FRAGMENT;
		
		// Create the Stanford CoreNLP pipeline
	    Properties props = new Properties();
	    props.setProperty("annotators", "tokenize,ssplit,pos,lemma,depparse,natlog,openie");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

	    // Annotate an example document.
	    Annotation doc = new Annotation(input);
	    pipeline.annotate(doc);

	    // Loop over sentences in the document
	    for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)) {
	    	
	      // Get the OpenIE triples for the sentence
	      Collection<RelationTriple> triples =
		          sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
	      
	     for (RelationTriple triple : triples) {
	    	 Word[] wordsarray = new Word[5];
	    	 Label l = new Label();
	    	 l.setWordsarray(wordsarray);
	    	 for(int i = 0; i< wordsarray.length; i++) {
		    	 wordsarray[i] = new Word();
	    	 }

	    	 System.out.println(triple.relationGloss());
	    	 System.out.println(triple.objectGloss());
	     }
	      
	      /* Print the triples
	      for (RelationTriple triple : triples) {
	        System.out.println(triple.confidence + "\t" +
	            triple.subjectLemmaGloss() + "\t" +
	            triple.relationLemmaGloss() + "\t" +
	            triple.objectLemmaGloss());
	      } */
	    }
	    
	}
}
