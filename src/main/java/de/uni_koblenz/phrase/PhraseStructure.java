package de.uni_koblenz.phrase;

public class PhraseStructure {

	String phraseElements;
	Phrase[] generatedPhrase;
	
	public PhraseStructure() {
		
	}
	
	public String getPhraseElements() {
		return phraseElements;
	}
	
	public void setPhraseElements(String phraseElements) {
		this.phraseElements = phraseElements;
	}
	
	public Phrase[] getGeneratedPhrase() {
		return generatedPhrase;
	}
	
	public void setGeneratedPhrase(Phrase[] generatedPhrase) {
		this.generatedPhrase = generatedPhrase;
	}
	
	
	
}
