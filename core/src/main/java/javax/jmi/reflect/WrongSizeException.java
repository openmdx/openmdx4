package javax.jmi.reflect;

/** Exception which arises when a collection contains
 * fewer or more values than is required by the corresponding
 * {@code Multiplicity.lower} and {@code Multiplicity.upper}.
 */
@SuppressWarnings("serial")
public class WrongSizeException extends JmiException {
    
    /** Creates {@code WrongSizeException} with a detail message.
     * @param elementInError element in error.
     */    
    public WrongSizeException(RefObject elementInError) {
        super(elementInError);
    }
    
    /** Creates {@code WrongSizeException} with a detail message.
     * @param elementInError element in error.
     * @param msg detail message.
     */    
    public WrongSizeException(RefObject elementInError, String msg) {
        super(elementInError, msg);
    }
}
