package de.uni_koblenz.label;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.*;

public class Label {
	
	private Word[] wordsarray;
	private CoreSentence labelAsCoreSentence;
	private String labelAsString;
	private SemanticGraph parsedLabel;
	private boolean isFinal;
	
	public Label() {
		
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

	
}
