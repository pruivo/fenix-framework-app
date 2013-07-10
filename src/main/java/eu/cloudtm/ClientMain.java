package eu.cloudtm;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import java.io.Console;
import java.util.ArrayList;
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
        List<Agent> runningAgent = new ArrayList<Agent>();
        while (true) {
            System.out.println("Choose an action: (max id=" + ServerMain.NUMBER_ELEMENTS + ")");
            System.out.println("setAge <agent id> <age>");
            System.out.println("getAge <agent id>");
            System.out.println("getAvgAuthorAge");
            System.out.println("setPrice <book id> <price>");
            System.out.println("getPrice <book id>");
            System.out.println("getAvgBookPrice");
            System.out.println("add-agent <command>");
            System.out.println("remove-agent <id>");
            System.out.println("remove-all-agents");
            System.out.println("quit");
            System.out.println("#### AGENTS ####");
            for (int i = 0; i < runningAgent.size(); ++i) {
                System.out.println(i + ": " + runningAgent.get(i));
            }
            System.out.println("#### AGENTS ####");
            String command = console.readLine(">");
            if ("quit".equals(command)) {
                for (Agent agent : runningAgent) {
                    agent.interrupt();
                }
                runningAgent.clear();
                break;
            } else if (command.startsWith("add-agent ") ) {
                Agent agent = new Agent(command.split(" ", 2)[1]);
                runningAgent.add(agent);
                agent.start();
            } else if (command.startsWith("remove-agent ")) {
                int id = Integer.parseInt(command.split(" ", 2)[1]);
                runningAgent.remove(id).interrupt();
            } else if (command.startsWith("remove-all-agents")) {
                for (Agent agent : runningAgent) {
                    agent.interrupt();
                }
                runningAgent.clear();
            } else {
                String hint = null;
                String[] requestArgs = command.split(" ");
                if (requestArgs.length >= 2) {
                    hint = requestArgs[1];
                }
                System.out.println("response: " + FenixFramework.sendRequest(command, hint, "server", true));
            }

        }
        console.printf("Shutting down...%s", LINE_SEPARATOR).flush();
        FenixFramework.shutdown();
        System.exit(0);
    }

    private static class Agent extends Thread {
        private final String command;
        private final String hint;
        private volatile String lastResponse;
        private volatile boolean running;

        private Agent(String command) {
            this.command = command;
            String[] requestArgs = command.split(" ");
            hint = requestArgs.length >= 2 ? requestArgs[1] : null;
        }

        @Override
        public void run() {
            running = true;
            while (running) {
                try {
                    lastResponse = String.valueOf(FenixFramework.sendRequest(command, hint, "server", true));
                } catch (Throwable t) {
                    //ignored
                }
            }
        }

        @Override
        public void interrupt() {
            running = false;
            super.interrupt();
        }

        @Override
        public String toString() {
            return "Agent{" +
                    "command='" + command + '\'' +
                    ", lastResponse='" + lastResponse + '\'' +
                    '}';
        }
    }

    @Atomic
    private static void initDomainRoot() {
        FenixFramework.getDomainRoot();
    }

}
