package eu.cloudtm.action;

import eu.cloudtm.Author;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class GetAgeAction extends AbstractRemoteAction {
    private static final int ID = NEXT_ID.incrementAndGet();
    private final int id;

    private GetAgeAction(int id) {
        this.id = id;
    }

    @Override
    public final String toString() {
        return "GetAgeAction{" +
                "id=" + id +
                '}';
    }

    @Override
    protected final String executeTransaction() {
        Author author = FenixFramework.getDomainRoot().getTheAuthorsById(id);
        if (author == null) {
            return "author(" + id + ") not found!";
        }
        return "author(" + id + ").age=" + author.getAge();
    }

    @Override
    protected final String toNetworkString() {
        return ID + "," + id;
    }

    @Override
    protected final String localityHint() {
        return Integer.toString(id);
    }

    public static class GetAgeActionFactory implements ActionFactory {

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
            return new GetAgeAction(id);
        }

        @Override
        public Action fromConsole(String[] consoleArgs) {
            if (consoleArgs.length != 2) {
                return null;
            }
            int id = Integer.parseInt(consoleArgs[1]);
            return new GetAgeAction(id);
        }

        @Override
        public String description() {
            return "Gets the author age: <int:id>";
        }
    }
}
