package model;

import java.util.*;

/**
 * Diese Klasse stellt Petrinetze dar. Hier werden die Knoten und Kanten des Petrinetzes sowie die jeweiligen Markierungen verwaltet.
 * Beinhaltet auch den Algorithmus zur Analyse der Beschränktheit des entsprechenden Netzes.
 * @author Hannes
 *
 */
public class Petrinet {

	/** Liste aller im Netz befindlichen Stellen. */
	private LinkedList<Place> places = new LinkedList<Place>();
	
	/** Liste aller im Netz befindlichen Transitionen. */
	private LinkedList<Transition> transitions = new LinkedList<Transition>();
	
	/** Liste aller im Netz befindlichen Kanten. */
	private LinkedList<Arc> arcs = new LinkedList<Arc>();
	
	/** Zugehöriger partieller Erreichbarkeitsgraph. */
	private ReachabilityNet reachabilityNet;
	
	/** Ergebnis der Beschränktheitsanalyse. */
	private BoundednessTestResult boundednessTestResult;
	
	/** Name des Petrinetzes. */
	private String name;
	
	/** Verweis auf die zuletzt im Graphen ausgewählte Stelle. */
	private Place lastSelectedPlace;
	
	/**
	 * Erzeugt ein neues Petrinetz und speichert den Name.
	 * @param name Name des Petrinetzes.
	 */
	public Petrinet(String name) {
		this.name = name;
	}
	
	/**
	 * Gibt den Name des Petrinetzes bzw. den Dateinamen der eingeladenen Datei zurück.
	 * @return Name des Petrinetzes bzw. Dateiname der eingeladenen Datei.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gibt die zuletzt im Graph gewählte Stelle zurück.
	 * @return Zuletzt im Graph ausgewählte Stelle oder null.
	 */
	public Place getLastSelectedPlace() {
		return lastSelectedPlace;
	}
	
	/**
	 * Gibt die aktuelle Markierung zurück.
	 * @return Aktuelle Markierung des Petrinetzes.
	 */
	public Marking getCurrentMarking() {
		return reachabilityNet.getCurrentMarking();
	}
	
	/**
	 * Gibt eine Liste aller Markierungen des (partiellen) Erreichbarkeitsgraphen zurück.
	 * Wurde das interne Modell des Erreichbarkeitsgraphen noch nicht erstellt, wird null zurückgegeben.
	 * @return Liste (LinkedList) aller Markierungen des (partiellen) Erreichbarkeitsgraphen oder null.
	 */
	public LinkedList<Marking> getReachabilityMarkings(){
		if(!(reachabilityNet == null)) {
			return reachabilityNet.getMarkings();
		} else {
			return null;
		}
	}
	
	/**
	 * Gibt eine Liste aller Kanten des (partiellen) Erreichbarkeitsgraphen zurück.
	 * Wurde das interne Modell des Erreichbarkeitsgraphen noch nicht erstellt, wird null zurückgegeben.
	 * @return Liste (LinkedList) aller Kanten des (partiellen) Erreichbarkeitsgraphen oder null.
	 */
	public LinkedList<MarkingConnector> getReachabilityMarkingConnectors(){
		if(!(reachabilityNet == null)) {
			return reachabilityNet.getMarkingConnectors();
		} else {
			return null;
		}
	}
	
	/**
	 * Gibt die Anfangsmarkierung des (partiellen) Erreichbarkeitsgraphen zurück.
	 * Wurde das interne Modell des Erreichbarkeitsgraphen noch nicht erstellt, wird null zurückgegeben.
	 * @return Anfangsmarkierung des (partiellen) Erreichbarkeitsgraphen oder null.
	 */
	public Marking getInitialMarking() {
		if(!(reachabilityNet == null)) {
			return reachabilityNet.getInitialMarking();
		} else {
			return null;
		}
	}
	
	/**
	 * Setzt die Marken im Petrinetz nach Klick in den Erreichbarkeitsgraphen auf die ausgewählte Markierung.
	 * Setzt den Zeiger der aktuellen Markierung des internen Modells des Erreichbarkeitsgraphen auf die gewählte Markierung.
	 * @param id id der im Erreichbarkeitsgraphen angeklickten Markierung.
	 */
	public void clickNodeInReachabilityGraph(String id) {
		Marking marking = reachabilityNet.getMarking(id);
		if(!(marking == null)) {
			setPetrinetTokensToMarking(marking);
		}
		reachabilityNet.setCurrentMarking(marking);
	}
	
	/**
	 * Löst die Aktionen für einen Klick auf die Knoten des Petrinetzes aus.
	 * Ist der gewählte Knoten eine Stelle, wird die bisher ausgewählte Stelle abgewählt und die bisher nicht gewählte Stelle ausgewählt.
	 * Erlaubt der Controller die gleichzeitige Auswahl mehrerer Stellen nicht, werden alle anderen Stellen abgewählt.
	 * Ist der gewählte Knoten eine Transition, wird diese wenn sie aktiviert ist, geschaltet.
	 * Ist die durch die Schaltung einer Transition entstehenden Markierung noch nicht Teil des partiellen Erreichbarkeitsgraphen wird diese erzeugt.
	 * Ist die Kante im partiellen Erreichbarkeitsgraphen noch nicht vorhanden, wird diese angelegt.
	 * Der Zeiger der aktuellen Markierung wird auf die so erzeugte Markierung gesetzt.
	 * Der Zeiger der zuletzt beschrittenen Kante im (partiellen) Erreichbarkeitsgraphen wird auf die so erzeugte Kante gesetzt.  
	 * @param id id des angeklickten Knotens.
	 * @param multipleSelectionsAllowed Information, ob der Controller die gleichzeitige Bearbeitung der Marken mehrerer Stellen erlaubt (true) oder nicht (false).
	 */
	public void clickNodeInPetrinetGraph(String id, boolean multipleSelectionsAllowed) {
		Node node = getNode(id);
		if(!(node == null)) {
			if(node.getClass() == Place.class) {
				Place placeClicked = (Place) node;
				boolean placeClickedWasSelected = placeClicked.getSelected();  
				if(!multipleSelectionsAllowed) {
					for(Place place : places) {
						place.setSelected(false);
					}
					placeClicked.setSelected(!placeClickedWasSelected);
				} else {
					for(Place place : places) {
						if(place.getId().equals(id)) {
							place.toggleSelected();
						}
					}
				}
				lastSelectedPlace = placeClicked;
			} else if(node.getClass() == Transition.class) {
				Transition transitionClicked = (Transition) node;
				if(transitionClicked.isEnabled()) {
					transitionClicked.fire();
					Marking marking = new Marking();
					updateMarking(marking);
					if(reachabilityNet.markingAlreadyExists(marking)) {
						marking = reachabilityNet.getSameMarking(marking);
					} else {
						reachabilityNet.addMarking(marking);
					}
					MarkingConnector markingConnector = new MarkingConnector(reachabilityNet.getCurrentMarking().getId(), marking.getId(), transitionClicked.getId());
					reachabilityNet.setCurrentMarking(marking);
					if(reachabilityNet.markingConnectorAlreadyExists(markingConnector)) {
						markingConnector = reachabilityNet.getSameMarkingConnector(markingConnector);
					} else {
						reachabilityNet.addMarkingConnector(markingConnector);
					}
					if(!reachabilityNet.getMarkingConnectors().isEmpty()) {
						for(MarkingConnector mc : reachabilityNet.getMarkingConnectors()) {
							mc.setIsCurrent(false);
						}
					}
					markingConnector.setIsCurrent(true);
				}
			}
		}
	}
	
	/**
	 * Löscht den bisher erzeugten (partiellen) Erreichbarkeitsgraphen.
	 * Setzt die Marken des Petrinetzes auf die Anfangsmarkierung zurück.
	 * Erzeugt eine neue Markierung, die der Anfangsmarkierung des Netzes entspricht.
	 * Erzeugt ein neues internes Modell des partiellen Erreichbarkeitsgraphen, welches ausschließlich die Anfangsmarkierung beinhaltet.
	 */
	public void resetReachabilityNet() {
		if(!places.isEmpty()) {
			resetPetrinetToInitialMarking();
			Marking initialMarking = new Marking();
			updateMarking(initialMarking);
			reachabilityNet = new ReachabilityNet(initialMarking);
		}
	}
	
	/**
	 * Erzeugt eine neue Markierung, deren Belegung der aktuellen Markierung entspricht.
	 * Erzeugt ein neues internes Modell des (partiellen) Erreichbarkeitsgraphen dessen Anfangsmarkierung die erzeugte Markierung ist.
	 */
	void initializeReachabilityNet() {
		Marking marking = new Marking();
		updateMarking(marking);
		reachabilityNet = new ReachabilityNet(marking);
	}
	
	/**
	 * Gibt die Anzahl der Knoten des (partiellen) Erreichbarkeitsgraphen zurück.
	 * @return Anzahl der Knoten des (partiellen) Erreichbarkeitsgraphen.
	 */
	public int getNumberOfMarkingsInReachabilityNet() {
		return reachabilityNet.getMarkings().size();
	}
	
	/**
	 * Gibt die Anzahl der Kanten des (partiellen) Erreichbarkeitsgraphen zurück.
	 * @return Anzahl der Kanten des (partiellen) Erreichbarkeitsgraphen zurück.
	 */
	public int getNumberOfConnectionsInReachabilityNet() {
		return reachabilityNet.getMarkingConnectors().size();
	}
	
	/**
	 * Gibt das Ergebnis der Beschränktheitsanalyse zurück.
	 * @return Ergebnis der Beschränktheitsanalyse.
	 */
	public BoundednessTestResult getBoundednessTestResult() {
		return boundednessTestResult;
	}
	
	/**
	 * Setzt das Petrinetz auf die Anfangsmarkierung zurück.
	 */
	public void resetPetrinetToInitialMarking() {
		if(!places.isEmpty()) {
			setPetrinetTokensToMarking(reachabilityNet.getInitialMarking());
			reachabilityNet.setCurrentMarking(reachabilityNet.getInitialMarking());
		}
	}
	
	/**
	 * Speichert die Information zwischen, ob die zuletzt ausgewählte Stelle ausgewählt war oder nicht.
	 * Setzt für alle Stellen des Netzes die Information, ob sie ausgewählt wurden auf false.
	 * Stellt für die zuletzt gewählte Stelle den Status wieder her.
	 */
	public void unselectPlaces() {
		if(!places.isEmpty()) {
			if(!(lastSelectedPlace == null)) {
				boolean lastSelected = lastSelectedPlace.getSelected();
				for(Place place : places) {
					place.setSelected(false);
				}
				lastSelectedPlace.setSelected(lastSelected);
			}
		}
	}
	
	/**
	 * Ändert die Anzahl der Marken der ausgewählten Stellen um den angegebenen Wert.
	 * Der Wert kann positiv oder negativ sein.
	 * Ist die resultierende Anzahl der Marken negativ wird dieser Wert auf 0 korrigiert.
	 * @param tokens Wert, um den die Anzahl der Marken an den ausgewählten Stellen verändert werden sollen. 
	 */
	public void changeTokensAtSelectedPlaces(int tokens) {
		if(!places.isEmpty()) {
			for (Place place : places) {
				if(place.getSelected()) {
					int newTokens = place.getTokens()+tokens;
					if(newTokens < 0) {
						place.setTokens(0);
					} else {
						place.setTokens(newTokens);
					}
				}
			}
			Marking marking = new Marking();
			updateMarking(marking);
			reachabilityNet = new ReachabilityNet(marking);
		}
	}
	
	/**
	 * Fügt eine Kante zum Petrinetz hinzu.
	 * @param arc Kante (Stelle - Transition oder Transition - Stelle) die zum Petrinetz hinzugefügt werden soll.
	 */
	void addArc(Arc arc) {
		arcs.add(arc);
	}
	
	/**
	 * Fügt eine Stelle zum Petrinetz hinzu.
	 * @param place Stelle, die zum Petrinetz hinzugefügt werden soll.
	 */
	void addPlace(Place place) {
		places.add(place);
	}
	
	/**
	 * Fügt eine Transition zum Petrinetz hinzu.
	 * @param transition Transition, die zum Petrinetz hinzugefügt werden soll.
	 */
	void addTransition(Transition transition) {
		transitions.add(transition);
	}
	
	/**
	 * Verwendet die Methode {@link Arc#setNodes(Petrinet)}, um die in den Kanten gespeicherten Stellen den Transitionen im Vor- bzw. Nachbereich hinzuzufügen.
	 */
	void connectArcs() {
		for(Arc a : arcs) {
			a.setNodes(this);
		}
	}
	
	/**
	 * Gibt eine Liste aller im Petrinetz vorhandenen Kanten zurück.
	 * @return Liste (LinkedList) aller im Petrinetz vorhandenen Kanten.
	 */
	public LinkedList<Arc> getArcs(){
		return arcs;
	}
	
	/**
	 * Erzeug eine Kopie dieses Petrinetzes.
	 * Dafür werden neue Stellen, Transitionen und Kanten erzeugt.
	 * Den neu erzeugten Elementen werden die entsprechenden Werte der in diesem Petrinetz vorhandenen Gegenstücke zugewiesen.
	 * Erzeugt das zur Kopie gehörende interne Modell des (partiellen) Erreichbarkeitsgraphen und weist dies dem neuen Petrinetz zu.
	 * @return Eine Kopie dieses Petrinetzes.
	 */
	public Petrinet getCopy() {
		Petrinet copy = new Petrinet(name);
		if(!places.isEmpty()) {
			for (Place place : places) {
				Place copyPlace = new Place(place.getId());
				copyPlace.setName(place.getName());
				copyPlace.setSelected(place.getSelected());
				copyPlace.setTokens(place.getTokens());
				copyPlace.setX(String.valueOf(place.getX()));
				copyPlace.setY(String.valueOf(place.getY()));
				copy.addPlace(copyPlace);
			}
		}
		if(!transitions.isEmpty()) {
			for (Transition transition : transitions) {
				Transition copyTransition = new Transition(transition.getId());
				copyTransition.setName(transition.getName());
				copyTransition.setX(String.valueOf(transition.getX()));
				copyTransition.setY(String.valueOf(transition.getY()));
				copy.addTransition(copyTransition);
			}
		}
		if(!arcs.isEmpty()) {
			for(Arc arc : arcs) {
				Arc copyArc = new Arc(arc.getId(),arc.getSourceId(), arc.getTargetId());
				copy.addArc(copyArc);
			}
			copy.connectArcs();
		}
		copy.initializeReachabilityNet();
		return copy;
	}
	
	/**
	 * Gibt den Knoten dieses Petrinetzes zurück, dessen id der übergebenen id entspricht.
	 * @param id id, die der des Knotens entsprechen soll.
	 * @return Knoten, dessen id dem eingegebenen String entspricht oder null.
	 */
	Node getNode(String id) {
		Node n = getPlace(id);
		if(!(n == null))
			return n;
		n = getTransition(id);
		if (!(n == null))
			return n;
		return null;
	}
	
	/**
	 * Gibt die Stelle dieses Petrinetzes zurück, deren id der übergebenen id entspricht.
	 * @param id id, die der der Stelle entsprechen soll.
	 * @return Stelle, deren id dem eingegebenen String entspricht oder null.
	 */
	Place getPlace(String id) {
		Place p = null;
		if(!(places.isEmpty())) {
			for(Place place : places) {
				if(id.equals(place.getId()))
					p = place;
			}
		}
		return p;
	}
	
	/**
	 * Gibt eine Liste aller im Petrinetz befindlichen Stellen zurück.
	 * @return Liste (LinkedList) aller im Netz befindlichen Stellen.
	 */
	public LinkedList<Place> getPlaces(){
		return places;
	}
	
	/**
	 * Gibt die Transition dieses Petrinetzes zurück, deren id der übergebenen id entspricht.
	 * @param id id, die der der Transition entsprechen soll.
	 * @return Transition, deren id dem eingegebenen String entspricht oder null.
	 */
	public Transition getTransition(String id) {
		Transition t = null;
		if(!(transitions.isEmpty())) {
			for(Transition transition : transitions) {
				if(id.equals(transition.getId()))
					t = transition;
			}
		}
		return t;
	}
	
	/**
	 * Gibt die Liste aller ausgewählten Stellen zurück.
	 * Durchläuft dafür die Liste aller Stellen und fügt alle Stellen in eine neue Liste ein, die ausgewählt sind.
	 * @return Liste (LinkedList) aller Stellen, deren Wert isSelected true ist.
	 */
	public LinkedList<Place> getSelectedPlaces(){
		LinkedList<Place> temp = new LinkedList<Place>();
		for(Place place : places) {
			if(place.getSelected())
				temp.add(place);
		}
		return temp;
	}
	
	/**
	 * Gibt die Liste aller im Petrinetz befindlichen Transitionen zurück.
	 * @return Liste (LinkedList) aller Transitionen des Petrinetzes.
	 */
	public LinkedList<Transition> getTransitions(){
		return transitions;
	}
	
	/**
	 * Startet die Beschränktheitsanalyse des Petrinetzes.
	 * Legt ein neues BoundednessTestResult an und übergibt den Namen des Petrinetzes.
	 * Setzt den Wert für Unbeschränktheit auf false und setzt den Startzustand der Analyse auf die Anfangsmarkierung des Netzes.
	 * Ruft den Analysemechanismus ({@link #analysisMechanism(Marking, BoundednessTestResult)}) auf.
	 * Speichert die Ergebnisse der Analyse (Unbeschränktheit, m, m', Teil des Pfades, Anzahl der Knoten / Kanten des Erreichbarkeitsgraphen) in das BoundednessTestResult.
	 */
	public void analysis() {
		if(!places.isEmpty()) {
			boundednessTestResult = new BoundednessTestResult();
			boundednessTestResult.setName(name);
			reachabilityNet = new ReachabilityNet(reachabilityNet.getInitialMarking());
			boundednessTestResult.setUnbounded(false);
			Marking currentMarking = reachabilityNet.getInitialMarking();
			boundednessTestResult = analysisMechanism(currentMarking, boundednessTestResult);
			
			if(boundednessTestResult.getUnbounded()) {
				boundednessTestResult.getStart().setIsPathStart(true);
				boundednessTestResult.getEnd().setIsPathEnd(true);
				
				for(MarkingConnector markingConnector : boundednessTestResult.getPath()) {
					markingConnector.setIsInPath(true);
				}
			}
			setPetrinetTokensToMarking(reachabilityNet.getCurrentMarking());
			boundednessTestResult.setNumberOfNodes(reachabilityNet.getMarkings().size());
			boundednessTestResult.setNumberOfEdges(reachabilityNet.getMarkingConnectors().size());
		}
	}
	
	/**
	 * Rekursiver Analysemechanismus des Petrinetzes.
	 * Prüft, ob im Petrinetz Transitionen vorhanden sind.
	 * Schaltet der Reihe nach die aktivierten Transitonen und erzeugt somit neue Markierungen und Kanten des (partiellen) Erreichbarkeitsgraphen.
	 * Sind die Markierungen oder Kanten noch nicht Teil des Erreichbarkeitsgraphen, werden diese hinzugefügt.
	 * Prüft, ob auf dem bisher beschrittenen Pfad zwei Markierungen m und m' vorhanden sind, sodass das Kriterium der Unbeschränktheit erfüllt ist.
	 * Ist das Kriterium der Unbeschränktheit nicht erfüllt und die erzeugte Kante noch nicht Teil des (partiellen) Erreichbarkeitsgraphen wird die Methode mit der neu erzeugten Markierung aufgerufen.
	 * Ist das Kriterium der Unbeschränktheit erfüllt oder keine Transition mehr aktiviert wird das Ergebnis zurückgegeben.
	 * @param currentMarking Markierung, von der aus die Unbeschränktheit untersucht werden soll.
	 * @param btr Container, in den die Ergebnisse der Beschränktheitsanlayse gespeichert werden sollen.
	 * @return Ergebnisse der Beschränktheitsanalyse.
	 */
	private BoundednessTestResult analysisMechanism(Marking currentMarking, BoundednessTestResult btr) {
		if(!transitions.isEmpty()) {
			for(Transition transition : transitions) {
				boolean markingConnectorAlreadyInReachabilityNet = false;
				setPetrinetTokensToMarking(currentMarking);
				if(transition.isEnabled()) {
					transition.fire();
					Marking newMarking = new Marking();
					updateMarking(newMarking);
					if(reachabilityNet.markingAlreadyExists(newMarking)) {
						newMarking = reachabilityNet.getSameMarking(newMarking);
					} else {
						reachabilityNet.addMarking(newMarking);
					}
					MarkingConnector markingConnector = new MarkingConnector(currentMarking.getId(), newMarking.getId(), transition.getId());
					if(reachabilityNet.markingConnectorAlreadyExists(markingConnector)) {
						markingConnector = reachabilityNet.getSameMarkingConnector(markingConnector);
						markingConnectorAlreadyInReachabilityNet = true;
					} else {
						reachabilityNet.addMarkingConnector(markingConnector);
					}
					newMarking.setPathMarkingList(currentMarking.getPathMarkings());
					newMarking.addPathMarking(currentMarking);
					newMarking.setPathMarkingConnectorList(currentMarking.getPathMarkingConnectors());
					newMarking.addPathMarkingConnector(markingConnector);
					Iterator<Marking> it = newMarking.getPathMarkings().descendingIterator();
					while(it.hasNext() & !btr.getUnbounded()) {
						Marking m = it.next();
						if(!(m.isEqual(newMarking))) {
							if(newMarking.isSameOrBigger(m)) {
								btr.setUnbounded(true);
								btr.setStart(m);
								btr.setEnd(newMarking);
								btr.setPath(newMarking.getPathMarkingConnectors());
							}
						}
					}
					reachabilityNet.setCurrentMarking(newMarking);
					if(!btr.getUnbounded() & !markingConnectorAlreadyInReachabilityNet) {
						analysisMechanism(newMarking, btr);
					}
				}
			}
		}
		return btr;
	}
	
	/**
	 * Setzt die Anzahl der Marken aller Stellen im Netz auf die in der Markierung gesetzten Anzahl.
	 * @param marking Markierung, auf die das Netz gesetzt werden soll.
	 */
	private void setPetrinetTokensToMarking(Marking marking){
		if(!places.isEmpty()) {
			for(Place place : places) {
				place.setTokens(marking.getTokensAtPlace(place.getId()));
			}
		}
	}
	
	/**
	 * Speichert die aktuellen Marken aller Stellen in die angegebene Markierung.
	 * @param marking Markierung, deren Inhalt angelegt oder aktualisiert werden soll.
	 */
	private void updateMarking(Marking marking) {
		if(!places.isEmpty()) {
			for (Place place : places) {
				marking.addEntry(place.getId(), place.getTokens());
			}
		}
	}
}
