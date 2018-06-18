import edu.stanford.nlp.coref.data.Dictionaries.Person;
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
         // p.setObject("invoice");
         // p.setVerb("checking");
        //  p.setFeature(Feature.FORM, Form.IMPERATIVE);
     //     String output = realiser.realiseSentence(p);
       //   System.out.println(output);
          NPPhraseSpec subject = nlgFactory.createNounPhrase("official");
          NPPhraseSpec object = nlgFactory.createNounPhrase("invoice");
          VPPhraseSpec verb = nlgFactory.createVerbPhrase("check");
          p.setFeature(Feature.TENSE, Tense.FUTURE);
          object.setPlural(true);
          subject.addModifier("beautiful");
        //  p.addModifier("quickly");
         // subject.setDeterminer("the");
        //  object.setDeterminer("a");
		//  subject.setPlural(true);
          p.setSubject(subject);
          p.setObject(object);
          p.setVerb("check");
          System.out.println(realiser.realiseSentence(p));
          System.out.println(realiser.realiseSentence(p).substring(0, realiser.realiseSentence(p).length()-1));
	  }
}
