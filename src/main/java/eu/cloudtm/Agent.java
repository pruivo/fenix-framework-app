package eu.cloudtm;

import eu.cloudtm.action.Action;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class Agent extends Thread {
    protected final Action action;
    protected volatile String lastResponse;
    protected volatile boolean running;

    public Agent(Action action) {
        this.action = action;
    }

    @Override
    public void run() {
        beforeStart();
        while (isRunning()) {
            try {
                lastResponse = action.executeLocal();
            } catch (Throwable t) {
                lastResponse = "Exception: "+ t.getLocalizedMessage();
            }
        }
        afterFinish();
    }

    protected boolean isRunning() {
        return running;
    }

    protected void beforeStart() {
        running = true;
    }

    protected void afterFinish() {

    }

    @Override
    public void interrupt() {
        running = false;
        super.interrupt();
    }

    @Override
    public String toString() {
        return "Agent{" +
                "action='" + action + '\'' +
                ", lastResponse='" + lastResponse + '\'' +
                '}';
    }
}
