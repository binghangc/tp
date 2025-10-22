package seedu.address.model.person.exceptions;

/**
 * Signals that the operation failed to update the student's payment status
 */
public class PaymentStatusUpdateException extends RuntimeException {
    public PaymentStatusUpdateException() {
        super("Payment status failed to update.");
    }
}
