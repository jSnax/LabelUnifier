import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadModels {
	
	// Creating a list of golden-standard-pairs for each comparison-document
	@SuppressWarnings("unlikely-arg-type")
	public ArrayList<String> GetLabelName (ArrayList<String> gold) throws FileNotFoundException{
		
		ReadModels Reader = new ReadModels() ;
		
		String filename1 = gold.get(0).substring(0,gold.get(0).indexOf("#"));
		String filename2 = gold.get(1).substring(0,gold.get(1).indexOf("#")).trim();
		
		File file1 = new File("src/test/resources/dataset2/models/birthCertificate_p"+filename1+".pnml");
		File file2 = new File("src/test/resources/dataset2/models/birthCertificate_p"+filename2+".pnml");
		
		ArrayList <String> IDs1 = new ArrayList <String> ();
		ArrayList <String> IDs2 = new ArrayList <String> ();
		ArrayList <String> Labelliste1 = new ArrayList <String> ();
		ArrayList <String> Labelliste2 = new ArrayList <String> ();
		
		IDs1= Reader.GetIDs(file1);
		IDs2= Reader.GetIDs(file2);
		Labelliste1 = Reader.ReadLabels(file1);
		Labelliste2 = Reader.ReadLabels(file2);
		
		ArrayList <String> id1 = new ArrayList <String>();
		ArrayList <String> id2 = new ArrayList <String>();
		ArrayList <Integer> pos1 = new ArrayList<Integer>();
		ArrayList <Integer> pos2 = new ArrayList<Integer>();
		ArrayList <String> labels1 = new ArrayList <String>();
		ArrayList <String> labels2 = new ArrayList <String>();
		ArrayList <String> labelsfinal = new ArrayList <String>();

		for (int i = 0; i< gold.size(); i++) {
			if(gold.get(i).startsWith(filename1)) 
				id1.add(gold.get(i).substring(gold.get(i).indexOf("#")).replace("#", ""));
			
			if(gold.get(i).startsWith(" "+filename2)) 
				id2.add(gold.get(i).substring(gold.get(i).indexOf("#")).replace("#", ""));
		}

		for (int i = 0; i< id1.size(); i++) {
			pos1.add(IDs1.indexOf(id1.get(i)));
			pos2.add(IDs2.indexOf(id2.get(i))); }	
		
		for (int i = 0; i< pos1.size(); i++) {
			labels1.add(Labelliste1.get(pos1.get(i)));
			labels2.add(Labelliste2.get(pos2.get(i))); }
		
		for (int i = 0; i < labels1.size(); i++) {
			labelsfinal.add(labels1.get(i));
			labelsfinal.add(labels2.get(i)); }
		System.out.println(labelsfinal);
		return labelsfinal;
	}
	

	public ArrayList <String> GetFileName (File y) throws FileNotFoundException{
		
		Scanner sc;
		ArrayList <String> FName = new ArrayList <String>();
		if(y.exists()) {
				 sc = new Scanner(y);
				while (sc.hasNextLine()) {
					if (sc.hasNext("<place") || sc.hasNext("<transition") ) {
						sc.next();
						FName.add(y.getName().replace(".pnml", ""));}	
					else 
						sc.nextLine();
				}
			}
		return FName;
	}
	
	public ArrayList <String> GetIDs (File y) throws FileNotFoundException {
		Scanner sc;
		ArrayList <String> ID = new ArrayList <String>();
		if(y.exists()) {
			 sc = new Scanner(y);
			while (sc.hasNextLine()) {
				if (sc.hasNext("<place") || sc.hasNext("<transition") ) {
					sc.next();
					ID.add(sc.next().replace("id=\"", "").replace("\">", ""));}	
				else 
					sc.nextLine();
			}
		}
		return ID ;
	}
	
	public ArrayList <String> ReadLabels (File y) throws FileNotFoundException{
		Scanner sc;
		ArrayList <String> LabelList = new ArrayList <String>();
		if(y.exists()) {
			 sc = new Scanner(y);
			while (sc.hasNextLine()) {
				if (sc.hasNext("<place") || sc.hasNext("<transition")) {
					sc.nextLine();
					sc.nextLine();
						LabelList.add(sc.nextLine().replace("<text>", "").replace("</text>", ""));}
					else 
						sc.nextLine();
				}
			}
		return LabelList;
	}
	
	public ArrayList <String> GetComparedIDs (File y) throws FileNotFoundException {
		Scanner sc;
		ArrayList <String> ComparedIDs = new ArrayList <String>();
		if(y.exists()) {
			 sc = new Scanner(y);
			while (sc.hasNextLine()) {
				if (sc.hasNext("<Cell>") ) {
					sc.nextLine();
					sc.next();
					ComparedIDs.add(sc.next().replace("rdf:resource='http://birthCertificate_p", "").replace("'/>", ""));
					ComparedIDs.add(sc.next().replace("<entity2", " ") + sc.next().replace("rdf:resource='http://birthCertificate_p", "").replace("'/>", "") );
				}	
				else 
					sc.nextLine();
			}
		}
		return ComparedIDs ;
	}
	
	

}