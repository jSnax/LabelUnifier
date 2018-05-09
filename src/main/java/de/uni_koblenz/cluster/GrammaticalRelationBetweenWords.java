package de.uni_koblenz.cluster;

import de.uni_koblenz.label.*;
import edu.stanford.nlp.trees.*;

public class GrammaticalRelationBetweenWords {

	private Word relatedWord1;
	private Word relatedWord2;
	private GrammaticalRelation grammaticalRelationType;
	
	public GrammaticalRelationBetweenWords() {
		
	}
	
	public Word getRelatedWord1() {
		return relatedWord1;
	}
	
	public void setRelatedWord1(Word relatedWord1) {
		this.relatedWord1 = relatedWord1;
	}
	
	public Word getRelatedWord2() {
		return relatedWord2;
	}
	
	public void setRelatedWord2(Word relatedWord2) {
		this.relatedWord2 = relatedWord2;
	}

	public GrammaticalRelation getGrammaticalRelationType() {
		return grammaticalRelationType;
	}

	public void setGrammaticalRelationType(GrammaticalRelation grammaticalRelationType) {
		this.grammaticalRelationType = grammaticalRelationType;
	}
	
	
	
	
}
