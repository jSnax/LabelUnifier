package de.uni_koblenz.label;

import java.io.IOException;
import java.util.ArrayList;

import de.uni_koblenz.cluster.GrammaticalRelation;
import de.uni_koblenz.enums.PartOfSpeechTypes;
import de.uni_koblenz.enums.RoleLeopold;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.patterns.surface.Token;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;

public class Word {
	// new variable #############
	private CoreLabel token;
	
	private PartOfSpeechTypes partOfSpeech;
	// changed from array to arraylist #####################
	private ArrayList<GrammaticalRelation> grammaticalRelations=new ArrayList<GrammaticalRelation>();
	private String baseform;
	private String originalForm;
	private RoleLeopold role;
	private Integer dominance;
	
	public Word() {
		
	}
	public Word(String originalForm) {
		this.originalForm = originalForm;
	}
	// main method
	public Word(CoreLabel token) throws JWNLException {
		this.token=token;
		setOriginalForm(token.originalText());
		tagLabel();
		stemWord();
	}
	public Word(PartOfSpeechTypes partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
	}
	public Word(String originalForm, PartOfSpeechTypes partOfSpeech) {
		this.originalForm = originalForm;
		this.partOfSpeech = partOfSpeech;
	}
	
	public PartOfSpeechTypes getPartOfSpeech() {
		return partOfSpeech;
	}
	
	public void setPartOfSpeech(PartOfSpeechTypes partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
	}
	
	public ArrayList<GrammaticalRelation> getGrammaticalRelations() {
		return grammaticalRelations;
	}
	
	public void setGrammaticalRelations(ArrayList<GrammaticalRelation> grammaticalRelations) {
		this.grammaticalRelations = grammaticalRelations;
	}
	// new method ##########################
	public void addGrammaticalRelation(GrammaticalRelation grammaticalRelation) {
		this.grammaticalRelations.add(grammaticalRelation);
	}
	
	public String getBaseform() {
		return baseform;
	}
	
	public void setBaseform(String baseform) {
		
		this.baseform = baseform;
	}
	
	public String getOriginalForm() {
		return originalForm;
	}
	
	public void setOriginalForm(String originalForm) {
		this.originalForm = originalForm;
	}
	
	public RoleLeopold getRole() {
		return role;
	}
	
	public void setRole(RoleLeopold role) {
		this.role = role;
	}
	
	public Integer getDominance() {
		return dominance;
	}
	
	public void setDominance(Integer dominance) {
		this.dominance = dominance;
	}
	
	/*
	 * method to tag a label into PartOfSpeechTypes
	 */
	public void tagLabel() {
		String pos=token.tag();

        
        // NEUE PART OF SPEECH ABFRAGE
        for (PartOfSpeechTypes type : PartOfSpeechTypes.values()) {
        	if (pos.equals(type.getShortType())) {
        		this.setPartOfSpeech(type);
        		break;
        	}
        }
        
        
        /**if(pos.equals("CC")) {
                this.setPartOfSpeech(PartOfSpeechTypes.COORDINATING_CONJUNCTION);
        	}else if(pos.equals("CD")){
        		this.setPartOfSpeech(PartOfSpeechTypes.CARDINAL_NUMBER);
        	}else if(pos.equals("DT")){
        		this.setPartOfSpeech(PartOfSpeechTypes.DETERMINER);
        	}else if(pos.equals("EX")){
        		this.setPartOfSpeech(PartOfSpeechTypes.EXISTENTIAL_THERE);
        	}else if(pos.equals("FW")){
        		this.setPartOfSpeech(PartOfSpeechTypes.FOREIGN_WORD);
        	}else if(pos.equals("IN")){
        		this.setPartOfSpeech(PartOfSpeechTypes.PREPOSITION_SUBORDINATING_CONJUNCTION);
        	}else if(pos.equals("JJ")){
        		this.setPartOfSpeech(PartOfSpeechTypes.ADJECTIVE);
        	}else if(pos.equals("JJR")){
        		this.setPartOfSpeech(PartOfSpeechTypes.ADJECTIVE_COMPARATIVE);
        	}else if(pos.equals("JJS")){
        		this.setPartOfSpeech(PartOfSpeechTypes.ADJECTIVE_SUPERLATIVE);
        	}else if(pos.equals("NN")){
        		this.setPartOfSpeech(PartOfSpeechTypes.NOUN_SINGULAR_MASS);
        	}else if(pos.equals("NNS")){
        		this.setPartOfSpeech(PartOfSpeechTypes.NOUN_PLURAL);
        	}else if(pos.equals("NNP")){
        		this.setPartOfSpeech(PartOfSpeechTypes.PROPER_NOUN_SINGULAR);
        	}else if(pos.equals("NNPS")){
        		this.setPartOfSpeech(PartOfSpeechTypes.PROPER_NOUN_PLURAL);
        	}else if(pos.equals("PDT")){
        		this.setPartOfSpeech(PartOfSpeechTypes.PREDETERMINER);		
        	}else if(pos.equals("NP")){
        		this.setPartOfSpeech(PartOfSpeechTypes.NOUN_PHRASE);
        	}else if(pos.equals("PP")){
        		this.setPartOfSpeech(PartOfSpeechTypes.PREPOSITIONAL_PHRASE);
        	}else if(pos.equals("VP")){
        		this.setPartOfSpeech(PartOfSpeechTypes.VERB_PHRASE);
        	}else if(pos.equals("PRP")){
        		this.setPartOfSpeech(PartOfSpeechTypes.PERSONAL_PRONOUN);
        	}else if(pos.equals("RB")){
        		this.setPartOfSpeech(PartOfSpeechTypes.ADVERB);
        	}else if(pos.equals("RBR")){
        		this.setPartOfSpeech(PartOfSpeechTypes.ADVERB_COMPARATIVE);
        	}else if(pos.equals("RBS")){
        		this.setPartOfSpeech(PartOfSpeechTypes.ADJECTIVE_SUPERLATIVE);
        	}else if(pos.equals("RP")){
        		this.setPartOfSpeech(PartOfSpeechTypes.PARTICLE);
        	}else if(pos.equals("SYM")){
        		this.setPartOfSpeech(PartOfSpeechTypes.SYMBOL);
        	}else if(pos.equals("TO")){
        		this.setPartOfSpeech(PartOfSpeechTypes.TO);
        	}else if(pos.equals("UH")){
        		this.setPartOfSpeech(PartOfSpeechTypes.INTERJECTION);
        	}else if(pos.equals("VB")){
        		this.setPartOfSpeech(PartOfSpeechTypes.VERB_BASE);
        	}else if(pos.equals("VBD")){
        		this.setPartOfSpeech(PartOfSpeechTypes.VERB_PAST);
        	}else if(pos.equals("VBG")){
        		this.setPartOfSpeech(PartOfSpeechTypes.VERB_GERUND_OR_PRESENT_PARTICIPLE);
        	}else if(pos.equals("VBN")){
        		this.setPartOfSpeech(PartOfSpeechTypes.VERB_PAST_PARTICIPLE);
        	}else if(pos.equals("VBP")){
        		this.setPartOfSpeech(PartOfSpeechTypes.VERB_NON_3RD_PERSON_SINGULAR_PRESENT);
        	}else if(pos.equals("VBZ")){
        		this.setPartOfSpeech(PartOfSpeechTypes.VERB_3RD_PERSON_SINGULAR_PRESENT);
        	}else if(pos.equals("WDT")){
        		this.setPartOfSpeech(PartOfSpeechTypes.WH_DETERMINER);
        	}else if(pos.equals("WP")){
        		this.setPartOfSpeech(PartOfSpeechTypes.WH_PRONOUN);
        	}else if(pos.equals("WRB")){
        		this.setPartOfSpeech(PartOfSpeechTypes.WH_ADVERB);
        	}else {
        		this.setPartOfSpeech(null);
        	}**/
        }
	
	/*  stem(String toStem)
	 *  method to get the lemma of a single Word using the CoreNLP Lemmatizer.
	 
	public void stem(String toStem){		 
		StanfordCoreNLP pipeline = new StanfordCoreNLP(new Properties(){
			
			private static final long serialVersionUID = 1L;
			{
			  setProperty("annotators", "tokenize,ssplit,pos,lemma"); 
			  	// initialize annotator dependencies 
			}});

			Annotation token = new Annotation(toStem);
			pipeline.annotate(token); 
			List<CoreMap> list = token.get(SentencesAnnotation.class);
			String stemmed = list
			                        .get(0).get(TokensAnnotation.class)
			                        .get(0).get(LemmaAnnotation.class);
			toStem = stemmed;
	}*/ 


	public void stemWord() throws JWNLException {
		
		Dictionary dict = Dictionary.getDefaultResourceInstance();
		
		POS pos = partOfSpeech.getJwnlType();
		//System.out.println(pos);
	    
		if(pos == null) {
			setBaseform(getOriginalForm());
		} else {
            IndexWord word = dict.getIndexWord(pos,getOriginalForm());
            String lemma = null;
            if (word != null) {
                lemma = word.getLemma();
            } else {
                IndexWord toForm = dict.getMorphologicalProcessor().lookupBaseForm(pos,getOriginalForm());
                if (toForm != null) {
                    lemma = toForm.getLemma();
                } else {
                    lemma = getOriginalForm();
                }
            }
            setBaseform(lemma);	
		}
	}
	@Override
	public String toString() {
		String grammaticalRelationAsString=" Grammatical relations: ";
		for(GrammaticalRelation grammaticalRelation:grammaticalRelations) {
			grammaticalRelationAsString+=grammaticalRelation.getGrammaticalRelationName()+"; ";
		}
		
		return "Word: "+ originalForm +" Base: " + baseform + " POS: " + partOfSpeech + grammaticalRelationAsString;
	}
}
