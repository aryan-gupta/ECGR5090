
/// This class is a throwable that is thrown when the user has not paid enough
/// to buy the product
public final class NotFullPaidException extends Exception {
    public NotFullPaidException() {
        super("Deposit not enough for product");
    }
}