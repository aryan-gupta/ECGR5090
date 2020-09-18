


public class Sundae extends IceCream {
    private String mTopping;
    private int mToppingPrice;

    public Sundae(String name, int price,
                  String topping, int toppingPrice
    ) {
        super(name, price);
        mTopping = new String(topping);
        mToppingPrice = toppingPrice;
    }

    @Override
    public int getCost() {
        return mToppingPrice + super.getCost();
    }

    public String toString() {
        String cost = DessertShoppe.cents2dollarsAndCents(getCost());

        int numSpacesName = DessertShoppe.MAX_ITEM_NAME_SIZE - super.getName().length();
        int numSpacesCost = DessertShoppe.COST_WIDTH - cost.length();

        StringBuilder sb = new StringBuilder();

        sb.append(mTopping + " Sundae with\n" +
               super.getName());

        // https://stackoverflow.com/questions/2255500
        sb.append(new String(new char[numSpacesName + numSpacesCost]).replace("\0", " "));

        sb.append(cost);

        return sb.toString();
    }
}