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
			"checking invoice quickly",
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
	PhraseStructure Structure = new PhraseStructure();
	List<PhraseStructureTypes> tempList = new ArrayList<PhraseStructureTypes>();
	tempList.add(PhraseStructureTypes.NOUN_PLURAL_OBJECT);
	tempList.add(PhraseStructureTypes.VERB_SIMPLEFUTURE);
	tempList.add(PhraseStructureTypes.ADVERB);
	Structure.setElements(tempList);
	System.out.println(Structure.getElements());
	Phrase p1 = testList.getInputLabels().get(0).getSentenceArray().get(0).toPhrase(Structure,realiser, p, nlgFactory);
	System.out.println("Full Phrase:");
	System.out.println(p1.getFullContent());
	System.out.println("Separated Phrase:");
	System.out.println(p1.getseparatedContent());
	}
}
