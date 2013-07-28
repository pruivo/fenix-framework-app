package eu.cloudtm.action;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public abstract class AbstractRemoteAction implements Action {

    @Override
    public final String executeLocal() throws Exception {
        return String.valueOf(FenixFramework.sendRequest(toNetworkString(), localityHint(), "server", true));
    }

    protected abstract String toNetworkString();

    protected abstract String localityHint();
}
