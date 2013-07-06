package eu.cloudtm;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class Book extends Book_Base {

    public Book(int id, double price) {
        super();
        setId(id);
        setPrice(price);
    }

    @Override
    public String toString() {
        return "Book " + getId();
    }
}
