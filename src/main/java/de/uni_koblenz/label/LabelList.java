package de.uni_koblenz.label;

import java.util.ArrayList;
import java.util.List;

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
		for (int i=0; i < allLabels.length; i++){
			// Iterate over all Labels, i refers to the current Label
			for (int j = 0; j < allLabels[i].getWordsarray().length; j++){
				// Iterate over all Words in a certain Label, j refers to the current Word
				tempWord = dictionary.getIndexWord(POS.VERB, allLabels[i].getWordsarray()[j].getBaseform());
				// Transform baseform of Word j in Label i into an indexWord so extjwnl can use it
				// NOT FINAL: POS hard coded as VERB here, but it should correspond to getPOS in Class Word
				tempSyn = tempWord.getSenses();
				// Synset for Word j
				allLabels[i].getWordsarray()[j].setSynonyms(new ArrayList<String>());
				// pre-create the Synonym list for Word j
				// CAUTION: This will override any pre-existing synonym list, so this method may only be called once
				for (int z=0; z < tempSyn.size(); z++){
					// Iterate over all meanings in the synset, z refers to the current meaning
					PointerTargetNodeList nodelist=PointerUtils.getDirectHypernyms(tempSyn.get(z));
					// Copy all synonyms to nodelist
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
}