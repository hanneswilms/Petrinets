package model;

/**
 * Diese Klasse stellt die Kanten innerhalb des Modells des Erreichbarkeitsgraphen dar.
 * Sie Speichert die Markierungen vor und hinter der Kante, die verursachende Transition sowie die Informationen, ob sich diese Kante im Pfad m - m' befindet und ob diese Kante die zuletzt beschrittene Kante ist.
 * @author Hannes Wilms
 *
 */
public class MarkingConnector {

	/** id dieser Kante. Setzt sich zusammen aus den ids der Quell- und Zielmarkierung sowie der verursachenden Transition.*/
	private String id;
	
	/** id der Quellmarkierung dieser Kante. */
	private String markingBefore;
	
	/** id der Zielmarkierung dieser Kante. */
	private String markingBehind;
	
	/** id der verursachenden Transition. */
	private String transitionCaused;
	
	/** Information, ob sich diese Kante im Pfad m -> m' befindet. */
	private boolean isInPath = false;
	
	/** Information, ob dies die zuletzt beschrittene Kante des Erreichbarkeitsgraphen ist. */
	private boolean isCurrent = false;
	
	/**
	 * Erzeugt eine neue Kante für das Modell des Erreichbarkeitsgraphen.
	 * Speichert die ids der an dieser Kante beteiligten Elemente.
	 * Setzt die id dieser Kante aus den anderen ids zusammen.
	 * @param markingBefore id der Markierung, die vor dem Schalten der verursachenden Transition aktuell war.
	 * @param markingBehind id der Markierung, die nach dem Schalten der verursachenden Transition aktuell wird.
	 * @param transitionCaused id der verursachenden Transition.
	 */
	MarkingConnector(String markingBefore, String markingBehind, String transitionCaused) {
		this.markingBefore = markingBefore;
		this.markingBehind = markingBehind;
		this.transitionCaused = transitionCaused;
		this.id = markingBefore + "," + transitionCaused+","+ markingBehind;
	}
	
	/**
	 * Gibt die id dieser Kante des Modells des Erreichbarkeitsgraphen zurück.
	 * @return id dieser Kante.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Speichert die Information, ob diese Kante die zuletzt beschrittene Kante ist.
	 * @param b Information, ob dies die zuletzte beschritte Kante ist (true) oder nicht (false).
	 */
	public void setIsCurrent(boolean b) {
		isCurrent = b;
	}
	
	/**
	 * Gibt die Information zurück, ob diese Kante die zuletzt beschrittene Kante ist.
	 * @return Information, ob dies die zuletzte beschritte Kante ist (true) oder nicht (false).
	 */
	public boolean getIsCurrent() {
		return isCurrent;
	}
	
	/**
	 * Speichert die Information, ob diese Kante des Modells des Erreichbarkeitsgraphen Teil des Pfades m - m' ist.
	 * @param b Information, ob diese Kante des Modells des Erreichbarkeitsgraphen Teil des Pfades m - m' ist (true) oder nicht (false).
	 */
	public void setIsInPath(boolean b) {
		isInPath = b;
	}
	
	/**
	 * Gibt die Information zurück, ob diese Kante des Modells des Erreichbarkeitsgraphen Teil des Pfades m - m' ist.
	 * @return Information, ob diese Kante Teil des Pfades m - m' ist (true) oder nicht (false).
	 */
	public boolean getIsInPath() {
		return isInPath;
	}
	
	/**
	 * Gibt die id der Quellmarkierung - die Markierung des Erreichbarkeitsgraphen vor Schalten der verursachenden Transition - zurück.
	 * @return id der Quellmarkierung.
	 */
	public String getMarkingBefore() {
		return markingBefore;
	}
	
	/**
	 * Gibt die id der Zielmarkierung - die Markierung des Erreichbarkeitsgraphen nach Schalten der verursachenden Transition - zurück.
	 * @return id der Zielmarkierung.
	 */
	public String getMarkingBehind() {
		return markingBehind;
	}
	
	/**
	 * Gibt die id der verursachenden Transition zurück.
	 * @return id der verursachenden Transition.
	 */
	public String getTransitionCaused() {
		return transitionCaused;
	}
	
	/**
	 * Vergleicht die ids dieser Kante mit einer Vergleichskante.
	 * Sind die beiden ids identisch, so handelt es sich um die gleiche Kante.
	 * @param other Kante, mit deren id die id dieser Markierung verglichen werden soll.
	 * @return Information, ob es sich um die gleiche Kante handelt (true) oder nicht (false).
	 */
	boolean isEqual(MarkingConnector other) {
		return this.id.equals(other.id);
	}
}
