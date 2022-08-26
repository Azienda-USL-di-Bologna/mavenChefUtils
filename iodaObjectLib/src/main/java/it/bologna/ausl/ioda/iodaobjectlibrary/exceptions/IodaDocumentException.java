package it.bologna.ausl.ioda.iodaobjectlibrary.exceptions;

/**
 *
 * @author Giuseppe De Marco (gdm)
 */
public class IodaDocumentException extends Exception {

    public IodaDocumentException() {
    }
    
    public IodaDocumentException(String message) {
        super(message);
    }

    public IodaDocumentException(Throwable cause) {
        super(cause);
    }

    public IodaDocumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
