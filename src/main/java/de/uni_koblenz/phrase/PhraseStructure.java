package de.uni_koblenz.phrase;

import java.util.ArrayList;
import java.util.List;
//<<<<<<< Updated upstream
//=======
import java.util.regex.Pattern;
//>>>>>>> Stashed changes
import java.util.regex.Matcher;

import de.uni_koblenz.enums.PartOfSpeechTypes;

import java.io.IOException;

public class PhraseStructure {

	String phraseElements;
	Phrase[] generatedPhrase;
	// TEMPORARY ADDITION OF STRUCTLIST BELOW, NO LONGER NEEDED ONCE THIS CLASS IS COMPLETED
	List<PartOfSpeechTypes> StructList;

	
	public PhraseStructure() {
		
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
	// TEMPORARY METHODS FOR STRUCTLIST BELOW
	public List<PartOfSpeechTypes> getStructList() {
		return StructList;
	}
		
	public void setStructList(List<PartOfSpeechTypes> StructList) {
		this.StructList = StructList;
	}
}
