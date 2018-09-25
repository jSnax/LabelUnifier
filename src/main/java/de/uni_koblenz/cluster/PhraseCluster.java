package de.uni_koblenz.cluster;

import java.util.ArrayList;
import java.util.List;

import de.uni_koblenz.label.*;
import de.uni_koblenz.phrase.Phrase;

public class PhraseCluster {

	public ArrayList<ArrayList<Integer>> labelAndSentencePositions;
	public ArrayList<Phrase> allPhrases;
	public String builtPhrase; 
	private int wasLabel;
	private int wasSentence;
	private boolean isAlternativeCluster; 
	

	
	public PhraseCluster() {
		this.allPhrases = new ArrayList<Phrase>();
	}


		public List<Phrase> getAllPhrases() {
			return allPhrases;
		}

		public void setAllPhrases(ArrayList<Phrase> allPhrases) {
			this.allPhrases = allPhrases;
		}


		public String getBuiltPhrase() {
			return builtPhrase;
		}


		public void setBuiltPhrase(String builtPhrase) {
			this.builtPhrase = builtPhrase;
		}	

		public ArrayList<ArrayList<Integer>> getLabelAndSentencePositions() {
			return labelAndSentencePositions;
		}

		public void setLabelAndSentencePositions(ArrayList<ArrayList<Integer>> labelAndSentencePositions) {
			this.labelAndSentencePositions = labelAndSentencePositions;
		}


		public int getWasLabel() {
			return wasLabel;
		}

		public void setWasLabel(int wasLabel) {
			this.wasLabel = wasLabel;
		}

		public int getWasSentence() {
			return wasSentence;
		}

		public void setWasSentence(int wasSentence) {
			this.wasSentence = wasSentence;
		}


		public boolean isAlternativeCluster() {
			return isAlternativeCluster;
		}


		public void setAlternativeCluster(boolean isAlternativeCluster) {
			this.isAlternativeCluster = isAlternativeCluster;
		}

		
		
		
}
