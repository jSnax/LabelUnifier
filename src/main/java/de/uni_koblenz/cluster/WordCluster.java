package de.uni_koblenz.cluster;

import java.util.ArrayList;
import java.util.List;

import de.uni_koblenz.label.*;

public class WordCluster {

	public List<Word> matchingWords;

	public WordCluster(LabelList RemainingLabels) {
		this.matchingWords = new ArrayList<Word>(); 
		Word DefiningWord = RemainingLabels.getInputLabels().get(0).getWordsarray().get(0); 
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
	
	
}
