package de.uni_koblenz.phrase;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uni_koblenz.label.*;
import de.uni_koblenz.enums.*;


public class Phrase {

	private String content;
	private Word[] wordsArray;

	public Phrase() {
		
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Word[] getWordsArray() {
		return wordsArray;
	}
	
	public void setWordsArray(Word[] wordsArray) {
		this.wordsArray = wordsArray;
	}
	
	// returns a list of Phrase Structure
		// a Phrase Structure e. g. <verb, imperative> <noun, singular> would be a list of String itself, where "verb, imperative" is one list element
	public List<List<String>> extractPhraseStructure (String Namingconventions){
//		String Namingconventions = "";
//		Namingconventions = new String(Files.readAllBytes(Paths.get("/Users/Monika/Project/SemanticProcessModeling/src/main/java/de/uni_koblenz/ppbks18/examplePhraseStructure.txt")));
		
		//the pattern for the first groups (activity, state) is left out, focus on it maybe later
		Pattern type = Pattern.compile("(<.*>)[,|\n]");
		List<String> tempPS = new ArrayList<String>();
		Matcher PS = type.matcher(Namingconventions);
		while(PS.find()){
			int i = 0;
			tempPS.add(PS.group(i));
			i++;
		}
//		for(int i = 0; i < tempPS.size(); i++){
//			System.out.println(tempPS.get(i));
//			}
		
		//Parts in this case divides the whole document into single Phrase Structures, so one element would be <verb, imperative> <noun, singular> (or longer)
		Pattern parts = Pattern.compile("(<.*?>)");
		List<List<String>> activityPSs = new ArrayList<List<String>>();
		
		// put these elements into a list of Strings
		for(int i = 0; i < tempPS.size(); i++){
			Matcher pp = parts.matcher(tempPS.get(i));
			List<String> oneAPS = new ArrayList<String>();
			while(pp.find()){
				int j = 0;
				oneAPS.add(pp.group(j));
				j++;
				}
			activityPSs.add(oneAPS);
			}
		// Testing the output of this list
//		for(int i = 0; i < activityPSs.size(); i++){
//			for(int j = 0; j < (activityPSs.get(i)).size(); j++){
//				System.out.println((activityPSs.get(i)).get(j));
//				}
//			System.out.println("");
//			}
//		}
		
		//Here each Phrase Structure is divided into a list of Strings
		// so  <verb, imperative> <noun, singular> is divided into the first list item: verb, imperative and second list item:  <noun, singular>
		List<List<String>> finalPSs = new ArrayList<List<String>>();
		for(int i = 0; i < 4; i++){
			finalPSs.add(activityPSs.get(i));
			}
		
		// Test the output of the List of Lists
//		for(int i = 0; i < finalPSs.size(); i++){
//			for(int j = 0; j < (finalPSs.get(i)).size(); j++){
//				System.out.println((finalPSs.get(i)).get(j));
//				}
//			System.out.println("");
//			}
		return finalPSs;
		/* the structure is as follows
		 * List
		 * 		List
		 * 			<verb, imperative>
		 * 			<noun, singular>
		 * 		List
		 * 			<verb, imperative>
		 * 			<noun, singular>
		 * 			<preposition> 
		 * 			<noun, singular, object case>
		 * 		...
		 */		
		
		}
	
	
	public List<String> findApplicablePS(List<String> lbls, List<List<String>> phraseStructures){
		List<String> resPS = new ArrayList<String>();
		//if Action/State
		// finding out which type of PhraseStructures to use
		for(int i = 0; i < phraseStructures.size(); i++){
			if (lbls.size() == phraseStructures.size()){
				resPS = phraseStructures.get(i);
			}
		}
		return resPS;
	}
	
	
	public boolean fulfillsPhraseStructure(String wordCluster, List<String> lbls, List<String> phraseStructure){
		boolean correct = true;
		int labelIndex = 1;
		int phraseIndex = 1;
		while(labelIndex < lbls.size()){
			String phrasePOS = phraseStructure.get(phraseIndex);
			String labelPOS = ""; 
//			labelPOS = getPartOfSpeech(lbls.get(labelIndex));
			if(phrasePOS != labelPOS){
				correct = false;
				break;
			}
			else {
				labelIndex++;
				phraseIndex++;
			}
		}
		return correct;
	}	
	
	// Calculate the dimension of the Vector for the two labels 
		public static Vector<String> phraseSpace (List<String> p1, List<String> p2){
			
			//initialize the vectors
			Vector<String> space = new Vector<String>();

			//add all the elements in the first list into the vector
			for(int i = 0; i < p1.size(); i++){
				space.add(p1.get(i));
			}
			
			//check if the vector already contains the elements that are iterated over, if not add them
			for(int i = 0; i < p2.size(); i++){
				if(space.contains(p2.get(i)) == false){
					space.add(p2.get(i));
				}
			}
			return space;
		}
		
		
		public static Vector<String> phraseToVec(List<String> phrase, Vector<String> space){
			//initialize the Phrase Vector
			Vector<String> phraseVec = new Vector<String>();
			
			//iterate over the vector space
			for(int i = 0 ; i < space.size(); i++) {
				//if the element of the phraseList is in the vector space, add it to the destination vector 
				if(phrase.contains(space.get(i)))
					phraseVec.add(space.get(i));
				//if the phraseList does not contain an element, add a "nill" value to the vector
				else{
					phraseVec.add("nill");	
				}
			}
			return phraseVec;
		}
		
		//Calculate the Term frequency of the word (how often does this word occur in the document)
		public static double calcTF (List<String> LabelListFinal, String word){
			double result = 0;
			for(String term : LabelListFinal){
				if(word.equalsIgnoreCase(term))
					result ++;
			}	
			return result / LabelListFinal.size();
		}
		
		//Calculate the Inverse Document Frequency (if the word common or rare across the document -> how much information does it contain)
		public static double calcIDF (List<List<String>> docs, String word){
			double res = 0;
			for(List<String> doc : docs){
				for(String term : doc){
					if (word.equalsIgnoreCase(term)){
						res++;
						break;
					}
				}
			}
			return Math.log(docs.size() / res);
		}

		//whole formula for tfifd
		public static double calcTFIFD(List<String> doc, List<List<String>> docs, String word){
			return calcTF(doc, word) ;
					//* calcIDF(docs, word);
		}
		
		public static double[] applyTFIDFinVector(List<String> fullLabelList , List<List<String>> docs, Vector<String> v1){
			double result[] = new double[v1.size()];
			for(int i=0; i < v1.size(); i++){
				//if the string is x, then the Value is 0 in this Vector
				if(v1.get(i) == "nill"){
					result[i] = 0;
				} else {
				// else calculate the TD-IDF and sava the Value in the Vector
				double t = calcTFIFD(fullLabelList, docs, v1.get(i));
				result[i] = t;
				}
			}
			return result;
		}
		
		//Calculate the length of a vector
		public static double metrics (double [] a){
			double result = 0;
			double sumsq = 0;
			for(int i = 0; i < a.length; i++){
				sumsq += a[i]*a[i];
			}
			result = Math.sqrt(sumsq);
			return result;
		}
		
		//calculate the dot product of two Vectors
		public static double dot(double[] a, double[] b) {
		    double sum = 0;
		    for (int i = 0; i < a.length; i++) {
		      sum += a[i] * b[i];
		    }
		    return sum;
		  }
		
		// use the formula for vector similatiry: dot(vec1, vec2) / met(vec1)*met(vec2)
		public static double calcVecSim (double [] v1, double[] v2){
			double result = 0;
			double denm = metrics(v1)*metrics(v2);
			//try this division
	        try{
	        	result = (double) (dot(v1, v2)) / (double) denm; 
	        //if denm == 0, throw exception
	        }catch (ArithmeticException e) {
	            System.out.println ("Can't divide a number by 0");
	            }
			return result;
		}
}

