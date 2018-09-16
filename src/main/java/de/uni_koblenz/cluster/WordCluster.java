package de.uni_koblenz.cluster;

import java.util.ArrayList;
import java.util.List;

import de.uni_koblenz.label.*;
import de.uni_koblenz.phrase.DomainThesaurus;

public class WordCluster {

	public List<Word> matchingWords;
	
	public WordCluster(){
		this.matchingWords = new ArrayList<Word>(); 
	}
	
	public List<Word> getMatchingWords() {
		return matchingWords;
	}

	public void setMatchingWords(List<Word> matchingWords) {
		this.matchingWords = matchingWords;
	}
	
	public void calculateDominance(DomainThesaurus thesaurus){
		boolean matchFound;
		boolean skipRest = false;
		int thesaurusCounter;
		for (int clusterCounter = 0; clusterCounter < this.matchingWords.size(); clusterCounter++){	
			matchFound = false;
			thesaurusCounter = 0;
			Word tempWord = this.matchingWords.get(clusterCounter);
			while (!(matchFound) && thesaurusCounter < thesaurus.getAllWords().size()){
				if (this.matchingWords.get(clusterCounter).getBaseform().equals(thesaurus.getAllWords().get(thesaurusCounter))){
					tempWord.setDominance(2147483647);
					matchFound = true;
					skipRest = true;
				}
				thesaurusCounter++;
			}
			// As soon as a word in the cluster is also in the DomainThesaurus, its dominance is set to max int and the while loop ends
			 if (!(matchFound)) tempWord.setDominance(0);
		}
		// Sets dominance for all other Words to 0 at first to avoid NullPointerException
		if (!(skipRest)){
			for (int i = 0; i < this.matchingWords.size(); i++){
				if (!(this.matchingWords.get(i).getDominance() > 0)){
					 Word tempWord = this.matchingWords.get(i);
				     List<Integer> tempIntList = new ArrayList<Integer>();
				     int counter = 1;
					 for (int j = i; j < this.matchingWords.size(); j++){
						 if (this.matchingWords.get(j).getBaseform().equals(tempWord.getBaseform())){
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
	}
	
	
	public void generalizeWords(){
		int current = 0; 
		String k=null; // k initialisieren
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
