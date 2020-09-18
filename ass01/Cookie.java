


public class Cookie extends DessertItem {
    private int mQuantity;
    private int mPricePerDozen;

    public Cookie(String name, int quantity, int ppd) {
        super(name);
        mQuantity = quantity;
        mPricePerDozen = ppd;
    }

    @Override
    public int getCost() {
        double price = (mPricePerDozen / 12.0f) * mQuantity;
        return (int) java.lang.Math.round(price);
    }


    public String toString() {
        String cost = DessertShoppe.cents2dollarsAndCents(getCost());

        int numSpacesName = DessertShoppe.MAX_ITEM_NAME_SIZE - super.getName().length();
        int numSpacesCost = DessertShoppe.COST_WIDTH - cost.length();

        StringBuilder sb = new StringBuilder();

        sb.append(mQuantity + " @ " + DessertShoppe.cents2dollarsAndCents(mPricePerDozen) + " /dz.\n" +
               super.getName());

        // https://stackoverflow.com/questions/2255500
        sb.append(new String(new char[numSpacesName + numSpacesCost]).replace("\0", " "));

        sb.append(cost);

        return sb.toString();
    }
}