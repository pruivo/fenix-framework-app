package eu.cloudtm;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainRoot;
import pt.ist.fenixframework.FenixFramework;

import java.io.Console;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class ServerMain {

    private static final int NUMBER_ELEMENTS = 600;
    private static final int DIVIDE_RATIO = 10;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void main(String[] args) {
        populateIfNeeded();
        FenixFramework.registerReceiver(new Worker());
        Console console = System.console();
        console.printf("Press enter to finish server... %s", LINE_SEPARATOR).flush();
        console.readLine();
        console.printf("Enter pressed! shutting down...%s", LINE_SEPARATOR).flush();
        FenixFramework.shutdown();
    }

    @Atomic
    private static void populateIfNeeded() {
        DomainRoot domainRoot = FenixFramework.getDomainRoot();
        if (!domainRoot.getTheBooks().isEmpty()) {
            return;
        }
        for (int i = 0; i < NUMBER_ELEMENTS; i++) {
            domainRoot.addTheBooks(new Book(i, i));
            domainRoot.addTheAuthors(new Author(i % (NUMBER_ELEMENTS / DIVIDE_RATIO), i));
            domainRoot.addThePublishers(new Publisher(i));
        }
    }

    private static class Worker implements RequestProcessor {

        @Override
        public Object onRequest(String s) {
            System.out.println(s);
            return null;
        }
    }

}
