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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


import de.uni_koblenz.cluster.*;
import de.uni_koblenz.enums.*;
import de.uni_koblenz.label.*;
import de.uni_koblenz.phrase.*;
import de.uni_koblenz.similarity.*;
import de.uni_koblenz.similarity.CosinePhraseSim;





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
		label1.getSentenceArray().get(0).setWordsarray(list1);
		label2.getSentenceArray().get(0).setWordsarray(list2);
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
			for (int k = 0; k < testList.getInputLabels().get(i).getSentenceArray().size(); k++) {
				for (int j = 0; j < testList.getInputLabels().get(i).getSentenceArray().get(k).getWordsarray().size(); j++){
					System.out.println(testList.getInputLabels().get(i).getSentenceArray().get(k).getWordsarray().get(j).getBaseform());
				}
			}
		}
		// Simply print the two labels
		testList.findSynsets(testList);
		// Fill Synonym lists for each word
		System.out.println("Printing Synonyms:");
		for (int i = 0; i < testList.getInputLabels().size(); i++){
			for (int k = 0; k < testList.getInputLabels().get(i).getSentenceArray().size(); k++) {
				for (int j = 0; j < testList.getInputLabels().get(i).getSentenceArray().get(k).getWordsarray().size(); j++){
					System.out.println(testList.getInputLabels().get(i).getSentenceArray().get(k).getWordsarray().get(j).getSynonyms());
				}
			}
		}
		// Print synonym lists
		System.out.println("Printing Wordclusters");
		List<WordCluster> AllClusters = new ArrayList<WordCluster>();
		LabelList safetyList = new LabelList();
		safetyList.setInputLabels(labellist1);
		safetyList.findSynsets(safetyList);
		int Position = 0;
		while (safetyList.getInputLabels().size() != 0){
			WordCluster tempCluster = new WordCluster(safetyList);
			safetyList = safetyList.matchSynonyms(safetyList, tempCluster, Position);
			AllClusters.add(tempCluster);
			Position++;
		}
		for (int i = 0; i < AllClusters.size(); i++){
			System.out.println("Cluster " +i);
			for (int j = 0; j < AllClusters.get(i).matchingWords.size(); j++){
				System.out.println(AllClusters.get(i).matchingWords.get(j).getBaseform());
			}
		}
		// Prints Wordclusters
		System.out.println("Printing LabelClusters");
		list1.add(CHECK);
		list1.add(INVOICE);
		list2.add(VERIFY);
		list2.add(BILL);
		// Create lists over words
		label1.getSentenceArray().get(0).setWordsarray(list1);;
		label2.getSentenceArray().get(0).setWordsarray(list2);
		// Fill Labels with word arrays
		labellist1.add(label1);
		labellist1.add(label2);
		safetyList.setInputLabels(labellist1);
		safetyList.findSynsets(safetyList);
		// TEST DOESN'T WORK AS EXPECTED RIGHT NOW
		// testList references same object as safetyList, so both Lists are empty at this point
		List<LabelCluster> AllLabelClusters = new ArrayList<LabelCluster>();
		while (safetyList.getInputLabels().size() != 0){
			LabelCluster tempLCluster = new LabelCluster(safetyList);
			safetyList = safetyList.matchLabels(safetyList, AllClusters, tempLCluster);
			AllLabelClusters.add(tempLCluster);
		}
		for (int i = 0; i < AllLabelClusters.size(); i++){
			System.out.println("Cluster " +i);
			for (int j = 0; j < AllLabelClusters.get(i).matchingLabels.size(); j++){
				System.out.println("Label " +j);
				for (int z = 0; z < AllLabelClusters.get(i).matchingLabels.get(j).getSentenceArray().size(); z++){
					for(int k = 0; k < AllLabelClusters.get(i).matchingLabels.get(j).getSentenceArray().get(z).getWordsarray().size(); k ++)
					System.out.println(AllLabelClusters.get(i).matchingLabels.get(j).getSentenceArray().get(k).getWordsarray().get(k).getBaseform());
				}
			}
		}
		
		String[] test=new String[] {"checking invoice","This is a short sentence","This is a sentence."};
		System.out.println("Algorithm started");

		LabelList input=new LabelList(test);
		
		System.out.println("Algorithm completed");
		//print all words and variables
		System.out.println(input);

	/*	List<String> l1;
		l1.add("Check");
		l1.add("if"); 
		l1.add("Bachelor");
		l1.add("Degree");
		l1.add("is");
		l1.add("within"); 
				
		List<String> l2;
		l2.add("Check");
		l2.add("if");
		l2.add("Bachelor");
		l2.add("is");
		l2.add("sufficient");
		
		Vector<String> l1l2spac = new Vector<String>();
		l1l2spac = phraseSpace(l1, l2);
		
		Vector<String> v1 = new Vector<String>();
		v1 = phraseToVec(l1, l1l2spac);
		
		Vector<String> v2 = new Vector<String>();
		v2 = phraseToVec(l2, l1l2spac);
		
		String input1 = "";
		input1 = new String(Files.readAllBytes(Paths.get("/FUB.txt")));
		String[] items = input1.split(",");
	    List<String> fulllabellist1 = new ArrayList<String>(Arrays.asList(items));
	      
	      
		String input2 = "";
		input1 = new String(Files.readAllBytes(Paths.get("/TUM.txt")));
		String[] item = input2.split(",");
	    List<String> fulllabellist2 = new ArrayList<String>(Arrays.asList(item));
		
	    List<List<String>> ultimate;
	    ultimate.add(fulllabellist1);
	    ultimate.add(fulllabellist2);
		double[] d1 = applyTFIDFinVector(ultimate, fulllabellist1, v1);
		double[] d2 = applyTFIDFinVector(ultimate, fulllabellist2, v2);
		
		double simres; 
		simres = calcVecSim(d1, d2);
		*/
	}	
}
