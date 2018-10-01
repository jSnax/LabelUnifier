package de.uni_koblenz.label;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class inputList {
	
	String[] input;   

	public String[] getInput() {
			return input;
		}

		public void setInput(String[] Input) {
			input = Input;
		}
	
	public inputList(/*String url*/) {		//	-> this line will be needed in productive mode
		
		// read the input label list from custom file input.txt 
		String inputFile = "src/test/resources/input.txt";
    	//String inputFile = url + "\\input.txt"; -> this line will be needed in productive mode
        // iterate a BufferedReader to read inputFile
		BufferedReader br = null;
        String line = "";
        // create a new ArrayList<String> in order to insert the split words from dtFile
        List<String> inputList = new ArrayList<String>();
		
		try {
        	
        	br = new BufferedReader(new FileReader(inputFile));
        	
        	while ((line = br.readLine()) != null) {			
        		inputList.add(line);
        	}
        	
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		String[] finalInput = new String[inputList.size()];
		for (int i = 0; i < inputList.size(); i++){
			finalInput[i] = inputList.get(i);
		}
		//finally, set the array of words split from the user's DomainThesaurus and add to domainWords as the List AllWords
		this.input = finalInput;
	}
}
