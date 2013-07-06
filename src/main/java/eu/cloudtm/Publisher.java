package eu.cloudtm;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class Publisher extends Publisher_Base {

    public Publisher() {
        super();
    }

    public Publisher(int id) {
        this();
        setId(id);
    }

    @Override
    public String toString() {
        return "Publisher " + getId();
    }
}
