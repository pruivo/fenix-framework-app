package eu.cloudtm.action;

import eu.cloudtm.Book;
import eu.cloudtm.ServerMain;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class GetPriceAction extends AbstractRemoteAction {
    private static final int ID = NEXT_ID.incrementAndGet();
    private final int id;

    private GetPriceAction(int id) {
        this.id = id;
    }

    @Override
    public final String toString() {
        return "GetPriceAction{" +
                "id=" + id +
                '}';
    }

    @Override
    protected final String executeTransaction() {
        Book book = FenixFramework.getDomainRoot().getTheBooksByIdCached(false, id);
        if (book == null) {
            return "book(" + id + ") not found!";
        }
        return "book(" + id + ").price=" + book.getPrice();
    }

    @Override
    protected final String toNetworkString() {
        return ID + "," + id;
    }

    @Override
    protected final String localityHint() {
        return ServerMain.USE_TX_CLASS ? transactionClass() : Integer.toString(id);
    }

    @Override
    protected boolean isWrite() {
        return false;
    }

    public static class GetPriceActionFactory implements ActionFactory {

        @Override
        public int getId() {
            return ID;
        }

        @Override
        public Action fromNetwork(String network) {
            String[] split = network.split(",");
            if (split.length != 2) {
                return null;
            }
            int actionId = Integer.parseInt(split[0]);
            if (ID != actionId) {
                return null;
            }
            int id = Integer.parseInt(split[1]);
            return new GetPriceAction(id);
        }

        @Override
        public Action fromConsole(String[] consoleArgs) {
            if (consoleArgs.length != 2) {
                return null;
            }
            int id = Integer.parseInt(consoleArgs[1]);
            return new GetPriceAction(id);
        }

        @Override
        public String description() {
            return "Gets the book price: <int:id>";
        }
    }
}
