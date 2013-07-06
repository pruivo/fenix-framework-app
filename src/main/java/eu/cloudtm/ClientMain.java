package eu.cloudtm;

import pt.ist.fenixframework.FenixFramework;

import java.io.Console;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class ClientMain {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void main(String[] args) throws Exception {
        FenixFramework.getDomainRoot(); //just to init
        Console console = System.console();
        console.printf("Client initialized! Connecting to server... %s", LINE_SEPARATOR).flush();
        FenixFramework.initConnection("server");
        console.printf("Client connected to server... %s", LINE_SEPARATOR).flush();
        while (true) {
            console.printf("Choose an option: %s r: sends a request %s q: exit %s", LINE_SEPARATOR, LINE_SEPARATOR, LINE_SEPARATOR).flush();
            String command = console.readLine();
            if ("q".equalsIgnoreCase(command)) {
                break;
            } else if ("r".equalsIgnoreCase(command)) {
                System.out.println("response: " + FenixFramework.sendRequest("bla", "bla", "server", true));
            } else {
                System.err.println("Unknown command: " + command);
            }

        }
        console.printf("Shutting down...%s", LINE_SEPARATOR).flush();
        FenixFramework.shutdown();
    }

}
