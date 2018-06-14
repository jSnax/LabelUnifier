package de.uni_koblenz.phrase;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uni_koblenz.label.*;

public class PhraseList {
	
	private Vector<String> vectorSpace;
	private List<Phrase> phrases;
	private List<List<String>> ultimate;
	
	public PhraseList() {
	}
	
	public Vector<String> getVectorSpace() {
		return vectorSpace;
	}
	
	public void setVectorSpace(Vector<String> vectorSpace) {
		this.vectorSpace = vectorSpace;
	}
	
	public List<Phrase> getPhrases() {
		return phrases;
	}
	
	public void setPhrases(List<Phrase> phrases) {
		this.phrases = phrases;
	}

	public List<List<String>> getUltimate() {
		return ultimate;
	}

	public void setUltimate(List<List<String>> ultimate) {
		this.ultimate = ultimate;
	}

	public void phraseSpace(){
		//add all the elements in the first list into the vector
		Vector<String> space = new Vector<String>();
		for(int phrasenZaehler = 0; phrasenZaehler < this.phrases.size(); phrasenZaehler++){
			for (int woerterZaehler = 0; woerterZaehler < this.phrases.get(phrasenZaehler).getseparatedContent().size(); woerterZaehler++)
				if(space.contains(this.phrases.get(phrasenZaehler).getseparatedContent().get(woerterZaehler))==false){
					space.add(this.phrases.get(phrasenZaehler).getseparatedContent().get(woerterZaehler));
				}
				else {
					continue;
				}
			this.vectorSpace = space;
		}
	}
}
