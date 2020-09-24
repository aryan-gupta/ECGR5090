 
 
# Vending Machine simulator
 
## Description
This project lets you run a simulation of a vending machine, complete with individual coins and various exception handling.
 
## How to use
- Compile with `javac *.java`
- Run with `java VMRunner`
 
## Classes
- Coin - Represents a coin of US currency. Has the ability to compare values with other coins.
- Product - Represents a product in the vending machine. Uses a HashMap to keep valid product names so more can be added.
- VendingMachine - Represents a vending machine. Allows the servicer to add coins and products. and user to start and cancel a buy transaction. Returns the change in the least amount of coins possible.
- VMRunner - Runner program that tests out the Vending machine. Allows the user to deposit coins into the vending machine, and buy a product. Will repeat if an invalid coin is entered. Will end the transaction if a product is sold out, the vending machine does not have the valid return coins, or the user has not deposited the sufficient amount to buy the product.
- NotFullPaidException - Throwable class that is thrown when the user does not have enough deposit to buy the selected product.
- NotSufficientChangeException - Throwable class that is thrown when the vending machine does not have the proper change denominations to return the return change.
- SoldOutException - Throwable class that is thrown when the product the user is intending to buy is sold out.
 
## Output Analysis
- Output 1
  - Lines 1 to 23 - Two 25 cent coins are deposited and a soda is bought. A nickel is returned from the vending machine as change.
  - Lines 26 to 46 - A 25 cent coin is deposited and a coke is bought. No change is returned.
  - Lines 49 to 77 - A 5 cent coin is deposited followed by a 3 cent coin, but is refused as it's not a valid coin. Then, jelly is deposited into the vending machine, which is also returned because it's not a valid coin. A quarter is then deposited, finally followed by a nickel. Pepsi is chosen by the user and it is vended.
  - Lines 80 to 102 - Two 25 cent coins are deposited and a soda is bought. The vending machine states that the product is sold out and returns all the change.
  - Lines 105 to 125 - A nickel is deposited and a coke is bought. The vendor error saying there is not enough change deposited. The vendor returns the change.
  - Lines 128 to 144 - Two dimes are deposited. The user cancels the transaction and the vendor returns the change.
- Output 2 - Lines 109 to 112 in VMRunner.java is commented out to remove all internal change coins.
  - Lines 1 to 23 - Two 25 cent coins are deposited and a soda is bought. The vending machine cancels the transaction because there are not enough coins to return the change.
  - Lines 26 to 47 - Two 25 cent coins are deposited. The user decides to cancel the transaction and the change is returned.

