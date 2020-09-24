
/// This class is a throwable that is thrown when there isnt enough internal
/// change to return the return change
public final class NotSufficientChangeException extends Exception {
    public NotSufficientChangeException() {
        super("Not enough coins to return change");
    }
}