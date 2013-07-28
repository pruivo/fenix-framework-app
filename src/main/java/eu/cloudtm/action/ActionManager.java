package eu.cloudtm.action;

import java.io.Console;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class ActionManager {

    public static final String NEW_LINE = System.getProperty("line.separator");
    private Map<Integer, ActionFactory> registeredActions;

    public ActionManager() {
        registerActions(
                new SetAgeAction.SetAgeActionFactory(),
                new GetAgeAction.GetAgeActionFactory(),
                new GetAuthorsInfoAction.GetAuthorsInfoActionFactory(),
                new SetPriceAction.SetPriceActionFactory(),
                new GetPriceAction.GetPriceActionFactory(),
                new GetBooksInfoAction.GetBooksInfoActionFactory(),
                new CommentBookAction.CommentBookActionFactory());
    }

    public final Action fromConsole(String[] arguments) {
        try {
            if (arguments == null || arguments.length == 0) {
                return null;
            }
            int id = Integer.parseInt(arguments[0]);
            ActionFactory factory = registeredActions == null ? null : registeredActions.get(id);
            return factory == null ? null : factory.fromConsole(arguments);
        } catch (Exception e) {
            return null;
        }
    }

    public final Action fromNetwork(String network) {
        for (ActionFactory factory : registeredActions.values()) {
            try {
                Action action = factory.fromNetwork(network);
                if (action != null) {
                    return action;
                }
            } catch (Exception e) {
                //no-op
            }
        }
        return null;
    }

    public final void registerAction(ActionFactory factory) {
        if (factory == null) {
            return;
        }
        if (registeredActions == null) {
            registeredActions = new TreeMap<Integer, ActionFactory>();
        }
        if (registeredActions.containsKey(factory.getId())) {
            throw new IllegalArgumentException("Factory for id " + factory.getId() + " already exists");
        }
        registeredActions.put(factory.getId(), factory);
    }

    public final void registerActions(ActionFactory... factories) {
        if (factories == null || factories.length == 0) {
            return;
        } else if (factories.length == 1) {
            registerAction(factories[0]);
            return;
        }
        for (ActionFactory factory : factories) {
            registerAction(factory);
        }
    }

    public final void printDescription(Console console) {
        for (ActionFactory factory : registeredActions.values()) {
            console.printf("%s: %s%s", factory.getId(), factory.description(), NEW_LINE);
        }
    }

}
