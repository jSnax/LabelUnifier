package de.uni_koblenz.cluster;

import de.uni_koblenz.label.*;
import de.uni_koblenz.enums.*;

public class GrammaticalRelationBetweenWords {

	private Word sourceWord;
	private Word targetWord;
	private RelationType grammaticalRelationType;
	private RelationName grammaticalRelationName;
	
	public GrammaticalRelationBetweenWords() {
		
	}
	// new method
	public GrammaticalRelationBetweenWords(Word sourceWord, Word targetWord, RelationName grammaticalRelationName) {
		this.sourceWord=sourceWord;
		this.targetWord=targetWord;		
		this.grammaticalRelationName=grammaticalRelationName;
	}
	
	public Word getSourceWord() {
		return sourceWord;
	}
	
	public void setSourceWord(Word sourceWord) {
		this.sourceWord = sourceWord;
	}
	
	public Word getTargetWord() {
		return targetWord;
	}
	public void setTargetWord(Word targetWord) {
		this.targetWord = targetWord;
	}
	
	public RelationType getGrammaticalRelationType() {
		return grammaticalRelationType;
	}
	
	public void setGrammaticalRelationType(RelationType grammaticalRelationType) {
		this.grammaticalRelationType = grammaticalRelationType;
	}
	public RelationName getGrammaticalRelationName() {
		return grammaticalRelationName;
	}
	public void setGrammaticalRelationName(RelationName grammaticalRelationName) {
		this.grammaticalRelationName = grammaticalRelationName;
	}

	
	@Override
	public String toString() {
		return "Source: "+sourceWord+" Target: "+targetWord+" Name: " +grammaticalRelationName;
	}
}

