package eu.cloudtm.action;

import eu.cloudtm.Agent;
import eu.cloudtm.TerminableAgent;

import java.util.List;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class GeographPopulationAction implements Action {

    private static final int ID = NEXT_ID.incrementAndGet();
    private final List<Agent> runningAgent;
    private final int maxComments;
    private final int numberOfAgents;
    private final int textSize;

    public GeographPopulationAction(List<Agent> runningAgent, int maxComments, int numberOfAgents,
                                    int textSize) {
        this.runningAgent = runningAgent;
        this.maxComments = maxComments;
        this.numberOfAgents = numberOfAgents;
        this.textSize = textSize;
    }

    @Override
    public String executeLocal() throws Exception {
        for (int i = 0; i < numberOfAgents; ++i) {
            TerminableAgent agent = new TerminableAgent(new CommentBookAction(maxComments, i, textSize), runningAgent);
            agent.start();
        }
        return "Added " + numberOfAgents + " agents!";
    }

    @Override
    public String executeRemote() throws Exception {
        return null;
    }

    public static class GeographPopultionActionFactory implements ActionFactory {

        private final List<Agent> runningAgents;

        public GeographPopultionActionFactory(List<Agent> runningAgents) {
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
            if (consoleArgs.length  < 3) {
                return null;
            }
            int actionId = Integer.parseInt(consoleArgs[0]);
            if (actionId != ID) {
                return null;
            }
            int numberOfAgents = Integer.parseInt(consoleArgs[1]);
            int maxComments = Integer.parseInt(consoleArgs[2]);
            int textSize = 128;
            if (consoleArgs.length == 4) {
                textSize = Integer.parseInt(consoleArgs[3]);
            }
            return new GeographPopulationAction(runningAgents, maxComments, numberOfAgents, textSize);
        }

        @Override
        public String description() {
            return "Simulates geograph population: <int:number of agents> <int:max comments> [<int:text size=128]>";
        }
    }
}
