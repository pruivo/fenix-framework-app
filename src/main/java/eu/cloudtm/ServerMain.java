package eu.cloudtm;

import eu.cloudtm.action.*;
import org.apache.log4j.Level;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainRoot;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.messaging.RequestProcessor;

import java.io.Console;

import static org.apache.log4j.Logger.getLogger;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class ServerMain {

    public static final int NUMBER_ELEMENTS = 600;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void main(String[] args) {
        init();
        populateIfNeeded();
        ActionManager actionManager = new ActionManager();
        FenixFramework.registerReceiver(new Worker(actionManager));
        Console console = System.console();
        console.printf("Press enter to finish server... %s", LINE_SEPARATOR).flush();
        console.readLine();
        console.printf("Enter pressed! shutting down...%s", LINE_SEPARATOR).flush();
        FenixFramework.shutdown();
        System.exit(0);
    }

    private static void populateIfNeeded() {
        if (!shouldPopulate()) {
            return;
        }
        //a little hack to make the populate faster
        Level old = getLogger("pt.ist.fenixframework").getLevel();
        getLogger("pt.ist.fenixframework").setLevel(Level.ERROR);
        populateAuthors();
        populateBooks();
        populatePublishers();
        getLogger("pt.ist.fenixframework").setLevel(old);
    }

    @Atomic
    private static void init() {
        DomainRoot domainRoot = FenixFramework.getDomainRoot();
        if (domainRoot.getApplication() == null) {
            domainRoot.setApplication(new Application());
        }
    }

    @Atomic
    private static boolean shouldPopulate() {
        Application application = FenixFramework.getDomainRoot().getApplication();
        if (application.getPopulated()) {
            return false;
        }
        application.setPopulated(true);
        return true;
    }

    @Atomic
    private static void populateAuthors() {
        DomainRoot domainRoot = FenixFramework.getDomainRoot();
        if (!domainRoot.getTheAuthors().isEmpty()) {
            return;
        }
        for (int i = 0; i < NUMBER_ELEMENTS; i++) {
            domainRoot.addTheAuthors(Author.createAuthorGroupedById(i, i));
        }
    }

    @Atomic
    private static void populateBooks() {
        DomainRoot domainRoot = FenixFramework.getDomainRoot();
        if (!domainRoot.getTheBooks().isEmpty()) {
            return;
        }
        for (int i = 0; i < NUMBER_ELEMENTS; i++) {
            domainRoot.addTheBooks(Book.createBookGroupedById(i, i));
        }
    }

    @Atomic
    private static void populatePublishers() {
        DomainRoot domainRoot = FenixFramework.getDomainRoot();
        if (!domainRoot.getThePublishers().isEmpty()) {
            return;
        }
        for (int i = 0; i < NUMBER_ELEMENTS; i++) {
            domainRoot.addThePublishers(new Publisher(i));
        }
    }

    private static class Worker implements RequestProcessor {

        private final ActionManager actionManager;

        private Worker(ActionManager actionManager) {
            this.actionManager = actionManager;
        }

        @Override
        public Object onRequest(String s) {
            if (s == null || s.isEmpty()) {
                return "Invalid request " + s;
            }
            Action action = actionManager.fromNetwork(s);
            if (action != null) {
                try {
                    return action.executeRemote();
                } catch (Exception e) {
                    return "Exception: " + e.getLocalizedMessage();
                }
            }
            return "Unknown request " + s;
        }
    }

}
