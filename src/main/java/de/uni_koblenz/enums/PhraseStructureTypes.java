package de.uni_koblenz.enums;

public enum PhraseStructureTypes {
	VERB_IMPERATIVE("Verb"),
	VERB_BASE("Verb"),
	VERB_INDICATIVE("Verb"),
	VERB_SIMPLEPRESENT("Verb"),
	VERB_SIMPLEPAST("Verb"),
	NOUN_SINGULAR_OBJECT("Noun"),
	NOUN_SINGULAR_SUBJECT("Noun"),
	NOUN_PLULAR_OBJECT("Noun"),
	NOUN_PLULAR_SUBJECT("Noun"),
	DETERMINER_DEFINITEARTICLE("Determiner"),
	DETERMINER_INDEFINITEARTICLE("Determiner"),
	PUNCTUATION_PERIOD("Punctuation"),
	PUNCTUATION_COMMA("Punctuation"),
	PUNCTUATION_QUESTIONMARK("Punctuation");
	
	private String determiner;
	
	private PhraseStructureTypes (String determiner) {
		this.determiner = determiner;
	}
}
