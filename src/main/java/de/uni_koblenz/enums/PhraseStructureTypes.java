package de.uni_koblenz.enums;

public enum PhraseStructureTypes {
	VERB_IMPERATIVE("Verb"),
	VERB_BASE("Verb"),
	VERB_PASSIVE("Verb"),
	VERB_PASSIVE_PAST("Verb"),
	VERB_PRESENT_PARTICIPLE("Verb"),
	VERB_SIMPLEPAST("Verb"),
	VERB_SIMPLEFUTURE("Verb"),
	NOUN_SINGULAR_OBJECT("Object"),
	NOUN_SINGULAR_SUBJECT("Subject"),
	NOUN_PLURAL_OBJECT("Object"),
	NOUN_PLURAL_SUBJECT("Subject"),
	DETERMINER_DEFINITEARTICLE("Determiner"),
	DETERMINER_INDEFINITEARTICLE("Determiner"),
	PUNCTUATION_PERIOD("Punctuation"),
	PUNCTUATION_QUESTIONMARK("Punctuation"),
	ADJECTIVE_FOR_OBJECT("Addition"),
	ADJECTIVE_FOR_SUBJECT("Addition"),
	ADVERB("Addition");
	
	private String determiner;
	
	private PhraseStructureTypes (String determiner) {
		this.determiner = determiner;
	}
	public String getdeterminer() {
		return this.determiner;
	}
}
