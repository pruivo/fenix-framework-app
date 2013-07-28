package eu.cloudtm.action;

import eu.cloudtm.Agent;

import java.util.List;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class RemoveAgentAction implements Action {

    private static final int ID = NEXT_ID.incrementAndGet();
    private final List<Agent> runningAgent;
    private final int index;

    private RemoveAgentAction(List<Agent> runningAgent, int index) {
        this.runningAgent = runningAgent;
        this.index = index;
    }

    @Override
    public String executeLocal() throws Exception {
        if (index < 0 || index >= runningAgent.size()) {
            return "index [" + index + "] out of bounds...";
        }
        Agent agent = runningAgent.remove(index);
        agent.interrupt();
        return "Agent removed: " + agent;
    }

    @Override
    public String executeRemote() throws Exception {
        return null;
    }

    @Override
    public String toString() {
        return "RemoveAgentAction{" +
                ", index=" + index +
                '}';
    }

    public static class RemoveAgentActionFactory implements ActionFactory {

        private final List<Agent> runningAgents;

        public RemoveAgentActionFactory(List<Agent> runningAgents) {
            this.runningAgents = runningAgents;
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
            if (consoleArgs.length != 2) {
                return null;
            }
            int actionId = Integer.parseInt(consoleArgs[0]);
            if (ID != actionId) {
                return null;
            }
            int index = Integer.parseInt(consoleArgs[1]);
            return new RemoveAgentAction(runningAgents, index);
        }

        @Override
        public String description() {
            return "Removes a agent: <int:id>";
        }
    }

}
