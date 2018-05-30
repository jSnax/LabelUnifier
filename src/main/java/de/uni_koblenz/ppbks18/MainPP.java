package de.uni_koblenz.ppbks18;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


import de.uni_koblenz.cluster.*;
import de.uni_koblenz.enums.*;
import de.uni_koblenz.label.*;
import de.uni_koblenz.phrase.*;
import de.uni_koblenz.similarity.*;
import de.uni_koblenz.similarity.CosinePhraseSim;




public class MainPP {
	
	public static void main (String args[]) {
	/*	List<String> l1;
		l1.add("Check");
		l1.add("if"); 
		l1.add("Bachelor");
		l1.add("Degree");
		l1.add("is");
		l1.add("within"); 
				
		List<String> l2;
		l2.add("Check");
		l2.add("if");
		l2.add("Bachelor");
		l2.add("is");
		l2.add("sufficient");
		
		Vector<String> l1l2spac = new Vector<String>();
		l1l2spac = phraseSpace(l1, l2);
		
		Vector<String> v1 = new Vector<String>();
		v1 = phraseToVec(l1, l1l2spac);
		
		Vector<String> v2 = new Vector<String>();
		v2 = phraseToVec(l2, l1l2spac);
		
		String input1 = "";
		input1 = new String(Files.readAllBytes(Paths.get("/FUB.txt")));
		String[] items = input1.split(",");
	    List<String> fulllabellist1 = new ArrayList<String>(Arrays.asList(items));
	      
	      
		String input2 = "";
		input1 = new String(Files.readAllBytes(Paths.get("/TUM.txt")));
		String[] item = input2.split(",");
	    List<String> fulllabellist2 = new ArrayList<String>(Arrays.asList(item));
		
	    List<List<String>> ultimate;
	    ultimate.add(fulllabellist1);
	    ultimate.add(fulllabellist2);
		double[] d1 = applyTFIDFinVector(ultimate, fulllabellist1, v1);
		double[] d2 = applyTFIDFinVector(ultimate, fulllabellist2, v2);
		
		double simres; 
		simres = calcVecSim(d1, d2);
		*/
	}
}
