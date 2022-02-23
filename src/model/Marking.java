package model;

import java.util.*;

/**
 * In dieser Klasse werden die Markierungen des Petrinetzes gespeichert, um zu einem späteren Zeitpunkt darauf zurückgreifen zu können. 
 * @author Hannes Wilms
 *
 */
public class Marking {

	/** id der Markierung. Setzt sich zusammen den Marken der Stellen, getrennt durch |-Striche */
	private String id="";
	
	/** Speichert die Marken zu jeder Stelle. */
	private Map<String, Integer> scoreSheet = new TreeMap<String, Integer>();
	
	/** Knoten im Erreichbarkeitsgraph, die besucht wurden, um zu dieser Markierung zu gelangen. */
	private LinkedList<Marking> pathMarkings = new LinkedList<Marking>();
	
	/** Pfad im Erreichbarkeitsgraph, der von der Anfangsmarkierung begangen wurde, um zu dieser Markierng zu gelangen. */
	private LinkedList<MarkingConnector> pathMarkingConnectors = new LinkedList<MarkingConnector>();
	
	/** Gibt an, ob diese Markierung m ist. */
	private boolean isPathStart = false;
	
	/** Gibt an, ob diese Markierung m' ist. */
	private boolean isPathEnd = false;
	
	/**
	 * Fügt einen neuen Eintrag in das Scoresheet ein.
	 * Ist für diese Stelle bereits ein Wert festgelegt, wird dieser überschrieben.
	 * @param placeId id der Stelle.
	 * @param tokens Marken an der Stelle für diese Markierung.
	 */
	void addEntry(String placeId, int tokens) {
		scoreSheet.put(placeId,  tokens);
		updateId();
	}
	
	/**
	 * Legt fest, ob diese Markierung die Markierung m ist.
	 * @param b Information, ob diese Markierung die Startmarkierung m ist (true) oder nicht (false).
	 */
	public void setIsPathStart(boolean b) {
		isPathStart = b;
	}
	
	/**
	 * Gibt die Information zurück, ob diese Markierung die Startmarkierung m ist (true) oder nicht (false).
	 * @return Information, ob diese Markierung die Markierung m ist.
	 */
	public boolean getIsPathStart() {
		return isPathStart;
	}
	
	/**
	 * Legt fest, ob diese Markierung die Markierung m' ist, die zum Abbruch der Beschränktheitsanalyse geführt hat.
	 * @param b Information, ob diese Markierung die Markierung m' ist (true) oder nicht (false).
	 */
	public void setIsPathEnd(boolean b) {
		isPathEnd = b;
	}
	
	/**
	 * Gibt die Information zurück, ob diese Markierung die Markierung m' ist, die zum Abbruch der Beschränktheitsanalyse geführt hat.
	 * @return Information, ob diese Markierung die Markierung m' ist (true) oder nicht (false). 
	 */
	public boolean getIsPathEnd() {
		return isPathEnd;
	}
	
	/**
	 * Fügt eine Kante aus dem Erreichbarkeitsgraphen zum Pfad hinzu, der beschritten wurde, um zu dieser Markierung zu gelangen.
	 * @param mc Kante des Erreichbarkeitsgraphen, die beschritten wurde.
	 */
	void addPathMarkingConnector(MarkingConnector mc) {
		this.pathMarkingConnectors.add(mc);
	}
	
	/**
	 * Fügt einen Knoten des Erreichbarkeitsgraphen (eine Markierung) hinzu, die besucht wurde, um zu diesem Knoten zu gelangen.
	 * @param m Knoten des Erreichbarkeitsgraph.
	 */
	void addPathMarking(Marking m) {
		this.pathMarkings.add(m);
	}
	
	/**
	 * Gibt die id dieser Markierung zurück. Aus dieser kann direkt die Anzahl der Marken je Stelle entnommen werden.
	 * @return id der Markierung.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Gibt den die Markierungen zurück, die besucht wurden, um zu diesem Knoten zu gelangen. 
	 * @return Liste (LinkedList) aller Markierungen die auf dem Pfad zu dieser Markierung besucht wurden.
	 */
	public LinkedList<Marking> getPathMarkings(){
		return this.pathMarkings;
	}
	
	/**
	 * Gibt die Anzahl der vorhergehenden Markierungen auf dem Pfad zu dieser Markierung zurück.
	 * @return Anzahl der Markierungen, die auf dem Pfad zu dieser Markierung besucht wurden.
	 */
	public int getPathMarkingSize() {
		return pathMarkings.size();
	}
	
	/**
	 * Gibt die Anzahl der Kanten im Erreichbarkeitsgraph zurück, die auf dem Pfad zu dieser Markierung beschritten wurden.
	 * @return Liste (LinkedList) der Kanten des Erreichbarkeitsgraphen, die beschritten wurden, um zu dieser Markierung zu gelangen.
	 */
	public LinkedList<MarkingConnector> getPathMarkingConnectors(){
		return this.pathMarkingConnectors;
	}
	
	/**
	 * Gibt den Kern dieser Markierung - die Markenanzahl aller Stellen - zurück.
	 * @return Zuordnungstabelle (Map), in der zu jeder Stelle die Anzahl der Marken gespeichert ist.
	 */
	public Map<String, Integer> getScoreSheet(){
		return this.scoreSheet;
	}
	
	/**
	 * Gibt die Anzahl der Marken zu einer spezifischen Stelle dieser Markierung zurück.
	 * @param id id der angefragten Stelle.
	 * @return Anzahl der Marken, die in dieser Markierung auf der Stelle liegen.
	 */
	int getTokensAtPlace(String id) {
		return scoreSheet.get(id);
	}
	
	/**
	 * Gibt die Information zurück, ob die eingegebene Markierung dieselbe Belegung hat, wie diese Markierung.
	 * Dafür werden die ids beider Markierungen (die die genaue Aufführung der Markierungen halten) miteinander verglichen.
	 * @param other Markierung, mit der diese Markierung verglichen werden soll.
	 * @return Information, ob beide Markierungen dieselben Marken je Stelle tragen (true) oder nicht (false).
	 */
	boolean isEqual(Marking other) {
		return (this.id.equals(other.id));
	}
	
	/**
	 * Gibt die Information zurück, ob die Marken aller Stellen an dieser Markierung gleich groß oder größer sind als in der Vergleichsmarkierung.
	 * Ist die Anzahl der Marken eines Eintrags der Vergleichsmarkierung größer als die dieser Markierung, wird false zurückgegeben, sonst true.
	 * Kann in Kombination mit {@link #isEqual(Marking)} verwendet werden, um zwei Markierungen eines Pfads auf m - m' zu untersuchen.
	 * @param other Markierung (potenziell m) die auf geeignete Verknüpfung untersucht werden soll.
	 * @return Information, ob die Marken aller Stellen dieser Markierung mindestens gleich groß sind wie die der Vergleichmarkierung.
	 */
	boolean isSameOrBigger(Marking other) {
		boolean isSameOrBigger = true;
		for(Map.Entry<String, Integer> entry : scoreSheet.entrySet()) {
			if(other.getTokensAtPlace(entry.getKey()) > entry.getValue()) {
				isSameOrBigger = false;
			}
		}
		return isSameOrBigger;
	}
	
	/**
	 * Speichert eine Liste der auf dem Pfad zu dieser Markierung beschrittenen Kanten des Erreichbarkeitsgraphen.
	 * @param markingConnectorList Liste (LinkedList) der Kanten des Erreichbarkeitsgraphen, die auf dem Pfad beschritten wurden.
	 */
	public void setPathMarkingConnectorList(LinkedList<MarkingConnector> markingConnectorList) {
		this.pathMarkingConnectors = new LinkedList<MarkingConnector>();
		if(!markingConnectorList.isEmpty()) {
			for(MarkingConnector mc : markingConnectorList) {
				this.pathMarkingConnectors.add(mc);
			}
		}
	}
	
	/**
	 * Speichert eine Liste der auf dem Pfad von der Anfangsmarkierung zu dieser Markierung besuchten Markierungen.
	 * @param markingList Liste (LinkedList) der bisher besuchten Markierungen auf dem Weg zu dieser Markierung.
	 */
	public void setPathMarkingList(LinkedList<Marking> markingList) {
		this.pathMarkings = new LinkedList<Marking>();
		if(!markingList.isEmpty()) {
			for(Marking m : markingList) {
				this.pathMarkings.add(m);
			}
		}
	}
	
	/**
	 * Aktualisiert die id dieser Markierung, um die hinzugefügten Werte abzubilden, sodass die Markierungen einfach verglichen werden können. 
	 */
	private void updateId() {
		String tempId = "";
		if(!scoreSheet.isEmpty()) {
			for(Map.Entry<String, Integer> entry : scoreSheet.entrySet()) {
				tempId = tempId + entry.getValue() + "|";
			}
			tempId = tempId.substring(0, tempId.length() - 1);
		}
		id = tempId;
	}
}
