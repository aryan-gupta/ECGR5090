
/// This class is a throwable that is thrown when the product the user chose
/// is sold out or is unavailable
public final class SoldOutException extends Exception {
    public SoldOutException() {
        super("Product is sold out");
    }
}