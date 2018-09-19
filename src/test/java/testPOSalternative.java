import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class testPOSalternative {
	public static void main (String args[]) throws ParserConfigurationException, SAXException, IOException {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, depparse");//, natlog, openie");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    
		// file names
		
		for(int j=1;j<37;j++) {
			
			File file = new File("src/test/resources/dataset3/testcase"+j+"/source-model.epml");
			
			// read XML file
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			
		    if(document != null)
		    {
		    	// iterate through labels from xml 
		        NodeList nList = document.getElementsByTagName("name");
		        for (int i = 0; i < nList.getLength(); i++) 
		        {
		        	// get text of label
		            Node nNode = nList.item(i);
		            String label=nNode.getTextContent();
		            if(label.length()>4 && label.substring(label.length()-1).matches("[a-zA-Z0-9]")) {
			            // transform sentence
			            String labelmodified=label;
			    		// set punctuation
			    		labelmodified+=".";
			    		// capitalize first letter
			    		labelmodified=labelmodified.substring(0, 1).toUpperCase()+labelmodified.substring(1);
			            
			            // annotate first sentence modified 
			    	    CoreDocument doc = new CoreDocument(labelmodified);
			    	    pipeline.annotate(doc);
			    	    // get sentence
			    	    CoreSentence sentence = doc.sentences().get(0);
			    	    // annotate second sentence unmodified
			    	    doc = new CoreDocument(label);
			    	    pipeline.annotate(doc);
			    	    // get sentence
			    	    CoreSentence sentence2 = doc.sentences().get(0);
			    	    // remove point again
			    	    List<String> posmodified=sentence.posTags();
			    	    posmodified.remove(posmodified.size()-1);
			    	    List<String> pos=sentence2.posTags();
			    	    // if there is difference print it
			    	    if (!pos.equals(posmodified)&&doc.sentences().size()==1) {
			    	    	for(int k=0;k<pos.size();k++) {
			    	    		if(pos.get(k).contains("NN")^posmodified.get(k).contains("NN")) {
					    	    	System.out.println(label);
						    	    System.out.println(posmodified); 
						    	    System.out.println(sentence2.posTags());  
			    	    		}
			    	    	}	   
			    	    }
		            }
		        }
		    }
		}	
	}
}