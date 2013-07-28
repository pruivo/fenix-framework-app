package eu.cloudtm.action;

import eu.cloudtm.Author;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class GetAuthorsInfoAction extends AbstractRemoteAction {
    private static final int ID = NEXT_ID.incrementAndGet();

    @Override
    public String executeRemote() throws Exception {
        int count = 0;
        int sum = 0;
        int min = 0;
        int max = 0;
        boolean first = true;
        for (Author author : FenixFramework.getDomainRoot().getTheAuthors()) {
            int age = author.getAge();
            if (first) {
                count = 1;
                sum = max = min = age;
                first = false;
            } else {
                count++;
                sum += age;
                min = Math.min(min, age);
                max = Math.max(max, age);
            }
        }
        return "number of authors=" + count + " (sum=" + sum + ", max=" + max + ", avg=" +
                (count == 0 ? 0 : (sum * 1.0 / count) + ", min=" + min + ")");
    }

    @Override
    public final String toString() {
        return "GetAuthorsInfoAction{}";
    }

    @Override
    protected String toNetworkString() {
        return Integer.toString(ID);
    }

    @Override
    protected String localityHint() {
        return null;
    }

    public static class GetAuthorsInfoActionFactory implements ActionFactory {

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
            return new GetAuthorsInfoAction();
        }

        @Override
        public Action fromConsole(String[] consoleArgs) {
            if (consoleArgs.length != 1) {
                return null;
            }
            return new GetAuthorsInfoAction();
        }

        @Override
        public String description() {
            return "Gets the author information, i.e. the min,max and avg age";
        }
    }
}
