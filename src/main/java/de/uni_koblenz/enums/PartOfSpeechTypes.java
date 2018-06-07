package de.uni_koblenz.enums;

import net.sf.extjwnl.data.POS;

public enum PartOfSpeechTypes {
	COORDINATING_CONJUNCTION("CC", 0.1), 
	CARDINAL_NUMBER("CD", 0.1), 
	DETERMINER("DT", 0.1), 
	EXISTENTIAL_THERE("EX", 0.1), 
	FOREIGN_WORD("FW", 0.1), 
	PREPOSITION_SUBORDINATING_CONJUNCTION("IN", 0.1), 
	ADJECTIVE("JJ",POS.ADJECTIVE, 0.8),
	ADJECTIVE_COMPARATIVE("JJR",POS.ADJECTIVE, 0.7), 
	ADJECTIVE_SUPERLATIVE("JJS",POS.ADJECTIVE, 0.6), 
	LIST_ITEM_MARKER(0.1), 
	MODAL(0.1), 
	NOUN_SINGULAR_MASS("NN",POS.NOUN, 1.0), 
	NOUN_PLURAL("NNS",POS.NOUN, 1.0), 
	PROPER_NOUN_SINGULAR("NNP",POS.NOUN, 1.0),
	PROPER_NOUN_PLURAL("NNPS",POS.NOUN, 1.0), 
	PREDETERMINER("PDT", 0.1), 
	POSSESSIVE_ENDING(0.1), 
	PERSONAL_PRONOUN("PRP", 0.1), 
	POSSESSIVE_PRONOUN(0.1), 
	ADVERB("RB",POS.ADVERB, 0.5), 
	ADVERB_COMPARATIVE("RBR",POS.ADVERB, 0.5), 
	ADVERB_SUPERLATIVE("RBS",POS.ADVERB, 0.5),
	PARTICLE("RP", 0.1), 
	SYMBOL("SYM", 0.1), 
	TO("TO", 0.1), 
	INTERJECTION("UH", 0.1), 
	VERB_BASE("VB",POS.VERB, 1.0), 
	VERB_PAST("VBD",POS.VERB, 0.7), 
	VERB_GERUND_OR_PRESENT_PARTICIPLE("VBG",POS.VERB, 0.9), 
	VERB_PAST_PARTICIPLE("VBN",POS.VERB, 0.8),
	VERB_NON_3RD_PERSON_SINGULAR_PRESENT("VBP",POS.VERB, 0.7), 
	VERB_3RD_PERSON_SINGULAR_PRESENT("VBZ",POS.VERB, 0.9), 
	WH_DETERMINER("WDT", 0.1), 
	WH_PRONOUN("WP", 0.1), 
	POSSESSIVE_WH_PRONOUN,
	WH_ADVERB("WRB", 0.1),
	NOUN_PHRASE("NP",POS.NOUN, 1.0), 
	PREPOSITIONAL_PHRASE("PP", 0.1), 
	VERB_PHRASE("VP",POS.VERB, 1.0);
	
	
	private String shortType;
	private POS jwnlType;
	private double weightedValue;

	
	private PartOfSpeechTypes() {
		
	}
	
	private PartOfSpeechTypes (double weightedValue) {
		this.weightedValue = weightedValue;
	}
	
	private PartOfSpeechTypes(String shortType, double weightedValue) {
		this.shortType = shortType;
		this.weightedValue = weightedValue;
	}
	
	private PartOfSpeechTypes(String shortType, POS jwnlType, double weightedValue) {
		this.shortType = shortType;
		this.jwnlType = jwnlType;
		this.weightedValue = weightedValue;
	}


	public String getShortType() {
		return this.shortType;
	}

	public POS getJwnlType() {
		return jwnlType;
	}

	public void setJwnlType(POS jwnlType) {
		this.jwnlType = jwnlType;
	}
	
	public double getWeightedValue() {
		return weightedValue;
	}

	public void setWeightedValue(double weightedValue) {
		this.weightedValue = weightedValue;
	}

}

