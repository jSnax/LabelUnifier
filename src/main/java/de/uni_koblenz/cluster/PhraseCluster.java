package de.uni_koblenz.cluster;

import java.util.ArrayList;
import java.util.List;

import de.uni_koblenz.label.*;
import de.uni_koblenz.phrase.Phrase;

public class PhraseCluster {

	public List<Phrase> matchingLabels;
	
	public PhraseCluster(LabelList RemainingLabels) {
		/*this.matchingPhrases = new ArrayList<Phrase>(); 
		Label DefiningLabel = RemainingLabels.getInputLabels().get(0); 
		matchingLabels.add(DefiningLabel);
		// Takes the first remaining label in the LabelList and creates a new LabelCluster for it*/
	}

	public List<Phrase> getMatchingLabels() {
		return matchingLabels;
	}

	public void setMatchingLabels(List<Phrase> matchingLabels) {
		this.matchingLabels = matchingLabels;
	}
	
	
	
}
