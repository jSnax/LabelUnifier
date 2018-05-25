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


public class MainPP {
	public static void main (String args[]) throws JWNLException {
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
		Word[] array1 = {CHECK, INVOICE};
		Word[] array2 = {VERIFY, BILL};
		// Create arrays over words
		label1.setWordsarray(array1);
		label2.setWordsarray(array2);
		// Fill Labels with word arrays
		LabelList testList = new LabelList();
		Label[] labelarray1 = {label1, label2};
		// Create empty Label array and fill it with the two labels
		testList.setInputLabels(labelarray1);
		// Set Input Labels to previously created Label array
		System.out.println("Printing Labels:");
		for (int i = 0; i < testList.getInputLabels().length; i++){
			for (int j = 0; j < testList.getInputLabels()[i].getWordsarray().length; j++){
				System.out.println(testList.getInputLabels()[i].getWordsarray()[j].getBaseform());
			}	
		}
		// Simply print the two labels
		testList.findSynsets(labelarray1);
		// Fill Synonym lists for each word
		System.out.println("Printing Synonyms:");
		for (int i = 0; i < testList.getInputLabels().length; i++){
			for (int j = 0; j < testList.getInputLabels()[i].getWordsarray().length; j++){
				System.out.println(testList.getInputLabels()[i].getWordsarray()[j].getSynonyms());
			}	
		}
		// Print synonym lists
		System.out.println("Printing Wordclusters");
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
		// Prints Wordclusters
	}
}
