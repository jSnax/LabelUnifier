package de.uni_koblenz.cluster;

import de.uni_koblenz.label.*;
import de.uni_koblenz.enums.*;

public class GrammaticalRelation {

	private Word relatedWord1;
	private Word relatedWord2;
	private RelationType grammaticalRelationType;
	private String grammaticalRelationName;
	
	public GrammaticalRelation() {
		
	}
	// new method
	public GrammaticalRelation(Word relatedWord1, Word relatedWord2, String grammaticalRelationName) {
		this.relatedWord1=relatedWord1;
		this.relatedWord2=relatedWord2;		
		this.grammaticalRelationName=grammaticalRelationName;
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
	
	public RelationType getGrammaticalRelationType() {
		return grammaticalRelationType;
	}
	
	public void setGrammaticalRelationType(RelationType grammaticalRelationType) {
		this.grammaticalRelationType = grammaticalRelationType;
	}
	public String getGrammaticalRelationName() {
		return grammaticalRelationName;
	}
	public void setGrammaticalRelationName(String grammaticalRelationName) {
		this.grammaticalRelationName = grammaticalRelationName;
	}
	
	
	
}
