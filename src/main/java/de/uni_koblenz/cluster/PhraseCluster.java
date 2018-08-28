package de.uni_koblenz.cluster;

import java.util.ArrayList;
import java.util.List;

import de.uni_koblenz.label.*;
import de.uni_koblenz.phrase.Phrase;

public class PhraseCluster {

	public ArrayList<String> matchingLabels;
	public String builtPhrase;
	
	public PhraseCluster(LabelList RemainingLabels) {
		/*this.matchingPhrases = new ArrayList<Phrase>(); 
		Label DefiningLabel = RemainingLabels.getInputLabels().get(0); 
		matchingLabels.add(DefiningLabel);
		// Takes the first remaining label in the LabelList and creates a new LabelCluster for it*/

}
	
	public PhraseCluster() {
		this.matchingLabels = new ArrayList<String>();
	}


		public List<String> getMatchingLabels() {
			return matchingLabels;
		}

		public void setMatchingLabels(ArrayList<String> matchingLabels) {
			this.matchingLabels = matchingLabels;
		}


		public String getBuiltPhrase() {
			return builtPhrase;
		}


		public void setBuiltPhrase(String builtPhrase) {
			this.builtPhrase = builtPhrase;
		}
		
		

}
