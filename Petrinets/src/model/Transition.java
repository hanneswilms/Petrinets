package model;

import java.util.*;

/**
 * Diese Klasse stellt die Transitionen im Petrinetz dar.
 * Die Klasse erbt von der Klasse {@link Node} und erhält zusätzlich zu deren Informationen je eine Liste für Stellen im Vor- bzw. Nachbereich.
 * @author Hannes Wilms
 *
 */
public class Transition extends Node implements Comparable<Transition> {
	
	/** Liste der Stellen im Vorbereich. */
	private LinkedList<Place> input = new LinkedList<Place>();
	
	/** Liste der Stellen im Nachbereich */
	private LinkedList<Place> output = new LinkedList<Place>();
	
	/**
	 * Erzeugt eine neue Transition und weist ihr die übergebene id zu.
	 * Übergibt die übergebene id an den Konstruktor der Superklasse ({@link Node}).
	 * @param id id der zu erzeugenden Transition.
	 */
	Transition(String id) {
		super(id);
	}
	
	/**
	 * Fügt eine Stelle in den Vorbereich der Transition hinzu.
	 * @param place Stelle, die dem Vorbereich hinzugefügt werden soll.
	 */
	void addInputPlace(Place place) {
		input.add(place);
	}
	
	/**
	 * Fügt eine Stelle in den Nachbereich der Transition hinzu.
	 * @param place Stelle, die dem Nachbereich hinzugefügt werden soll.
	 */
	void addOutputPlace(Place place) {
		output.add(place);
	}

	/**
	 * Vergleicht die ids zweier Transitionen miteinander.
	 * @param other Transition mit der verglichen werden soll.
	 * @return -1 wenn id der Transition in alphabetischer Reihenfolge vor der von other steht, 0 wenn beide dieselbe id besitzen und 1 wenn die id der Transition in alphabetischer Reihenfolge nach der von other steht.
	 */
	public int compareTo(Transition other) {
		return this.getId().compareTo(other.getId());
	}
	
	/**
	 * Gibt die Information zurück, ob die Transition aktiviert ist oder nicht.
	 * Ist der Vorbereich leer, wird true zurückgegeben.
	 * Durchläuft alle Stellen im Vorbereich und gibt nur dann true zurück, wenn alle Stellen mindestens eine Stelle besitzen.
	 * Ansonsten wird false zurückgegeben.
	 * @return Information, ob die Transition aktiviert ist (true) oder nicht (false).
	 */
	public boolean isEnabled() {
		if(input.isEmpty())
			return true;
		for(Place place : input) {
			if (place.getTokens() < 1)
				return false;
		}
		return true;
	}
	
	/**
	 * Prüft, ob die Transition aktiviert ist.
	 * Ist die Transition aktiviert, wird die Anzahl der Marken an jeder Stelle im Vorbereich um 1 reduziert und aller Stellen im Nachbereich um 1 erhöht.
	 * Ist die Transition nicht aktiviert, geschieht nichts.
	 */
	void fire() {
		if(isEnabled()) {
			if(!(input.isEmpty())) {
				for (Place place : input) {
					place.setTokens(place.getTokens()-1);
				}
			}
			if(!(output.isEmpty())){
				for(Place place : output) {
					place.setTokens(place.getTokens()+1);
				}
			}
		}
	}
}
