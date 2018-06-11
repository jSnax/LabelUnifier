package de.uni_koblenz.phrase;

import java.util.ArrayList;
import java.util.List;
//<<<<<<< Updated upstream
//=======
import java.util.regex.Pattern;
//>>>>>>> Stashed changes
import java.util.regex.Matcher;

import de.uni_koblenz.enums.PartOfSpeechTypes;
import de.uni_koblenz.enums.PhraseStructureTypes;

import java.io.IOException;

public class PhraseStructure {

	String phraseElements;
	List<PhraseStructureTypes> elements = new ArrayList<PhraseStructureTypes>();
	Phrase[] generatedPhrase;

	
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
	
	public List<PhraseStructureTypes> getElements() {
		return elements;
	}
	
	public void setElements(List<PhraseStructureTypes> elements) {
		this.elements = elements;
	}

}
