package view;

import org.graphstream.graph.implementations.MultiGraph;

/**
 * Diese Klasse stellt den Erreichbarkeitsgraphen dar.
 * @author Hannes Wilms
 *
 */
public class ReachabilityGraph extends MultiGraph {
	
	/** relativer Pfad zur verwendeten CSS-Datei für das Layout des Erreichbarkeitsgraphen. */
	private static String CSS_FILE = "url("+ PetrinetGraph.class.getResource("/reachability_graph.css")+")";
	
	/**
	 * Erzeugt einen neuen ReachabilityGraph und legt den Pfad zur CSS-Datei mit dem Layout fest.
	 */
	public ReachabilityGraph() {
		super("Reachability");
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
