package eu.cloudtm;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import java.io.Console;

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
        while (true) {
            System.out.println("Choose an action: (max id=" + ServerMain.NUMBER_ELEMENTS + ")");
            System.out.println("setAge <agent id> <age>");
            System.out.println("getAge <agent id>");
            System.out.println("getAvgAuthorAge");
            System.out.println("setPrice <book id> <price>");
            System.out.println("getPrice <book id>");
            System.out.println("getAvgBookPrice");
            System.out.println("quit");
            String command = console.readLine(">");
            if ("quit".equals(command)) {
                break;
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

    @Atomic
    private static void initDomainRoot() {
        FenixFramework.getDomainRoot();
    }

}
