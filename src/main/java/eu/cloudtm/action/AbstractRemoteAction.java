package eu.cloudtm.action;

import pt.ist.fenixframework.FenixFramework;

import java.util.concurrent.Callable;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public abstract class AbstractRemoteAction implements Action {

    @Override
    public final String executeLocal() throws Exception {
        return String.valueOf(FenixFramework.sendRequest(toNetworkString(), localityHint(), "server", true, isWrite()));
    }

    @Override
    public final String executeRemote() throws Exception {
        return FenixFramework.getTransactionManager().withTransaction(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return executeTransaction();
            }
        }, transactionClass());

    }

    protected final String transactionClass() {
        return getClass().getSimpleName();
    }

    protected abstract String executeTransaction();

    protected abstract String toNetworkString();

    protected abstract String localityHint();

    protected abstract boolean isWrite();
}
