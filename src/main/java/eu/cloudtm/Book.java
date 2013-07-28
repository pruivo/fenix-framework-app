package eu.cloudtm;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class Book extends Book_Base {

    public Book(int id, double price) {
        this(null, id, price);
    }

    public Book(LocalityHints hints, int id, double price) {
        super(hints);
        setId(id);
        setPrice(price);
    }

    @Override
    public String toString() {
        return "Book " + getId();
    }

    public final Comment comment(String text) {
        if (text == null) {
            return null;
        }
        Comment comment = new Comment(getLocalityHints(), this, text);
        addComments(comment);
        return comment;
    }

    public static Book createBookGroupedById(int id, double price) {
        LocalityHints localityHints = new LocalityHints();
        localityHints.addHint(Constants.GROUP_ID, String.valueOf(id));
        return new Book(localityHints, id, price);
    }
}
