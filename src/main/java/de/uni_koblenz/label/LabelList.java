package de.uni_koblenz.label;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uni_koblenz.cluster.PhraseCluster;
import de.uni_koblenz.cluster.WordCluster;
import de.uni_koblenz.enums.PartOfSpeechTypes;
import de.uni_koblenz.phrase.Phrase;
import de.uni_koblenz.phrase.PhraseStructure;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.list.PointerTargetNode;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.dictionary.Dictionary;
import java.util.Properties;
 
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import net.sf.extjwnl.JWNLException;
 
 
public class LabelList implements java.io.Serializable{
   
 
    private List<Word> allWords = new ArrayList<Word>();
    private List<Label> inputLabels = new ArrayList<Label>();
   
    public transient static StanfordCoreNLP pipeline;
    public LabelList() {
       
    }
    
    private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {

        // 1. Convert Map to List of Map
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        /*
        //classic iterator example
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }*/


        return sortedMap;
    }
 
    /*
     *  Constructor
     */
    public LabelList(String[] input) throws JWNLException {
        // Setup CORENLP Pipeline
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, depparse, natlog");//, openie");//, ner");
        pipeline = new StanfordCoreNLP(props);
       
        // add Label objects for each string of the input array.
        for (String stringLabel:input) {
            inputLabels.add(new Label(stringLabel));
        }
       
    }
    
    /*public LabelList cloneList(){
    	LabelList newList = new LabelList();
    	for (Label tempLabel : this.getInputLabels()){
    		List<Label> tempList = newList.getInputLabels();
    		tempList.add(tempLabel);
    		newList.setInputLabels(tempList);
    	}
    	return (newList);
    }*/
    
    public LabelList cloneList(){
    	LabelList newList = new LabelList();
    	for (int i = 0; i < this.getInputLabelsSize(); i++){
    		Label tempLabel = new Label();
    		List<Sentence> tempSentenceList = new ArrayList<Sentence>();
    		for (int j = 0; j < this.getSentenceArraySize(i); j++){
    			Sentence tempSentence = new Sentence();
    			tempSentence.setSentencePosition(this.getInputLabels().get(i).getSentenceArray().get(j).getSentencePosition());
    			tempSentence.setContentAsString(this.getInputLabels().get(i).getSentenceArray().get(j).getContentAsString());
    			tempSentence.setPossiblePhrases(this.getInputLabels().get(i).getSentenceArray().get(j).getPossiblePhrases());
    			tempSentenceList.add(tempSentence);
    		}
    		tempLabel.setSentenceArray(tempSentenceList);
    		tempLabel.setLabelPosition(this.getInputLabels().get(i).getLabelPosition());
    		List<Label> tempList = newList.getInputLabels();
    		tempList.add(tempLabel);
    		newList.setInputLabels(tempList);
    	}
    	return (newList);
    }
    
    public List<Label> getInputLabels() {
        return inputLabels;
    }
 
    public void setInputLabels(List<Label> inputLabels) {
        this.inputLabels = inputLabels;
    }
 
    public List<Word> getAllWords() {
        return allWords;
    }
 
    public void setAllWords(List<Word> allWords) {
        this.allWords = allWords;
    }
 
   
    public void createAllWordsArray() {
        for (Label l : this.inputLabels) {
            for (Sentence s : l.getSentenceArray()) {
                for (Word w : s.getWordsarray()) {
                    this.allWords.add(w);
                }
            }
        }
    }
   
   
    @Override
    public String toString() {
        String result="Result:\n";
        for (Label label:inputLabels) {
            result+=label.toString()+"\n";
           
        }
        return result;
    }
    
    

	
    public void findSynsets(ForbiddenWords banList) throws JWNLException{
        Dictionary dictionary = Dictionary.getDefaultResourceInstance();
        net.sf.extjwnl.data.IndexWord tempWord;
        List<net.sf.extjwnl.data.Synset> tempSyn;
        PointerTargetNodeList nodelist = null;       
        // iteration over all labels
        for (Label l : this.getInputLabels()) {
            // iteration over all sentences in label
            for (Sentence s : l.getSentenceArray()) {
                // iteration over all words in sentence
                for (Word w : s.getWordsarray()) {
                	w.setSynonyms(new ArrayList<String>());
                    w.addSynonym(w.getBaseform());
                    if ((w.getPartOfSpeech().getJwnlType() != null) && (!banList.isForbiddenWord(w)) && (w.getPartOfSpeech() != PartOfSpeechTypes.NONE)){
                        tempWord = dictionary.getIndexWord(w.getPartOfSpeech().getJwnlType(), w.getBaseform());
                        // Transform baseform of Word j in Label i into an indexWord so extjwnl can use it
                        try{
                            tempSyn = tempWord.getSenses();
                            // Synset for Word j
                            // pre-create the Synonym list for Word j
                            // CAUTION: This will override any pre-existing synonym list, so this method may only be called once
                            for (net.sf.extjwnl.data.Synset syn : tempSyn) {
                                // Iterate over all meanings in the synset, z refers to the current meaning
                                if (w.getPartOfSpeech().getJwnlType() != POS.ADJECTIVE) {
                                    nodelist=PointerUtils.getCoordinateTerms(syn);
                                    for(PointerTargetNode node:nodelist) {
                                        for(net.sf.extjwnl.data.Word word:node.getSynset().getWords()) {
                                            //if (!w.getSynonyms().contains(word.getLemma()))
                                        	// TODO: Change back to old if-clause right above this comment if needed
                                        	// TODO: Check whether banList works below
                                        	if (!w.isSynonym(word.getLemma()))
                                            	if (!banList.isForbiddenString(word.getLemma())){
                                                	w.addSynonym(word.getLemma());
                                            	}
                                                // Go through the synonym list and add each synonym to synonym list for word j, unless it's already in there
                                        }
                                    }
                                    nodelist=PointerUtils.getDirectHypernyms(syn);
                                }
                                else {
                                    nodelist=PointerUtils.getSynonyms(syn);
                                }
                                // Copy all synonyms to nodelist.
                                // For nouns and verbs, a combination of getDirectHypernyms and getCoordinateTerms has to be used since getSynonyms only works on adjectives
                                for(PointerTargetNode node:nodelist) {
                                    for(net.sf.extjwnl.data.Word word:node.getSynset().getWords()) {
                                        //if (!w.getSynonyms().contains(word.getLemma()))
                                    	// TODO: Change back to old if-clause right above this comment if needed
                                    	// TODO: Check whether banList works below
                                    	if (!w.isSynonym(word.getLemma()))
                                        	if (!banList.isForbiddenString(word.getLemma())){
                                        		w.addSynonym(word.getLemma());
                                        	}
                                        	
                                            // Go through the synonym list and add each synonym to synonym list for word j, unless it's already in there
                                    }
                                }
                            }
                        }
                        catch (NullPointerException q){
                        }

                    }
                }
            }
        }
       
       
  
       
    }

    // Different function for finding Synonyms. Since it's not strictly better than the other method, it's not used right now.
    // Difference between the two methods is that findSynsets searches for synonyms by taking the hypernyms of words and then returning all their hyponyms as synonyms
    // While this only takes the different "meanings" of words as synonyms. Both methods have shortcomings, namely that
    // The first one finds way too many synonyms and that this one does not find all synonyms
    // Both methods return false positives still
    public void findSynsets2(ForbiddenWords banList) throws JWNLException{
        Dictionary dictionary = Dictionary.getDefaultResourceInstance();
        net.sf.extjwnl.data.IndexWord tempWord;
        List<net.sf.extjwnl.data.Synset> tempSyn;
        // iteration over all labels
        for (Label l : this.getInputLabels()) {
            // iteration over all sentences in label
            for (Sentence s : l.getSentenceArray()) {
                // iteration over all words in sentence
                for (Word w : s.getWordsarray()) {
                    w.setSynonyms(new ArrayList<String>());
                    w.addSynonym(w.getBaseform());
                    if ((w.getPartOfSpeech().getJwnlType() != null) && (!banList.isForbiddenWord(w)) && (w.getPartOfSpeech() != PartOfSpeechTypes.NONE)){
                    	// TODO: Check whether this catches all exceptions already
                        tempWord = dictionary.lookupIndexWord(w.getPartOfSpeech().getJwnlType(), w.getBaseform());
                        // Transform baseform of Word j in Label i into an indexWord so extjwnl can use it
                        try{
                        	tempSyn = tempWord.getSenses();
                        	// Synset for Word j
                        	for (net.sf.extjwnl.data.Synset synset : tempSyn){
                        		List<net.sf.extjwnl.data.Word> words = synset.getWords();
                        		for (net.sf.extjwnl.data.Word word : words){
                        			w.addSynonym(word.getLemma());
                        		}
                        	}
                        }
                        catch (NullPointerException q){
                           }
                        	/*tempSyn = tempWord.getSenses();
                            // Synset for Word j
                            w.setSynonyms(new ArrayList<String>());
                            // pre-create the Synonym list for Word j
                            // CAUTION: This will override any pre-existing synonym list, so this method may only be called once
                            for (net.sf.extjwnl.data.Synset syn : tempSyn) {
                                // Iterate over all meanings in the synset, z refers to the current meaning
                                if (w.getPartOfSpeech().getJwnlType() != POS.ADJECTIVE) {
                                    nodelist=PointerUtils.getCoordinateTerms(syn);
                                    for(PointerTargetNode node:nodelist) {
                                        for(net.sf.extjwnl.data.Word word:node.getSynset().getWords()) {
                                            //if (!w.getSynonyms().contains(word.getLemma()))
                                        	// TODO: Change back to old if-clause right above this comment if needed
                                        	// TODO: Check whether banList works below
                                        	if (!w.isSynonym(word.getLemma()))
                                            	if (!banList.isForbiddenString(word.getLemma())){
                                                	w.addSynonym(word.getLemma());
                                            	}
                                                // Go through the synonym list and add each synonym to synonym list for word j, unless it's already in there
                                        }
                                    }
                                    nodelist=PointerUtils.getDirectHypernyms(syn);
                                }
                                else {
                                    nodelist=PointerUtils.getSynonyms(syn);
                                }
                                // Copy all synonyms to nodelist.
                                // For nouns and verbs, a combination of getDirectHypernyms and getCoordinateTerms has to be used since getSynonyms only works on adjectives
                                for(PointerTargetNode node:nodelist) {
                                    for(net.sf.extjwnl.data.Word word:node.getSynset().getWords()) {
                                        //if (!w.getSynonyms().contains(word.getLemma()))
                                    	// TODO: Change back to old if-clause right above this comment if needed
                                    	// TODO: Check whether banList works below
                                    	if (!w.isSynonym(word.getLemma()))
                                        	if (!banList.isForbiddenString(word.getLemma())){
                                        		w.addSynonym(word.getLemma());
                                        	}
                                        	
                                            // Go through the synonym list and add each synonym to synonym list for word j, unless it's already in there
                                    }
                                }
                            }*/


                    }
                }
            }
        }
       
       
  
       
    }
    
    // Aktuellste Version von matchSynonyms
    public List<WordCluster> matchSynonyms (){
    	boolean WordsLeft = true;
    	int position = 0;
    	List<WordCluster> AllClusters = new ArrayList<WordCluster>();
    	while (WordsLeft){
    		Word definingWord = new Word();
    		WordCluster Cluster = new WordCluster();
	    	boolean searching = true;
	    	int counterLabels = 0;
	    	int counterSentences = 0;
	    	int counterWords = 0;
	    	while (searching && counterLabels < this.getInputLabelsSize()){
	    		counterSentences = 0;
	    		while (searching && counterSentences < this.getSentenceArraySize(counterLabels)){
	    			counterWords = 0;
	    			while (searching && counterWords < this.getWordsarraySize(counterLabels, counterSentences)){
	    				if (this.getInputLabels().get(counterLabels).getSentenceArray().get(counterSentences).getWordsarray().get(counterWords).getClusterPosition() == null){
	    					definingWord = this.getInputLabels().get(counterLabels).getSentenceArray().get(counterSentences).getWordsarray().get(counterWords);
	    					definingWord.setClusterPosition(position);
	    					Cluster.matchingWords.add(definingWord);
	    					searching = false;
	    				}
	    				counterWords++;
	    			}
	    			counterSentences++;
	    		}
	    		counterLabels++;
	    	}
	    	if (definingWord.getBaseform() != null){
		        for (Label l : this.getInputLabels()) {
		        	for (Sentence s : l.getSentenceArray()) { 
		        		//tempIndex = 0;
		        		for (Word w : s.getWordsarray()) {   			   
		        			if (definingWord.getPartOfSpeech().getJwnlType() == w.getPartOfSpeech().getJwnlType() && w.getClusterPosition() == null) {    				   
		        				if (definingWord.isSynonym(w.getBaseform())  || w.isSynonym(definingWord.getBaseform())) {
		        					Cluster.matchingWords.add(w);
		        					w.setClusterPosition(position);
		        					// Most basic form of matching. If POS match and one word is a synonym of the other, they have the same meaning
		        				}
		        			}
		        		}
		        	}
		        }
		        AllClusters.add(Cluster);
	    	}
	    	else WordsLeft = false;
	    	position++;
    	}
    	return(AllClusters);
    }
    
    // Alternative Version of Matching, compatible with findSynsets2.
    // Difference to matchSynonyms is that that instead of checking whether Word A is a synonym of Word B or vice versa,
    // This checks whether the synonym list of either two compared words have any common word amongst them
    public List<WordCluster> matchSynonyms2 (){
    	boolean WordsLeft = true;
    	int position = 0;
    	List<WordCluster> AllClusters = new ArrayList<WordCluster>();
    	while (WordsLeft){
    		Word definingWord = new Word();
    		WordCluster Cluster = new WordCluster();
	    	boolean searching = true;
	    	int counterLabels = 0;
	    	int counterSentences = 0;
	    	int counterWords = 0;
	    	while (searching && counterLabels < this.getInputLabelsSize()){
	    		counterSentences = 0;
	    		while (searching && counterSentences < this.getSentenceArraySize(counterLabels)){
	    			counterWords = 0;
	    			while (searching && counterWords < this.getWordsarraySize(counterLabels, counterSentences)){
	    				if (this.getInputLabels().get(counterLabels).getSentenceArray().get(counterSentences).getWordsarray().get(counterWords).getClusterPosition() == null){
	    					definingWord = this.getInputLabels().get(counterLabels).getSentenceArray().get(counterSentences).getWordsarray().get(counterWords);
	    					definingWord.setClusterPosition(position);
	    					Cluster.matchingWords.add(definingWord);
	    					searching = false;
	    				}
	    				counterWords++;
	    			}
	    			counterSentences++;
	    		}
	    		counterLabels++;
	    	}
	    	if (definingWord.getBaseform() != null){
		        for (Label l : this.getInputLabels()) {
		        	for (Sentence s : l.getSentenceArray()) { 
		        		for (Word w : s.getWordsarray()) {   			   
		        			if (definingWord.getPartOfSpeech().getJwnlType() == w.getPartOfSpeech().getJwnlType() && w.getClusterPosition() == null) {    				   
		        				boolean found = false;
		        				int i = 0;
		        				while (i < definingWord.getSynonyms().size() && !found){
		        					int j = 0;
			        				while (j < w.getSynonyms().size() && !found){
			        					if (definingWord.getSynonyms().get(i).equals(w.getSynonyms().get(j))){
	    		        					Cluster.matchingWords.add(w);
	    		        					w.setClusterPosition(position);
	    		        					found = true;
			        					}
			        					j++;
			        				}
			        				i++;
		        				}
		        			}
		        		}
		        	}
		        }
		        AllClusters.add(Cluster);
	    	}
	    	else WordsLeft = false;
	    	position++;
    	}
    	return(AllClusters);
    }
    
        // Methoden zu matchSynonyms
 
    public int getInputLabelsSize(){
    return( this.getInputLabels().size() );
    }
     
    public int getSentenceArraySize (int i){
    return( this.getInputLabels().get(i).getSentenceArray().size());
    }
                                                                 
    public int getWordsarraySize (int i, int t){
    return(this.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().size());
    }
     
     public POS getJwnlTypeOfdefiningWord(Word definingWord){
    return( definingWord.getPartOfSpeech().getJwnlType() );
    }
   
                                     
    public POS getJwnlType (int i, int t, int j){
    return(this.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().get(j).getPartOfSpeech().getJwnlType());
    }
 
    public boolean getBaseformOfdefiningWord (int i, int t, int j, Word definingWord){
    return(definingWord.getSynonyms().contains(this.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().get(j).getBaseform()));
    }
 
    public boolean getHasSynonym (int i, int t, int j, Word definingWord){
    return(this.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().get(j).getSynonyms().contains(definingWord.getBaseform()));
    }
 
    public int getInputLabelsSizeShortened(){
    return( this.getInputLabels().size() - 1 );
    }
     
    public int getSentenceArraySizeShortened (int i){
    return( this.getInputLabels().get(i).getSentenceArray().size() - 1);
    }
 
    public boolean getWordsarrayIsEmpty (int i, int t){
    return( this.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().isEmpty());
    }
   
    
    public void numberLabels(){
    	for (int i = 0; i < this.getInputLabelsSize(); i++){
    		this.getInputLabels().get(i).setLabelPosition(i);
    		int j = 0;
    		for (j = 0; j < this.getSentenceArraySize(i); j++){
    			this.getInputLabels().get(i).getSentenceArray().get(j).setSentencePosition(j);
    		}
    	}
    }
   
    public ArrayList<PhraseCluster> createClusters(){
    	ArrayList<PhraseCluster> alternativeClusters = new ArrayList<PhraseCluster>();
		ArrayList<PhraseCluster> frequencyClusters = new ArrayList<PhraseCluster>();
		for (int labelCounter = 0; labelCounter < this.getInputLabelsSize(); labelCounter++){
			for (int sentenceCounter = 0; sentenceCounter < this.getSentenceArraySize(labelCounter); sentenceCounter++){
				PhraseCluster currentCluster = new PhraseCluster();
				currentCluster.setWasLabel(labelCounter);
				currentCluster.setWasSentence(sentenceCounter);
				currentCluster.setAlternativeCluster(true);
				currentCluster.setAllPhrases(this.getInputLabels().get(labelCounter).getSentenceArray().get(sentenceCounter).getPossiblePhrases());
				alternativeClusters.add(currentCluster);
			}
		}
		LabelList newList = this.cloneList(); 
		// Stores all possible phrases to all labels and sentences in order on ArrayList "alternativeClusters"
		// This List will be displayed last in the final output file, it needs to be created first though
		while (newList.getInputLabelsSize() > 0){
			Map<String,Integer> frequencies = new HashMap<String, Integer>();
			// In this map, the frequencies of all phrases are stored and linked to the corresponding phrase
			Map<String,ArrayList<ArrayList<Integer>>> labelsOfPhrases = new HashMap<String, ArrayList<ArrayList<Integer>>>();
			// In this map, the position of labels in the original input list is stored and linked to the corresponding phrase
			Map<String,ArrayList<ArrayList<Integer>>> positionsMap = new HashMap<String, ArrayList<ArrayList<Integer>>>();
			// In this map, the position of labels in the shortened input list is stored and linked to the corresponding phrase
			for (int labelCounter = 0; labelCounter < newList.getInputLabelsSize(); labelCounter++){
				for (int sentenceCounter = 0; sentenceCounter < newList.getSentenceArraySize(labelCounter); sentenceCounter++){
					for (int phraseCounter = 0; phraseCounter < newList.getInputLabels().get(labelCounter).getSentenceArray().get(sentenceCounter).getPossiblePhrases().size(); phraseCounter++){
						if (frequencies.containsKey(newList.getInputLabels().get(labelCounter).getSentenceArray().get(sentenceCounter).getPossiblePhrases().get(phraseCounter).getFullContent())){
							// If the maps already contain the current phrase...
							frequencies.put(newList.getInputLabels().get(labelCounter).getSentenceArray().get(sentenceCounter).getPossiblePhrases().get(phraseCounter).getFullContent(), frequencies.get(newList.getInputLabels().get(labelCounter).getSentenceArray().get(sentenceCounter).getPossiblePhrases().get(phraseCounter).getFullContent())+1);
							// Increase the frequency counter of said phrase by 1 and...
							ArrayList<ArrayList<Integer>> tempListList = new ArrayList<ArrayList<Integer>>();
							tempListList = labelsOfPhrases.get(newList.getInputLabels().get(labelCounter).getSentenceArray().get(sentenceCounter).getPossiblePhrases().get(phraseCounter).getFullContent());
							ArrayList<Integer> tempList = new ArrayList<Integer>();
							tempList.add(newList.getInputLabels().get(labelCounter).getLabelPosition());
							tempList.add(newList.getInputLabels().get(labelCounter).getSentenceArray().get(sentenceCounter).getSentencePosition());
							tempListList.add(tempList);
							labelsOfPhrases.put(newList.getInputLabels().get(labelCounter).getSentenceArray().get(sentenceCounter).getPossiblePhrases().get(phraseCounter).getFullContent(), tempListList);
							// Add the position in the original input list to labelsOfPhrases and...
							ArrayList<ArrayList<Integer>> tempListList2 = new ArrayList<ArrayList<Integer>>();
							tempListList2 = positionsMap.get(newList.getInputLabels().get(labelCounter).getSentenceArray().get(sentenceCounter).getPossiblePhrases().get(phraseCounter).getFullContent());
							ArrayList<Integer> tempList2 = new ArrayList<Integer>();
							tempList2.add(labelCounter);
							tempList2.add(sentenceCounter);
							tempListList2.add(tempList2);
							positionsMap.put(newList.getInputLabels().get(labelCounter).getSentenceArray().get(sentenceCounter).getPossiblePhrases().get(phraseCounter).getFullContent(), tempListList2);
							// Add the position in the shortened input list to positionsMap
						}
						else {
							// If the maps does not already contain the current phrase...
							frequencies.put(newList.getInputLabels().get(labelCounter).getSentenceArray().get(sentenceCounter).getPossiblePhrases().get(phraseCounter).getFullContent(), 1);
							// Add the phrase to frequencies with counter 1 and ...
							ArrayList<ArrayList<Integer>> tempListList = new ArrayList<ArrayList<Integer>>();
							ArrayList<Integer> tempList = new ArrayList<Integer>();
							tempList.add(newList.getInputLabels().get(labelCounter).getLabelPosition());
							tempList.add(newList.getInputLabels().get(labelCounter).getSentenceArray().get(sentenceCounter).getSentencePosition());
							tempListList.add(tempList);
							labelsOfPhrases.put(newList.getInputLabels().get(labelCounter).getSentenceArray().get(sentenceCounter).getPossiblePhrases().get(phraseCounter).getFullContent(), tempListList);
							// Add the phrase to labelsOfPhrases with the corresponding label and sentence position of the original input list and ...
							ArrayList<ArrayList<Integer>> tempListList2 = new ArrayList<ArrayList<Integer>>();
							ArrayList<Integer> tempList2 = new ArrayList<Integer>();
							tempList2.add(labelCounter);
							tempList2.add(sentenceCounter);
							tempListList2.add(tempList2);
							positionsMap.put(newList.getInputLabels().get(labelCounter).getSentenceArray().get(sentenceCounter).getPossiblePhrases().get(phraseCounter).getFullContent(), tempListList2);
							// Add the phrase to positionsMap with the corresponding label and sentence position of the shortened input list

						}
					}
				}
			}
			frequencies = sortByValue(frequencies);
			// sorts Map by value with highest value first
			PhraseCluster currentCluster = new PhraseCluster();
			// creates Cluster that will be filled
			Map.Entry<String,Integer> entry = frequencies.entrySet().iterator().next();
			currentCluster.setBuiltPhrase(entry.getKey());
			// Writes first KEY of map [a phrase content as string] on the current cluster
			ArrayList<ArrayList<Integer>> positions = labelsOfPhrases.get(entry.getKey());
			currentCluster.setLabelAndSentencePositions(positions);
			// saves the positions of original labels AND sentences on the cluster
			// They are mapped as a ArrayList of ArrayLists of Integers, in the following way:
			// In each ArrayList of Integers, there are only two entries: The first one is the original position of the label
			// while the second one is the original position of the sentence within the label
			// Example: [5,3] refers to the 3rd sentence of the 5th label from the original list
			positions = positionsMap.get(entry.getKey());
			// Saves the positions of the current phrase in the shortened list in order to remove them for further counts
			currentCluster.setAlternativeCluster(false);
			for (int removalCounter = 0; removalCounter < positions.size(); removalCounter++){
				int tempInt = positions.get(positions.size()-removalCounter-1).get(0);
				if (newList.getSentenceArraySize(tempInt) == 1){
					newList.getInputLabels().remove(tempInt);

				}
				else {
					int tempInt2 = positions.get(positions.size()-removalCounter-1).get(1);
					newList.getInputLabels().get(tempInt).getSentenceArray().remove(tempInt2);
				}
			}
			// removes Labels from which a phrase was already displayed as an optimal phrase from counting
			frequencyClusters.add(currentCluster);
		}
		for (int i = 0; i < alternativeClusters.size(); i++){
			frequencyClusters.add(alternativeClusters.get(i));
		}
		
		return frequencyClusters;
	}
   

    

    
       
 
    public int getSentenceSizeofremainingLabels(int i){
    	return(this.getInputLabels().get(i).getSentenceArray().size());
    }
 

           
       
    }