package model;

/**
 * Abstrakte Klasse, die als Grundgerüst für Stellen und Transitionen genutzt wird. Beinhaltet id, name sowie die Positionen x und y.
 * @author Hannes Wilms
 *
 */
abstract class Node {

	/** id des Knotens. */
	private String id;
	
	/** Name des Knotens. */
	private String name;
	
	/** X-Koordinate des Knotens. */
	private double posX;
	
	/** Y-Koordinate des Knotens. */
	private double posY;
	
	/**
	 * Erzeugt einen neuen Knoten mit der eingegebenen id.
	 * Speichert die id.
	 * @param id id des zu erzeugenden Knotens.
	 */
	Node(String id) {
		this.id = id;
	}
	
	/**
	 * Gibt die id dieses Knotens zurück.
	 * @return id des Knotens.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gibt den Namen dieses Knotens zurück.
	 * @return Name des Knotens
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gibt die X-Koordinate dieses Knotens zurück.
	 * @return X-Koordinate des Knotens
	 */
	public double getX() {
		return posX;
	}
	
	/**
	 * Gibt die Y-Koordinate dieses Knotens zurück.
	 * @return Y-Koordinate des Knotens
	 */
	public double getY() {
		return posY;
	}

	/**
	 * Speichert den Namen des Knotens.
	 * @param name Name des Knotens.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Speichert die X-Koordinate des Knotens.
	 * @param x X-Koordinate des Knotens.
	 */
	public void setX(String x) {
		this.posX = Double.parseDouble(x);
	}
	
	/**
	 * Speichert die Y-Koordinate des Knotens.
	 * @param y Y-Koordinate des Knotens.
	 */
	public void setY(String y) {
		this.posY = Double.parseDouble(y);
	}
}
