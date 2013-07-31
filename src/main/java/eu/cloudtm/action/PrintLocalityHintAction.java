package eu.cloudtm.action;

import eu.cloudtm.ServerMain;
import pt.ist.fenixframework.FenixFramework;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class PrintLocalityHintAction implements Action {

    private static final int ID = NEXT_ID.incrementAndGet();
    private static final List<String> LOCALITY_HINTS;

    static {
        LOCALITY_HINTS = new ArrayList<String>(ServerMain.NUMBER_ELEMENTS);
        for (int i = 0; i < ServerMain.NUMBER_ELEMENTS; ++i) {
            LOCALITY_HINTS.add(Integer.toString(i));
        }
    }

    private PrintLocalityHintAction() {}

    @Override
    public String executeLocal() throws Exception {
        Map<String, String> result = FenixFramework.printLocationInfo("server", LOCALITY_HINTS);
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : result.entrySet()) {
            builder.append("Node: ").append(entry.getKey()).append("=").append(entry.getValue())
                    .append(ActionManager.NEW_LINE);
        }
        return builder.toString();
    }

    @Override
    public String executeRemote() throws Exception {
        return null;
    }

    public static class PrintLocalityHintActionFactory implements ActionFactory {

        public PrintLocalityHintActionFactory() {}

        @Override
        public int getId() {
            return ID;
        }

        @Override
        public Action fromNetwork(String network) {
            return null;
        }

        @Override
        public Action fromConsole(String[] consoleArgs) {
            if (consoleArgs.length != 1) {
                return null;
            }
            int actionId = Integer.parseInt(consoleArgs[0]);
            if (ID != actionId) {
                return null;
            }
            return new PrintLocalityHintAction();
        }

        @Override
        public String description() {
            return "Prints the locality hint information.";
        }
    }

}
