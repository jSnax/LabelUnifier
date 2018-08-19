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
	private ArrayList<ArrayList<Phrase>> wholeInput;
	private ArrayList<String> phrasesFinal;
	
	public PhraseList() {
		this.phrasesFinal = new ArrayList<String>();
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

	public ArrayList<ArrayList<Phrase>> getWholeInput() {
		return wholeInput;
	}

	public void setWholeInput(ArrayList<ArrayList<Phrase>> wholeInput) {
		this.wholeInput = wholeInput;
	}


	public List<String> getPhrasesFinal() {
		return phrasesFinal;
	}

	public void setPhrasesFinal(ArrayList<String> phrasesFinal) {
		this.phrasesFinal = phrasesFinal;
	}

	public void addPhrase(Phrase phrase) {
		Phrase phrase2 = phrase;
		this.phrases.add(phrase2);
	}
	
	public void phraseCompareAndDecision() {
		System.out.println("Die vollständige Phrasenlist beinhaltet:");
		for (int i = 0; i < this.wholeInput.size(); i++) {
			this.phrasesFinal.add(this.wholeInput.get(i).get(0).getFullContent());
			System.out.println(this.phrasesFinal.get(i));
		}
		for (int zaehlerLabels = 0; zaehlerLabels < this.wholeInput.size(); zaehlerLabels++) {
			for (int zaehlerPhrasen = 0; zaehlerPhrasen < this.wholeInput.get(zaehlerLabels).size(); zaehlerPhrasen++) {
				System.out.println(this.wholeInput.get(zaehlerLabels).get(zaehlerPhrasen).getFullContent());
			}
		}
		for (int zaehlerLabels = 0; zaehlerLabels < this.wholeInput.size(); zaehlerLabels++) {
			for (int zaehlerPhrase = 0; zaehlerPhrase < this.wholeInput.get(zaehlerLabels).size(); zaehlerPhrase++) {
				String currentPhrase = this.wholeInput.get(zaehlerLabels).get(zaehlerPhrase).getFullContent();
				System.out.println(phrasesFinal.get(0));
				System.out.println("Die aktuell kontrollierte Phrase ist:");
				System.out.println(currentPhrase);
				for (int labelDimension = zaehlerLabels + 1; labelDimension < this.wholeInput.size(); labelDimension ++) {
					for (int phrasenDimension = 0; phrasenDimension < this.wholeInput.get(labelDimension).size(); phrasenDimension++){
						if (currentPhrase.equals(this.wholeInput.get(labelDimension).get(phrasenDimension).getFullContent())) {
							this.phrasesFinal.set(zaehlerLabels, currentPhrase);
							break;
							
						}
						continue;
					}
					break;
				}
				break;
			}
		}
		System.out.println("Die finalen Phrasen sind: ");
			for (int i = 0; i < this.phrasesFinal.size(); i++) {
				System.out.println(phrasesFinal.get(i));
			}
		}
	}
