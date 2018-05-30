package de.uni_koblenz.similarity;

import java.util.HashSet;
import java.util.Set;
import java.io.StringReader;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

public class Jaccard {
	
	// String needs to be tokenized into a Set of Corelabels
	HashSet<CoreLabel> tokening (String sentence){
		HashSet<CoreLabel> set = new HashSet<CoreLabel>(); 
		PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer <CoreLabel>(new StringReader(sentence),new CoreLabelTokenFactory(), "");
		while (ptbt.hasNext()) {
			set.add(ptbt.next());
		 }
		return set;
	} 
	
	// This function takes the two sets of corelabels and calculates their Jaccard similarity
	// Which is: Intersection of two sets divided by the result of the union minus the intersection
	double calculateSimilarity(HashSet<CoreLabel> s1, HashSet<CoreLabel> s2){
		final Set<CoreLabel> intersectionSet = new HashSet<CoreLabel>(s1);
		final Set<CoreLabel> unionSet = new HashSet<CoreLabel>(s1);
		intersectionSet.retainAll(s2);
		unionSet.addAll(s2);
		
		double jaccardSimilarity = (double)intersectionSet.size()/ (double)unionSet.size();
		return jaccardSimilarity;
	}
}
