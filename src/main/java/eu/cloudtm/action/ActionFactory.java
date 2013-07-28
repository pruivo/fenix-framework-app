package eu.cloudtm.action;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public interface ActionFactory {

    int getId();

    Action fromNetwork(String network);

    Action fromConsole(String[] consoleArgs);

    String description();

}
