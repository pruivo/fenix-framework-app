package eu.cloudtm.action;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public interface Action {

    public static final AtomicInteger NEXT_ID = new AtomicInteger(0);

    String executeLocal() throws Exception;

    String executeRemote() throws Exception;

}
