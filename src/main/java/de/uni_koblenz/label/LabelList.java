package de.uni_koblenz.label;
 
import java.util.ArrayList;
import java.util.List;
 
import de.uni_koblenz.cluster.PhraseCluster;
import de.uni_koblenz.cluster.WordCluster;
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
    // new variable #####################################
   
    public transient static StanfordCoreNLP pipeline;
    public LabelList() {
       
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
    
    //shorter version of cloneList
    public LabelList cloneList() {
    	LabelList clone = new LabelList();
    	List<Label> tempLabelList = new ArrayList<Label>();
    	
    	for (Label l : this.getInputLabels()) {
    		Label tempLabel = new Label();
    		List<Sentence> tempSentenceList = new ArrayList<Sentence>();  
    		for (Sentence s : l.getSentenceArray()) {
    			Sentence tempSentence = new Sentence();
    			List<Word> tempWordList = new ArrayList<Word>();
    			for (Word w : s.getWordsarray()) {
    				tempWordList.add(w.cloneWord());
    			}
    			tempSentence.setWordsarray(tempWordList);
    			tempSentenceList.add(tempSentence);
    		}	
    		tempLabel.setSentenceArray(tempSentenceList);
    		tempLabelList.add(tempLabel);
    	}
    	clone.setInputLabels(tempLabelList);
    	return(clone);
    }
    
   
    public void findSynsets(ForbiddenWords banList) throws JWNLException{
        Dictionary dictionary = Dictionary.getDefaultResourceInstance();
        net.sf.extjwnl.data.IndexWord tempWord;
        List<net.sf.extjwnl.data.Synset> tempSyn;
        PointerTargetNodeList nodelist = null;
       
        //shorter version of the for loops below ##############################
       
        // iteration over all labels
        for (Label l : this.getInputLabels()) {
            // iteration over all sentences in label
            for (Sentence s : l.getSentenceArray()) {
                // iteration over all words in sentence
                for (Word w : s.getWordsarray()) {
                    if ((w.getPartOfSpeech().getJwnlType() != null) && (!banList.isForbiddenWord(w))){
                    	// TODO: Exception for "will" as a verb or just refining the function as whole, e.g. stop using contains below
                    	// Changed to: Incorporate list of forbidden words
                        tempWord = dictionary.getIndexWord(w.getPartOfSpeech().getJwnlType(), w.getBaseform());
                        // Transform baseform of Word j in Label i into an indexWord so extjwnl can use it
                        try{
                            tempSyn = tempWord.getSenses();
                            // Sysnte for Word j
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
                            }
                        }
                        catch (NullPointerException q){
                        	w.setSynonyms(new ArrayList<String>());
                        	w.addSynonym(w.getBaseform());
                        }

                    }
                    else{
                        w.setSynonyms(new ArrayList<String>());
                        w.addSynonym(w.getBaseform());
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
		        				//if ((definingWord.getSynonyms().contains(w.getBaseform()) && w.getBaseform() != "be" && w.getBaseform()!= "have") || w.getSynonyms().contains(definingWord.getBaseform())) {
		        				// TODO: Change back to old if-clause located above if needed
		        				if (definingWord.isSynonym(w.getBaseform())  || w.isSynonym(definingWord.getBaseform())) {
		        					Cluster.matchingWords.add(w);
		        					w.setClusterPosition(position);
		        					// Most basic form of matching. If POS match and one word is a synonym of the other, they have the same meaning
		        					//TODO: Add "will" as a verb to exceptions
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
   
   
 
   
    /*public LabelList matchLabels(LabelList remainingLabels, List<WordCluster> ClusterList, PhraseCluster Cluster){
        Label DefiningLabel = remainingLabels.getInputLabels().get(0);
        remainingLabels.getInputLabels().remove(0);
        boolean Equals = true;
        List<Integer> tempIntList = new ArrayList<Integer>();
        for (int i = 0; i < remainingLabels.getInputLabels().size(); i++){
            int j = 0;
            while (Equals && j < remainingLabels.getInputLabels().get(i).getSentenceArray().get(0).getWordsarray().size()){
                if (remainingLabels.getInputLabels().get(i).getSentenceArray().get(0).getWordsarray().get(j).getClusterPosition() != DefiningLabel.getSentenceArray().get(0).getWordsarray().get(j).getClusterPosition()){
                    Equals = false;
                }
                j++;
            }
            /* Iterates over all Words in all Labels. If within a certain label each word at position i lies in the same WordCluster as
               the word at position i in DefiningLabel, they are considered equal. Else Equals will be set false.
            if (Equals){
                Cluster.matchingLabels.add(remainingLabels.getInputLabels().get(i));
                tempIntList.add(i);
            }
            else Equals = true;
            // Adds the equal label to the LabelCluster
        }
        for (int i = tempIntList.size() - 1; i >= 0; i--){
            int z = tempIntList.get(i);
            remainingLabels.getInputLabels().remove(z);
            tempIntList.remove(i);
        }
        // Removes matched Labels from the LabelList
        return (remainingLabels);
    }
    // It's important to call this method using a LabelCluster that was just created from remainingLabels*/
   
    /* public List<Phrase> generatePhraseList(LabelList remainingLabels, PhraseStructure Structure){
        List<Phrase> ReturnList = new ArrayList<Phrase>();
        for (int i = 0; i < remainingLabels.getInputLabels().size(); i++){
            for (int j = 0; j < remainingLabels.getInputLabels().get(i).getSentenceArray().size(); j++){
                ReturnList.add(remainingLabels.getInputLabels().get(i).getSentenceArray().get(j).toPhrase);
            }
        }
        return(ReturnList);
        */
    
    //kürzere Version von generatePhraseList
    public List<Phrase> generatePhraseList(LabelList remainingLabels, PhraseStructure Structure){
        List<Phrase> ReturnList = new ArrayList<Phrase>();
        for (Label l : remainingLabels.getInputLabels()) {
        	for (Sentence s : l.getSentenceArray()) {
        		ReturnList.add(s.toPhrase);
        	}
        }
        return(ReturnList);
    }
    
    /**public List<Phrase> generatePhraseList(LabelList remainingLabels, PhraseStructure Structure){
        List<Phrase> ReturnList = new ArrayList<Phrase>();
        for (int i = 0; i < remainingLabels.getInputLabels().size(); i++){
//alt:   for (int j = 0; j < remainingLabels.getInputLabels().get(i).getSentenceArray().size(); j++){
                  for (int j = 0; j < this.getSentenceSizeofremainingLabels(i); j++){
                     //alt:   ReturnList.add(remainingLabels.getInputLabels().get(i).getSentenceArray().get(j).toPhrase);
                     ReturnList.add(this.PhraseremainingLabels(i,j));
                }
        }
        return(ReturnList);
}**/
       
        //Methode generatePhrase
 
        public int getSentenceSizeofremainingLabels(int i){
            return(this.getInputLabels().get(i).getSentenceArray().size());
            }
 
        public Phrase PhraseremainingLabels(int i, int j){
            return(this.getInputLabels().get(i).getSentenceArray().get(j).toPhrase);
            }
           
       
    }