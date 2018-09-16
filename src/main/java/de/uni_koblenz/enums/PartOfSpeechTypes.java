package de.uni_koblenz.enums;

import net.sf.extjwnl.data.POS;

public enum PartOfSpeechTypes {
	COORDINATING_CONJUNCTION("CC"), 
	CARDINAL_NUMBER("CD"), 
	DETERMINER("DT"), 
	EXISTENTIAL_THERE("EX"), 
	FOREIGN_WORD("FW"), 
	PREPOSITION_SUBORDINATING_CONJUNCTION("IN"), 
	ADJECTIVE("JJ",POS.ADJECTIVE),
	ADJECTIVE_COMPARATIVE("JJR",POS.ADJECTIVE), 
	ADJECTIVE_SUPERLATIVE("JJS",POS.ADJECTIVE), 
	LIST_ITEM_MARKER(), 
	MODAL("MD"), 
	NOUN_SINGULAR_MASS("NN",POS.NOUN), 
	NOUN_PLURAL("NNS",POS.NOUN), 
	PROPER_NOUN_SINGULAR("NNP",POS.NOUN),
	PROPER_NOUN_PLURAL("NNPS",POS.NOUN), 
	PREDETERMINER("PDT"), 
	POSSESSIVE_ENDING(), 
	PERSONAL_PRONOUN("PRP"), 
	POSSESSIVE_PRONOUN(), 
	ADVERB("RB",POS.ADVERB), 
	ADVERB_COMPARATIVE("RBR",POS.ADVERB), 
	ADVERB_SUPERLATIVE("RBS",POS.ADVERB),
	PARTICLE("RP"), 
	SYMBOL("SYM"), 
	TO("TO"), 
	INTERJECTION("UH"), 
	VERB_BASE("VB",POS.VERB), 
	VERB_PAST("VBD",POS.VERB), 
	VERB_GERUND_OR_PRESENT_PARTICIPLE("VBG",POS.VERB), 
	VERB_PAST_PARTICIPLE("VBN",POS.VERB),
	VERB_NON_3RD_PERSON_SINGULAR_PRESENT("VBP",POS.VERB), 
	VERB_3RD_PERSON_SINGULAR_PRESENT("VBZ",POS.VERB), 
	WH_DETERMINER("WDT"), 
	WH_PRONOUN("WP"), 
	POSSESSIVE_WH_PRONOUN,
	WH_ADVERB("WRB"),
	NOUN_PHRASE("NP",POS.NOUN), 
	PREPOSITIONAL_PHRASE("PP"), 
	VERB_PHRASE("VP",POS.VERB),
	
	NONE();
	
	
	private String shortType;
	private POS jwnlType;

	
	private PartOfSpeechTypes() {
		
	}
	

	
	private PartOfSpeechTypes(String shortType) {
		this.shortType = shortType;
	}
	
	
	private PartOfSpeechTypes(String shortType, POS jwnlType) {
		this.shortType = shortType;
		this.jwnlType = jwnlType;
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
	


}

