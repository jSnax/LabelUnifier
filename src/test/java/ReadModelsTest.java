
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class ReadModelsTest {
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("Algorithmus gestarted");
		
		ReadModels Reader = new ReadModels() ;

		ArrayList<String> IDs = new ArrayList <String> ();
		ArrayList <String> Labelliste = new ArrayList <String> ();
		ArrayList <String> FileName = new ArrayList <String> ();
		
		
		File dir = new File("src/test/resources/dataset2/models");
		File[] fileList = dir.listFiles();
		
		
		for(File f : fileList) {
			File Model = new File(f.toURI());
			
				FileName = Reader.GetFileName(Model);
				System.out.println("Dateiname: " + FileName);
			
				IDs= Reader.GetIDs(Model);
				System.out.println("Die IDs des Models sind: " + IDs);
			
				Labelliste = Reader.ReadLabels(Model);
				System.out.println("Die Labels des Models sind: " + Labelliste);
				System.out.println();
				
		}
		
		System.out.println("Algorithmus beendet");
	}
}	

