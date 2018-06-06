package de.uni_koblenz.cluster;

import java.util.ArrayList;
import java.util.List;

import de.uni_koblenz.label.*;

public class LabelCluster {

	public List<Label> matchingLabels;
	
	public LabelCluster(LabelList RemainingLabels) {
		this.matchingLabels = new ArrayList<Label>(); 
		Label DefiningLabel = RemainingLabels.getInputLabels().get(0); 
		matchingLabels.add(DefiningLabel);
		// Takes the first remaining label in the LabelList and creates a new LabelCluster for it
	}

	public List<Label> getMatchingLabels() {
		return matchingLabels;
	}

	public void setMatchingLabels(List<Label> matchingLabels) {
		this.matchingLabels = matchingLabels;
	}
	
	
	
}
