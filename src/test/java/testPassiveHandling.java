import java.io.IOException;


import java.util.*;

import de.uni_koblenz.cluster.*;
import de.uni_koblenz.enums.*;
import de.uni_koblenz.label.*;
import de.uni_koblenz.phrase.*;
import de.uni_koblenz.phrase.PhraseStructure;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.list.PointerTargetNode;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.dictionary.Dictionary;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

import java.io.IOException;

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

public class testPassiveHandling {
	
	public static boolean isPassive(List<Word> w){

		for (int i = 0; i < w.size(); i++) {
			for (int j = 0; j < w.get(i).getGrammaticalRelations().size(); j++) {	
				if(w.get(i).getGrammaticalRelations().get(j).getGrammaticalRelationName()!=null) {
					
					if(w.get(i).getGrammaticalRelations().get(j).getGrammaticalRelationName().equals(RelationName.NOMINAL_PASSIVE_SUBJECT)
						|| w.get(i).getGrammaticalRelations().get(j).getGrammaticalRelationName().equals(RelationName.CONTROLLING_NOMINAL_PASSIVE_SUBJECT)) 
						return true;
					}
				}
			}
		return false;		
	}

	public static void passiveHandling(List<Word> w) {
	
		for(int i = 0; i < w.size(); i++) {
		
			if(isPassive(w)==true) {
			
				if((w.get(i).getPartOfSpeech()!=null)
						&& (w.get(i).getPartOfSpeech().getJwnlType()==POS.NOUN || w.get(i).getPartOfSpeech()==PartOfSpeechTypes.PERSONAL_PRONOUN)
						&& w.get(i).getRole().equals(RoleLeopold.BUSINESS_OBJECT)){

					if(i==1) {
						if(w.get(i-1).getOriginalForm().equals("by")) {
							w.get(i).setRole(RoleLeopold.SUBJECT); }
					
					}else if(i==2) {
						if(w.get(i-2).getOriginalForm().equals("by")|| w.get(i-1).getOriginalForm().equals("by")) {
							w.get(i).setRole(RoleLeopold.SUBJECT); }
					
					}else if(i>=3) {
						if(w.get(i-3).getOriginalForm().equals("by") || w.get(i-2).getOriginalForm().equals("by") || w.get(i-1).getOriginalForm().equals("by")){
							w.get(i).setRole(RoleLeopold.SUBJECT); }
						if(w.get(i-2).getRole().equals(RoleLeopold.SUBJECT) || w.get(i-1).getRole().equals(RoleLeopold.SUBJECT)) {
							w.get(i).setRole(RoleLeopold.OPTIONAL_INFORMATION_FRAGMENT); }
						
					}else{
						if(w.get(i).getOriginalForm().equals("by") && w.get(i+1).getPartOfSpeech().getJwnlType()==POS.NOUN) {
							w.get(i+1).setRole(RoleLeopold.SUBJECT);
						}else if(w.get(i).getOriginalForm().equals("by") && w.get(i+2).getPartOfSpeech().getJwnlType()==POS.NOUN) {
							w.get(i+1).setRole(RoleLeopold.SUBJECT); }
					}	
				}
			}
		}
	}
	
	public static void main(String[] args) throws JWNLException {
		
		Lexicon lexicon = Lexicon.getDefaultLexicon();
		NLGFactory nlgFactory = new NLGFactory(lexicon);
		SPhraseSpec p = nlgFactory.createClause();
	    Realiser realiser = new Realiser(lexicon);
		Dictionary dictionary = Dictionary.getDefaultResourceInstance();
		
		System.out.println("Passive Handling Test started.");
		System.out.println();
				
		String[] inputActive=new String[] {
				"Mr Grant calls an ambulance.",
				"The company pays bill",
				"Tom writes a letter.",
				"Harry ate six shrimp at dinner.",
				"Beautiful giraffes roam the savannah.",
				"The salt water eventually corroded the metal beams.",
				"Some people raise sugar caine in Hawaii.",
				"I ran the obstacle course in record time.",
				"The crew paved the entire stretch of highway",
				"The staff is required to watch a safety video every year",

		};
		String[] inputPassive=new String[] {
				"An ambulance is called by Mr Grant.",
				"Bill is payed by company",
				"The letter is written by Tom.",	
				"At dinner, Six shrimp were eaten by Harry.",
				"The savannah is roamed by beautiful giraffes.",
				"A movie is going to be watched by us tonight.",
				"The Grand Canyon is viewed by thousands of tourists every year.",
				"The metal beams were eventually corroded by the saltwater",
				"Sugar cane is raised by some people in Hawaii.",
				"The obstacle course was run by me in record time.",
				"The entire stretch of highway was paved by the crew.",
				"A safety video will be watched by the staff every year.",

		};
		
		LabelList lActive = new LabelList(inputActive);
		LabelList lPassive = new LabelList(inputPassive);
		
		for (int i=0; i<lActive.getInputLabelsSize(); i++) {
			for(int j =0; j<lActive.getInputLabels().get(i).getSentenceArray().size(); j++) {
				passiveHandling(lActive.getInputLabels().get(i).getSentenceArray().get(j).getWordsarray());
			}
		}
		for (int i=0; i<lPassive.getInputLabelsSize(); i++) {
			for(int j =0; j<lPassive.getInputLabels().get(i).getSentenceArray().size(); j++) {
				passiveHandling(lPassive.getInputLabels().get(i).getSentenceArray().get(j).getWordsarray());
			}
		}
			
		System.out.println("Active Sentences:");
		for (int i=0; i<lActive.getInputLabelsSize(); i++) {
			for(int j =0; j<lActive.getInputLabels().get(i).getSentenceArray().size(); j++) {
				System.out.println();
				System.out.println("Sentence "+ i + ": (isPassive: " + isPassive(lActive.getInputLabels().get(i).getSentenceArray().get(j).getWordsarray()) + ")");
				for(int k=0; k<lActive.getInputLabels().get(i).getSentenceArray().get(j).getWordsarray().size();k++) {
					System.out.println(lActive.getInputLabels().get(i).getSentenceArray().get(j).getWordsarray().get(k).getOriginalForm() + ": " + lActive.getInputLabels().get(i).getSentenceArray().get(j).getWordsarray().get(k).getRole());
				}
			}
		}
		
		System.out.println();
		System.out.println("Passive Sentences:");
		for (int i=0; i<lPassive.getInputLabelsSize(); i++) {
			for(int j =0; j<lPassive.getInputLabels().get(i).getSentenceArray().size(); j++) {
				System.out.println();
				System.out.println("Sentence "+ i + ": (isPassive: " + isPassive(lPassive.getInputLabels().get(i).getSentenceArray().get(j).getWordsarray()) + ")");
				for(int k=0; k<lPassive.getInputLabels().get(i).getSentenceArray().get(j).getWordsarray().size();k++) {
					System.out.println(lPassive.getInputLabels().get(i).getSentenceArray().get(j).getWordsarray().get(k).getOriginalForm() + ": " + lPassive.getInputLabels().get(i).getSentenceArray().get(j).getWordsarray().get(k).getRole());
				}
			}
		}	
	}
}
