package de.uni_koblenz.label;

import java.util.ArrayList;
import java.util.List;

import de.uni_koblenz.cluster.WordCluster;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.list.PointerTargetNode;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.dictionary.Dictionary;

public class LabelList {
	
	private Label[] inputLabels;
	private Word[] allWords;
	
	public LabelList() {
		
	}
	
	public LabelList(Word[] allWords) {
		this.allWords = allWords;
	}

	public Label[] getInputLabels() {
		return inputLabels;
	}

	public void setInputLabels(Label[] inputLabels) {
		this.inputLabels = inputLabels;
	}

	public Word[] getAllWords() {
		return allWords;
	}

	public void setAllWords(Word[] allWords) {
		this.allWords = allWords;
	}
	
	
	
	public void findSynsets(Label[] allLabels) throws JWNLException{
		Dictionary dictionary = Dictionary.getDefaultResourceInstance();
		net.sf.extjwnl.data.IndexWord tempWord;
		List<net.sf.extjwnl.data.Synset> tempSyn;
		PointerTargetNodeList nodelist = null;
		for (int i=0; i < allLabels.length; i++){
			// Iterate over all Labels, i refers to the current Label
			for (int j = 0; j < allLabels[i].getWordsarray().length; j++){
				// Iterate over all Words in a certain Label, j refers to the current Word
				tempWord = dictionary.getIndexWord(allLabels[i].getWordsarray()[j].getPartOfSpeech().getJwnlType(), allLabels[i].getWordsarray()[j].getBaseform());
				// Transform baseform of Word j in Label i into an indexWord so extjwnl can use it
				tempSyn = tempWord.getSenses();
				// Synset for Word j
				allLabels[i].getWordsarray()[j].setSynonyms(new ArrayList<String>());
				// pre-create the Synonym list for Word j
				// CAUTION: This will override any pre-existing synonym list, so this method may only be called once
				for (int z=0; z < tempSyn.size(); z++){
					// Iterate over all meanings in the synset, z refers to the current meaning
					if (allLabels[i].getWordsarray()[j].getPartOfSpeech().getJwnlType() != POS.ADJECTIVE){
						nodelist=PointerUtils.getCoordinateTerms(tempSyn.get(z));
						for(PointerTargetNode node:nodelist) {
							for(net.sf.extjwnl.data.Word word:node.getSynset().getWords()) {
								if (!allLabels[i].getWordsarray()[j].getSynonyms().contains(word.getLemma()))
								allLabels[i].getWordsarray()[j].addSynonym(word.getLemma());
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
							if (!allLabels[i].getWordsarray()[j].getSynonyms().contains(word.getLemma()))
							allLabels[i].getWordsarray()[j].addSynonym(word.getLemma());
							// Go through the synonym list and add each synonym to synonym list for word j, unless it's already in there 
						}
					}
				}
			}
		}
	}
	
	public LabelList matchSynonyms(LabelList RemainingLabels, Word DefiningWord, WordCluster Cluster) {
		List<List<Word>> RemainingLabelsAsList = new ArrayList<List<Word>>();
		List<Integer> tempIntList = new ArrayList<Integer>();
		for (int i = 0; i < RemainingLabels.getInputLabels().length; i++){
			List<Word> tempWordList = new ArrayList<Word>();
			for (int j = 0; j < RemainingLabels.getInputLabels()[i].getWordsarray().length; j++){
				tempWordList.add(RemainingLabels.getInputLabels()[i].getWordsarray()[j]);
			}
			RemainingLabelsAsList.add(tempWordList);
		}
		RemainingLabelsAsList.get(0).remove(0);
		// Transforms the Array of Arrays of Words into a List of List of Words. The first entry is removed since it was used to create the wordcluster
		// If we change LabelList to an actual List instead of an array, this becomes redundant
		for (int i = 0; i < RemainingLabelsAsList.size(); i++){
			for (int j = 0; j < RemainingLabelsAsList.get(i).size(); j++){
				if (DefiningWord.getPartOfSpeech() == RemainingLabelsAsList.get(i).get(j).getPartOfSpeech()){
					if (DefiningWord.getSynonyms().contains(RemainingLabelsAsList.get(i).get(j).getBaseform()) || RemainingLabelsAsList.get(i).get(j).getSynonyms().contains(DefiningWord.getBaseform())){
						Cluster.matchingWords.add(RemainingLabelsAsList.get(i).get(j));
						tempIntList.add(j);
						// Most basic form of matching. If POS match and one word is a synonym of the other, they have the same meaning
						}
					}
				}
			for (int j = tempIntList.size() - 1; j >= 0; j--){
				int z = tempIntList.get(j);
				RemainingLabelsAsList.get(i).remove(z);
				tempIntList.remove(j);
				// Removing all words in current label that found a match
				// This has to be called after the previous for-loop in order to not mess around with the list size while iterating over it
			}
			}
		for (int i = RemainingLabelsAsList.size() - 1; i >= 0; i--){
			if (RemainingLabelsAsList.get(i).isEmpty()){
				RemainingLabelsAsList.remove(i);
				// Removing all labels that contain no more unmatched words
				// This has to be called after the previous for-loop in order to not mess around with the list size while iterating over it
			}
		}
		LabelList returningList = new LabelList();
		Label[] returningArray = new Label[RemainingLabelsAsList.size()];
		for (int i = RemainingLabelsAsList.size()-1; i >= 0; i--){
			Word[] temp = new Word[RemainingLabelsAsList.get(i).size()];
			for (int j = RemainingLabelsAsList.get(i).size() - 1; j >= 0; j--){
				temp[j] = RemainingLabelsAsList.get(i).get(j);
			}
			Label tempLabel = new Label();
			tempLabel.setWordsarray(temp);
			returningArray[i] = tempLabel;
		}
		returningList.setInputLabels(returningArray);
		// Transforms the list back into an array. Naturally, this will also be useless if we change LabelList to an actual list
		return(returningList);
		/* At this point, it seems logical to provide an example implementation of this function in a main-method. 
		* In this example, testList is not defined. It has to be of type LabelList however.
		* 
		List<WordCluster> AllClusters = new ArrayList<WordCluster>();
		LabelList safetyList = new LabelList();
		safetyList = testList;
		while (safetyList.getInputLabels().length != 0){
			WordCluster tempCluster = new WordCluster(safetyList);
			safetyList = safetyList.matchSynonyms(safetyList, safetyList.getInputLabels()[0].getWordsarray()[0], tempCluster);
			AllClusters.add(tempCluster);
		}
		for (int i = 0; i < AllClusters.size(); i++){
			System.out.println("Cluster " +i);
			for (int j = 0; j < AllClusters.get(i).matchingWords.size(); j++){
				System.out.println(AllClusters.get(i).matchingWords.get(j).getBaseform());
			}
		}
		*
		 */
	}
}