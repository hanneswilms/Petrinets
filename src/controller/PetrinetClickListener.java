package controller;

import org.graphstream.ui.view.ViewerListener;

/**
 * Diese Klasse stellt einen ClickListener für den Graphen des Petrinetzes dar.
 * @author Hannes Wilms
 *
 */
public class PetrinetClickListener implements ViewerListener {

	/**
	 * Controller, der die View und das Modell verwaltet.
	 */
	private Controller controller;
	
	/**
	 * Erzeugt einen neuen PetrinetClickListener für einen spezifischen Controller.
	 * @param controller Controller, dem die Klicks mitgeteilt werden sollen.
	 */
	public PetrinetClickListener(Controller controller) {
		this.controller = controller;
	}
	
	/**
	 * Teilt dem Kontroller mit, dass ein Knoten im Graphen angeklickt wurde.
	 * Ruft die Methode {@link Controller#clickNodeInPetrinetGraph(String)} auf und übergibt dem Controller damit die id des angeklickten Knotens.
	 * @param id id des Knotens, der angeklickt wurde.
	 */
	@Override
	public void buttonPushed(String id) {
		if (controller != null) {
			controller.clickNodeInPetrinetGraph(id);
		}
	}
	
	/*
	 * Ohne Funktion.
	 */
	@Override
	public void buttonReleased(String id) {
	}
	
	/*
	 * Ohne Funktion.
	 */
	@Override
	public void viewClosed(String viewName) {
	}
}
