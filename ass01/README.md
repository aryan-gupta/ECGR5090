

# Assignment 1 - Inheritance and Polymorphism

## Description
This program emulates a checkout system for a dessert store

## How To Use
    1. run `javac *.java` to compile
    2. run `java TestCheckout` to see the test output

## File Listing
    - `Candy.java` - Child class of `DessertItem`. Contains the weight of the candy and the price per pound.
    - `Cookie.java` - Child class of `DessertItem`. Contains the quantity of cookies and the price per dozen of cookies. 
    - `IceCream.java` - Child class of `DessertItem`. Contains the price of the ice cream. 
    - `Sundae.java` - Child class of `Icecream`. Contains the topping and its price on top of the ice cream. 
    - `DessertItem.java` - Parent class for above classes. Contains name of the dessert and a abstract function to get the cost of the item.
    - `Checkout.java` - A class to handle the checkout process for the store. Models a "receipt" system in most brick and mortar stores. 
    - `DessertShoppe.java` - A class with static members with useful utility algorithms and constants. Given by rubric.
    - `TestCheckout.java` - A test runner class to test the above classes. Given by rubric.
    - `output.txt` - The test output of the program
