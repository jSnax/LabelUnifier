import edu.stanford.nlp.coref.data.Dictionaries.Person;
import simplenlg.features.Feature;
import simplenlg.features.Tense;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;
import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;

public class testSimpleNLG {
	  public static void main(String[] args) {
		  Lexicon lexicon = Lexicon.getDefaultLexicon();

		  WordElement word = lexicon.getWord("go", LexicalCategory.VERB);
		/* WordElement word = lexicon.getWord("go", LexicalCategory.VERB);
		  InflectedWordElement infl = new InflectedWordElement(word);
		  infl.setFeature(Feature.TENSE, Tense.PAST);
		  infl.setFeature(Feature.PERSON, Person.HE);
		  Realiser realiser = new Realiser(lexicon);
		  String past = realiser.realise(infl).getRealisation();
		  System.out.println(past);
		  System.out.println(past);*/
		  NLGFactory nlgFactory = new NLGFactory(lexicon);
		  SPhraseSpec p = nlgFactory.createClause();
          Realiser realiser = new Realiser(lexicon);
          p.setObject("invoice");
          p.setVerb("fight back");
         // p.setFeature(Feature.FORM, Form.IMPERATIVE);
          String output = realiser.realiseSentence(p);
          System.out.println(output);
          NPPhraseSpec subject = nlgFactory.createNounPhrase("Official");
          NPPhraseSpec object = nlgFactory.createNounPhrase("bill");
          VPPhraseSpec verb = nlgFactory.createVerbPhrase("verify");
          subject.addModifier("talented");
          subject.setDeterminer("the");
          object.setDeterminer("a");
          p.setSubject(subject);
          p.setVerb(verb);
          p.setObject(object);
          System.out.println(realiser.realiseSentence(p));
	  }
}
