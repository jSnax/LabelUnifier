

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.parser.Parser;
import edu.stanford.nlp.parser.common.ParserQuery;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.nndep.DependencyParser;
//import edu.stanford.nlp.coref.data.CorefChain;
//import edu.stanford.nlp.ling.*;
//import edu.stanford.nlp.ie.util.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.tagger.common.Tagger;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.trees.UniversalEnglishGrammaticalRelations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.ScoredObject;
import edu.stanford.nlp.util.TypesafeMap;

//import edu.stanford.nlp.semgraph.*;
//import edu.stanford.nlp.tagger.maxent.MaxentTagger;
//import edu.stanford.nlp.trees.*;
import java.util.*;


public class testProbability {
	/*
	 *  Test to get best parses of parser. 
	 */
	// WRONG OUTPUT TO-DO: USE SAME PARSER AS IN CORENLP
	// https://stackoverflow.com/questions/27450207/stanford-nlp-annotation-ranking-or-score
	// https://nlp.stanford.edu/nlp/javadoc/javanlp/edu/stanford/nlp/parser/common/ParserQuery.html
	public static void main(String[] args) {
	    // set up pipeline properties
		  Properties props = new Properties();
		  props.setProperty("annotators", "tokenize, ssplit,pos");// depparse natlog pos lemma
		  StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    // Annotate an example document.
	    Annotation doc = new Annotation("verifying bill.");
	    pipeline.annotate(doc);
	    
	    /*  used in corenlp
	     * MAXENTTAGGER   TO-DO: RETRIEVING RANK/PROBABILITY
	     */
	    List<CoreLabel> mySentence =doc.get(CoreAnnotations.TokensAnnotation.class) ;
	    Tagger tagger=MaxentTagger.loadModel("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
		List<TaggedWord> result=tagger.apply(mySentence);
		
		
		/*  used in corenlp only returns one result
		 * DEPENDENCY PARSER  
		 */
		DependencyParser depparser=DependencyParser.loadFromModelFile("edu/stanford/nlp/models/parser/nndep/english_UD.gz"); 
		GrammaticalStructure depstructure=depparser.predict(mySentence);
		
		System.out.print(depstructure);
		
		
		
		/*   NOT used in corenlp  inaccurate
		 *  LEXICALIZED PARSER    PROBABILITY  DONE
		 */
	    LexicalizedParser parser = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
		ParserQuery pq = parser.parserQuery();
		if (pq.parse(mySentence)) {
			// Get best parse and associated score
			Tree parse = pq.getBestPCFGParse();
			double score = pq.getPCFGScore();
			System.out.println(score);
			// Print parse
			parse.pennPrint();
	
		    // ----
		    // Convert a constituency parse to dependency representation
		    GrammaticalStructure gs = parser.treebankLanguagePack().grammaticalStructureFactory().newGrammaticalStructure(parse);
		    List<TypedDependency> dependencies = gs.typedDependenciesCCprocessed();
		    System.out.println(dependencies);
		    
		    
		    // ----
		    // Get collection of best parses
		    List<ScoredObject<Tree>> bestParses = pq.getKBestParses(8);	    
		    
		    for(ScoredObject<Tree> tree: bestParses) {
		    	System.out.println("\n##################### RANK "+(bestParses.indexOf(tree)+1)+" #####################");
		    	System.out.println(tree.score());
		    	tree.object().pennPrint();
			    GrammaticalStructure gstructure = parser.treebankLanguagePack().grammaticalStructureFactory().newGrammaticalStructure(tree.object());
			    List<TypedDependency> dependency = gstructure.typedDependenciesCCprocessed();
			    System.out.println("\n"+dependency);
		    }
		}

	}

}
