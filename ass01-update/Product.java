

import java.util.Arrays;
import java.lang.IllegalArgumentException;
import java.util.HashMap;

/// Class to represent a product. A list of all possible products and their
/// prices are stored in a HashMap so more products can be added with
/// minimal effort. 
public final class Product {
    /// HashMap containing all the possible types of products
    private static HashMap <String, Integer> PRODUCT_LIST;
    /// The name of the product this is holding
    private String mName;

    static {
        // There has to be a prettier way to do this
        /// @todo research on how to initialize a static member hashmap
        /// @todo figure out a way to have a case sensitive name for the
        ///       products so it can be properly returned in getName()
        PRODUCT_LIST = new HashMap <String, Integer>();
        PRODUCT_LIST.put("coke", 25);
        PRODUCT_LIST.put("pepsi", 35);
        PRODUCT_LIST.put("soda", 45);
    }

    /// Creates the product using the name passed in
    /// If the prodcut is not sold, an IllegalArgumentException is thrown
    ///
    /// @param name The name of the product to create
    /// @throws IllegalArgumentException If no such product exists
    public Product(String name) throws IllegalArgumentException {
        mName = name.toLowerCase();

        if (!PRODUCT_LIST.containsKey(mName)) {
            throw new IllegalArgumentException("Not a valid type of product: " + name);
        }
    }

    /// Returns the cost of the Product
    /// @return The cost of this Product
    public int getCost() {
        return PRODUCT_LIST.get(mName);
    }

    /// Returns the name of the Product
    /// @return The name of this Product
    public String getName() {
        return mName;
    }

    /// Tests if this object is equal to another Object
    /// @param o The other object
    /// @return If this is equal to o
    @Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (!(o instanceof Product)) return false;
		return mName.equals(((Product) o).mName);
	}
}