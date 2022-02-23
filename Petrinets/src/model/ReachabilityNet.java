package model;

import java.util.*;

/**
 * Stellt das interne Modell des (partiellen) Erreichbarkeitsgraphen dar.
 * Speichert die bisher erreichten Markierungen und Kanten des (partiellen) Erreichbarkeitsgraphen.
 * Besitzt je einen Verweis auf die Anfangsmarkierung und die aktuelle Markierung (dabei kann es sich auch um dieselbe Markierung handeln).
 * @author Hannes Wilms
 *
 */
class ReachabilityNet {

	/** Liste der bisher erreichten Markiernugen. */
	private LinkedList<Marking> markings = new LinkedList<Marking>();
	
	/** Liste der bisher beschrittenen Kanten im (partiellen) Erreichbarkeitsgraphen. */
	private LinkedList<MarkingConnector> markingConnectors = new LinkedList<MarkingConnector>();
	
	/** Verweis auf die Anfangsmarkierung. */
	private Marking initialMarking;
	
	/** Verweis auf die aktuelle Markierung. */
	private Marking currentMarking;
	
	/**
	 * Erzeugt ein neues Modell des Erreichbarkeitsgraphen.
	 * Legt die Anfangsmarkierung und die aktuelle Markierung auf die übergebene Markierung fest. 
	 * @param initialMarking Anfangsmarkierung des Netzes.
	 */
	ReachabilityNet(Marking initialMarking) {
		this.initialMarking = initialMarking;
		this.currentMarking = initialMarking;
		markings.add(initialMarking);
	}
	
	/**
	 * Fügt eine Markierung (einen Knoten) zum Modell es Erreichbarkeitsgraphen hinzu.
	 * @param marking Markierung, die zum Erreichbarkeitsgraphen hinzugefügt werden soll.
	 */
	void addMarking(Marking marking) {
		markings.add(marking);
	}
	
	/**
	 * Fügt eine Kante zum Modell des Erreichbarkeitsgraphen hinzu. 
	 * @param mc Kante, die zum Erreichbarkeitsgraphen hinzugefügt werden soll.
	 */
	void addMarkingConnector(MarkingConnector mc) {
		markingConnectors.add(mc);
	}
	
	/**
	 * Gibt die aktuelle Markierung zurück.
	 * @return Die aktuelle Markierung des Erreichbarkeitsgraphen.
	 */
	public Marking getCurrentMarking() {
		return currentMarking;
	}
	
	/**
	 * Gibt die Anfangsmarkierung des Petrinetzes bzw. Erreichbarkeitsgraphen zurück.
	 * @return Die Anfangsmarkierung des Erreichbarkeitsgraphen.
	 */
	public Marking getInitialMarking() {
		return initialMarking;
	}
	
	/**
	 * Gibt die Liste aller Kanten im (partiellen) Erreichbarkeitsgraph zurück.
	 * @return Liste (LinkedList) aller Kanten im (partiellen) Erreichbarkeitsgraphen.
	 */
	public LinkedList<MarkingConnector> getMarkingConnectors(){
		return markingConnectors;
	}
	
	/**
	 * Gibt die Markierung zurück, deren id der übergebenen id entspricht.
	 * @param id id, zu der die Markierung zurückgegeben werden soll.
	 * @return Markierung, deren id der übergebenen id entspricht.
	 */
	Marking getMarking(String id) {
		Marking m1 = null;
		Iterator<Marking> it = markings.iterator();
		while(it.hasNext() & m1 == null) {
			Marking m2 = it.next();
			if (m2.getId().equals(id)){
				m1 = m2;
			}
		}
		return m1;
	}
	
	/**
	 * Gibt die Information zurück, ob die Markierung bereits im (partiellen) Erreichbarkeitsgraphen vorhanden ist.
	 * @param m Markierung, die auf Vorhandensein geprüft werden soll.
	 * @return Information, ob die Markierung bereits vorhanden ist (true) oder nicht (false).
	 */
	boolean markingAlreadyExists(Marking m) {
		boolean exists = false;
		if(!markings.isEmpty()) {
			for(Marking marking : markings) {
				if(m.isEqual(marking)) {
					exists = true;
				}
			}
		}
		return exists;
	}
	
	/**
	 * Gibt die Information zurück, ob die Kante bereits im (partiellen) Erreichbarkeitsgraphen vorhanden ist.
	 * @param mc Kante, die auf Vorhandensein geprüft werden soll.
	 * @return Information, ob die Kante bereits vorhanden ist (true) oder nicht (false).
	 */
	boolean markingConnectorAlreadyExists(MarkingConnector mc) {
		boolean exists = false;
		if(!markingConnectors.isEmpty()) {
			for(MarkingConnector markingConnector : markingConnectors) {
				if(mc.isEqual(markingConnector)) {
					exists = true;
				}
			}
		}
		return exists;
	}
	
	/**
	 * Gibt eine Liste aller im (partiellen) Erreichbarkeitsgraphen befindlichen Markierungen (Knoten) zurück.
	 * @return Liste (LinkedList) aller im Erreichbarkeitsgraphen befindlichen Markierungen.
	 */
	public LinkedList<Marking> getMarkings(){
		return markings;
	}
	
	/**
	 * Gibt die Markierung aus dem (partiellen) Erreichbarkeitsgraphen zurück, dessen id der der übergebenen Markierung entspricht.
	 * Ist diese Markierung noch nicht vorhanden, wird die übergebene Markierung zurückgegeben. 
	 * @param other Markierung, deren Äquivalent zurückgegeben werden soll.
	 * @return Äquivalent der übergebenen Markierung oder die übergebene Markierung (wenn kein Äquivalent vorhanden ist).
	 */
	Marking getSameMarking(Marking other) {
		Marking m = other;
		if(!markings.isEmpty()) {
			for(Marking marking : markings) {
				if(other.isEqual(marking)) {
					m = marking;
				}
			}
		}
		return m;
	}
	
	/**
	 * Gibt die Kante aus dem (partiellen) Erreichbarkeitsgraphen zurück, dessen id der der übergebenen Kante entspricht.
	 * Ist diese Kante noch nicht vorhanden, wird die übergebene Kante zurückgegeben. 
	 * @param other Kante, deren Äquivalent zurückgegeben werden soll.
	 * @return Äquivalent der übergebenen Kante oder die übergebene Kante (wenn kein Äquivalent vorhanden ist).
	 */
	MarkingConnector getSameMarkingConnector(MarkingConnector other) {
		MarkingConnector m = other;
		if(!markingConnectors.isEmpty()) {
			for(MarkingConnector mc : markingConnectors) {
				if(other.isEqual(mc)) {
					m = mc; 
				}
			}
		}
		return m;
	}
	
	/**
	 * Setzt den Zeiger auf die aktuelle Markierung des Erreichbarkeitsgraphen auf die übergebene Markierung.
	 * @param m Markierung, die als aktuelle Markierung gesetzt werden soll.
	 */
	public void setCurrentMarking(Marking m) {
		this.currentMarking = m;
	}
}
