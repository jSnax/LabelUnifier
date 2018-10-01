import static org.junit.Assert.assertEquals;
import org.hamcrest.core.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.uni_koblenz.cluster.PhraseCluster;
import de.uni_koblenz.cluster.WordCluster;
import de.uni_koblenz.label.ForbiddenWords;
import de.uni_koblenz.label.LabelList;
import de.uni_koblenz.label.Word;
import de.uni_koblenz.phrase.DomainThesaurus;
import de.uni_koblenz.phrase.PhraseStructureList;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.dictionary.Dictionary;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

public class dataset1 {

	public static void main (String args[]) throws Exception {
		ReadModels Reader = new ReadModels() ;
		File path = new File("src/test/resources/dataset1/goldstandard");
		File[] files=path.listFiles();
		
		// iterate through all goldstandard files which compare two models
		for(File file : files) {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
		    if(document != null)
		    {
		    	// get names of the compared models
		    	String[] parts = file.getName().split("[-.]");
		    	System.out.println("##########################################################");
		        System.out.println(parts[0]+ " and " +parts[1]);
		        System.out.println("#################################");

		    	// iterate through labels from xml 
		        NodeList nList1 = document.getElementsByTagName("entity1");
		        NodeList nList2 = document.getElementsByTagName("entity2");

		        //System.out.println(nList1.item(0).getAttributes().item(0).getNodeValue());
		        
		        /*
		         * read labels of models
		         */
		        // combined labelnames and labelsids of both models
		    	List<String> labelname = new ArrayList<String>();
		    	List<String> labelid = new ArrayList<String>();
		    	
		    	labelname.addAll(readlabels(parts[0]).get("labelname"));
		    	labelname.addAll(readlabels(parts[1]).get("labelname"));
		    	labelid.addAll(readlabels(parts[0]).get("labelid"));
		    	labelid.addAll(readlabels(parts[1]).get("labelid"));

		    	for(String label:labelname) {
		    		System.out.println(label);
		    	}
		    	
		        /*###################################################################
		         * apply our algorithm
		         * ###################################################################
		         *
		        
		    	System.out.println("Started");
		    	Lexicon lexicon = Lexicon.getDefaultLexicon();
		    	NLGFactory nlgFactory = new NLGFactory(lexicon);
		    	SPhraseSpec p = nlgFactory.createClause();
		        Realiser realiser = new Realiser(lexicon);
		    	Dictionary dictionary = Dictionary.getDefaultResourceInstance();
		    	

		    	// Fill Labels with word arrays
		    	LabelList testList = new LabelList(labelname.toArray(new String[labelname.size()]));
		    	
		    	System.out.println("Printing Preprocessing results:");
		    	System.out.println(testList);
		    	
		    	// Create DomainThesaurus
		    	DomainThesaurus thesaurus = new DomainThesaurus();
		    	
		    	ForbiddenWords bannedList = new ForbiddenWords();
		    	testList.numberLabels();
		    	testList.findSynsets(bannedList);
		    	
		    	System.out.println("Printing Wordclusters");
		    	List<WordCluster> AllClusters = testList.matchSynonyms();
		    	for (int i = 0; i < AllClusters.size(); i++){
		    		System.out.println("Start of Cluster " +i);
		    		for (int j = 0; j < AllClusters.get(i).matchingWords.size(); j++){
		    			System.out.println(AllClusters.get(i).matchingWords.get(j).getBaseform());
		    		}
		    		System.out.println("End of Cluster " +i);
		    	}
		    	
		    	for (int i = 0; i < 3; i++){
		    		System.out.println("");
		    	}
		    	System.out.println("Printing Generalized Wordclusters");

		    	for (int i = 0; i < AllClusters.size(); i++){
		    		AllClusters.get(i).calculateDominance(thesaurus);
		    		AllClusters.get(i).generalizeWords();
		    	}
		    	
		    	for (int i = 0; i < AllClusters.size(); i++){
		    		System.out.println("Start of Generalized Cluster " +i);
		    		for (int j = 0; j < AllClusters.get(i).matchingWords.size(); j++){
		    			System.out.println(AllClusters.get(i).matchingWords.get(j).getBaseform());
		    		}
		    		System.out.println("End of Generalized Cluster " +i);
		    	}
		    	PhraseStructureList completeList = new PhraseStructureList();
		    	

		    	completeList.sortStructures();
		    	
		    	for (int i = 0; i < testList.getInputLabels().size(); i++){
		    		for (int j = 0; j < testList.getInputLabels().get(i).getSentenceArray().size(); j++){
		    			testList.getInputLabels().get(i).getSentenceArray().get(j).toPhrase(completeList, realiser, nlgFactory);
		    		}
		    	}
		    	
		    	for (int i = 0; i < 3; i++){
		    		System.out.println("");
		    	}
		    	
		        List<String> lines = new ArrayList<String>(); 
		    	ArrayList<String> originalContent = new ArrayList<String>();
		    	for (int i = 0; i < testList.getInputLabels().size(); i++){
		    		for (int j = 0; j < testList.getInputLabels().get(i).getSentenceArray().size(); j++){
		    			originalContent.add(testList.getInputLabels().get(i).getSentenceArray().get(j).getContentAsString());
		    		}
		    	}
		    	ArrayList<PhraseCluster> finalList = testList.createClusters();
		    	// Below, the output is realized
		    	int i = 0;
		    	lines.add("Recommended generalized labels:");
		    	while (i < finalList.size() && !finalList.get(i).isAlternativeCluster()){
		    		lines.add("");
		    		lines.add("Phrase "+i);
		    		lines.add(finalList.get(i).getBuiltPhrase());
		    		lines.add("With the corresponding original labels:");
		    		for (int j = 0; j < finalList.get(i).getLabelAndSentencePositions().size(); j++){
		    			//lines.add("Label "+finalList.get(i).getLabelAndSentencePositions().get(j).get(0)+", Sentence "+finalList.get(i).getLabelAndSentencePositions().get(j).get(1));
		    			//######################## new
		    			lines.add(labelname.get(finalList.get(i).getLabelAndSentencePositions().get(j).get(0)));
		    		}
		    		// Simply prints out the Phrases and matching original labels
		    		i++;
		    	}
		    	lines.add("");
		    	lines.add("----------------------------------------------");
		    	lines.add("");
		    	lines.add("Full list of possible phrases:");
		    	for (int j = i; j < finalList.size(); j++){
		    		lines.add("");
		    		lines.add("Label "+finalList.get(j).getWasLabel()+", Sentence "+finalList.get(j).getWasSentence()+" was:");
		    		lines.add(originalContent.get(j-i));
		    		if (finalList.get(j).getAllPhrases().get(0).getNoStructureFound()){
		    			lines.add("No applicable PhraseStructure was found.");
		    		}
		    		else {
		    			lines.add("Possible Phrases:");
		    			for (int counter = 0; counter < finalList.get(j).getAllPhrases().size(); counter++){
		    				lines.add("Phrase "+counter+": "+finalList.get(j).getAllPhrases().get(counter).getFullContent());
		    			}
		    		}
		    	}
		    	java.nio.file.Path file1 = Paths.get("result.txt");
		        Files.write(file1, lines, Charset.forName("UTF-8"));
		    	/*####################################################################
		    	 * End of algorithm
		    	 *###################################################################
		    	*/
		    	System.out.println("#################################");
		    	System.out.println("pairs have to be identical:");
		    	System.out.println("#################################");
		        for(int k=0;k<nList1.getLength();k++) {
	        		System.out.println(labelname.get(labelid.indexOf(nList1.item(k).getAttributes().item(0).getNodeValue().replaceAll(".*#", ""))));
	        		System.out.println(labelname.get(labelid.indexOf(nList2.item(k).getAttributes().item(0).getNodeValue().replaceAll(".*#", ""))));
	        		System.out.println();
	        		
	        		/*
	        		int i1=0;
	        		String final1="not";
	        		String final2="found";
	        		while (i1 < finalList.size() && !finalList.get(i1).isAlternativeCluster()){
			    		//lines.add(finalList.get(i1).getBuiltPhrase());
			    		for (int j = 0; j < finalList.get(i1).getLabelAndSentencePositions().size(); j++){
			    			if (finalList.get(i1).getLabelAndSentencePositions().get(j).get(0)==labelid.indexOf(nList1.item(k).getAttributes().item(0).getNodeValue().replaceAll(".*#", ""))){
			    				final1=finalList.get(i1).getBuiltPhrase();
			    			}
			    			if (finalList.get(i1).getLabelAndSentencePositions().get(j).get(0)==labelid.indexOf(nList2.item(k).getAttributes().item(0).getNodeValue().replaceAll(".*#", ""))){
			    				final2=finalList.get(i1).getBuiltPhrase();
			    			}
			    			
			    			
			    		}
			    		i1++;
	        		}
	        		System.out.println(final1+" "+final2);
	        		System.out.println();
	        		//    collector.checkThat(final1, containsString(final2));
	        		 * 
	        		 * 
	        		 */
	        	
		        }
		            
		    }
		    /*
		     *  ONLY TEST FIRST COMPAREFILE
		     */

		    
		    //break;
		
		} 
		
		
		
		
	}
	public static Map<String, List<String>> readlabels(String university) throws SAXException, IOException, ParserConfigurationException{
		String[] tags={
			"task",
			"intermediateCatchEvent",
			"startEvent"
		};
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse("src/test/resources/dataset1/models/"+university+".bpmn");
    	List<String> labelname = new ArrayList<String>();
    	List<String> labelid = new ArrayList<String>();
		if(document != null)
	    {
			for(String tag:tags){
		    	NodeList tempList=document.getElementsByTagName(tag);
		    	for(int i=0;i<tempList.getLength();i++) {
		    		if(!tempList.item(i).getAttributes().getNamedItem("name").getTextContent().equals("")){
			    		labelname.add(tempList.item(i).getAttributes().getNamedItem("name").getTextContent());
			    		labelid.add(tempList.item(i).getAttributes().getNamedItem("id").getTextContent());
		    		}
		    	}
			}
	    }
	    Map<String,List<String>> map =new HashMap();
	    map.put("labelname",labelname);
	    map.put("labelid",labelid);
	    return map;
	}
	
}
