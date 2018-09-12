
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.dictionary.Dictionary;

import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.IndexWordSet;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.Word;
import net.sf.extjwnl.data.list.PointerTargetNode;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.data.list.PointerTargetTree;

public class testEXTJWNL {

	public static void main (String args[]) throws JWNLException{
	
		System.out.println("Algorithm started");
		Dictionary dict = Dictionary.getDefaultResourceInstance();	
		/*IndexWord[] wordlist= {
				dict.getIndexWord(POS.VERB, "check"),
				dict.getIndexWord(POS.NOUN, "car")};
		for(IndexWord indexword:wordlist) {
			System.out.println("#####################  "+indexword.getLemma()+"  #####################");
			for(Synset synset:indexword.getSenses()) {
				//PointerTargetNodeList nodelist=PointerUtils.getDirectHyponyms(synset);
				
				//  Hypernyms ##############
				//PointerTargetNodeList nodelist=PointerUtils.getDirectHypernyms(synset);
				
				//  Meronyms  ##############
				//  Parts of an object e.g. a car has a engine, a window and so on
				//PointerTargetNodeList nodelist=PointerUtils.getMeronyms(synset);
				
				// Antonyms  ##############
				//PointerTargetNodeList nodelist=PointerUtils.getAntonyms(synset);
				
				//  Synonyms ###############
				// getSynonym gibt nichts zurueck daher per getSynonymTree
				// depth parameter ????? 
				PointerTargetNodeList nodelist=PointerUtils.getSynonymTree(synset, 0).toList().get(0);
								
				for(PointerTargetNode node:nodelist) {
					System.out.println(node);
					//System.out.println(node.getSynset().getGloss());
					// HOW-TO get a word from Node
					for(Word word:node.getSynset().getWords()) {
						System.out.print(word.getLemma()+"; ");
					}
					System.out.println();
				}
			}
		}
		
		System.out.println("Algorithm completed");*/
		
		Dictionary wordnet = Dictionary.getDefaultResourceInstance();
        IndexWordSet set = wordnet.lookupAllIndexWords("check");
        IndexWord[] ws = set.getIndexWordArray(); 
        POS p = ws[1].getPOS();
        List<String> synonyms = new ArrayList<String>();
        IndexWord indexWord = wordnet.lookupIndexWord(p, "check");
        List<Synset> synSets = indexWord.getSenses();
        for (Synset synset : synSets)
        {
           List<Word> words = synset.getWords();
          
           for (Word word : words)
           {
              synonyms.add(word.getLemma());
           }
        }
        System.out.println(synonyms);
        set = wordnet.lookupAllIndexWords("take");
        ws = set.getIndexWordArray(); 
        p = ws[1].getPOS();
        List<String> synonyms2 = new ArrayList<String>();
        indexWord = wordnet.lookupIndexWord(p, "take");
        synSets = indexWord.getSenses();
        for (Synset synset : synSets)
        {
           List<Word> words = synset.getWords();
          
           for (Word word : words)
           {
              synonyms2.add(word.getLemma());
           }
        }
        System.out.println(synonyms2);
        for (int i = 1; i < synonyms.size(); i++){
        	for (int j = 1; j < synonyms2.size(); j++){
        		if (synonyms.get(i).equals(synonyms2.get(j))){
        			System.out.println(synonyms.get(i));
        		}
        	}
        }
	}
}
