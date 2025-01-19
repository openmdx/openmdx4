package javax.jmi.reflect;

/** Exception thrown when a collection passed to an operation parameter with {@code isUnique} set to true
 * contains duplicate values.
 */
@SuppressWarnings("serial")
public class DuplicateException extends JmiException {

    /**
     * Constructs an {@code DuplicateException} without detail message.
     * @param objectInError object that was duplicated.
     * @param elementInError Attribute, Parameter or Association End that defines the multiplicity which is being violated.
     */
    public DuplicateException(Object objectInError, RefObject elementInError) {
        super(objectInError, elementInError);
    }
    
    /**
     * Constructs an {@code DuplicateException} with the specified detail message.
     * @param objectInError object that was duplicated.
     * @param elementInError Attribute, Parameter or Association End that defines the multiplicity which is being violated.
     * @param msg the detail message.
     */
    public DuplicateException(Object objectInError, RefObject elementInError, String msg) {
        super(objectInError, elementInError, msg);
    }
}