package eu.cloudtm.action;

import eu.cloudtm.Book;
import eu.cloudtm.ServerMain;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class GetBooksInfoAction extends AbstractRemoteAction {
    private static final int ID = NEXT_ID.incrementAndGet();

    @Override
    public final String toString() {
        return "GetBooksInfoAction{}";
    }

    @Override
    protected final String executeTransaction() {
        int count = 0;
        int sum = 0;
        int min = 0;
        int max = 0;
        boolean first = true;
        for (Book book : FenixFramework.getDomainRoot().getTheBooksCached(false)) {
            int price = (int) book.getPrice();
            if (first) {
                count = 1;
                sum = max = min = price;
                first = false;
            } else {
                count++;
                sum += price;
                min = Math.min(min, price);
                max = Math.max(max, price);
            }
        }
        return "number of books=" + count + " (sum=" + sum + ", max=" + max + ", avg=" +
                (count == 0 ? 0 : (sum * 1.0 / count) + ", min=" + min + ")");
    }

    @Override
    protected String toNetworkString() {
        return Integer.toString(ID);
    }

    @Override
    protected String localityHint() {
        return ServerMain.USE_TX_CLASS ? transactionClass() : null;
    }

    @Override
    protected boolean isWrite() {
        return false;
    }

    public static class GetBooksInfoActionFactory implements ActionFactory {

        @Override
        public int getId() {
            return ID;
        }

        @Override
        public Action fromNetwork(String network) {
            int actionId = Integer.parseInt(network);
            if (ID != actionId) {
                return null;
            }
            return new GetBooksInfoAction();
        }

        @Override
        public Action fromConsole(String[] consoleArgs) {
            if (consoleArgs.length != 1) {
                return null;
            }
            return new GetBooksInfoAction();
        }

        @Override
        public String description() {
            return "Gets the books information, i.e. the min,max and avg age";
        }
    }
}
