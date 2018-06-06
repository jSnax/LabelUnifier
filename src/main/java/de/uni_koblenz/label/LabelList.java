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


public class LabelList {
	

	private List<Word> allWords = new ArrayList<Word>();
	private List<Label> inputLabels = new ArrayList<Label>();
	// new variable #####################################
	
	public static StanfordCoreNLP pipeline;
	public LabelList() {
		
	}

	// NEW METHOD ########################################
	
	public LabelList(String[] input) throws JWNLException {
		// Setup CORENLP Pipeline
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, depparse, natlog, openie");//, ner");
		pipeline = new StanfordCoreNLP(props);
		
		// add Label objects for each string of the input array.
		for (String stringLabel:input) {
			inputLabels.add(new Label(stringLabel));
		}
		
		// transform arraylist into array and store it in LabeList.

		
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
		for(int labelZaehler = 0; labelZaehler < this.inputLabels.size(); labelZaehler++) {	//Iteration over Labels
			for (int satzZaehler = 0; satzZaehler < this.inputLabels.get(labelZaehler).getSentenceArray().size(); satzZaehler++) {
				for(int wordZaehler = 0; wordZaehler < this.inputLabels.get(labelZaehler).getSentenceArray().get(satzZaehler).getWordsarray().size(); wordZaehler++) {//Iteration over Words
					this.allWords.add(this.inputLabels.get(labelZaehler).getSentenceArray().get(satzZaehler).getWordsarray().get(wordZaehler));		//add Word to allWords[]
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
	
	
	public void findSynsets(LabelList allLabels) throws JWNLException{
		Dictionary dictionary = Dictionary.getDefaultResourceInstance();
		net.sf.extjwnl.data.IndexWord tempWord;
		List<net.sf.extjwnl.data.Synset> tempSyn;
		PointerTargetNodeList nodelist = null;
		for (int i=0; i < allLabels.getInputLabels().size(); i++){
			// Iterate over all Labels, i refers to the current Label
				for (int t = 0; t < allLabels.getInputLabels().get(i).getSentenceArray().size(); t++){
					// Iterate over all Sentences in a certain Label, t refers to the current Sentence
					for (int j = 0; j < allLabels.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().size(); j++){
						// Iterate over all Words in a certain Sentence, j refers to the current Word
						tempWord = dictionary.getIndexWord(allLabels.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().get(j).getPartOfSpeech().getJwnlType(), allLabels.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().get(j).getBaseform());
						// Transform baseform of Word j in Label i into an indexWord so extjwnl can use it
						tempSyn = tempWord.getSenses();
						// Synset for Word j
						allLabels.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().get(j).setSynonyms(new ArrayList<String>());
						// pre-create the Synonym list for Word j
						// CAUTION: This will override any pre-existing synonym list, so this method may only be called once
						for (int z=0; z < tempSyn.size(); z++){
							// Iterate over all meanings in the synset, z refers to the current meaning
							if (allLabels.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().get(j).getPartOfSpeech().getJwnlType() != POS.ADJECTIVE){
								nodelist=PointerUtils.getCoordinateTerms(tempSyn.get(z));
								for(PointerTargetNode node:nodelist) {
									for(net.sf.extjwnl.data.Word word:node.getSynset().getWords()) {
										if (!allLabels.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().get(j).getSynonyms().contains(word.getLemma()))
											allLabels.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().get(j).addSynonym(word.getLemma());
										// Go through the synonym list and add each synonym to synonym list for word j, unless it's already in there 
									}
								}
								nodelist=PointerUtils.getDirectHypernyms(tempSyn.get(z));
							}
							else {
								nodelist=PointerUtils.getSynonyms(tempSyn.get(z));
							}
							// Copy all synonyms to nodelist.
							// For nouns and verbs, a combination of getDirectHypernyms and getCoordinateTerms has to be used since getSynonyms only works on adjectives
							for(PointerTargetNode node:nodelist) {
								for(net.sf.extjwnl.data.Word word:node.getSynset().getWords()) {
									if (!allLabels.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().get(j).getSynonyms().contains(word.getLemma()))
										allLabels.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().get(j).addSynonym(word.getLemma());
									// Go through the synonym list and add each synonym to synonym list for word j, unless it's already in there 
								}
							}
						}
					}
			}
		}
	}
	
	public LabelList matchSynonyms(LabelList RemainingLabels, WordCluster Cluster, Integer ClusterPosition) {
		List<Integer> tempIntList = new ArrayList<Integer>();
		Word DefiningWord = RemainingLabels.getInputLabels().get(0).getSentenceArray().get(0).getWordsarray().get(0);
		RemainingLabels.getInputLabels().get(0).getSentenceArray().get(0).getWordsarray().get(0).setClusterPosition(ClusterPosition);
		RemainingLabels.getInputLabels().get(0).getSentenceArray().get(0).getWordsarray().remove(0);
		// Removing first entry of RemainingLabels since it is the 
		for (int i = 0; i < RemainingLabels.getInputLabels().size(); i++){
			for (int t = 0; t < RemainingLabels.getInputLabels().get(i).getSentenceArray().size(); t++){
				for (int j = 0; j < RemainingLabels.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().size(); j++){
					if (DefiningWord.getPartOfSpeech().getJwnlType() == RemainingLabels.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().get(j).getPartOfSpeech().getJwnlType()){
						if (DefiningWord.getSynonyms().contains(RemainingLabels.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().get(j).getBaseform()) || RemainingLabels.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().get(j).getSynonyms().contains(DefiningWord.getBaseform())){
							Cluster.matchingWords.add(RemainingLabels.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().get(j));
							RemainingLabels.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().get(j).setClusterPosition(ClusterPosition);
							tempIntList.add(j);
							// Most basic form of matching. If POS match and one word is a synonym of the other, they have the same meaning
							}
						}
					}
				for (int j = tempIntList.size() - 1; j >= 0; j--){
					int z = tempIntList.get(j);
					RemainingLabels.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().remove(z);
					tempIntList.remove(j);
					// Removing all words in current label that found a match
					// This has to be called after the previous for-loop in order to not mess around with the list size while iterating over it
					}
				}
			}
		for (int i = RemainingLabels.getInputLabels().size() - 1; i >= 0; i--){
			for (int t = RemainingLabels.getInputLabels().get(i).getSentenceArray().size() - 1; t >= 0; t--){
				if (RemainingLabels.getInputLabels().get(i).getSentenceArray().get(t).getWordsarray().isEmpty()){
					RemainingLabels.getInputLabels().remove(i);
					// Removing all labels that contain no more unmatched words
					// This has to be called after the previous for-loop in order to not mess around with the list size while iterating over it
				}
		}
		}
		return(RemainingLabels);
	}
	
	/*public LabelList matchLabels(LabelList RemainingLabels, List<WordCluster> ClusterList, LabelCluster Cluster){
		Label DefiningLabel = RemainingLabels.getInputLabels().get(0);
		RemainingLabels.getInputLabels().remove(0);
		boolean Equals = true;
		List<Integer> tempIntList = new ArrayList<Integer>();
		for (int i = 0; i < RemainingLabels.getInputLabels().size(); i++){
			int j = 0;
			while (Equals && j < RemainingLabels.getInputLabels().get(i).getSentenceArray().get(0).getWordsarray().size()){
				if (RemainingLabels.getInputLabels().get(i).getSentenceArray().get(0).getWordsarray().get(j).getClusterPosition() != DefiningLabel.getSentenceArray().get(0).getWordsarray().get(j).getClusterPosition()){
					Equals = false;
				}
				j++;
			}
			/* Iterates over all Words in all Labels. If within a certain label each word at position i lies in the same WordCluster as
			   the word at position i in DefiningLabel, they are considered equal. Else Equals will be set false.
			if (Equals){
				Cluster.matchingLabels.add(RemainingLabels.getInputLabels().get(i));
				tempIntList.add(i);
			}
			else Equals = true;
			// Adds the equal label to the LabelCluster
		}
		for (int i = tempIntList.size() - 1; i >= 0; i--){
			int z = tempIntList.get(i);
			RemainingLabels.getInputLabels().remove(z);
			tempIntList.remove(i);
		}
		// Removes matched Labels from the LabelList
		return (RemainingLabels);
	}
	// It's important to call this method using a LabelCluster that was just created from RemainingLabels */ 
	
	public List<Phrase> generatePhraseList(LabelList RemainingLabels, PhraseStructure Structure){
		List<Phrase> ReturnList = new ArrayList<Phrase>();
		for (int i = 0; i < RemainingLabels.getInputLabels().size(); i++){
			for (int j = 0; j < RemainingLabels.getInputLabels().get(i).getSentenceArray().size(); j++){
				ReturnList.add(RemainingLabels.getInputLabels().get(i).getSentenceArray().get(j).toPhrase);
			}
		}
		return(ReturnList);
	}
}