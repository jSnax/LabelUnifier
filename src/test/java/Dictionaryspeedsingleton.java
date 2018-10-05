import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.dictionary.Dictionary;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;


public class Dictionaryspeedsingleton {
	  // Eine (versteckte) Klassenvariable vom Typ der eigenen Klasse
	  private static Dictionaryspeedsingleton instance;
	private static Dictionary dict;
	  // Verhindere die Erzeugung des Objektes über andere Methoden
	  private Dictionaryspeedsingleton () {}
	  // Eine Zugriffsmethode auf Klassenebene, welches dir '''einmal''' ein konkretes 
	  // Objekt erzeugt und dieses zurückliefert.
	  public static Dictionaryspeedsingleton getInstance () throws JWNLException {
	    if (Dictionaryspeedsingleton.instance == null) {
	      Dictionaryspeedsingleton.instance = new Dictionaryspeedsingleton ();
	      dict = Dictionary.getDefaultResourceInstance();
	    }
	    return Dictionaryspeedsingleton.instance;
	  }
	  public IndexWord getnounresult(String word) throws JWNLException {
		return dict.getIndexWord(POS.NOUN,word);
	  }
}
