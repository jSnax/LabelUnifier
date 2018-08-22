package de.uni_koblenz.phrase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


// Als einzelne Klasse oder als Methode möglich.

public class choosePhraseStructure {

        String tsvFile = "[file-source-here]";
        BufferedReader br = null;
        String line = "";
        // slash-t ist ein Tab
        // Weiß ehrlich gesagt nicht warum Eclipse auf dem { in der nächsten Zeile besteht
        String tsvSplitBy = "\t";{
        //declare file for saving phraseStructures if phraseElement[0] == y	

        try {

            br = new BufferedReader(new FileReader(tsvFile));
            while ((line = br.readLine()) != null) {

                // use tab as separator
                String[] phraseElement = line.split(tsvSplitBy);
		
		/* if array at [0] is y, then for full length of array phraseElement starting at [1] add the phraseElement to "file"?? <-- what kind of file?
		if(phraseElement[0] == "y"){
			for(i = 1; i < phraseElement.length; i++){
				//add phraseElements to "file"(?)
			}
		} */
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

	}
}