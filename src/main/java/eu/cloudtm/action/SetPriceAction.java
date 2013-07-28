package eu.cloudtm.action;

import eu.cloudtm.Book;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class SetPriceAction extends AbstractRemoteAction {
    private static final int ID = NEXT_ID.incrementAndGet();
    private final int id;
    private final int price;

    private SetPriceAction(int id, int price) {
        this.id = id;
        this.price = price;
    }

    @Atomic
    @Override
    public final String executeRemote() throws Exception {
        Book book = FenixFramework.getDomainRoot().getTheBooksById(id);
        if (book == null) {
            return "book(" + id + ") not found!";
        }
        book.setPrice(price);
        return "book(" + id + ").price=" + price;
    }

    @Override
    public final String toString() {
        return "SetPriceAction{" +
                "id=" + id +
                ", age=" + price +
                '}';
    }

    @Override
    protected final String toNetworkString() {
        return ID + "," + id + "," + price;
    }

    @Override
    protected final String localityHint() {
        return Integer.toString(id);
    }

    public static class SetPriceActionFactory implements ActionFactory {

        @Override
        public int getId() {
            return ID;
        }

        @Override
        public Action fromNetwork(String network) {
            String[] split = network.split(",");
            if (split.length != 3) {
                return null;
            }
            int actionId = Integer.parseInt(split[0]);
            if (ID != actionId) {
                return null;
            }
            int id = Integer.parseInt(split[0]);
            int age = Integer.parseInt(split[1]);
            return new SetPriceAction(id, age);
        }

        @Override
        public Action fromConsole(String[] consoleArgs) {
            if (consoleArgs.length != 3) {
                return null;
            }
            int id = Integer.parseInt(consoleArgs[1]);
            int age = Integer.parseInt(consoleArgs[2]);
            return new SetPriceAction(id, age);
        }

        @Override
        public String description() {
            return "Sets the book price: <int:id> <int:price>";
        }
    }
}
