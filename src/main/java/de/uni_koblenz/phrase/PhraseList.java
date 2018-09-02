package de.uni_koblenz.phrase;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
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
	private ArrayList<String> finalPhrasesAndTheirLabels = new ArrayList<String>();;
	
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
		System.out.println("Die vollst�ndige Phrasenlist beinhaltet:");
		ArrayList<Integer> controller = new ArrayList<Integer>();
		for (int i = 0; i < this.wholeInput.size(); i++) {
			// add all the  casted i-strings of wholeInput(which is actually an  Arraylist of Phrases) to phraseList as a String. 
			this.phrasesFinal.add(this.wholeInput.get(i).get(0).getFullContent());
			//display these Strings in PhraseFinal, one String in a row
			System.out.println(this.phrasesFinal.get(i));
		}
		//printe all entrys in wholeInput as Strings
		for (int zaehlerLabels = 0; zaehlerLabels < this.wholeInput.size(); zaehlerLabels++) {
			for (int zaehlerPhrasen = 0; zaehlerPhrasen < this.wholeInput.get(zaehlerLabels).size(); zaehlerPhrasen++) {
				System.out.println(this.wholeInput.get(zaehlerLabels).get(zaehlerPhrasen).getFullContent());
			}
		}
		//iterate, zaehlerlabels gets the size of the first Dimension of the wholeInput
		for (int zaehlerLabels = 0; zaehlerLabels < this.wholeInput.size(); zaehlerLabels++) {
				//define currentPhrase as the phrase at wholeInput at the entry zaehlerLabels 
				String currentPhrase = this.wholeInput.get(zaehlerLabels).get(0).getFullContent();
				System.out.println("Die aktuell kontrollierte Phrase ist:");
				System.out.println(currentPhrase);
				//here we go further in the each phrase and check each entry. We start at next entry of zählerlabels and traverse 
				for (int labelDimension = zaehlerLabels + 1; labelDimension < this.wholeInput.size(); labelDimension++) {
						//if currentPhrase is the same as wholeInput at the entry labelDimension
						if (currentPhrase.equals(this.wholeInput.get(labelDimension).get(0).getFullContent())) {
							//define a new Phrasecluster called Currentcluster
							PhraseCluster currentCluster = new PhraseCluster();
							//put the phrase at wholeinput on the entry labelDimension in this Cluster
							currentCluster.setBuiltPhrase(wholeInput.get(labelDimension).get(0).getFullContent());
							//seems like getInputLabels at entry zaehlerLabels and labelDimension are added as a String to currentCluster
							currentCluster.getMatchingLabels().add(labelList.getInputLabels().get(zaehlerLabels).getLabelAsString());
							currentCluster.getMatchingLabels().add(labelList.getInputLabels().get(labelDimension).getLabelAsString());
							// adding currentCluster to allBuiltClusters
							this.allBuiltClusters.add(currentCluster);
							// ans adding labelDimension and zaehlerLabels to controller
							controller.add(zaehlerLabels);
							controller.add(labelDimension);
							break;
						}
						else if(!(controller.contains(zaehlerLabels))){
							PhraseCluster currentCluster = new PhraseCluster();
							currentCluster.setBuiltPhrase(wholeInput.get(zaehlerLabels).get(0).getFullContent());
							currentCluster.getMatchingLabels().add(labelList.getInputLabels().get(zaehlerLabels).getLabelAsString());
							this.allBuiltClusters.add(currentCluster);
							controller.add(zaehlerLabels);
						}
					}
				if(!(controller.contains(zaehlerLabels))) {
					PhraseCluster currentCluster = new PhraseCluster();
					currentCluster.setBuiltPhrase(wholeInput.get(zaehlerLabels).get(0).getFullContent());
					currentCluster.getMatchingLabels().add(labelList.getInputLabels().get(zaehlerLabels).getLabelAsString());
					this.allBuiltClusters.add(currentCluster);
					controller.add(zaehlerLabels);
				}
			}
		System.out.println("Die finalen Phrasen sind: ");
			for (int i = 0; i < this.allBuiltClusters.size(); i++) {
				System.out.println(allBuiltClusters.get(i).getBuiltPhrase());
				
				System.out.println("Mit den Labels: ");
				System.out.println(allBuiltClusters.get(i).getMatchingLabels().get(0));
				//System.out.println(allBuiltClusters.get(i).getMatchingLabels().get(1));
				System.out.println("\n");
				finalPhrasesAndTheirLabels.add("The current Phrase is: " +  allBuiltClusters.get(i).getBuiltPhrase() + "\n" + "The matching labels are:");
				for(int j = 0; j < allBuiltClusters.get(i).getMatchingLabels().size(); j++){
					finalPhrasesAndTheirLabels.add(allBuiltClusters.get(i).getMatchingLabels().get(j));
				}
				finalPhrasesAndTheirLabels.add("\n");	
		}
	}
	public void writeToFile() throws Exception{
		java.nio.file.Path file = Paths.get("finalFile.txt");
	    Files.write(file, finalPhrasesAndTheirLabels, Charset.forName("UTF-8"));
	}
}


