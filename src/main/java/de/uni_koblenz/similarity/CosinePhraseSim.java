package de.uni_koblenz.similarity;

import java.util.List;
import java.util.Vector;

public class CosinePhraseSim {
	
	// Calculate the dimension of the Vector for the two labels 
	public static Vector<String> phraseSpace (List<String> p1, List<String> p2){
		
		//initialize the vectors
		Vector<String> space = new Vector<String>();

		//add all the elements in the first list into the vector
		for(int i = 1; i <= p1.size(); i++){
			space.add(p1.get(i));
		}
		
		//check if the vector already contains the elements that are iterated over, if not add them
		for(int i = 1; i <= p2.size(); i++){
			if(space.contains(p2.get(i)) == false){
				space.add(p2.get(i));
			}
		}
		return space;
	}
	
	
	public Vector<String> phraseToVec(List<String> phrase, Vector<String> space){
		//initialize the Phrase Vector
		Vector<String> phraseVec = new Vector<String>();
		
		//iterate over the vector space
		for(int i = 1 ; i <= space.size(); i++) {
			//if the element of the phraseList is in the vector space, add it to the destination vector 
			if(phrase.contains(space.get(i)))
				phraseVec.add(phrase.get(i));
			//if the phraseList does not contain an element, add a "nill" value to the vector
			else{
				phraseVec.add("x");	
			}
		}
		return phraseVec;
	}
	
	//Calculate the Term frequency of the word (how often does this word occur in the document)
	public double calcTF (String[] LabelListFinal, String word){
		double result = 0;
		for(String term : LabelListFinal){
			if(word.equalsIgnoreCase(term))
				result ++;
		}	
		return result / LabelListFinal.length;
	}
	
	//Calculate the Inverse Document Frequency (if the word common or rare across the document -> how much information does it contain)
	public double calcIDF (List<List<String>> docs, String word){
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
	public double calcTFIFD(String[] doc, List<List<String>> docs, String word){
		return calcTF(doc, word) * calcIDF(docs, word);
	}
	
	public double[] applyTFIDFinVector(String[] fullLabelList, List<List<String>> docs, Vector<String> v1){
		double result[] = {0};
		for(int i=0; i < v1.size(); i++){
			//if the string is x, then the Value is 0 in this Vector
			if(v1.get(i+1) == "x"){
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
	public double metrics (double [] a){
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
	public double calcVecSim (double [] v1, double[] v2){
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


