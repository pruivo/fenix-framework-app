package eu.cloudtm.action;

import eu.cloudtm.Agent;

import java.util.List;

import static eu.cloudtm.action.ActionManager.NEW_LINE;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class GetAgentsInfoAction implements Action {

    private static final int ID = NEXT_ID.incrementAndGet();
    private final List<Agent> runningAgents;

    public GetAgentsInfoAction(List<Agent> runningAgents) {
        this.runningAgents = runningAgents;
    }

    @Override
    public String executeLocal() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("#### AGENTS ####").append(NEW_LINE);
        int index = 0;
        for (Agent agent : runningAgents) {
            builder.append(index++).append(": ").append(agent).append(NEW_LINE);
        }
        builder.append("#### AGENTS ####").append(NEW_LINE);
        return builder.toString();
    }

    @Override
    public String executeRemote() throws Exception {
        return null;
    }

    public static class GetAgentsInfoActionFactory implements ActionFactory {

        private final List<Agent> runningAgents;

        public GetAgentsInfoActionFactory(List<Agent> runningAgents) {
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
            int actionId = Integer.parseInt(consoleArgs[0]);
            if (actionId != ID) {
                return null;
            }
            return new GetAgentsInfoAction(runningAgents);
        }

        @Override
        public String description() {
            return "Shows the current agents state";
        }
    }
}
