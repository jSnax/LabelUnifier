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

	
	public PhraseStructure() {
		
	}

	public void setPhraseElements(String phraseElements) {
		this.phraseElements = phraseElements;
	}

	
	public List<PhraseStructureTypes> getElements() {
		return elements;
	}
	
	public void setElements(List<PhraseStructureTypes> elements) {
		this.elements = elements;
	}
	
	public int getTrueSize() {
		if (this.getElements().contains(PhraseStructureTypes.PUNCTUATION_PERIOD) || this.getElements().contains(PhraseStructureTypes.PUNCTUATION_QUESTIONMARK)){
			return (this.getElements().size()-1);
		}
		else return (this.getElements().size());
	}
	
	
	public boolean isProperSentence(){
		boolean hasSubject = false;
		boolean hasObject = false;
		boolean hasVerb = false;
		for (PhraseStructureTypes current : this.getElements()){
			if (current.getdeterminer() == "Verb") hasVerb = true;
			if (current.getdeterminer() == "Subject") hasSubject = true;
			if (current.getdeterminer() == "Object") hasObject = true;
		}
		return (hasSubject && hasObject && hasVerb);
	}

}
