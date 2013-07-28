package eu.cloudtm.action;

import eu.cloudtm.Agent;

import java.util.List;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class AddAgentAction implements Action {

    private static final int ID = NEXT_ID.incrementAndGet();
    private final List<Agent> runningAgent;
    private final Action action;

    private AddAgentAction(List<Agent> runningAgent, Action action) {
        this.runningAgent = runningAgent;
        this.action = action;
    }

    @Override
    public String executeLocal() throws Exception {
        Agent agent = new Agent(action);
        runningAgent.add(agent);
        agent.start();
        return "Agent added: " + agent;
    }

    @Override
    public String executeRemote() throws Exception {
        return null;
    }

    public static class AddAgentActionFactory implements ActionFactory {

        private final List<Agent> runningAgents;
        private final ActionManager actionManager;

        public AddAgentActionFactory(List<Agent> runningAgents, ActionManager actionManager) {
            this.runningAgents = runningAgents;
            this.actionManager = actionManager;
        }

        @Override
        public int getId() {
            return ID;
        }

        @Override
        public Action fromNetwork(String network) {
            return null;
        }

        @Override
        public Action fromConsole(String[] consoleArgs) {
            if (consoleArgs.length == 1) {
                return null;
            }
            int actionId = Integer.parseInt(consoleArgs[0]);
            if (ID != actionId) {
                return null;
            }
            String[] command = new String[consoleArgs.length - 1];
            System.arraycopy(consoleArgs, 1, command, 0, command.length);
            Action action = actionManager.fromConsole(command);
            return action == null ? null : new AddAgentAction(runningAgents, action);
        }

        @Override
        public String description() {
            return "Adds a new agent: <command>";
        }
    }

}
