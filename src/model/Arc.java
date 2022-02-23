package model;

/**
 * Diese Klasse stellt die Verbindungen zwischen Stelle und Transition im Petrinetz dar.
 * @author Hannes Wilms
 *
 */
public class Arc implements Comparable<Arc> {
	
	/** id der Kante im Petrinetz */
	private String id;
	
	/** Quellknoten der Verbindung  */
	private Node source;
	
	/** Zielknoten der Verbindung */
	private Node target;
	
	/** id des Quellknotens der Verbindung */
	private String sourceId;
	
	/** id des Zielknotens der Verbindung */
	private String targetId;
	
	/**
	 * Erzeugt eine neue Verbindung für ein Petrinetz. Sepichert die übergebenen ids in seine lokale Variablen.
	 * @param id id der Verbindung.
	 * @param source id des Quellknotens.
	 * @param target id des Zielknotens.
	 */
	Arc(String id, String source, String target) {
		this.id = id;
		this.sourceId = source;
		this.targetId = target;
	}
	
	/**
	 * Vergleicht die ids zweier Verbindungen miteinander.
	 * @param other Verbindung mit der verglichen werden soll.
	 * @return -1 wenn id der Verbindung in alphabetischer Reihenfolge vor der von other steht, 0 wenn beide dieselbe id besitzen und 1 wenn die id der Verbindung in alphabetischer Reihenfolge nach der von other steht.
	 */
	public int compareTo(Arc other) {
		return id.compareTo(other.id);
	}
	
	/**
	 * Gibt die id der Verbindung zurück.
	 * @return id der Verbindung.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Gibt die id des Quellknotens zurück.
	 * @return id des Quellknotens.
	 */
	public String getSourceId() {
		return source.getId();
	}
	
	/**
	 * Gibt die id des Zielknotens zurück.
	 * @return id des Zielknotens.
	 */
	public String getTargetId() {
		return target.getId();
	}
	
	/**
	 * Fügt die Stelle (Quellknoten) dem Vorbereich der Transition (Zielknoten) hinzu.
	 * @param target Transition, die Zielknoten dieser Verbindung ist.
	 * @param source Stelle, die Quellknoten dieser Verbindung ist.
	 */
	private void placeToTransitionInput (Transition target, Place source) {
		target.addInputPlace(source);
	}
	
	/**
	 * Fügt die Stelle (Zielknoten) dem Nachbereich der Transition (Quelknoten) hinzu.
	 * @param source Transition, die Quellknoten dieser Verbidung ist.
	 * @param target Stelle, die Zielknoten dieser Verbindung ist.
	 */
	private void placeToTransitionOutput (Transition source, Place target) {
		source.addOutputPlace(target);
	}
	
	/**
	 * Übernimmt Referenzen auf die Knoten aus dem Petrinetz und fügt dann die jeweiligen Stellen dem Vor- bzw. Nachbereich der Transition hinzu.
	 * @param petrinet Petrinetz, dem die Knoten und die Verbindung angehören.
	 */
	public void setNodes(Petrinet petrinet) {
		this.source = petrinet.getNode(sourceId);
		this.target = petrinet.getNode(targetId);
		if(this.source.getClass() == (new Place("").getClass())) {
			Place p = (Place) this.source;
			Transition t = (Transition) this.target;
			placeToTransitionInput(t, p);
		} else {
			Transition t = (Transition) this.source;
			Place p = (Place) this.target;
			placeToTransitionOutput(t,p);
		}
	}
}
