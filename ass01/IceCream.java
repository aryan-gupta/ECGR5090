


public class IceCream extends DessertItem {
    private int mPrice;

    public IceCream(String name, int price) {
        super(name);
        mPrice = price;
    }

    @Override
    public int getCost() {
        return mPrice;
    }

    public String toString() {
        String cost = DessertShoppe.cents2dollarsAndCents(getCost());

        int numSpacesName = DessertShoppe.MAX_ITEM_NAME_SIZE - super.getName().length();
        int numSpacesCost = DessertShoppe.COST_WIDTH - cost.length();

        StringBuilder sb = new StringBuilder();

        sb.append(super.getName());

        // https://stackoverflow.com/questions/2255500
        sb.append(new String(new char[numSpacesName + numSpacesCost]).replace("\0", " "));

        sb.append(cost);

        return sb.toString();
    }
}