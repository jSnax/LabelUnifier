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
				if(w.get(i).getGrammaticalRelations().get(j).getGrammaticalRelationName().equals(RelationName.NOMINAL_PASSIVE_SUBJECT))
				return true;
			}
		}
		return false;
	}
	
	public static void passiveHandling(List<Word> w) {
		
		for(int i=0; i<w.size(); i++) {
			for(int j = 0; j < w.get(i).getGrammaticalRelations().size(); j++) {
				
				if(isPassive(w)==true && w.get(i).getPartOfSpeech().getJwnlType()==POS.NOUN) {
					if(!w.get(i).getGrammaticalRelations().get(j).equals(RelationName.NOMINAL_PASSIVE_SUBJECT) && w.get(i).getRole()!=RoleLeopold.BUSINESS_OBJECT && w.get(i).getPartOfSpeech().getJwnlType()==POS.NOUN) {
						w.get(i).setRole(RoleLeopold.SUBJECT);
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
		
		
		String[] inputActive=new String[] {
				"Mr Grant calls an ambulance.",
				"The classes use computers.",
				"Tom writes a letter.",				
		};
		String[] inputPassive=new String[] {
				"Bill is payed by the company",
				"An ambulance is called by Mr Grant.",
				"The letter is written by Tom.",				
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
				for(int k=0; k<lPassive.getInputLabels().get(i).getSentenceArray().get(j).getWordsarray().size();k++) {
					System.out.println(lPassive.getInputLabels().get(i).getSentenceArray().get(j).getWordsarray().get(k).getOriginalForm() + ": " + lPassive.getInputLabels().get(i).getSentenceArray().get(j).getWordsarray().get(k).getRole());
				}
			}
		}
		
	}

	
}
