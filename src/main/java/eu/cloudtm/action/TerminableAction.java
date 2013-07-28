package eu.cloudtm.action;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public interface TerminableAction extends Action {

    boolean isTerminated();

}
