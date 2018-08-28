package de.uni_koblenz.phrase;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uni_koblenz.cluster.PhraseCluster;
import de.uni_koblenz.label.*;

public class PhraseList {
	
	private Vector<String> vectorSpace;
	private List<Phrase> phrases;
	private ArrayList<ArrayList<Phrase>> wholeInput;
	private ArrayList<String> phrasesFinal;
	private ArrayList<PhraseCluster> allBuiltClusters;
	
	public PhraseList() {
		this.phrasesFinal = new ArrayList<String>();
		this.allBuiltClusters = new ArrayList<PhraseCluster>();
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
	

	public ArrayList<PhraseCluster> getAllBuiltClusters() {
		return allBuiltClusters;
	}

	public void setAllBuiltClusters(ArrayList<PhraseCluster> allBuiltClusters) {
		this.allBuiltClusters = allBuiltClusters;
	}

	public void addPhrase(Phrase phrase) {
		Phrase phrase2 = phrase;
		this.phrases.add(phrase2);
	}
	
	public void phraseCompareAndDecision(LabelList labelList) {
		System.out.println("Die vollständige Phrasenlist beinhaltet:");
		ArrayList<Integer> controller = new ArrayList<Integer>();
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
				String currentPhrase = this.wholeInput.get(zaehlerLabels).get(0).getFullContent();
				System.out.println("Die aktuell kontrollierte Phrase ist:");
				System.out.println(currentPhrase);
				for (int labelDimension = zaehlerLabels + 1; labelDimension < this.wholeInput.size(); labelDimension++) {
						if (currentPhrase.equals(this.wholeInput.get(labelDimension).get(0).getFullContent())) {
							PhraseCluster currentCluster = new PhraseCluster();
							currentCluster.setBuiltPhrase(wholeInput.get(labelDimension).get(0).getFullContent());
							currentCluster.getMatchingLabels().add(labelList.getInputLabels().get(zaehlerLabels));
							currentCluster.getMatchingLabels().add(labelList.getInputLabels().get(labelDimension));
							this.allBuiltClusters.add(currentCluster);
							controller.add(zaehlerLabels);
							controller.add(labelDimension);
							break;
						}
						else if(!(controller.contains(zaehlerLabels))){
						PhraseCluster currentCluster = new PhraseCluster();
						currentCluster.setBuiltPhrase(wholeInput.get(zaehlerLabels).get(0).getFullContent());
						currentCluster.getMatchingLabels().add(labelList.getInputLabels().get(zaehlerLabels));
						this.allBuiltClusters.add(currentCluster);
						controller.add(zaehlerLabels);
						}
					}
			}
		System.out.println("Die finalen Phrasen sind: ");
			for (int i = 0; i < this.allBuiltClusters.size(); i++) {
				System.out.println(allBuiltClusters.get(i).getBuiltPhrase());
		}
	}
}


