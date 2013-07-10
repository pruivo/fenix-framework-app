package eu.cloudtm;

import org.apache.log4j.Level;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainRoot;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.messaging.RequestProcessor;

import java.io.Console;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
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
        FenixFramework.registerReceiver(new Worker());
        Console console = System.console();
        console.printf("Press enter to finish server... %s", LINE_SEPARATOR).flush();
        console.readLine();
        console.printf("Enter pressed! shutting down...%s", LINE_SEPARATOR).flush();
        FenixFramework.shutdown();
        System.exit(0);
    }

    private static void populateIfNeeded() {
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
        FenixFramework.getDomainRoot();
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

    @Atomic
    private static String setAge(int id, int age) {
        Author author = FenixFramework.getDomainRoot().getTheAuthorsById(id);
        if (author == null) {
            return "author(" + id + ") not found!";
        }
        author.setAge(age);
        return "author(" + id + ").age=" + age;
    }

    @Atomic
    private static String getAge(int id) {
        Author author = FenixFramework.getDomainRoot().getTheAuthorsById(id);
        if (author == null) {
            return "author(" + id + ") not found!";
        }
        return "author(" + id + ").age=" + author.getAge();
    }

    @Atomic
    private static String getAverageAge() {
        int count = 0;
        int sum = 0;
        for (Author author : FenixFramework.getDomainRoot().getTheAuthors()) {
            count++;
            sum += author.getAge();
        }
        return "number of authors=" + count + ", average age=" + (count == 0 ? 0 : (sum * 1.0 / count));
    }

    @Atomic
    private static String setPrice(int id, double price) {
        Book book = FenixFramework.getDomainRoot().getTheBooksById(id);
        if (book == null) {
            return "book(" + id + ") not found!";
        }
        book.setPrice(price);
        return "book(" + id + ").price=" + price;
    }

    @Atomic
    private static String getPrice(int id) {
        Book book = FenixFramework.getDomainRoot().getTheBooksById(id);
        if (book == null) {
            return "book(" + id + ") not found!";
        }
        return "book(" + id + ").price=" + book.getPrice();
    }

    @Atomic
    private static String getAveragePrice() {
        int count = 0;
        double sum = 0;
        for (Book book : FenixFramework.getDomainRoot().getTheBooks()) {
            count++;
            sum += book.getPrice();
        }
        return "number of books=" + count + ", average price=" + (count == 0 ? 0 : (sum * 1.0 / count));
    }

    private static class Worker implements RequestProcessor {

        @Override
        public Object onRequest(String s) {
            if (s == null || s.isEmpty()) {
                return "Invalid request " + s;
            }
            String[] args = s.split(" ");
            if (args[0].equals("setAge") && args.length == 3) {
                return setAge(parseInt(args[1]), parseInt(args[2]));
            } else if (args[0].equals("getAge") && args.length == 2) {
                return getAge(parseInt(args[1]));
            } else if (args[0].equals("getAvgAuthorAge") && args.length == 1) {
                return getAverageAge();
            } else if (args[0].equals("setPrice") && args.length == 3) {
                return setPrice(parseInt(args[1]), parseDouble(args[2]));
            } else if (args[0].equals("getPrice") && args.length == 2) {
                return getPrice(parseInt(args[1]));
            } else if (args[0].equals("getAvgBookPrice") && args.length == 1) {
                return getAveragePrice();
            }
            return "Unknown request " + s;
        }
    }

}
