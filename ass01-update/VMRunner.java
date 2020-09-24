
import java.util.Scanner;
import java.lang.IllegalArgumentException;
import java.util.Collections;
import java.util.Vector;
import java.io.PrintStream;

public final class VMRunner {
    /// Asks the user to deposit coins. Loops until user if finished
    /// @param keyboardIn The keyboard Scanner
    /// @param vender The vending machine object
    /// @return If the user wants to quit or not
    public static boolean getUserCoins(Scanner keyboardIn, VendingMachine vender) {
        PrintStream out = System.out;

        out.println("Products you can buy: ");
        out.println("\tCoke  25\u00A2");
        out.println("\tPepsi 35\u00A2");
        out.println("\tSoda  45\u00A2");
        
        do {
            out.print("Insert coins (q to quit, f to finish):: ");
            String input = keyboardIn.nextLine();

            if (input.toLowerCase().equals("q")) {
                return true;
            }

            if (input.toLowerCase().equals("f") || input.equals("")) {
                break;
            }

            Coin coin;
            try {
                coin = new Coin(input);
            } catch (IllegalArgumentException e) {
                out.println("Invalid types of coin. Valid options are 1, 5, 10, 25, penny, nickel, dime, quarter");
                continue;
            }
            
            vender.deposit(coin);
            out.println(coin.getName() + " deposited. Total: " + vender.getDepositAmount() + "\u00A2");
        } while (true);

        return false;
    } 
    
    /// Asks the user which product to buy. 
    /// @param keyboardIn The keyboard Scanner
    /// @param vender The vending machine object
    /// @param If the user wants to quit or not. Will simply return all change if true.
    /// @return If the user wants to quit or not
    public static boolean askProduct(Scanner keyboardIn, VendingMachine vender, boolean quit) {
        PrintStream out = System.out;
        String input;
        if (quit) {
            input = new String(""); // cencel buy transaction, return all change
            out.println("\nYour deposit amount: " + vender.getDepositAmount() + "\u00A2");
        } else {
            out.println("Select Product: ");
            out.println("\tCoke  25\u00A2");
            out.println("\tPepsi 35\u00A2");
            out.println("\tSoda  45\u00A2");

            out.println("\nYour deposit amount: " + vender.getDepositAmount() + "\u00A2");

            out.print("Selection (q to quit):: ");
            input = keyboardIn.nextLine();

            if (input.toLowerCase().equals("q")) {
                input = "";
                quit = true;
            }  
        }

        Vector<Coin> returnChange;
        try {
            returnChange = vender.buy(input);
            if (!input.equals(""))
                out.println("\tVending " + input);
        } catch (SoldOutException e) {
            out.println("Product is sold out or is unavailable");
            returnChange = vender.returnAllChange();
        } catch (NotFullPaidException e) {
            out.println("Need more coins for that product");
            returnChange = vender.returnAllChange();
        } catch (NotSufficientChangeException e) {
            out.println("Not enough coins in the machine to dispence this product");
            returnChange = vender.returnAllChange();
        }

        out.println("Your return change is follows: ");
        for (int coinValue : new int[]{ 25, 10, 5, 1 }) {

        }
        out.println("" + Collections.frequency(returnChange, new Coin(25)) + " quarter(s)");
        out.println("" + Collections.frequency(returnChange, new Coin(10)) + " dime(s)");
        out.println("" + Collections.frequency(returnChange, new Coin(5))  + " nickel(s)");
        out.println("" + Collections.frequency(returnChange, new Coin(1))  + " pennies(s)");

        return quit;
    }

    public static void main(String[] a) {
        PrintStream out = System.out;
        Scanner keyboardIn = new Scanner(java.lang.System.in);

        VendingMachine vender = new VendingMachine();
        vender.serviceAddCoins(new Coin(1), 8);
        vender.serviceAddCoins(new Coin(5), 6);
        vender.serviceAddCoins(new Coin(10), 10);
        vender.serviceAddCoins(new Coin(25), 7);
        vender.serviceAddProducts(new Product("Coke"), 7);
        vender.serviceAddProducts(new Product("Pepsi"), 5);
        vender.serviceAddProducts(new Product("Soda"), 1);
    
        boolean quit = false;
        do {
            out.println("Welcome to Aryan's Vending Machine");
            quit = getUserCoins(keyboardIn, vender);
            quit = askProduct(keyboardIn, vender, quit);
            out.println("\n\n");
        } while (!quit);
    }
}