import static org.junit.Assert.*;

import org.junit.Test;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;

public class dictionaryspeed {
	String[]nouns= {
			 "artc",
			 "attack",
			 "attempt",
			 "attention",
			 "attraction",
			 "authority",
			 "back",
			 "balance",
			 "base",
			 "behavior",
			 "belief",
			 "birth",
			 "bit",
			 "bite",
			 "blood",
			 "blow",
			 "body",
			 "brass",
			 "bread",
			 "breath",
			 "brother"
	};
	@Test
	public void test0() throws JWNLException {
		Dictionary dict = Dictionary.getDefaultResourceInstance();

	}
	
	@Test
	public void test() throws JWNLException {
		Dictionary dict = Dictionary.getDefaultResourceInstance();

		
		for(String noun:nouns) {
		
			IndexWord word = dict.getIndexWord(POS.NOUN,noun);
		}
	}

	@Test
	public void test2() throws JWNLException {
		

		
		for(String noun:nouns) {
			Dictionary dict = Dictionary.getDefaultResourceInstance();
			IndexWord word = dict.getIndexWord(POS.NOUN,noun);
		}
	}
}
