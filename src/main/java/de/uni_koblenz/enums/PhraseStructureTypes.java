package de.uni_koblenz.enums;

public enum PhraseStructureTypes {
	VERB_IMPERATIVE("Verb"),
	VERB_BASE("Verb"),
	VERB_PASSIVE("Verb"),
	VERB_PRESENT_PARTICIPLE("Verb"),
	VERB_INDICATIVE("Verb"),
	VERB_SIMPLEPRESENT("Verb"),
	VERB_SIMPLEPAST("Verb"),
	NOUN_SINGULAR_OBJECT("Object"),
	NOUN_SINGULAR_SUBJECT("Subject"),
	NOUN_PLULAR_OBJECT("Object"),
	NOUN_PLULAR_SUBJECT("Subject"),
	DETERMINER_DEFINITEARTICLE("Determiner"),
	DETERMINER_INDEFINITEARTICLE("Determiner"),
	PUNCTUATION_PERIOD("Punctuation"),
	PUNCTUATION_COMMA("Punctuation"),
	PUNCTUATION_QUESTIONMARK("Punctuation");
	
	private String determiner;
	
	private PhraseStructureTypes (String determiner) {
		this.determiner = determiner;
	}
	public String getdeterminer() {
		return this.determiner;
	}
}
