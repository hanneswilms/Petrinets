package model;

/**
 * Diese Klasse stellt die Stellen eines Petrinetzes dar. 
 * @author Hannes Wilms
 *
 */
public class Place extends Node implements Comparable<Place> {

	/** Anzahl der Marken, die die Stelle trägt. */
	private int tokens;
	
	/** Information, ob die Stelle ausgewählt ist. */
	private boolean isSelected = false;
	
	/**
	 * Erzeugt eine neue Stelle.
	 * Ruft den Konstruktor der Superklasse {@link Node} auf und übergibt diesem die id.
	 * @param id id der Stelle.
	 */
	Place (String id) {
		super(id);
	}
	
	/**
	 * Vergleicht die ids zweier Stellen miteinander.
	 * @param other Stelle mit der verglichen werden soll.
	 * @return -1 wenn id der Stelle in alphabetischer Reihenfolge vor der von other steht, 0 wenn beide dieselbe id besitzen und 1 wenn die id der Stelle in alphabetischer Reihenfolge nach der von other steht.
	 */
	public int compareTo(Place other) {
		return this.getId().compareTo(other.getId());
	}
	
	/**
	 * Gibt die Information zurück, ob diese Stelle ausgewählt ist.
	 * @return Information, ob diese Stelle ausgewählt ist (true) oder nicht (false).
	 */
	public boolean getSelected() {
		return isSelected;
	}
	
	/**
	 * Gibt die Anzahl der Marken dieser Stelle zurück.
	 * @return Anzahl der Marken der Stelle.
	 */
	public int getTokens() {
		return tokens;
	}
	
	/**
	 * Legt fest, ob die Stelle ausgewählt ist.
	 * @param b Information, ob diese Stelle ausgewählt ist (true) oder nicht (false).
	 */
	public void setSelected(boolean b) {
		isSelected = b;
	}
	
	/**
	 * Legt die Anzahl der Marken dieser Stelle fest.
	 * @param tokens Anzahl der Marken dieser Stelle.
	 */
	public void setTokens(int tokens) {
		this.tokens = tokens;
	}
	
	/**
	 * Wählt die ausgewählt Stelle ab und umgekehrt.
	 */
	void toggleSelected() {
		isSelected = !isSelected;
	}
}
