package eu.cloudtm;

import eu.cloudtm.action.TerminableAction;

import java.util.List;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class TerminableAgent extends Agent {

    private final List<Agent> runningAgents;

    public TerminableAgent(TerminableAction action, List<Agent> runningAgents) {
        super(action);
        this.runningAgents = runningAgents;
    }

    @Override
    protected boolean isRunning() {
        return super.isRunning() && !((TerminableAction) action).isTerminated();
    }

    @Override
    protected void beforeStart() {
        super.beforeStart();
        runningAgents.add(this);
    }

    @Override
    protected void afterFinish() {
        super.afterFinish();
        runningAgents.remove(this);
    }
}
