package de.uni_koblenz.ppbks18;
import java.util.*;

import de.uni_koblenz.cluster.*;
import de.uni_koblenz.enums.*;
import de.uni_koblenz.label.*;
import de.uni_koblenz.phrase.*;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.list.PointerTargetNode;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.dictionary.Dictionary;

import java.io.IOException;

import de.uni_koblenz.label.Label;
import de.uni_koblenz.label.LabelList;
import net.sf.extjwnl.JWNLException;


public class MainPP {
	public static void main (String args[]) throws JWNLException, ClassNotFoundException, IOException {
		System.out.println("Started.");
		Dictionary dictionary = Dictionary.getDefaultResourceInstance();
		Word CHECK = new Word();
		Word INVOICE = new Word();
		Word VERIFY = new Word();
		Word BILL = new Word();
		// Create several words
		CHECK.setBaseform("check");
		INVOICE.setBaseform("invoice");
		VERIFY.setBaseform("verify");
		BILL.setBaseform("bill");
		CHECK.setPartOfSpeech(PartOfSpeechTypes.VERB_BASE);
		INVOICE.setPartOfSpeech(PartOfSpeechTypes.NOUN_SINGULAR_MASS);
		VERIFY.setPartOfSpeech(PartOfSpeechTypes.VERB_BASE);
		BILL.setPartOfSpeech(PartOfSpeechTypes.NOUN_SINGULAR_MASS);
		// set their baseform and their POS
		Label label1 = new Label();
		Label label2 = new Label();
		// Create two Labels
		List<Word> list1 = new ArrayList<Word>();
		List<Word> list2 = new ArrayList<Word>();
		list1.add(CHECK);
		list1.add(INVOICE);
		list2.add(VERIFY);
		list2.add(BILL);
		// Create lists over words
		label1.setWordsarray(list1);
		label2.setWordsarray(list2);
		// Fill Labels with word arrays
		LabelList testList = new LabelList();
		List<Label> labellist1 = new ArrayList<Label>();
		labellist1.add(label1);
		labellist1.add(label2);
		// Create empty Label list and fill it with the two labels
		testList.setInputLabels(labellist1);
		// Set Input Labels to previously created Label list
		System.out.println("Printing Labels:");
		for (int i = 0; i < testList.getInputLabels().size(); i++){
			for (int j = 0; j < testList.getInputLabels().get(i).getWordsarray().size(); j++){
				System.out.println(testList.getInputLabels().get(i).getWordsarray().get(j).getBaseform());
			}	
		}
		// Simply print the two labels
		testList.findSynsets(testList);
		// Fill Synonym lists for each word
		System.out.println("Printing Synonyms:");
		for (int i = 0; i < testList.getInputLabels().size(); i++){
			for (int j = 0; j < testList.getInputLabels().get(i).getWordsarray().size(); j++){
				System.out.println(testList.getInputLabels().get(i).getWordsarray().get(j).getSynonyms());
			}	
		}
		// Print synonym lists
		System.out.println("Printing Wordclusters");
		List<WordCluster> AllClusters = new ArrayList<WordCluster>();
		LabelList safetyList = new LabelList();
		safetyList = testList;
		while (safetyList.getInputLabels().size() != 0){
			WordCluster tempCluster = new WordCluster(safetyList);
			safetyList = safetyList.matchSynonyms(safetyList, tempCluster);
			AllClusters.add(tempCluster);
		}
		for (int i = 0; i < AllClusters.size(); i++){
			System.out.println("Cluster " +i);
			for (int j = 0; j < AllClusters.get(i).matchingWords.size(); j++){
				System.out.println(AllClusters.get(i).matchingWords.get(j).getBaseform());
			}
		}
		
		String[] test=new String[] {"checking invoice","This is a short sentence","This is a sentence."};
		System.out.println("Algorithm started");

		LabelList input=new LabelList(test);
		
		System.out.println("Algorithm completed");
		//print all words and variables
		System.out.println(input);
		// Prints Wordclusters

	}
}
