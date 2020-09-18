
import java.util.Vector;
import java.lang.StringBuilder;

public final class Checkout {
    private Vector<DessertItem> mCart;
    private boolean mDirty;
    private int mCost;

    public Checkout() {
        mDirty = true;
        mCart = new Vector<DessertItem>();
    }

    public void enterItem(DessertItem item) {
        mDirty = true;
        mCart.add(item);
    }

    public int numberOfItems() {
        return mCart.size();
    }

    private void updateCost() {
        mDirty = false;
        mCost = 0; 
        for (DessertItem item : mCart)
            mCost += item.getCost();
    }

    public int totalCost() {
        if (mDirty) {
            updateCost();
        }

        return mCost;
        //return DessertShoppe.cents2dollarsAndCents(mCost);
    }

    public int totalTax() {
        if (mDirty) {
            updateCost();
        }

        double taxFP = mCost * (DessertShoppe.TAX_RATE / 100.0);
        return (int) java.lang.Math.round(taxFP);
        //return DessertShoppe.cents2dollarsAndCents(tax);
    }

    public void clear() {
        mCart.clear();
    }

    private String stringLine(String str, int costInCents) {
        String cost = DessertShoppe.cents2dollarsAndCents(costInCents);

        int numSpacesName = DessertShoppe.MAX_ITEM_NAME_SIZE - str.length();
        int numSpacesCost = DessertShoppe.COST_WIDTH - cost.length();

        StringBuilder sb = new StringBuilder();

        sb.append(str);

        // https://stackoverflow.com/questions/2255500
        sb.append(new String(new char[numSpacesName + numSpacesCost]).replace("\0", " "));

        sb.append(cost + "\n");

        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("     " + DessertShoppe.STORE_NAME + "\n");
        sb.append("     --------------------\n\n");
        
        for (DessertItem item : mCart)
            sb.append(item.toString() + "\n");

        sb.append("\n");
        sb.append(stringLine("Tax", totalTax()));
        sb.append(stringLine("Total Cost", totalCost() + totalTax()));

        return sb.toString();
    }
}