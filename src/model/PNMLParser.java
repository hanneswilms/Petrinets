package model;

import java.io.File;
import propra.pnml.*;

/**
 * Diese Klasse dient als Parser für PNML-Dateien.
 * Aus einer eingelesenen PNML-Datei wird ein Petrinet-Objekt erzeugt.
 * Eine Überprüfung der eingegebenen PNML-Datei erfolgt nicht.
 * @author Hannes Wilms
 *
 */
public class PNMLParser extends PNMLWopedParser {

	/** Petrinetz, das aus der PNML-Datei erzeugt wurde. */
	private Petrinet petrinet;
	
	/**
	 * Erzeugt einen neuen PNML-Parser, legt ein leeres Petrinetz an und befüllt dieses dann mit den Stellen, Transitionen und Verbindungen.
	 * @param pnml PNML-Datei, aus der das Petrinetz gelesen werden soll.
	 */
	public PNMLParser(final File pnml) {
		super(pnml);
		initParser();
		petrinet = new Petrinet(pnml.getName());
		parse();
		petrinet.connectArcs();
		petrinet.initializeReachabilityNet();
	}
	
	/**
	 * Gibt das erzeugte Petrinetz zurück.
	 * @return Das erzeugte Petrinetz.
	 */
	public Petrinet getPetrinet() {
		return petrinet;
	}
	

	/**
	 * Fügt eine Verbindung zwischen Stelle und Transition bzw. Transition und Stelle in das Petrinetz ein.
	 * @param id id der Verbindung.
	 * @param source id des Quellknotens.
	 * @param target id des Zielknotens.
	 */
	public void newArc(final String id, final String source, final String target) {
		petrinet.addArc(new Arc(id, source, target));
	}
	
	/**
	 * Fügt eine neue Stelle in das Petrinetz ein.
	 * @param id id der zu erzeugenden Stelle.
	 */
	public void newPlace(final String id) {
		petrinet.addPlace(new Place(id));
	}
	
	/**
	 * Fügt eine neue Transition in das Petrinetz ein.
	 * @param id id der zu erzeugenden Transition.
	 */
	public void newTransition(final String id) {
		petrinet.addTransition(new Transition(id));
	}
	
	/**
	 * Speichert den Namen des zur id passenden Elements.
	 * @param id id des entsprechenden Elementes.
	 * @param name Name des Elementes.
	 */
	public void setName(final String id, final String name) {
		try {
			Node n = petrinet.getNode(id);
			n.setName(name);
		} catch (NullPointerException e) {
//			Arc a = petrinet.getArc(id);
//			a.setName(name);
		}
	}
	
	/**
	 * Setzt die X- und Y-Koordinaten des Elements mit der jeweiligen id.
	 * @param id id des Elements.
	 * @param x X-Koordinate des Elements.
	 * @param y Y-Koordinate des Elements.
	 */
	public void setPosition(final String id, final String x, final String y) {
		Node n = petrinet.getNode(id);
		n.setX(x);
		n.setY(y);
	}
	
	/**
	 * Weist der Stelle mit der passenden id die Anzahl der Marke hinzu.
	 * @param id id der Stelle.
	 * @param tokens Anzahl der Token, die die Stelle tragen soll.
	 */
	public void setTokens(final String id, final String tokens) {
		Place p = petrinet.getPlace(id);
		p.setTokens(Integer.parseInt(tokens));
	}
	
}
