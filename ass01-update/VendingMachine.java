
import java.util.Vector;
import java.util.Collections;

public final class VendingMachine {
    private Vector<Coin> mInternalChange;
    private int mUserDeposit;
    private Vector<Product> mAvailableProducts;

    /// Initializes an empty Vending Machine with no internal change
    /// or products
    public VendingMachine() {
        mInternalChange = new Vector<Coin>();
        mAvailableProducts = new Vector<Product>();
        mUserDeposit = 0;
    }

    /// Services the Vending Machine and adds coins to the machine
    /// will create \param quantity quantities of the coin and add
    /// it to internal change bank
    /// @param coin The type of coin to add
    /// @param quantity The number of coins to add
    public void serviceAddCoins(Coin coin, int quantity) {
        while (quantity --> 0) {
            mInternalChange.add(new Coin(coin.getName()));
        }
    }

    /// Services the Vending Machine and adds products
    /// @sa public void serviceAddCoins(Coin, int)
    /// @param product The type of product to add
    /// @param quantity The number of product to add
    public void serviceAddProducts(Product product, int quantity) {
        while (quantity --> 0) {
            mAvailableProducts.add(new Product(product.getName()));
        }
    }

    /// Deposits a user coin
    /// @note will increase the mUserDeposit amount
    /// @param coin The Coin to deposit
    public void deposit(Coin coin) {
        mInternalChange.add(coin);
        mUserDeposit += coin.getValue();
    }

    /// Returns the current user deposit amount
    /// @return The current user deposit amount in cents
    public int getDepositAmount() {
        return mUserDeposit;
    }

    /// Helper function to transfer a coin from internal change bank to \param dest
    /// Checks if the transfer is possible and if the coin is available
    /// @warning mInternalChange must be sorted before this function is called
    /// @param dest The destination of where to put the Coin
    /// @param value The value of coin to transfer
    /// @param remaining The remaining value needed to be transfered
    /// @return true if the coin was successfully transfered, false if no coin
    ///         exists or the transfer would couse the remaining value to go into
    ///         the negative
    private boolean xferCoin(Vector<Coin> dest, int value, int remaining) {
        if (remaining < value)
            return false;
        
        int coinIdx = Collections.binarySearch(mInternalChange, new Coin(value));

        if (coinIdx < 0)
            return false;

        dest.add(mInternalChange.get(coinIdx));
        mInternalChange.remove(coinIdx);

        return true;
    }

    /// Calculates the types of coin needed to equal the mUserDeposit amount
    /// This function can be used to return the change after buying an item or
    /// canceling the buy transaction. Function is private and must be called by the
    /// public functions \sa returnAllChange() and \sa buy(String)
    /// @return An array of the coins where the value equals mUserDeposit
    /// @throws NotSufficientChangeException If not enough coins exists to equal mUserDeposit
    private Vector<Coin> getReturnChange() throws NotSufficientChangeException {
        Vector<Coin> returnChange = new Vector<Coin>();
        
        // Since the Coin class implements the Comparator class
        // we can pass null as the parameter and it will use the 
        // interfaces compareTo() function
        // The array needs to be sorted because xferCoin uses binary
        // search to find coins
        mInternalChange.sort(null);

        // try to get the types of coins needed to total mUserDeposit, if
        // we cant NotSufficientChangeException will be thrown and will need to
        // return the coins we tried to remove back into mInternalChange
        /// @note We could probs speed this up by caching the value of internal
        ///       change we have and using it as a first step to see if we have
        ///       sufficient change, but I just want to get this done @todo
        int remainingChange = mUserDeposit;
        for (int coinValue : new int[]{ 25, 10, 5, 1 }) {
            while (xferCoin(returnChange, coinValue, remainingChange)) {
                remainingChange -= coinValue;
            }
        }

        if (remainingChange != 0) {
            mInternalChange.addAll(returnChange);
            throw new NotSufficientChangeException();
        } else {
            mUserDeposit = 0;
        }

        return returnChange;
    }

    /// Returns all the users change. Helpful when the user cancels a buy transaction.
    /// @note Is nothrow compared to \sa getReturnChange() because if the user cancels
    ///       a buy transaction, the deposited coins must exits and NotSufficientChangeException
    ///       cannot be thrown. The exception is wrapped in a try/catch but the exception
    ///       is simply thrown away and an Error is thrown (For debugging purposes)
    /// @return An array of the coins where the value equals mUserDeposit
    public Vector<Coin> returnAllChange() {
        Vector<Coin> returnChange;
        try {
            returnChange = getReturnChange();
        } catch (NotSufficientChangeException e) {
            throw new Error("Code path not intended to occur");
        }

        return returnChange;
    }

    /// Buys an item from the Vending Machine and returns the return change
    /// The product is removed and the remaining change is returned
    /// @note If the product name is empty, it will assume buy transaction was canceled
    /// @param productName The name of the product to buy, cancel transaction if empty
    /// @throws SoldOutException If the product does not exit or is sold out
    /// @throws NotFullPaidException If the user deposit amount is not sufficient to buy the product
    /// @throws NotSufficientChangeException If not enough coins exist to return the change
    public Vector<Coin> buy(String productName)
        throws SoldOutException, NotFullPaidException, NotSufficientChangeException
    {
        if (productName.equals("")){
            return returnAllChange();
        }

        int productIdx;
        try {
            productIdx= mAvailableProducts.indexOf(new Product(productName));
        } catch (IllegalArgumentException e) {
            throw new SoldOutException();
        }

        if (productIdx < 0) {
            throw new SoldOutException();
        }

        int cost = mAvailableProducts.get(productIdx).getCost();
        if (cost > mUserDeposit) {
            throw new NotFullPaidException();
        }

        Vector<Coin> returnChange;
        try {
            mUserDeposit -= cost;
            returnChange = getReturnChange();
        } catch (NotSufficientChangeException e) {
            mUserDeposit += cost;
            throw e;
        }

        mAvailableProducts.remove(productIdx);

        return returnChange;
    }
}