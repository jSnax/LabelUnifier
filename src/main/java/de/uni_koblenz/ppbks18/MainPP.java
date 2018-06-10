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




public class MainPP {
	public static void main (String args[]) throws JWNLException, ClassNotFoundException, IOException {
		
		System.out.println("Started.");
		Dictionary dictionary = Dictionary.getDefaultResourceInstance();
		
		String[] input=new String[] {
				"checking invoice",
				"verifying bill"
		};
		

		// Fill Labels with word arrays
		LabelList testList = new LabelList(input);

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
		LabelList safetyList = new LabelList(input);

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

		safetyList.findSynsets(safetyList);
		// TEST DOESN'T WORK AS EXPECTED RIGHT NOW
		// testList references same object as safetyList, so both Lists are empty at this point
		/*List<PhraseCluster> AllLabelClusters = new ArrayList<PhraseCluster>();
		while (safetyList.getInputLabels().size() != 0){
			PhraseCluster tempLCluster = new PhraseCluster(safetyList);
			safetyList = safetyList.matchLabels(safetyList, AllClusters, tempLCluster);
			AllLabelClusters.add(tempLCluster);
		}
		for (int i = 0; i < AllLabelClusters.size(); i++){
			System.out.println("Cluster " +i);
			for (int j = 0; j < AllLabelClusters.get(i).matchingLabels.size(); j++){
				System.out.println("Label " +j);
				for (int z = 0; z < AllLabelClusters.get(i).matchingLabels.get(j).getSentenceArray().size(); z++){
					for(int k = 0; k < AllLabelClusters.get(i).matchingLabels.get(j).getSentenceArray().get(z).getWordsarray().size(); k ++)
					System.out.println(AllLabelClusters.get(i).matchingLabels.get(j).getSentenceArray().get(z).getWordsarray().get(k).getBaseform());
				}
			}
		}
		System.out.print(testList);
		
*/
		
		
	//Phrase calculations test class
		List<String> l1 = new ArrayList<String>();
		l1.add("Check");
		l1.add("if"); 
		l1.add("Bachelor");
		l1.add("is");
		l1.add("within"); 

		List<String> l2 = new ArrayList<String>();
		l2.add("Check");
		l2.add("if");
		l2.add("Bachelor");
		l2.add("sufficient");
		
		Phrase p1 = new Phrase();
		p1.setContent(l1);
		
		Phrase p2 = new Phrase();
		p2.setContent(l2);
		
		
		
		Vector<String> l1l2spac = new Vector<String>();
		l1l2spac = p1.phraseSpace(l1, l2);
//		for(int i = 0; i < l1l2spac.size(); i++){
//			System.out.println(l1l2spac.get(i));
//		}
		
		Vector<String> v1 = new Vector<String>();
		v1 = p1.phraseToVec(l1, l1l2spac);
//		for(int i = 0; i < l1l2spac.size(); i++){
//			System.out.println(v1.get(i));
//		}
		
		Vector<String> v2 = new Vector<String>();
		v2 = p2.phraseToVec(l2, l1l2spac);
//		for(int i = 0; i < l1l2spac.size(); i++){
//			System.out.println(v2.get(i));
//		}
		
		String input1 ="Complete application, Fill out application form, Add certificate of Bachelor degree, Add certificate of German language, Send application, \n" + 
				"Take Interview, Receive Rejection, Receive acceptance, Immatriculate, \n" + 
				"Check application in time, Ckeck application complete, Hand application over to examining board, \n" + 
				"Check if Bachelor is sufficient, Send additional requirements, Check if Bachelors Degree is within top 85%, \n" + 
				"Invite for talk, talk to applicant, Document, Rank with other applicants, \n" + 
				"Send acceptance, Send rejection";
		
		String[] items1 = input1.split("\\s*(=>|,|\\s)\\s*");
	    List<String> fulllabellist1 = new ArrayList<String>(Arrays.asList(items1));
	      
//	      for(int i = 0; i < fulllabellist1.size(); i++ ){
//	    	  	System.out.println(fulllabellist1.get(i));
//	      }
	      
		String input2 = "Apply online, Send documents by post, Wait for results, Go to the Interview, \n" + 
				"Check Bachelor's degree, Check documents, Forward Documents, \n" + 
				"Evaluate, Send interview Invitation, Send letter of rejection, Send letter of acceptance, Conduct Interview, Send letter of rejection, Send letter of acceptance\n";
		
		String[] items2 = input2.split("\\s*(=>|,|\\s)\\s*");
	    List<String> fulllabellist2 = new ArrayList<String>(Arrays.asList(items2));

		
//	    for(int i = 0; i < fulllabellist2.size(); i++ ){
//    	  		System.out.println(fulllabellist2.get(i));
//	    }
	    List<List<String>> ultimate = new ArrayList<List<String>>();
	    ultimate.add(fulllabellist1);
	    ultimate.add(fulllabellist2);
	    
		double[] d1 = p1.applyTFIDFinVector(fulllabellist1, ultimate, v1);
		/*
		for(int i = 0; i < d1.length; i++){
			System.out.println(d1[i]);
		}
		*/
		
		double[] d2 = p2.applyTFIDFinVector(fulllabellist2, ultimate, v2);
		/*
		for(int i = 0; i < d2.length; i++){
			System.out.println(d2[i]);
		}
		*/
	    
	    double first = p1.calcTF(fulllabellist1, "check");
	    System.out.println("First Phrase TF value for \"check\": " + first);
	    
	    double result = p2.calcIDF(ultimate, v2.get(1));
	    System.out.println("IDF for \"if\" in the whole input (labellist1 + labellist2): " + result);
	    
	    double res = p1.calcTFIFD(fulllabellist1,ultimate,v2.get(1));
	    System.out.println("TFIDF for \"if\" in the whole labellist " + res);
	  		
		double simres;
		simres = p1.calcVecSim(d1, d2);
		System.out.println("Vector Similarity between phrase 1 and phrase 2" + simres);

	}	
}
