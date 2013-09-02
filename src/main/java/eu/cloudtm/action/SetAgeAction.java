package eu.cloudtm.action;

import eu.cloudtm.Author;
import eu.cloudtm.ServerMain;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class SetAgeAction extends AbstractRemoteAction {
    private static final int ID = NEXT_ID.incrementAndGet();
    private final int id;
    private final int age;

    private SetAgeAction(int id, int age) {
        this.id = id;
        this.age = age;
    }

    @Override
    public final String toString() {
        return "SetAgeAction{" +
                "id=" + id +
                ", age=" + age +
                '}';
    }

    @Override
    protected final String executeTransaction() {
        Author author = FenixFramework.getDomainRoot().getTheAuthorsById(id);
        if (author == null) {
            return "author(" + id + ") not found!";
        }
        author.setAge(age);
        return "author(" + id + ").age=" + age;
    }

    @Override
    protected final String toNetworkString() {
        return ID + "," + id + "," + age;
    }

    @Override
    protected final String localityHint() {
        return ServerMain.USE_TX_CLASS ? transactionClass() : Integer.toString(id);
    }

    @Override
    protected boolean isWrite() {
        return true;
    }

    public static class SetAgeActionFactory implements ActionFactory {

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
            int id = Integer.parseInt(split[1]);
            int age = Integer.parseInt(split[2]);
            return new SetAgeAction(id, age);
        }

        @Override
        public Action fromConsole(String[] consoleArgs) {
            if (consoleArgs.length != 3) {
                return null;
            }
            int id = Integer.parseInt(consoleArgs[1]);
            int age = Integer.parseInt(consoleArgs[2]);
            return new SetAgeAction(id, age);
        }

        @Override
        public String description() {
            return "Sets the author age: <int:id> <int:age>";
        }
    }
}
