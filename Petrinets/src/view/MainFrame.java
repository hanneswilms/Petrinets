package view;

import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.imageio.*;
import controller.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;

/**
 * Diese Klasse stellt die graphische Oberfläche des Programms dar. Erbt von JFrame.
 * 
 * @author Hannes Wilms
 *
 */
public class MainFrame extends JFrame {
	
	/** Dient zur eindeutigen Identifikation der Version */
	private static final long serialVersionUID = 1L;
	
	/** Menüleiste, beinhaltet die Menüs */
	private JMenuBar menubar;
	
	/** Datei-Menü. Beinhaltet Menüitems zum Laden, Neuladen, Analyse mehrerer Dateien und Schließen */
	private JMenu fileMenu;
	
	/** Menüitem zum Öffnen von PNML-Dateien. Öffnet einen JFileChooser. */
	private JMenuItem openMenuItem;
	
	/** Menüitem zum Neuladen des geladenen Netzes */
	private JMenuItem reloadMenuItem;
	
	/** Menüitem zur Analyse mehrerer Dateien in alphabetischer Reihenfolge ohne Ausgabe im grafischen Bereich des Fensters. Öffnet einen JFileCHooser. */
	private JMenuItem multiAnalysisMenuItem;
	
	/** Schließt das Fenster und beendet damit die Anwendung. */
	private JMenuItem closeWindowMenuItem;
	
	/** Menü zum Exportieren von aktuellen Informationen. Beinhaltet Menüitems zum Speichern einer PNML-Datei mit aktuellem Zustand sowie von Screenshots der beiden Graphen. */
	private JMenu exportMenu;
	
	/** Menüitem zum Speichern des Petrinetzes mit der aktuellen Markierung als neuen Startzustand. Öffnet einen JFileChooser. */
	private JMenuItem saveMenuItem;
	
	/** Menüitem zum Speichern des aktuell dargestellten Petrinetzes als JPEG-Datei. */
	private JMenuItem pnScreenshotMenuItem;
	
	/** Menüitem zum Speichern des aktuell dargestellten Erreichbarkeitsgraphen als JPEG-Datei. */
	private JMenuItem rgScreenshotMenuItem;
	
	/** Hilfe-Menü. Ermöglicht Anzeige der Information über den Entwickler. */
	private JMenu helpMenu;
	
	/** Menüitem zum Anzeigen der Informationen über den Entwickler. */
	private JMenuItem aboutMenuItem;
	
	/** Toolbar in der die Buttons zur grafischen Analyse des geladenen Petrinetzes aufgeführt sind. */
	private JToolBar toolbar;
	
	/** Button zum Leeren des Textbereichs im unteren Abschnitt des Fensters. */
	private JButton deleteTextAreaButton;
	
	/** Button zum Zurücksetzen des Erreichbarkeitsgraphen auf die Anfangsmarkierung. */
	private JButton deleteReachabilityGraphButton;
	
	/** Button, um ausgewählten Stellen Marken hinzuzufügen. */
	private JButton addTokenButton;
	
	/** Button, um an ausgewählten Stellen Marken zu entfernen. */
	private JButton removeTokenButton;
	
	/** Button, um das Petrinetz auf die aktuelle Anfangsmarkierung zurückzusetzen. */
	private JButton resetButton;
	
	/** Button zur Analyse der Beschränktheit des aktuell geladenen Petrinetzes. */
	private JButton analysisButton;
	
	/** Button, um die gleichzeitige Bearbeitung der Marken mehrerer Stellen ein- oder auszuschalten. */
	private JToggleButton multiplePlacesButton;
	
	/** Hauptbereich der grafischen Oberfläche. Panel, das die Panels zur Darstellung der Graphen sowie den Textbereich beinhaltet. */
	private JSplitPane mainPanel;
	
	/** Oberer Bereich der grafischen Oberfläche. Panel, das die Panels zur Darstellung der Graphen beinhaltet. */
	private JSplitPane topPanel;
	
	/** Panel, in dem das Petrinetz dargestellt wird. */
	private JPanel petrinetPanel;
	
	/** Panel, ion dem der (partielle) Erreichbarkeitsgraph dargestellt wird. */
	private JPanel reachabilityPanel;
	
	/** Panel, in dem der Textbereich zur Ergebnisausgabe dargestellt wird. */
	private JScrollPane bottomPanel;
	
	/** Textbereich zur Ergebnis- und Meldungsausgabe. */
	private JTextArea terminal;
	
	/** Viewer für das Petrinetz. */
	private Viewer petrinetViewer;
	
	/** ViewPanel, in dem das Petrinetz dargestellt wird. */
	private ViewPanel petrinetViewPanel;
	
	/** Viewer für den Erreichbarkeitsgraphen. */
	private Viewer reachabilityViewer;
	
	/** ViewPanel, in dem der Erreichbarkeitsgraph dargestellt wird. */
	private ViewPanel reachabilityViewPanel;
	
	/** Controller, der die aufgerufenen Befehle an das Petrinetz weitergibt bzw. selbst ausführt und Aktualisierungen in diesem MainPanel befiehlt. */
	private Controller controller;
	
	/**
	 * Erzeugt einen neuen MainFrame. Gibt den Namen und die Matrikelnummer an die Superklasse ({@link JFrame}) weiter.
	 * Erzeugt einen neuen {@link Controller} und übergibt sich selbst an diesen Controller in dessen Konstruktor.
	 * Das Fenster wird über die privaten Methoden {@link initializeMenuBar}, {@link initializeToolBar} und {@link initializeMainPanel} aufgebaut.
	 */
	public MainFrame() {
		super("Hannes Wilms - 9739335");
		System.setProperty("org.graphstream.ui.renderer",
				"org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		try {
			setIconImage(ImageIO.read(ClassLoader.getSystemResource("images/preditor.png")));
		} catch (Exception e) {System.out.println("Start ohne Abbildungen");}
		
		controller = new Controller(this);
		
		this.setSize(1000,700);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		initializeMenuBar();
		this.setJMenuBar(menubar);
		initializeToolBar();
		this.add(toolbar, BorderLayout.NORTH);
		initializeMainPanel();
		this.add(mainPanel, BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	/**
	 * Fügt den übergebenen String in das Textfeld im unteren Bereich der grafischen Oberfläche an. Fügt anschließend einen Zeilenumrbuch an.
	 * @param s String, der dem Textfeld hinzugefügt werden soll. 
	 */
	public void addTextToTextArea(String s) {
		terminal.setText(terminal.getText()+s+"\n");
	}
	
	/**
	 * Methode zur Initialisierung des Haupt-Panels der grafischen Oberfläche.
	 * Ruft die Methoden zur Iinitialisierung der Panels von Petrinetz und Erreichbarkeitsgraph auf.
	 */
	private void initializeMainPanel() {
		mainPanel = new JSplitPane();
		mainPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		topPanel = new JSplitPane();
		topPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		initializePetrinetPanel();
		initializeReachabilityPanel();
		topPanel.setLeftComponent(petrinetPanel);
		topPanel.setRightComponent(reachabilityPanel);
		topPanel.setResizeWeight(0.5);
		terminal = new JTextArea("");
		terminal.setFont(new Font("monospaced", Font.PLAIN, 12));
		bottomPanel = new JScrollPane(terminal);
		bottomPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		bottomPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		mainPanel.setTopComponent(topPanel);
		mainPanel.setBottomComponent(bottomPanel);
		mainPanel.setResizeWeight(0.75);
	}
	
	/**
	 * Methode zur Initialisierung der Menüleiste.
	 */
	private void initializeMenuBar() {
		menubar = new JMenuBar();
		fileMenu = new JMenu("Datei");
		menubar.add(fileMenu);
		
		openMenuItem = new JMenuItem("Öffnen...");
		openMenuItem.addActionListener(new ActionListener() {
			/**
			 * Erzeugt einen JFileChooser zum Öffnen einer Datei.
			 * Weist den Controller an, die ausgewählte Datei zu laden.
			 */
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(new File(controller.getOpenFilePath()));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Petrinetze (*.pnml, *.PNML)","pnml","PNML");
				chooser.setFileFilter(filter);
				chooser.setDialogTitle("Öffnen... graphische Analyse");
				int rueckgabeWert = chooser.showOpenDialog(mainPanel);
				if(rueckgabeWert == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					controller.loadFile(file);
				}
			}
		});
		fileMenu.add(openMenuItem);
		
		reloadMenuItem = new JMenuItem("Neu laden");
		reloadMenuItem.addActionListener(new ActionListener() {
			/**
			 * Weist den Controller an, die geöffnete Datei erneut zu laden.
			 */
			public void actionPerformed(ActionEvent e) {
				controller.reloadFile();
			}
		});
		fileMenu.add(reloadMenuItem);
		
		multiAnalysisMenuItem = new JMenuItem("Analyse mehrerer Dateien");
		multiAnalysisMenuItem.addActionListener(new ActionListener() {
			/**
			 * Öffnet einen JFileChooser zur Auswahl mehrerer Dateien.
			 * Weist den Controller an, die Dateien in der Liste zu analysieren.
			 */
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(new File(controller.getOpenFilePath()));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Petrinetze (*.pnml, *.PNML)","pnml","PNML");
				chooser.setFileFilter(filter);
				chooser.setDialogTitle("Öffnen... Stapelanalyse");
				chooser.setMultiSelectionEnabled(true);
				int rueckgabeWert = chooser.showOpenDialog(mainPanel);
				if(rueckgabeWert == JFileChooser.APPROVE_OPTION) {
					File[] files = chooser.getSelectedFiles();
					java.util.Arrays.sort(files);
					controller.multipleAnalysis(files);
				}
			}
		});
		fileMenu.add(multiAnalysisMenuItem);
		
		closeWindowMenuItem = new JMenuItem("Beenden");
		closeWindowMenuItem.addActionListener(new ActionListener() {
			/**
			 * Beendet die Anwendung und schließt das Fenster.
			 */
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(closeWindowMenuItem);
		
		exportMenu = new JMenu("Export");
		menubar.add(exportMenu);
		saveMenuItem = new JMenuItem("Speichern");
		saveMenuItem.addActionListener(new ActionListener() {
			/**
			 * Öffnet einen JFileChooser, um eine neue PNML-Datei zu erzeugen, in die das Petrinetz mit der aktuellen Markierung als Anfangsmarkierung gespeichert werden kann.
			 * Weist den Controller an, das Netz in die übergebene Datei zu speichern.
			 */
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(new File(controller.getOpenFilePath()));
				if(!(controller.getPetrinetName() == null)) {
					chooser.setSelectedFile(new File(controller.getOpenFilePath()+"/"+controller.getPetrinetName()));
				}
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Petrinetze (*.pnml, *.PNML)","pnml","PNML");
				chooser.setFileFilter(filter);
				chooser.setDialogTitle("Speichern...");
				int rueckgabeWert = chooser.showSaveDialog(mainPanel);
				if(rueckgabeWert == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					controller.saveFile(file);
				}
			}
		});
		exportMenu.add(saveMenuItem);
		
		pnScreenshotMenuItem = new JMenuItem("Petrinetz-Darstellung speichern");
		pnScreenshotMenuItem.addActionListener(new ActionListener() {
			/**
			 * Weist den Controller an, die aktuelle Darstellung des Petrinetzes als JPEG-Datei zu speichern.
			 */
			public void actionPerformed(ActionEvent e) {
				controller.pnScreenshot();
			}
		});
		exportMenu.add(pnScreenshotMenuItem);
		
		rgScreenshotMenuItem = new JMenuItem("Erreichbarkeitsgraph-Darstellung speichern");
		rgScreenshotMenuItem.addActionListener(new ActionListener() {
			/**
			 * Weist den Controller an, die aktuelle Darstellung des Erreichbarkeitsgraphen als JPG-Datei zu speichern.
			 */
			public void actionPerformed(ActionEvent e) {
				controller.rgScreenshot();
			}
		});
		exportMenu.add(rgScreenshotMenuItem);
		
		helpMenu = new JMenu ("Hilfe");
		menubar.add(helpMenu);
		
		aboutMenuItem = new JMenuItem("Über");
		aboutMenuItem.addActionListener(new ActionListener() {
			/**
			 * Öffnet ein Message-Fenster, in dem die Informationen über das Programm und den Entwickler angegeben sind.
			 */
			public void actionPerformed(ActionEvent e) {
				ImageIcon logo = new ImageIcon(ClassLoader.getSystemResource("images/preditor.png"));
				String about = "Petrinet & Reachability Editor\nHannes Wilms - 9739335 - FernUniversität in Hagen\nProgrammierpraktikum Wintersemester 2020/21\nProgramm zur Darstellung, Bearbeitung und Analyse\nvon Petrinetzen und Erreichbarkeitsgraphen\n";
				JOptionPane.showMessageDialog(mainPanel, about, "Über", JOptionPane.INFORMATION_MESSAGE, logo);
			}
		});
		helpMenu.add(aboutMenuItem);
		
	}
	
	/**
	 * Initialisiert das Panel des Petrinetzes.
	 * Ruft dafür die private Methode initializePetrinetView auf.
	 * Erzeugt ein neues JPanel, dem das ViewPanel des Petrinetzes zugewiesen wird.
	 */
	private void initializePetrinetPanel() {
		initializePetrinetView();
		petrinetPanel = new JPanel(new BorderLayout());
		petrinetPanel.add(BorderLayout.CENTER, petrinetViewPanel);
	}
	
	/**
	 * Initialisiert den Viewer des Petrinetzes.
	 * Erzeugt einen Neuen PetrinetClickListener, der an diesem Panel angemeldet wird.
	 */
	private void initializePetrinetView() {
		petrinetViewer = new Viewer(controller.getPetrinetGraph(), Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		petrinetViewer.disableAutoLayout();
		petrinetViewPanel = petrinetViewer.addDefaultView(false);
		ViewerPipe viewerPipe = petrinetViewer.newViewerPipe();
		PetrinetClickListener petrinetClickListener = new PetrinetClickListener(controller);
		viewerPipe.addViewerListener(petrinetClickListener);
		petrinetViewPanel.addMouseListener(new MouseAdapter() {
			/**
			 * Ruft die Methode pump der ViewerPipe auf, um den Mausklick weiterzugeben.
			 */
			@Override
			public void mousePressed(MouseEvent me) {
				viewerPipe.pump();
			}
			
			/**
			 * Ruft die Methode pump der ViewerPipe auf, um den Release des Mausklicks weiterzugeben.
			 */
			@Override
			public void mouseReleased(MouseEvent me) {
				viewerPipe.pump();
			}
		});
	}
	
	/**
	 * Initiailisert das Panel des Erreichbarkeitsgraphen.
	 * Ruft dafür die private Methode initializeReachabilityView auf.
	 * Erzeugt ein neues JPanel, dem das ViewPanel des Erreichbarkeitsgraphen zugewiesen wird.
	 */
	private void initializeReachabilityPanel() {
		initializeReachabilityView();
		reachabilityPanel = new JPanel(new BorderLayout());
		reachabilityPanel.add(BorderLayout.CENTER, reachabilityViewPanel);
	}
	
	/**
	 * Initialisiert den Viewer des Erreichbarkeitsgraphen.
	 * Erzeugt einen neuen ReachabilityClickListener, der an diesem Panel angemeldet wird.
	 */
	private void initializeReachabilityView() {
		reachabilityViewer = new Viewer(controller.getReachabilityGraph(), Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);	
		reachabilityViewer.enableAutoLayout();
		reachabilityViewPanel = reachabilityViewer.addDefaultView(false);
		ViewerPipe viewerPipe = reachabilityViewer.newViewerPipe();
		ReachabilityClickListener reachabilityClickListener = new ReachabilityClickListener(controller);
		viewerPipe.addViewerListener(reachabilityClickListener);
		
		reachabilityViewPanel.addMouseListener(new MouseAdapter() {
			/**
			 * Ruft die Methode pump der ViewerPipe auf, um den Mausklick weiterzugeben.
			 */
			@Override
			public void mousePressed(MouseEvent me) {
				viewerPipe.pump();
			}
			
			/**
			 * Ruft die Methode pump der ViewerPipe auf, um den Release des Mausklicks weiterzugeben.
			 */
			@Override
			public void mouseReleased(MouseEvent me) {
				viewerPipe.pump();
			}
		});
	}
	
	/**
	 * Initialisiert die Toolbar.
	 * Erzeugt die Buttons zum Leeren des Textfelds, Löschen des Erreichbarkeitsgraphen, Hinzufügen und Entfernen von Marken,
	 * Zurücksetzen des Petrinetzes auf die aktuelle Anfangsmarkierung, Analyse des Netzes und der Auswahl mehrerer Stellen.
	 * 
	 */
	private void initializeToolBar() {
		toolbar = new JToolBar();
		
		try {
			deleteTextAreaButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/DeleteTerminal.png")));
		} catch (Exception e) {
			deleteTextAreaButton = new JButton("Terminal leeren");
		}
		
		deleteTextAreaButton.setToolTipText("<html><b>Terminal leeren</b><br>Durch Klicken dieses Buttons wird das Textfeld im unteren Fensterabschnitt geleert.</html>");
		deleteTextAreaButton.addActionListener(new ActionListener() {
			/**
			 * Löscht das Textfeld, indem der Text auf einen leeren Text gesetzt wird.
			 */
			public void actionPerformed(ActionEvent e) {
				terminal.setText("");
			}
		});
		toolbar.add(deleteTextAreaButton);
		
		try {
			deleteReachabilityGraphButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/deleteEG.png")));
		} catch (Exception e) {
			deleteReachabilityGraphButton = new JButton("Lösche EG");
		}
		
		deleteReachabilityGraphButton.setToolTipText("<html><b>Lösche EG</b><br>Durch Klicken dieses Buttons wird der (ggf. partielle) Erreichbarkeitsgraph gelöscht.<br>Dabei wird das Petrinetz auf die Anfangsmarkierung zurückgesetzt.<html>");
		deleteReachabilityGraphButton.addActionListener(new ActionListener() {
			/**
			 * Weist den Controller an, die Methode zum Löschen bzw. Zurücksetzen des Erreichbarkeitsgraphen aufzurufen.
			 */
			public void actionPerformed(ActionEvent e) {
				controller.deleteReachabilityGraph();
			}
		});
		toolbar.add(deleteReachabilityGraphButton);
		
		try {
			addTokenButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/tokenPlus.png")));
		} catch (Exception e) {
			addTokenButton = new JButton("Marke hinzufügen");
		}
		
		addTokenButton.setToolTipText("<html><b>Marke hinzufügen</b><br>Fügt einer ausgewählten Stelle eine Marke hinzu.<br>Stelle muss vorher ausgewählt werden.</html>");
		addTokenButton.addActionListener(new ActionListener() {
			/**
			 * Weist den Controller an, die Methode zum Hinzufügen von Marken zu ausgewählten Stellen aufzurufen.
			 */
			public void actionPerformed(ActionEvent e) {
				controller.addTokensToSelectedPlaces();
			}
		});
		
		try {
			removeTokenButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/tokenMinus.png")));
		} catch (Exception e) {
			removeTokenButton = new JButton("Marke entfernen");
		}
		
		removeTokenButton.setToolTipText("<html><b>Marke entfernen</b><br>Entfernt eine Marke der ausgewählten Stelle.<br>Stelle muss vorher ausgewählt werden.<br>Stelle muss mindestens eine Marke besitzen.</html>");
		removeTokenButton.addActionListener(new ActionListener() {
			/**
			 * Weist den Controller auf, die Methode zum Entfernen von Marken an ausgewählten Stellen aufzurufen.
			 */
			public void actionPerformed(ActionEvent e) {
				controller.removeTokensFromSelectedPlaces();
			}
		});
		
		try {
			resetButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/reset.png")));
		} catch (Exception e) {
			resetButton = new JButton("Reset");
		}

		resetButton.setToolTipText("<html><b>Reset</b><br>Setzt das Petrinetz auf die aktuelle Anfangsmarkierung zurück.<br><i>Achtung: dies muss nicht zwingend die Anfangsmarkierung der geladenen PNML-Datei sein!</i></html>");
		resetButton.addActionListener(new ActionListener() {
			/**
			 * Weist den Controller an, die Methode zum Zurücksetzen des Petrinetzes auf die aktuelle Anfangsmarkierung aufzurufen.
			 */
			public void actionPerformed(ActionEvent e) {
				controller.resetPetrinet();
			}
		});
		
		try {
			analysisButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/analysis.png")));
		} catch (Exception e) {
			analysisButton = new JButton("Analyse");
		}
		
		analysisButton.setToolTipText("<html><b>Analyse</b><br>Löscht den vorhandenen Erreichbarkeitsgraphen.<br>Setzt die Markierung des Petrinetzes auf die Anfangsmarkierung zurück.<br>Führt den Algorithmus zur Beschränktheitsanalyse durch.");
		analysisButton.addActionListener(new ActionListener() {
			/**
			 * Weist den Controller an, die Analyse des geladenen Petrinetzes durchzuführen.
			 * Gibt in einem Message-Fenster aus, ob das Petrinetz beschränkt oder unbeschränkt ist.
			 * Zeigt ggf. an, dass noch kein Petrinetz geladen wurde.
			 */
			public void actionPerformed(ActionEvent e) {
				Boolean result = controller.analysis();
				if(result == null) {
					JOptionPane.showMessageDialog(mainPanel, "Es wurde kein Petrinetz geladen.", "Analysefehler", JOptionPane.ERROR_MESSAGE);
				} else {
					String prefix = "";
					if(result) {
						prefix = "un";
					}
					JOptionPane.showMessageDialog(mainPanel, "Das Petrinetz ist "+prefix+"beschränkt.", "Analyseergebnis", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		
		toolbar.add(addTokenButton);
		toolbar.add(removeTokenButton);
		toolbar.add(resetButton);
		toolbar.add(analysisButton);
		
		try {
			multiplePlacesButton = new JToggleButton(new ImageIcon(ClassLoader.getSystemResource("images/SelectMultiplePlaces.png")));
			
		} catch (Exception e) {
			multiplePlacesButton = new JToggleButton("Auswahl mehrerer Stellen");
		}
		
		multiplePlacesButton.setToolTipText("<html><b>Auswahl mehrerer Stellen</b><br>Durch das Klicken des Buttons wird es ermöglicht, die Anzahl der Marken mehrerer Stellen gleichzeitig zu erhöhen oder zu reduzieren.<br>Ein erneutes Klicken des Buttons macht jeweils nur eine Stelle auswählbar.</html>");
		multiplePlacesButton.addActionListener(new ActionListener() {
			/**
			 * Weist den Controller an, die Methode zum Erlauben bzw. Verbieten der gleichzeitigen Bearbeitung mehrerer Stellen aufzurufen.
			 */
			public void actionPerformed(ActionEvent e) {
				controller.toggleAllowMultiplePlaces();
			}
		});
		toolbar.add(multiplePlacesButton);
	}
	
}
