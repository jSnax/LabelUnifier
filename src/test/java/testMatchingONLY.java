import java.io.IOException;

import de.uni_koblenz.cluster.WordCluster;
import de.uni_koblenz.label.LabelList;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.dictionary.Dictionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class testMatchingONLY {
	public static void main (String args[]) throws JWNLException, ClassNotFoundException, IOException {
		Dictionary dictionary = Dictionary.getDefaultResourceInstance();
		/*
		 *  IF YOU CHANGE INPUT LABELS:
		 */
		// delte the labellist.txt file in src/test/resources
		String[] input=new String[] {
				"checking invoice",
				"verifying bill"
		};
		
		LabelList testList=new LabelList();
		LabelList safetyList=new LabelList();
		// SOURCE:http://www.avajava.com/tutorials/lessons/how-do-i-write-an-object-to-a-file-and-read-it-back.html
		try {
			// read object from file
			FileInputStream fis = new FileInputStream("src/test/resources/labellist.txt");
			System.out.println("FOUND EXISTING FILE");
		} catch (FileNotFoundException e) {
			try {
				System.out.println("NO FILE FOUND. CREATING NEW FILE");
				// Fill Labels with word arrays
				LabelList inputList = new LabelList(input);
				// write object to file
				FileOutputStream fos = new FileOutputStream("src/test/resources/labellist.txt");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(inputList);
				oos.close();
			} catch (IOException e2) {
				e.printStackTrace();
			} 
		} finally{
			FileInputStream fis = new FileInputStream("src/test/resources/labellist.txt");
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			testList = (LabelList) ois.readObject();
			ois.close();
			System.out.print(testList);
			fis.close();
			FileInputStream fis2 = new FileInputStream("src/test/resources/labellist.txt");
			ObjectInputStream ois2 = new ObjectInputStream(fis2);
			
			safetyList = (LabelList) ois2.readObject();
			ois2.close();
		}
		// make copy of original testList

		/*
		 *  ORIGINAL CODE FROM MAIN 
		 */
		// Simply print the two labels
		testList.findSynsets();
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
		List<WordCluster> AllClusters = testList.matchSynonyms();

		for (int i = 0; i < AllClusters.size(); i++){
			System.out.println("Cluster " +i);
			for (int j = 0; j < AllClusters.get(i).matchingWords.size(); j++){
				System.out.println(AllClusters.get(i).matchingWords.get(j).getBaseform());
			}
		}
		// Prints Wordclusters
		System.out.println("Printing LabelClusters");

		safetyList.findSynsets();
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
/*
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
		p1.setseparatedContent(l1);
		
		Phrase p2 = new Phrase();
		p2.setseparatedContent(l2);
		
		List<Phrase> phraseKram = new ArrayList<Phrase>();
		phraseKram.add(p1);
		phraseKram.add(p2);
		PhraseList phraseList = new PhraseList();
		phraseList.setPhrases(phraseKram);
		phraseList.phraseSpace();
		System.out.println("Kram" + phraseList.getPhrases().get(0).getPersonalVectorSpace());
		System.out.println(phraseList.getPhrases().get(1).getPersonalVectorSpace());
		
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
	    List<List<String>> wholeInput = new ArrayList<List<String>>();
	    wholeInput.add(fulllabellist1);
	    wholeInput.add(fulllabellist2);
	    
	    phraseList.setWholeInput(wholeInput);
	    phraseList.calculatePersonalVectors();
	    System.out.println(phraseList.getPhrases().get(0).getPersonalVector());
	    phraseList.tfIDFApplier();
	  		
		double simres;
		simres =phraseList.calcVecSim(phraseList.getPhrases().get(0).getVectorNumeration(), phraseList.getPhrases().get(1).getVectorNumeration());
		System.out.println("Vector Similarity between phrase 1 and phrase 2" + simres);
		*/

		
	}
}
