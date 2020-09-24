

import java.util.Arrays;

/// A class to represent a US Currency Coin
public final class Coin implements Comparable<Coin> {
	/// The value of the coin in cents
	private int mValue;

	/// Creates the coin using the value passed in
	/// @param The value in cents
	/// @throws IllegalArgumentException If param is not a valid representation of US currency coin
	public Coin(int value) throws IllegalArgumentException {
		// Check if the value is a valid US coin value
		if (Arrays.binarySearch(new int[]{ 1, 5, 10, 25 }, value) < 0) {
			throw new IllegalArgumentException("Not a valid type of USA Currency Coins");
		}

		mValue = value;
	}
	
	/// Creates the coin using the name passed in.
	/// The name can be a String representation of the value such
	/// as "1" or "25" or can be the name of the coin such as
	/// "penny" or "quarter"
	///
	/// @param name The String name or value of the coin to create
	/// @throws IllegalArgumentException If param is not a valid representation of US currency coin
	public Coin(String name) throws IllegalArgumentException {
		switch (name.toLowerCase()) {
			case "1":
			case "penny":
				mValue = 1;
				break;

			case "5":
			case "nickel":
				mValue = 5;
				break;

			case "10":
			case "dime":
				mValue = 10;
				break;

			case "25":
			case "quarter":
				mValue = 25;
				break;

			default:
				throw new IllegalArgumentException("Not a valid type of USA Currency Coins");
		}
	}

	/// Returns the value of the coin in cents
	/// @return The value of the coin 
	public int getValue() {
		return mValue;
	}

	/// Returns the name of the coin
	/// @return The name of the coins
	public String getName() {
		switch (mValue) {
			case 1:  return "Penny";
			case 5:  return "Nickel";
			case 10: return "Dime";
			case 25: return "Quarter";
			default: throw new Error("The universe has fallen into despair");
		}
	}

	/// Compares two coins
	/// @param The other coin to compare to
	/// @return 0 if the coins are equal; < 1 if this is smaller coin; > 1 if this coin is
	///         larger
	@Override
	public int compareTo(Coin c) {
		return Integer.compare(mValue, c.mValue);
	}

    /// Tests if this object is equal to another Object
    /// @param o The other object
    /// @return If this is equal to o
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (!(o instanceof Coin)) return false;
		return Integer.compare(mValue, ((Coin) o).mValue) == 0;
	}
}