package eu.cloudtm;

/**
 * @author Pedro Ruivo
 * @since 1.0
 */
public class Author extends Author_Base {

    public Author() {
        super();
    }

    public Author(int id, int age) {
        this();
        setId(id);
        setAge(age);
    }

    protected Author(LocalityHints hints, int id, int age) {
        super(hints);
        setId(id);
        setAge(age);
    }

    public static Author createAuthorGroupedByAge(int id, int age) {
        LocalityHints localityHints = new LocalityHints();
        localityHints.addHint(Constants.GROUP_ID, String.valueOf(age));
        return new Author(localityHints, id, age);
    }

    @Override
    public String toString() {
        return "Author " + getId();
    }
}
