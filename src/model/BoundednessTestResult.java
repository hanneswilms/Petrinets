package model;

import java.util.*;

/**
 * In dieser Klasse werden die Ergebnisse der Beschränktheitsanalyse gespeichert.
 * @author Hannes Wilms
 *
 */
public class BoundednessTestResult {

	/** Name der Datei, die untersucht wurde. */
	private String name;
	
	/** Markierung m, die zum Abbruch der Untersuchung geführt hat. */
	private Marking start;
	
	/** Markierung m', die zum Abbruch der Untersuchung geführt hat. */
	private Marking end;
	
	/** Anzahl der Knoten (Markierungen) des resultierenden Erreichbarkeitsgraphen. */
	private int numberOfNodes;
	
	/** Anzahl der Verbindungen (Transition von Markierung zu Markierung) des resultierenden Erreichbarkeitsgraphen. */
	private int numberOfEdges;
	
	/** Pfad, der von der Anfangsmarkierung bis m' untersucht wurde. */
	private LinkedList<MarkingConnector> path = new LinkedList<MarkingConnector>();
	
	/** Information, ob das Netz unbeschränkt (true) oder beschränkt (false) ist. */
	private boolean unbounded = true;
	
	/**
	 * Gibt die Information, ob das Netz unbeschränkt oder beschränkt ist zurück.
	 * @return Information, ob das Netz unbeschränkt (true) oder beschränkt (false) ist. 
	 */
	public boolean getUnbounded() {
		return unbounded;
	}
	
	/**
	 * Speichert die Anzahl der Markierungen im resultierenden Erreichbarkeitsgraphen in das BoundednessTestResult.
	 * @param numberOfNodes Anzahl der Markierungen im resultierenden Erreichbarkeitsgraphen.
	 */
	public void setNumberOfNodes(int numberOfNodes) {
		this.numberOfNodes = numberOfNodes;
	}
	
	/**
	 * Gibt die Anzahl der Markierungen im resultierenden Erreichbarkeitsgraphen zurück.
	 * @return Anzahl der Markierungen im resultierenden Erreichbarkeitsgraphen.
	 */
	public int getNumberOfNodes() {
		return numberOfNodes;
	}
	
	/**
	 * Speichert die Anzahl der Kanten im resultierenden Erreichbarkeitsgraphen in das BoundednessTestResult. 
	 * @param numberOfEdges Anzahl der Kanten im resultierenden Erreichbarkeitsgraphen.
	 */
	public void setNumberOfEdges(int numberOfEdges) {
		this.numberOfEdges = numberOfEdges;
	}
	
	/**
	 * Gibt die Anzahl der Kanten im resultierenden Erreichbarkeitsgraphen zurück.
	 * @return Anzahl der Kanten im resultierenden Erreichbarkeitsgraphen.
	 */
	public int getNumberOfEdges() {
		return numberOfEdges;
	}
	
	/**
	 * Gibt die Markierung m' zurück, die zum Abbruch des Beschränktheitsalgorithmus geführt hat.
	 * @return Markierung m', die zum Abbruch des Beschränktheitsalgorithmus geführt hat.
	 */
	public Marking getEnd() {
		return end;
	}
	
	/**
	 * Gibt den Namen der untersuchten Datei zurück.
	 * @return Name der untersuchten Datei.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gibt die Kanten des Pfads zu m' zurück.
	 * @return Kanten des Pfads zu m'.
	 */
	public LinkedList<MarkingConnector> getPath(){
		return path;
	}
	
	/**
	 * Gibt die Länge des Pfads von der Anfangsmarkierung zu m' zurück.
	 * @return Länge des Pfads von der Anfangsmarkierung zu m'.
	 */
	public int getPathLength() {
		return path.size();
	}
	
	/**
	 * Gibt die Markierung m zurück.
	 * @return Markiernug m.
	 */
	public Marking getStart() {
		return start;
	}
	
	/**
	 * Speichert die Information fest, ob das untersuchte Netz unbeschränkt oder beschränkt ist. 
	 * @param unbounded Information, ob das untersuchte Petrinetz unbeschränkt (true) oder beschränkt (false) ist.
	 */
	public void setUnbounded(boolean unbounded) {
		this.unbounded = unbounded;
	}
	
	/**
	 * Speichert die Markienrung m', die zum Abbruch der Beschränktheitsanalyse geführt hat, in das BoundednessTestResult.
	 * @param end Makrierung m', die zum Abbruch der Beschränktheitsanalyse geführt hat.
	 */
	public void setEnd(Marking end) {
		this.end = end;
	}
	
	/**
	 * Speichert den Namen der analysierten Datei in das BoundednessTestResult.
	 * @param name Name der analysierten Datei.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Speichert den Pfad, der von der Anfangsmarkierung bis zu m' begangen wurde in das BoundednessTestResult.
	 * @param path Pfad, der von der Anfangsmarkierung bis zu m' begangen wurde.
	 */
	public void setPath(LinkedList<MarkingConnector> path) {
		this.path = path;
	}
	
	/**
	 * Speichert die Markierung m in das BoundednessTestResult.
	 * @param start Markierung m.
	 */
	public void setStart(Marking start) {
		this.start = start;
	}
}
