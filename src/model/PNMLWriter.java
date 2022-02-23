package model;

import propra.pnml.*;
import java.io.File;
import java.util.*;

/**
 * Diese Klasse stellt einen Writer für PNML-Dateien dar.
 * Die Klasse kann verwendet werden, um aus einem Petrinet-Objekt eine PNML-Datei zu erzeugen
 * @author Hannes Wilms
 *
 */
public class PNMLWriter {

	/** Petrinetz, das in die PNML-Datei geschrieben werden soll. */
	private Petrinet petrinet;

	/** PNMLWopedParser, der verwendet wird, um die PNML-Datei zu schreiben. */
	private PNMLWopedWriter pnmlWriter;
	
	/**
	 * Erzeugt einen neuen PNMLWriter. Speichert eine Kopie des Petrinetzes und erzeugt einen neuen PNMLWopedWriter.
	 * @param petrinet Petrinetz, das in die PNML-Datei geschrieben werden soll.
	 * @param file Datei, in die das Petrinetz geschrieben werden soll.
	 */
	public PNMLWriter(Petrinet petrinet, File file) {
		this.petrinet = petrinet.getCopy();
		pnmlWriter = new PNMLWopedWriter(file);
	}
	
	/**
	 * Setzt das Petrinetz auf die Anfangsmarkierung zurück.
	 * Schreibt das Petrinetz in die PNML-Datei.
	 * Ruft dafür die Methoden {@link PNMLWopedWriter#startXMLDocument()} und {@link PNMLWopedWriter#finishXMLDocument()} auf.
	 */
	public void write() {
		pnmlWriter.startXMLDocument();
		petrinet.resetPetrinetToInitialMarking();
		LinkedList<Place> places = petrinet.getPlaces();
		if(!places.isEmpty()) {
			for(Place place : places) {
				pnmlWriter.addPlace(place.getId(), place.getName(),
						String.valueOf(place.getX()), String.valueOf(place.getY()),
						String.valueOf(place.getTokens()));
			}
		}
		LinkedList<Transition> transitions = petrinet.getTransitions();
		if(!transitions.isEmpty()) {
			for(Transition transition : transitions) {
				pnmlWriter.addTransition(transition.getId(), transition.getName(),
						String.valueOf(transition.getX()), String.valueOf(transition.getY()));
			}
		}
		LinkedList<Arc> arcs = petrinet.getArcs();
		if(!arcs.isEmpty()) {
			for(Arc arc : arcs) {
				pnmlWriter.addArc(arc.getId(), arc.getSourceId(), arc.getTargetId());
			}
		}
		pnmlWriter.finishXMLDocument();
	}
	
	
}
