package eu.cloudtm;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class Comment extends Comment_Base {

    public Comment() {
        super();
    }

    public Comment(LocalityHints hints, Book book, String text) {
        super(hints);
        setBook(book);
        setText(text);
    }

    public final void like() {
        int likes = getLikes();
        setLikes(++likes);
    }

    @Override
    public final String toString() {
        return "Comment{text='" + getText() + "', likes=" + getLikes() + '}';
    }

}
