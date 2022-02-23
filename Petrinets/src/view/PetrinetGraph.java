package view;

//import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

/**
 * Diese Klasse stellt den Graphen des Petrinetzes dar.
 * @author Hannes Wilms
 *
 */
public class PetrinetGraph extends MultiGraph {

	/** relativer Pfad zur verwendeten CSS-Datei für das Layout des Petrinetzes. */
	private static String CSS_FILE = "url("+ PetrinetGraph.class.getResource("/petrinet_graph.css")+")";
	
	/**
	 * Erzeugt einen neuen PetrinetGraph und legt den Pfad zur CSS-Datei mit dem Layout fest.
	 */
	public PetrinetGraph() {
		super("Petrinet");
		this.addAttribute("ui.stylesheet", CSS_FILE);
	}

	/**
	 * Leert den aktuell gezeichneten Graphen und fügt anschließend das Attribut mit dem Pfad zur CSS-Datei wieder an.
	 */
	public void clear() {
		super.clear();
		this.addAttribute("ui.stylesheet", CSS_FILE);
	}
}
