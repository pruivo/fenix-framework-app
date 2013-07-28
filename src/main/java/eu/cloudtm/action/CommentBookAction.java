package eu.cloudtm.action;

import eu.cloudtm.Book;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import java.util.Random;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class CommentBookAction implements TerminableAction {

    private static final char[] symbols = new char[36];

    static {
        for (int idx = 0; idx < 10; ++idx)
            symbols[idx] = (char) ('0' + idx);
        for (int idx = 10; idx < 36; ++idx)
            symbols[idx] = (char) ('a' + idx - 10);
    }

    private static final int ID = NEXT_ID.incrementAndGet();
    private final int maxComments;
    private final int bookId;
    private final int textSize;
    private final Random random;
    private int commentsAdded;

    public CommentBookAction(int maxComments, int bookId, int textSize) {
        this.maxComments = maxComments;
        this.bookId = bookId;
        this.textSize = textSize;
        this.commentsAdded = 0;
        this.random = new Random(System.nanoTime());
    }

    @Override
    public String executeLocal() throws Exception {
        if (isTerminated()) {
            return "Done! " + commentsAdded + "/" + maxComments;
        }
        commentsAdded++;
        return String.valueOf(FenixFramework.sendRequest(toNetworkString(), localityHint(), "server", true)) +
                " (" + commentsAdded + "/" + maxComments + ")";
    }

    @Atomic
    @Override
    public String executeRemote() throws Exception {
        Book book = FenixFramework.getDomainRoot().getTheBooksById(bookId);
        if (book == null) {
            return "book(" + bookId + ") not found!";
        }
        String text = nextString();
        book.comment(text);
        return "book(" + bookId + ").comment=" + text;
    }

    @Override
    public boolean isTerminated() {
        return commentsAdded > maxComments;
    }

    private String toNetworkString() {
        return ID + "," + bookId + "," + textSize + "," + maxComments;
    }

    private String localityHint() {
        return Integer.toString(bookId);
    }

    private String nextString() {
        StringBuilder builder = new StringBuilder(textSize);
        for (int idx = 0; idx < textSize; ++idx) {
            builder.append(symbols[random.nextInt(symbols.length)]);
        }
        return builder.toString();
    }

    public static class CommentBookActionFactory implements ActionFactory {

        @Override
        public int getId() {
            return ID;
        }

        @Override
        public Action fromNetwork(String network) {
            String[] split = network.split(",");
            if (split.length != 4) {
                return null;
            }
            int actionId = Integer.parseInt(split[0]);
            if (actionId != ID) {
                return null;
            }
            int bookId = Integer.parseInt(split[1]);
            int textSize = Integer.parseInt(split[2]);
            int maxComments = Integer.parseInt(split[3]);
            return new CommentBookAction(maxComments, bookId, textSize);
        }

        @Override
        public Action fromConsole(String[] consoleArgs) {
            int textSize = 128;
            int maxComments = 1;
            if (consoleArgs.length == 1) {
                return null;
            }
            int bookId = Integer.parseInt(consoleArgs[1]);
            if (consoleArgs.length == 4) {
                textSize = Integer.parseInt(consoleArgs[2]);
                maxComments = Integer.parseInt(consoleArgs[3]);
            }
            return new CommentBookAction(maxComments, bookId, textSize);
        }

        @Override
        public String description() {
            return "Makes a comment in a book: <int:book id> [<int:comment size=128> <int:max comments=1>]";
        }
    }
}
