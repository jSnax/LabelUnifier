package de.uni_koblenz.cluster;

import java.util.ArrayList;
import java.util.List;

import de.uni_koblenz.label.*;

public class WordCluster {

	public List<Word> matchingWords;

	public WordCluster(LabelList RemainingLabels) {
		this.matchingWords = new ArrayList<Word>(); 
		Word DefiningWord = RemainingLabels.getInputLabels().get(0).getSentenceArray().get(0).getWordsarray().get(0); 
		matchingWords.add(DefiningWord);
		// Takes the first remaining word in the LabelList and creates a new WordCluster for it
		//[CODE] RemainingLabels.matchSynonyms(RemainingLabels, this, Position); [CODE]
		// Fill MatchingWords, has to be called in main since RemainingLabels will be changed by matchSynonyms
	}
	
	public List<Word> getMatchingWords() {
		return matchingWords;
	}

	public void setMatchingWords(List<Word> matchingWords) {
		this.matchingWords = matchingWords;
	}
	
	// TODO: Übergabe eines Domänenthesaurus, der Dominanz vorgibt zu einzelnen Worten
	public void calculateDominance(){
		for (int i = 0; i < this.matchingWords.size(); i++){
			 Word tempWord = this.matchingWords.get(i);
			 tempWord.setDominance(0);
		}
		// Sets dominance for all Words to 0 at first to avoid NullPointerException
		for (int i = 0; i < this.matchingWords.size(); i++){
			if (!(this.matchingWords.get(i).getDominance() > 0)){
				 Word tempWord = this.matchingWords.get(i);
			     List<Integer> tempIntList = new ArrayList<Integer>();
			     int counter = 1;
				 for (int j = i; j < this.matchingWords.size(); j++){
					 if (this.matchingWords.get(j).getBaseform() == tempWord.getBaseform()){
						 counter++;
						 tempIntList.add(j);
					 }
				 }
				 this.matchingWords.get(i).setDominance(counter);
				 for (int j = tempIntList.size() - 1; j >= 0 ; j--){
					 this.matchingWords.get(j).setDominance(counter);
					 tempIntList.remove(j);
				 }
			}
			// Calculates Dominance for each Word in the Cluster
			else 
				continue;
		}
	}
	
	
	public void generalizeWords(){
		int current = 0; 
		String k=null; // k inizialisieren ?
		for (int i = 0; i<this.matchingWords.size(); i++){ 	
			if (current < this.matchingWords.get(i).getDominance()){ 		
				current = this.matchingWords.get(i).getDominance(); 		
				k = this.matchingWords.get(i).getBaseform(); 	
				} 	
		this.matchingWords.get(i).setDominance(this.matchingWords.size()); 	// Index size + 1?
		} 
		for (int i = 0; i<this.matchingWords.size(); i++){ 	
			this.matchingWords.get(i).setBaseform(k); 
		}
	}
	
}
