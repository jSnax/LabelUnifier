package de.uni_koblenz.label;

public class LabelList {
	
	private Label[] inputLabels;
	private Word[] allWords;
	
	public LabelList() {
		
	}
	
	public LabelList(Word[] allWords) {
		this.allWords = allWords;
	}

	public Label[] getInputLabels() {
		return inputLabels;
	}

	public void setInputLabels(Label[] inputLabels) {
		this.inputLabels = inputLabels;
	}

	public Word[] getAllWords() {
		return allWords;
	}

	public void setAllWords(Word[] allWords) {
		this.allWords = allWords;
	}
	
	
	
	
}
