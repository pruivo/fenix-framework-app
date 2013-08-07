package eu.cloudtm;

import eu.cloudtm.action.*;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class ClientMain {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void main(String[] args) throws Exception {
        initDomainRoot();
        Console console = System.console();
        console.printf("Client initialized! Connecting to server... %s", LINE_SEPARATOR).flush();
        FenixFramework.initConnection("server");
        console.printf("Client connected to server... %s", LINE_SEPARATOR).flush();
        List<Agent> runningAgent = Collections.synchronizedList(new ArrayList<Agent>());
        ActionManager actionManager = new ActionManager();
        actionManager.registerActions(
                new GeographPopulationAction.GeographPopultionActionFactory(runningAgent),
                new GetAgentsInfoAction.GetAgentsInfoActionFactory(runningAgent),
                new AddAgentAction.AddAgentActionFactory(runningAgent, actionManager),
                new RemoveAgentAction.RemoveAgentActionFactory(runningAgent),
                new RemoveAllAgentsAction.RemoveAllAgentsActionFactory(runningAgent),
                new PrintLocalityHintAction.PrintLocalityHintActionFactory(),
                new ExitAction.ExitActionFactory(runningAgent, console));
        //noinspection InfiniteLoopStatement
        while (true) {
            actionManager.printDescription(console);
            String command = console.readLine(">");
            Action action = actionManager.fromConsole(command.split(" "));
            if (action == null) {
                System.err.println("Unknown " + command);
                continue;
            }
            try {
                System.out.println(action.executeLocal());
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    @Atomic
    private static void initDomainRoot() {
        FenixFramework.getDomainRoot();
    }


}
