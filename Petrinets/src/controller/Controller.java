package controller;

import model.*;
import view.*;
import java.util.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

/**
 * Diese Klasse stellt einen Controller für die Darstellung von Petrinetzen und Erreichbarkeitsgraphen dar. 
 * @author Hannes Wilms
 *
 */
public class Controller {
	
	/** Petrinetz, das dargestellt und bearbeitet werden soll. */
	private Petrinet petrinet;
	
	/** Graph des Petrinetzes, der vom Controller aktualisiert und auf dem passenden ViewerPanel inder View dargestellt wird. */
	private PetrinetGraph petrinetGraph = new PetrinetGraph();
	
	/** Erreichbarkeitsgraph, der vom Controller aktualisiert und auf dem passenden ViewerPanel inder View dargestellt wird. */
	private ReachabilityGraph reachabilityGraph = new ReachabilityGraph();
	
	/** Hauptfenster, welches vom Controller aktualisiert wird.
	 * Der Controller nimmt Anfragen vom MainFrame entgegen.
	 */
	private MainFrame frame;
	
	/** Pfad, in dem die zuletzt geöffnete oder gespeicherte Datei liegt.
	 * Bei Speichern und Laden wird dieser zuletzt geöffnete Ordner erneut geladen.
	 */
	private String openFilePath = "../ProPra-WS20-Basis/Beispiele";
	
	/** Zuletzt geöffnete Datei.
	 * Wird bei Neuladen des Petrinetzes und Erreichbarkeitsgraphen verwendet.
	 */
	private File openFile;
	
	/** Angabe, ob die gleichzeitige Bearbeitung mehrerer Stellen erlaubt ist. */
	private boolean allowMultiplePlaces = false;
	
	/**
	 * Erzeugt einen neuen Controller für einen spezifischen MainFrame.
	 * @param frame MainFrame, den der Controller steuern soll.
	 */
	public Controller (MainFrame frame) {
		this.frame = frame;
	}
	
	/**
	 * Erzeugt ein neues Petrinetz in einer leeren Datei.
	 * Speichert das leere Netz in diese Datei.
	 * Ruft dazu die Methode {@link #saveFile(File)} auf.
	 * @param file Datei, in der das Petrinetz gespeichert werden soll.
	 */
	public void createNewPetrinet(File file) {
		petrinet = new Petrinet(file.getName());
		setOpenFilePath(file.getParent());
		setOpenFile(file);
		saveFile(file);
	}
	
	/**
	 * Gibt den Name des Petrinetzes bzw. den Dateinamen der eingeladenen Datei zurück.
	 * @return Name des Petrinetzes bzw. Dateiname der eingeladenen Datei.
	 */
	public String getPetrinetName() {
		if(!(petrinet == null)) {
			return petrinet.getName();
		} else {
			return null;
		}
	}
	
	/**
	 * Hinzufügen einer Marke zu allen aktuell ausgewählten Stellen, wenn ein Petrinetz geladen wurde.
	 * Sonst Ausgabe auf dem Textfeld des MainFrame.
	 * Aktualisiert den Grahen des Petrinetzes und zeichnet den Erreichbarkeitsgraphen neu.
	 */
	public void addTokensToSelectedPlaces() {
		if(!(petrinet == null)) {
			petrinet.changeTokensAtSelectedPlaces(1);
			updatePetrinetGraph();
			drawReachabilityGraph();
		} else {
			frame.addTextToTextArea("Noch kein Petrinetz geladen.");
		}
	}
	
	/**
	 * Speichert das aktuelle Petrinetz in eine neue pnml-Datei, sodass diese später wiederverwendet werden kann.
	 * Wird keine Dateiendung (oder abweichend zu pnml / PNML) angegeben, wird die Dateiendung .pnml hinzugefügt.
	 * Nutzt für das Speichern den {@link PNMLWriter}.
	 * Ist kein Petrinetz geladen erfolgt eine Ausgabe im Textfeld des MainFrames.
	 * @param file Datei, in die das Petrinetz gespeichert wird.
	 */
	public void saveFile(File file) {
		if(!(petrinet == null)) {
			System.out.println();
			if ((!file.getAbsolutePath().endsWith(".pnml")) & (!file.getAbsolutePath().endsWith(".PNML"))) {
				file = new File(file.getAbsolutePath()+".pnml");
			}
			PNMLWriter writer = new PNMLWriter(petrinet, file);
			writer.write();
			setOpenFilePath(file.getParent());
			setOpenFile(file);
		} else {
			frame.addTextToTextArea("Noch kein Petrinetz geladen.");
		}
	}

	
	/**
	 * Erstellt einen Screenshot des aktuell im ViewerPanel angezeigten Petrinetzes im JPG-Format.
	 * Öffnet einen Speichern-Dialog, in dem eine Datei zum Überschreiben ausgewählt oder ein neuer Dateiname angegeben werden kann.
	 * Lautet die angegebene Dateiendung nicht .jpg wird eine neue Endung .jpg angehängt.
	 * Nutzt zum Erstellen des Screenshots das Attribute ui.screenshot von {@link org.graphstream.graph.implementations.MultiGraph}.
	 * Ist kein Petrinetz geladen erfolgt eine Ausgabe im Textfeld des MainFrames.
	 */
	public void pnScreenshot() {
		if(!(petrinet == null)) {
			JFileChooser chooser = new JFileChooser(new File(openFilePath));
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG (*.jpg)","jpg");
			chooser.setFileFilter(filter);
			chooser.setDialogTitle("Speichern...");
			int rueckgabeWert = chooser.showSaveDialog(frame);
			if(rueckgabeWert == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				if (!file.getAbsolutePath().endsWith(".jpg")) {
					file = new File(file.getAbsolutePath()+".jpg");
				}
				if(!(petrinetGraph == null)) {
					petrinetGraph.addAttribute("ui.screenshot",file.getAbsolutePath());
					updatePetrinetGraph();
				}
				setOpenFilePath(file.getParent());
				setOpenFile(file);
			}
		} else {
			frame.addTextToTextArea("Noch kein Petrinetz geladen.");
		}
	}
	
	/**
	 * Erstellt einen Screenshot des aktuell im ViewerPanel angezeigten Erreichbarkeitsgraphen im JPG-Format.
	 * Öffnet einen Speichern-Dialog, in dem eine Datei zum Überschreiben ausgewählt oder ein neuer Dateiname angegeben werden kann.
	 * Lautet die angegebene Dateiendung nicht .jpg wird eine neue Endung .jpg angehängt.
	 * Nutzt zum Erstellen des Screenshots das Attribute ui.screenshot von {@link org.graphstream.graph.implementations.MultiGraph}.
	 * Ist kein Petrinetz geladen erfolgt eine Ausgabe im Textfeld des MainFrames.
	 */
	public void rgScreenshot() {
		if(!(petrinet == null)) {
			JFileChooser chooser = new JFileChooser(new File(openFilePath));
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG (*.jpg)","jpg");
			chooser.setFileFilter(filter);
			chooser.setDialogTitle("Speichern...");
			int rueckgabeWert = chooser.showSaveDialog(frame);
			if(rueckgabeWert == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				if (!file.getAbsolutePath().endsWith(".jpg")) {
					file = new File(file.getAbsolutePath()+".jpg");
				}
				if(!(reachabilityGraph == null)) {
					reachabilityGraph.addAttribute("ui.screenshot",file.getAbsolutePath());
					updateReachabilityGraph();
				}
				setOpenFilePath(file.getParent());
				setOpenFile(file);
			}
		} else {
			frame.addTextToTextArea("Noch kein Petrinetz geladen.");
		}
	}
	
	/**
	 * Startet den Beschränktheitsalgorithmus des geladenen Petrinetzes.
	 * Ruft dazu die Methoden {@link model.Petrinet#analysis()} und {@link model.Petrinet#getBoundednessTestResult()} des Petrinetzes auf.
	 * Das Ergebnis der Analyse auf Beschränktheit wird direkt im Textfeld des zugeordneten MainFrame augegeben.
	 * Ist kein Petrinetz geladen erfolgt eine Ausgabe im Textfeld des MainFrames.
	 * @return Boolean Gibt an, ob das geladene Netz unbeschränkt (true) oder beschränkt (false) ist. Ist kein Petrinetz geladen, wird null zurückgegeben.
	 */
	public Boolean analysis() {
		Boolean unbounded = null;
		if(!(petrinet == null)) {

			if(!petrinet.getPlaces().isEmpty()) {
				petrinet.analysis();
				BoundednessTestResult boundednessTestResult = petrinet.getBoundednessTestResult();
				String bounded = "";
				String result = "Ergebnis: ";
				if(boundednessTestResult.getUnbounded()) {
					unbounded = true;
					boundednessTestResult.getStart().setIsPathStart(true);
					boundednessTestResult.getEnd().setIsPathEnd(true);
					result = "Pfadlänge: " + boundednessTestResult.getPathLength() + " Pfad: (";
					bounded = "nicht";
					for(MarkingConnector markingConnector : boundednessTestResult.getPath()) {
						markingConnector.setIsInPath(true);
						result = result + markingConnector.getTransitionCaused()+",";
					}
					result = result.substring(0, result.length()-1) + "); m -> m': (" + boundednessTestResult.getStart().getId() +") -> (" +boundednessTestResult.getEnd().getId()+").";
				} else {
					unbounded = false;
					result = result + "Knoten: "+ petrinet.getNumberOfMarkingsInReachabilityNet() + ", Kanten: "+ petrinet.getNumberOfConnectionsInReachabilityNet() +".";
				}
				frame.addTextToTextArea("Das dargestellte Petrinetz "+boundednessTestResult.getName() + " ist "+bounded+" beschränkt.");
				frame.addTextToTextArea(result);
				drawReachabilityGraph();
				updatePetrinetGraph();
				updateReachabilityGraph();
			} else {
				frame.addTextToTextArea("Das Petrinetz muss mindestens eine Stelle besitzen.");
			}
		} else {
			frame.addTextToTextArea("Noch kein Petrinetz geladen.");
		}
		return unbounded;
	}

	/**
	 * Ruft den Algorithmus zur Beschränktheitsanalyse mehrerer Netze nacheinander auf.
	 * Speichert das aktuell angezeigte Petrinetz temporär zwischen.
	 * Lädt die Dateien mit dem {@link model.PNMLParser} ein.
	 * Die Ergebnisse werden in einer LinkedList abgespeichert.
	 * Bringt vor Beginn der Analyse jedes Petrinetzes den jeweiligen Namen zur Anzeige auf dem Textfeld des MainFrames.
	 * Ruft die Methode {@link model.Petrinet#analysis()} des Petrinetzes auf.
	 * Bringt nach Ende aller Analysen die Ergebnisse in einer Tabelle auf das Textfeld des MainFrames.
	 * Ist kein Petrinetz geladen erfolgt eine Ausgabe im Textfeld des MainFrames.
	 * @param files Liste mit den zu analysierenden Dateien.
	 */
	public void multipleAnalysis(File[] files) {
		LinkedList<BoundednessTestResult> boundednessTestResultList = new LinkedList<BoundednessTestResult>();
		Petrinet petrinetHelp = petrinet;
		if(!(files.length == 0)) {
			for(File file : files) {
				frame.addTextToTextArea("Analysiere: " + file.getName());
				PNMLParser parser = new PNMLParser(file);
				petrinet = parser.getPetrinet();
				
				if(!petrinet.getPlaces().isEmpty()) {
					petrinet.analysis();
					BoundednessTestResult boundednessTestResult = petrinet.getBoundednessTestResult();
					boundednessTestResultList.add(boundednessTestResult);
					
					String bounded = "";
					String result = "Ergebnis: ";
					if(boundednessTestResult.getUnbounded()) {
						bounded = "nicht";
						result = "Pfadlänge: "+boundednessTestResult.getPathLength() + " Pfad: (";
						for(MarkingConnector markingConnector : boundednessTestResult.getPath()) {
							result = result + markingConnector.getTransitionCaused()+",";
						}
						result = result.substring(0, result.length()-1) + "); m -> m': (" + boundednessTestResult.getStart().getId() +") -> (" +boundednessTestResult.getEnd().getId()+").";
					} else {
						result = result + "Knoten: "+ petrinet.getNumberOfMarkingsInReachabilityNet() + ", Kanten: "+ petrinet.getNumberOfConnectionsInReachabilityNet()+".";
					}
					frame.addTextToTextArea("Das analysierte Petrinetz "+boundednessTestResult.getName() + " ist "+bounded+" beschränkt.");
					frame.addTextToTextArea(result);
					frame.addTextToTextArea("");
				} else {
					frame.addTextToTextArea("Das Netz besitzt keine Stellen.");
				}
			}
			if(!boundednessTestResultList.isEmpty()) {
				int maxLenName = "Dateiname".length();
				int lenBounded = "beschränkt".length();
				int maxLenNodesEdgesPath = "Pfadlänge:Pfad; m, m'".length();
				int maxLenNodes = 1;
				int maxLenEdges = 1;
				int maxLenPathLen = 1;
				int maxLenPath = 1;
				int maxLenMarking = 0;
				for(BoundednessTestResult boundednessTestResult : boundednessTestResultList) {
					if(boundednessTestResult.getName().length() > maxLenName) {
						maxLenName = boundednessTestResult.getName().length();
					}
					if(boundednessTestResult.getUnbounded()) {
						int lenPathLen = (boundednessTestResult.getPathLength()+"").length();
						if(lenPathLen > maxLenPathLen) {
							maxLenPathLen = lenPathLen;
						}
						int lenMarking = ("("+boundednessTestResult.getStart().getId()+"),").length();
						if(lenMarking > maxLenMarking) {
							maxLenMarking = lenMarking;
						}
						String path = "(";
						for(MarkingConnector mc : boundednessTestResult.getPath()) {
							path = path+mc.getTransitionCaused() + ",";
						}
						path = path.substring(0, path.length()-1) + ");";
						if(path.length() > maxLenPath) {
							maxLenPath = path.length();
						}
					} else {
						int lenNodes = (boundednessTestResult.getNumberOfNodes() +"").length();
						if(lenNodes > maxLenNodes)
							maxLenNodes = lenNodes;
						int lenEdges = (boundednessTestResult.getNumberOfEdges()+"").length();
						if(lenEdges > maxLenEdges)
							maxLenEdges = lenEdges;
						if((maxLenNodes+3+maxLenEdges)>maxLenNodesEdgesPath)
							maxLenNodesEdgesPath = maxLenNodes+3+maxLenEdges;
					}
					if(maxLenNodesEdgesPath < (maxLenPathLen+1+maxLenPath+maxLenMarking+maxLenMarking-1)) {
						maxLenNodesEdgesPath = (maxLenPathLen+1+maxLenPath+maxLenMarking+maxLenMarking-1);
					}
				}
				String topLine = "";
				for(int i = 0; i < maxLenName;i++) {
					topLine = topLine + "-";
				}
				topLine = topLine+"-|-";
				for(int i = 0; i < lenBounded;i++) {
					topLine = topLine + "-";
				}
				topLine = topLine+"-|-";
				for(int i = 0; i < maxLenNodesEdgesPath;i++) {
					topLine = topLine + "-";
				}
				String heading1 = String.format("%-"+maxLenName+"s | %"+lenBounded+"s | %-"+maxLenNodesEdgesPath+"s", "","","Knoten / Kanten bzw.");
				frame.addTextToTextArea(heading1);
				String heading2 = String.format("%-"+maxLenName+"s | %"+lenBounded+"s | %-"+maxLenNodesEdgesPath+"s", "Dateiname","beschränkt","Pfadlänge:Pfad; m, m'");
				frame.addTextToTextArea(heading2);
				frame.addTextToTextArea(topLine);		
				for(BoundednessTestResult boundednessTestResult : boundednessTestResultList) {
					if (boundednessTestResult.getUnbounded()) {
						String path = "(";
						for(MarkingConnector mc : boundednessTestResult.getPath()) {
							path = path+mc.getTransitionCaused()+",";
						}
						path = path.substring(0, path.length()-1) + ");";
						String startMarking = "("+boundednessTestResult.getStart().getId()+"),";
						String endMarking = "("+boundednessTestResult.getEnd().getId()+")";
						
						String table = String.format("%-"+maxLenName+"s | %-"+lenBounded+"s | %-"+maxLenPathLen+"d:%-"+maxLenPath+"s %-"+maxLenMarking+"s %-"+(maxLenMarking-1)+"s",
								boundednessTestResult.getName(), "nein", boundednessTestResult.getPathLength(), path, startMarking, endMarking);
						frame.addTextToTextArea(table);
					} else {
						String table = String.format("%-"+maxLenName+"s | %-"+lenBounded+"s | %"+maxLenNodes+"d / " + "%"+maxLenEdges+"d",boundednessTestResult.getName(), "ja", boundednessTestResult.getNumberOfNodes(), boundednessTestResult.getNumberOfEdges());
						frame.addTextToTextArea(table);
					}
				}
			}
		} else {
			frame.addTextToTextArea("Leere Liste übergeben.");
		}
		petrinet = petrinetHelp;
	}

	/**
	 * Ruft die Methode {@link model.Petrinet#clickNodeInPetrinetGraph(String, boolean)} auf.
	 * Dadurch werden Stellen aus-/abgewählt oder Transitionen geschaltet.
	 * Gibt dabei die Information weiter, ob mehrere Stellen gleichzeitig bearbeitet werden dürfen und welcher Knoten angeklickt wurde.
	 * Aktualisiert anschließend die beiden im MainFrame abgebildeten Graphen.
	 * Ist kein Petrinetz geladen erfolgt eine Ausgabe im Textfeld des MainFrames.
	 * @param id id des im Petrinetzes angeklickten Knotens.
	 */
	void clickNodeInPetrinetGraph(String id) {
		if(!(petrinet == null)) {
			petrinet.clickNodeInPetrinetGraph(id, allowMultiplePlaces);
			updatePetrinetGraph();
			updateReachabilityGraph();
		} else {
			frame.addTextToTextArea("Noch kein Petrinetz geladen.");
		}	
	}
	
	/**
	 * Ruft die Methode {@link model.Petrinet#clickNodeInReachabilityGraph(String)} auf.
	 * Dadurch wird das Petrinetz auf die ausgewählte Markierung gesetzt und diese Markierung im Erreichbarkeitsgraph visuell hervorgehoben.
	 * Aktualisiert anschließend die beiden im MainFrame abgebildeten Graphen.
	 * Ist kein Petrinetz geladen erfolgt eine Ausgabe im Textfeld des MainFrames.
	 * @param id id des im Erreichbarkeitsgraphen angeklickten Knotens / Markierung.
	 */
	void clickNodeInReachabilityGraph(String id) {
		if(!(petrinet == null)) {
			petrinet.clickNodeInReachabilityGraph(id);
			updatePetrinetGraph();
			updateReachabilityGraph();
		} else {
			frame.addTextToTextArea("Noch kein Petrinetz geladen.");
		}
	}
	
	/**
	 * Setzt den Erreichbarkeitsgraphen und das Petrinetz auf die aktuelle Anfangsmarkierung zurück.
	 * Aktualisiert anschließend die beiden im MainFrame abgebildeten Graphen.
	 * Ist kein Petrinetz geladen erfolgt eine Ausgabe im Textfeld des MainFrames.
	 */
	public void deleteReachabilityGraph() {
		if(!(petrinet == null)) {
			petrinet.resetReachabilityNet();
			drawPetrinetGraph();
			drawReachabilityGraph();
		} else {
			frame.addTextToTextArea("Noch kein Petrinetz geladen.");
		}
	}
	
	/**
	 * Zeichnet den Graphen des Petrinetzes.
	 * Ruft die Methode zum leeren des Graphen auf ({@link view.PetrinetGraph#clear()}).
	 * Übernimmt je eine Liste aller Stellen, Transitionen und Kanten vom zugeordneten Petrinetz mithilfe der zur Verfügung gestellten Funktionen ({@link model.Petrinet#getPlaces()}, {@link model.Petrinet#getTransitions()}. {@link model.Petrinet#getArcs()}).
	 * Fügt für jedes Element der Liste das passende Element im Graphen (Stellen und Transitionen Knoten, Arcs Kanten) hinzu.
	 */
	private void drawPetrinetGraph() {
		petrinetGraph.clear();
		if(!(petrinet == null)) {
			LinkedList<Place> places = petrinet.getPlaces();
			if(!places.isEmpty()) {
				for(Place place : places) {
					Node n = petrinetGraph.addNode(place.getId());
					n.addAttribute("ui.label", "["+place.getId()+"] "+place.getName()+"<"+place.getTokens()+">");
					n.addAttribute("xy", place.getX(), -place.getY());
					int tokens = place.getTokens();
					String attribute = "marke";
					if(tokens <= 9) {
						attribute = attribute + tokens;
					} else {
						attribute = attribute + "Plus";
					}
					if(place.getSelected()) {
						attribute = attribute + "Selected";
					}
					n.addAttribute("ui.class", attribute);
				}
			}
			LinkedList<Transition> transitions = petrinet.getTransitions();
			if(!transitions.isEmpty()) {
				for(Transition transition : transitions) {
					Node n = petrinetGraph.addNode(transition.getId());
					n.addAttribute("ui.label", "["+transition.getId()+"] "+transition.getName());
					n.addAttribute("xy", transition.getX(), -transition.getY());
					if(transition.isEnabled()) {
						n.addAttribute("ui.class", "transitionEnabled");
					} else {
						n.addAttribute("ui.class", "transition");
					}
				}
			}
			LinkedList<Arc> arcs = petrinet.getArcs();
			if(!arcs.isEmpty()) {
				for(Arc arc : arcs) {
					Edge e = petrinetGraph.addEdge(arc.getId(), arc.getSourceId(), arc.getTargetId(), true);
					e.addAttribute("ui.label", "["+arc.getId()+"]");
				}
			}
		}
	}
	
	/**
	 * Zeichnet den Erreichbarkeitsgraphen in der Anfangsmarkierung.
	 */
	private void drawReachabilityGraph() {
		reachabilityGraph.clear();
		if(!(petrinet == null)) {
			if(!petrinet.getPlaces().isEmpty()) {
				Marking marking = petrinet.getInitialMarking();
				Node n = reachabilityGraph.addNode(marking.getId());
				n.addAttribute("ui.label", "("+marking.getId()+")");
				n.addAttribute("ui.class", "initialHighlight");
			}
			
		}
	}
	
	/**
	 * Gibt den Pfad der zuletzt geöffneten PNML-Datei aus.
	 * @return Pfad der zuletzt geöffneten PNML-Datei bzw. Ordner mit den Beispiel-PNML-Dateien (default).
	 */
	public String getOpenFilePath() {
		return openFilePath;
	}
	
	/**
	 * Gibt den Graphen des Petrinetzes zurück, um diesen in das passende ViewerPanel des MainFrame einzubinden.
	 * @return Graph des aktuell geladenen Petrinetzes.
	 */
	public PetrinetGraph getPetrinetGraph() {
		return petrinetGraph;
	}
	
	/**
	 * Gibt den Erreichbarkeitsgraph zurück, um diesen in das passende ViewerPanel des MainFrame einzubinden.
	 * @return Erreichbarkeitsgraph des aktuell geladenen Petrinetzes.
	 */
	public ReachabilityGraph getReachabilityGraph() {
		return reachabilityGraph;
	}
	
	/**
	 * Übergibt die geladene PNML-Datei an den Parser und weist das dadurch erzeugte Petrinetz der Variable {@link #petrinet} zu.
	 * Zeichnet den Graphen des Petrinetzes und des Erreichbarkeitsgraphen für das so resultierende Petrinetz neu.
	 */
	private void loadAndReload() {
		try {
			PNMLParser parser = new PNMLParser(openFile);
			petrinet = parser.getPetrinet();
			drawPetrinetGraph();
			drawReachabilityGraph();
		} catch (NullPointerException ex) {
			frame.addTextToTextArea("Fehler beim Laden der Datei.");
		}
		
	}
	
	/**
	 * Laden einer PNML-Datei, Anlegen des Petrinetzes und Zeichnen des initialen Petrinetzes und Anfangsmarkierung des Erreichbarkeitsgraphen.
	 * Nutzt dafür die private Hilfsmethode {@link #loadAndReload()}.
	 * Speichert Datei-Name und Pfad im Controller
	 * @param file Eingabedatei
	 */
	public void loadFile(File file) {
		//setOpenFilePath(file.getAbsolutePath());
		//System.out.println(file.getParent());
		setOpenFilePath(file.getParent());
		setOpenFile(file);
		loadAndReload();
	}
	
	/**
	 * Erneutes Laden der zuletzt geladenen PNML-Datei. Setzt das Petrinetz und den Erreichbarkeitsgraphen auf die Anfangsmarkierung zurück.
	 * Nutzt dazu die private Hilfsmethode {@link #loadAndReload()}.
	 * Ist kein Petrinetz geladen erfolgt eine Ausgabe im Textfeld des MainFrames.
	 */
	public void reloadFile() {
		if(!(openFile == null)) {
			loadAndReload();
		} else {
			frame.addTextToTextArea("Noch kein Petrinetz geladen.");
		}
	}
	
	/**
	 * Entfernen einer Marke von allen aktuell ausgewählten Stellen.
	 * Ruft dafür die Methode {@link model.Petrinet#changeTokensAtSelectedPlaces(int)} mit dem Wert -1 auf.
	 * Erneuert den Graphen des Petrinetzes mittels der Methode {@link #updatePetrinetGraph()}.
	 * Zeichnet den Erreichbarkeitsgraph mittels der Methode {@link #drawReachabilityGraph()}.
	 * Ist kein Petrinetz geladen erfolgt eine Ausgabe im Textfeld des MainFrames.
	 */
	public void removeTokensFromSelectedPlaces() {
		if(!(petrinet == null)){
			petrinet.changeTokensAtSelectedPlaces(-1);
			updatePetrinetGraph();
			drawReachabilityGraph();
		} else {
			frame.addTextToTextArea("Noch kein Petrinetz geladen.");
		}
	}
	
	/**
	 * Setzt das Petrinetz auf die Anfangsmarkierung zurück.
	 * Ermöglicht ein Durchschalten auf alternativem Pfad.
	 * Die bisher erreichten Knoten im Erreichbarkeitsgraph bleiben bestehen.
	 * Die Anfangsmarkierung wird durch Hervorhebung als aktuelle Markierung gekennzeichnet.
	 * Ist kein Petrinetz geladen erfolgt eine Ausgabe im Textfeld des MainFrames.
	 */
	public void resetPetrinet() {
		if(!(petrinet == null)) {
			petrinet.resetPetrinetToInitialMarking();
			updatePetrinetGraph();
			updateReachabilityGraph();
		} else {
			frame.addTextToTextArea("Noch kein Petrinetz geladen.");
		}
	}
	

	/**
	 * Setzt den internen Verweis der aktuellen Datei auf die übergebene Datei.
	 * @param file Datei, die geladen worden ist.
	 */
	private void setOpenFile(File file) {
		this.openFile = file;
	}
	
	/**
	 * Setzt den internen Verweis des aktuellen Dateipfads auf den im String übergebenen Pfad.
	 * @param openFilePath Pfad zur aktuellen Datei.
	 */
	private void setOpenFilePath(String openFilePath) {
		this.openFilePath = openFilePath;
	}
	
	/**
	 * Umschalten zwischen der Auswahl einer einzelnen Stelle und mehreren Stellen, um gleichzeitig an mehreren Stellen Marken hinzufügen zu können.
	 * Ist mehrfache Auswahl nicht erlaubt wird die Methode {@link model.Petrinet#unselectPlaces()} aufgerufen.
	 * Aktualisiert den Graphen des Petrinetzes.
	 */
	public void toggleAllowMultiplePlaces() {
		allowMultiplePlaces = !allowMultiplePlaces;
		if(!(petrinet == null)) {
			if(!allowMultiplePlaces) {
				petrinet.unselectPlaces();
			}
			updatePetrinetGraph();
		}
	}
	
	/**
	 * Aktualisiert den Graphen des Petrinetzes.
	 * Lädt dafür alle Stellen und Transitionen des Petrinetzes und fügt sukzessive neue Attriobute hinzu.
	 */
	private void updatePetrinetGraph() {
		for(Place p : petrinet.getPlaces()) {
			Node n = petrinetGraph.getNode(p.getId());
			String s = "marke";
			if(p.getTokens() > 9) {
				s = s + "Plus";
			} else {
				s = s+p.getTokens();
			}
			if(p.getSelected()) {
				s = s + "Selected";
			}
			n.addAttribute("ui.class", s);
			n.addAttribute("ui.label", "["+p.getId()+"] "+p.getName()+"<"+p.getTokens()+">");
		}
		for(Transition t : petrinet.getTransitions()) {
			Node n = petrinetGraph.getNode(t.getId());
			String s = "transition";
			if(t.isEnabled()) {
				s = s+"Enabled";
			}
			n.addAttribute("ui.class", s);
			n.addAttribute("ui.label", "["+t.getId()+"] "+t.getName());
		}
	}

	/**
	 * Hilfsmethode für {@link #updateReachabilityGraph()}.
	 * Fügt die jeweiligen Eigenschaften aus der Markierung hinzu.
	 * @param n Knoten dessen Eigenschaften angepasst werden sollen.
	 * @param marking Markierung, die auf ihre Eigenschaften untersucht werden soll.
	 */
	private void updateReachabilityNode(Node n, Marking marking) {
		if(marking == petrinet.getCurrentMarking()) {
			if(marking.getIsPathStart()) {
				n.addAttribute("ui.class", "startPathHighlight");
			} else if (marking.getIsPathEnd()) {
				n.addAttribute("ui.class", "endPathHighlight");
			} else if (marking == petrinet.getInitialMarking()) {
				n.addAttribute("ui.class", "initialHighlight");
			} else {
				n.addAttribute("ui.class", "highlight");
			}
		} else {
			if(marking.getIsPathStart()) {
				n.addAttribute("ui.class", "startPath");
			} else if (marking.getIsPathEnd()) {
				n.addAttribute("ui.class", "endPath");
			} else if (marking == petrinet.getInitialMarking()) {
				n.addAttribute("ui.class", "initial");
			}
		}
	}
	
	/**
	 * Hilfsmethode für {@link #updateReachabilityGraph()}.
	 * Fügt die jeweiligen Eigenschaften aus der Kante hinzu.
	 * @param e Kante deren Eigenschaften angepasst werden sollen.
	 * @param markingConnector Kante des Erreichbarkeitsnetzes (aus dem Modell), das auf seine Eigenschaften untersucht werden soll.
	 */
	private void updateReachabilityEdge(Edge e, MarkingConnector markingConnector) {
		if(markingConnector.getIsInPath()) {
			if(markingConnector.getIsCurrent()) {
				e.addAttribute("ui.class", "pathHighlight");
			} else {
				e.addAttribute("ui.class", "path");
			}
		} else {
			if(markingConnector.getIsCurrent()) {
				e.addAttribute("ui.class", "highlight");
			} else {
				e.removeAttribute("ui.class");
			}
		}
	}
	
	/**
	 * Aktualisiert den Erreichbarkeitsgraphen. Nutzt dafür die Hilfsmethoden {@link #updateReachabilityNode(Node, Marking)} und {@link #updateReachabilityEdge(Edge, MarkingConnector)}.
	 * Versucht für jede Markierung und jede Kante im Erreichbarkeitsnetz einen neuen Knoten bzw. eine neue Kante einzufügen.
	 * Ist das jeweilige Element bereits vorhanden werden die Eigenschaften mit der passenden Hilfsmethode angepasst.
	 */
	private void updateReachabilityGraph() {
		if(!(petrinet == null)) {
			if(!(petrinet.getReachabilityMarkings() == null)) {
				for(Marking marking : petrinet.getReachabilityMarkings()) {
					try {
						Node n = reachabilityGraph.addNode(marking.getId());
						n.addAttribute("ui.label", "("+marking.getId()+")");
						updateReachabilityNode(n, marking);
					} catch (org.graphstream.graph.IdAlreadyInUseException ex) {
						Node n = reachabilityGraph.getNode(marking.getId());
						n.removeAttribute("ui.class");
						updateReachabilityNode(n, marking);
					} catch (Exception ex) {
						System.out.println(ex);
					}
				}
			}
			if(!(petrinet.getReachabilityMarkingConnectors() == null)) {
				for(MarkingConnector markingConnector : petrinet.getReachabilityMarkingConnectors()) {
					try {
						Edge e = reachabilityGraph.addEdge(markingConnector.getId(), markingConnector.getMarkingBefore(), markingConnector.getMarkingBehind(), true);
						e.addAttribute("ui.label", "["+markingConnector.getTransitionCaused()+"] " + petrinet.getTransition(markingConnector.getTransitionCaused()).getName());
						updateReachabilityEdge(e, markingConnector);
					} catch (org.graphstream.graph.IdAlreadyInUseException ex) {
						Edge e = reachabilityGraph.getEdge(markingConnector.getId());
						updateReachabilityEdge(e, markingConnector);
					} catch (Exception ex) {
						System.out.println(ex);
					}
				}
			}
		}
	}
}
