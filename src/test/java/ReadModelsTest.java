
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class ReadModelsTest {
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("Algorithmus gestarted");
		ReadModels Reader = new ReadModels() ;
		ArrayList <String> IDs = new ArrayList <String> ();
		ArrayList <String> Labelliste = new ArrayList <String> ();
		File Model = new File("C:\\Users\\Jan Angemeer\\git\\SemanticProcessModeling\\src\\test\\resources\\dataset2\\models\\birthCertificate_p246.pnml");
		// not done yet
		
		IDs= Reader.GetIDs(Model);
		System.out.println("Die IDs des Models sind:" + IDs);
		
		Labelliste = Reader.ReadLabels(Model);
		System.out.println("Die Labels des Models sind:" + Labelliste);
		System.out.println("Algorithmus beendet");
	}
}	

