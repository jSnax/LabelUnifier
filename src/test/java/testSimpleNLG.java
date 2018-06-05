
import simplenlg.features.Feature;
import simplenlg.features.Tense;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.realiser.english.Realiser;

public class testSimpleNLG {
	  public static void main(String[] args) {
		  Lexicon lexicon = Lexicon.getDefaultLexicon();

		  WordElement word = lexicon.getWord("go", LexicalCategory.VERB);
		  InflectedWordElement infl = new InflectedWordElement(word);
		  infl.setFeature(Feature.TENSE, Tense.PAST);
		  Realiser realiser = new Realiser(lexicon);
		  String past = realiser.realise(infl).getRealisation();
		  System.out.println(past);
	  }
}
