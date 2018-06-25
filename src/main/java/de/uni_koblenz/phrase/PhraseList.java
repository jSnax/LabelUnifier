package de.uni_koblenz.phrase;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uni_koblenz.label.*;

public class PhraseList {
	
	private Vector<String> vectorSpace;
	private List<Phrase> phrases;
	private List<List<String>> wholeInput;
	
	public PhraseList() {
	}
	
	public Vector<String> getVectorSpace() {
		return vectorSpace;
	}
	
	public void setVectorSpace(Vector<String> vectorSpace) {
		this.vectorSpace = vectorSpace;
	}
	
	public List<Phrase> getPhrases() {
		return phrases;
	}
	
	public void setPhrases(List<Phrase> phrases) {
		this.phrases = phrases;
	}

	public List<List<String>> getWholeInput() {
		return wholeInput;
	}

	public void setWholeInput(List<List<String>> wholeInput) {
		this.wholeInput = wholeInput;
	}

	public void phraseSpace(){
		//add all the elements in the first list into the vector
		Vector<String> space = new Vector<String>();
		//System.out.println("Größe phrases "+ this.phrases.size());
		for(int phrasenZaehler = 0; phrasenZaehler < this.phrases.size(); phrasenZaehler++){
			for (int woerterZaehler = 0; woerterZaehler < this.phrases.get(phrasenZaehler).getseparatedContent().size(); woerterZaehler++)
				if(space.contains(this.phrases.get(phrasenZaehler).getseparatedContent().get(woerterZaehler))==false){
					space.add(this.phrases.get(phrasenZaehler).getseparatedContent().get(woerterZaehler));
				}
				else {
					continue;
				}
			//System.out.println("VectorSpace beinhaltet " + space.toString());
			this.vectorSpace = space;
		}
		for (int spaceAddition = 0; spaceAddition < this.phrases.size(); spaceAddition++) {
			this.phrases.get(spaceAddition).setPersonalVectorSpace(space);
		}
	}
	
	public void calculatePersonalVectors() {
	for(int phrasenZaehler = 0; phrasenZaehler < this.phrases.size(); phrasenZaehler++){
		this.phrases.get(phrasenZaehler).phraseToVec(phrases.get(phrasenZaehler).getseparatedContent(), vectorSpace);
		}
	}
	
	public void tfIDFApplier() {
		for(int phrasenZaehler = 0; phrasenZaehler < this.phrases.size(); phrasenZaehler++){
			//System.out.println("Aktueller Personal Vector" + this.phrases.get(phrasenZaehler).getPersonalVector().toString());
			//System.out.println("Aktueller Input: " + this.wholeInput.get(phrasenZaehler));
			this.phrases.get(phrasenZaehler).applyTFIDFinVector(this.wholeInput.get(phrasenZaehler), this.wholeInput, this.phrases.get(phrasenZaehler).getPersonalVector());
		}
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
	public double dot(double[] a, double[] b) {
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
	
	public void addPhrase(Phrase phrase) {
		Phrase phrase2 = phrase;
		this.phrases.add(phrase2);
	}
}
