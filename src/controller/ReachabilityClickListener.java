package controller;

import org.graphstream.ui.view.ViewerListener;

/**
 * Diese Klasse stellt einen ClickListener für den Erreichbarkeitsgraphen dar.
 * @author Hannes Wilms
 *
 */
public class ReachabilityClickListener implements ViewerListener {

	/**
	 * Controller, der die View und das Modell verwaltet.
	 */
	private Controller controller;
	
	/**
	 * Erzeugt einen neuen ReachabilityClickListener für einen spezifischen Controller.
	 * @param controller Controller, dem die Klicks mitgeteilt werden sollen.
	 */
	public ReachabilityClickListener(Controller controller) {
		this.controller = controller;
	}
	
	/**
	 * Teilt dem Kontroller mit, dass ein Knoten im Graphen angeklickt wurde.
	 * Ruft die Methode {@link Controller#clickNodeInReachabilityGraph(String)} auf und übergibt dem Controller damit die id der angeklickten Markierung.
	 * @param id id des Knotens, der angeklickt wurde.
	 */
	@Override
	public void buttonPushed(String id) {
		if (controller != null) {
			controller.clickNodeInReachabilityGraph(id);
		}
	}
	
	/**
	 * Ohne Funktion.
	 */
	@Override
	public void buttonReleased(String id) {
	}
	
	/**
	 * Ohne Funktion.
	 */
	@Override
	public void viewClosed(String viewName) {
	}
}
