package eu.cloudtm.action;

import eu.cloudtm.Agent;
import pt.ist.fenixframework.FenixFramework;

import java.io.Console;
import java.util.List;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class ExitAction implements Action {

    private final List<Agent> runningAgent;
    private final Console console;

    private ExitAction(List<Agent> runningAgent, Console console) {
        this.runningAgent = runningAgent;
        this.console = console;
    }

    @Override
    public final String executeLocal() throws Exception {
        for (Agent agent : runningAgent) {
            agent.interrupt();
        }
        runningAgent.clear();
        console.printf("Shutting down...%s", ActionManager.NEW_LINE).flush();
        FenixFramework.shutdown();
        System.exit(0);
        return null;
    }

    @Override
    public final String executeRemote() throws Exception {
        return null;
    }

    public static class ExitActionFactory implements ActionFactory {

        private final List<Agent> runningAgents;
        private final Console console;

        public ExitActionFactory(List<Agent> runningAgents, Console console) {
            this.runningAgents = runningAgents;
            this.console = console;
        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public Action fromNetwork(String network) {
            return null;
        }

        @Override
        public Action fromConsole(String[] consoleArgs) {
            if (consoleArgs.length != 1) {
                return null;
            }
            return new ExitAction(runningAgents, console);
        }

        @Override
        public String description() {
            return "Exit application.";
        }
    }

}
