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

public class timTest {
	public static void main (String args[]) throws JWNLException, ClassNotFoundException, IOException {
		
	System.out.println("Started.");
	Lexicon lexicon = Lexicon.getDefaultLexicon();
	NLGFactory nlgFactory = new NLGFactory(lexicon);
	SPhraseSpec p = nlgFactory.createClause();
    Realiser realiser = new Realiser(lexicon);
	Dictionary dictionary = Dictionary.getDefaultResourceInstance();
	
	String[] input=new String[] {
			"Employee checked invoice",
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
	PhraseStructureList completeList = new PhraseStructureList();
	PhraseStructure Structure = new PhraseStructure();
	PhraseStructure Structure2 = new PhraseStructure();
	List<PhraseStructure> allStructures = new ArrayList<PhraseStructure>(); 
	List<PhraseStructureTypes> tempList = new ArrayList<PhraseStructureTypes>();
	List<PhraseStructureTypes> tempList2 = new ArrayList<PhraseStructureTypes>();
	tempList.add(PhraseStructureTypes.NOUN_SINGULAR_SUBJECT);
	tempList.add(PhraseStructureTypes.VERB_SIMPLEFUTURE);
	tempList.add(PhraseStructureTypes.NOUN_SINGULAR_OBJECT);
	Structure.setElements(tempList);
	tempList2.add(PhraseStructureTypes.NOUN_SINGULAR_SUBJECT);
	tempList2.add(PhraseStructureTypes.VERB_SIMPLEPAST);
	tempList2.add(PhraseStructureTypes.NOUN_SINGULAR_OBJECT);
	tempList2.add(PhraseStructureTypes.ADVERB);
	Structure2.setElements(tempList2);
	allStructures.add(Structure);
	allStructures.add(Structure2);
	completeList.setAllStructures(allStructures);
	System.out.println(Structure.getElements());
	System.out.println(Structure2.getElements());
	List<Phrase> p1 = testList.getInputLabels().get(0).getSentenceArray().get(0).toPhrase(completeList,realiser, nlgFactory);
	System.out.println("Full Phrases:");
	for (int i = 0; i < p1.size(); i++){
		System.out.println(p1.get(i).getFullContent());
	}
	System.out.println("Separated Phrases:");
	for (int i = 0; i < p1.size(); i++){
		System.out.println(p1.get(i).getseparatedContent());
	}
	}
}
