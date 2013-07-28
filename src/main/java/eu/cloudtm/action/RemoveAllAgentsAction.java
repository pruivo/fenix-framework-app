package eu.cloudtm.action;

import eu.cloudtm.Agent;

import java.util.List;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class RemoveAllAgentsAction implements Action {

    private static final int ID = NEXT_ID.incrementAndGet();
    private final List<Agent> runningAgent;

    private RemoveAllAgentsAction(List<Agent> runningAgent) {
        this.runningAgent = runningAgent;
    }

    @Override
    public String executeLocal() throws Exception {
        String result = "Agents removed: " + runningAgent;
        for (Agent agent : runningAgent) {
            agent.interrupt();
        }
        runningAgent.clear();
        return result;
    }

    @Override
    public String executeRemote() throws Exception {
        return null;
    }

    @Override
    public String toString() {
        return "RemoveAllAgentsAction{}";
    }

    public static class RemoveAllAgentsActionFactory implements ActionFactory {

        private final List<Agent> runningAgents;

        public RemoveAllAgentsActionFactory(List<Agent> runningAgents) {
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
            if (consoleArgs.length != 1) {
                return null;
            }
            return new RemoveAllAgentsAction(runningAgents);
        }

        @Override
        public String description() {
            return "Removes all agents.";
        }
    }

}
