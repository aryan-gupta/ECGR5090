


public class Candy extends DessertItem {
    private double mWeight;
    private int mPricePerPound;

    public Candy(String name, double weight, int ppp) {
        super(name);
        mWeight = weight;
        mPricePerPound = ppp;
    }

    @Override
    public int getCost() {
        double price = mPricePerPound * mWeight;
        return (int) java.lang.Math.round(price);
    }


    public String toString() {
        String cost = DessertShoppe.cents2dollarsAndCents(getCost());

        int numSpacesName = DessertShoppe.MAX_ITEM_NAME_SIZE - super.getName().length();
        int numSpacesCost = DessertShoppe.COST_WIDTH - cost.length();

        StringBuilder sb = new StringBuilder();

        sb.append(mWeight + " lbs. @ " + DessertShoppe.cents2dollarsAndCents(mPricePerPound) + " /lb.\n" +
               super.getName());

        // https://stackoverflow.com/questions/2255500
        sb.append(new String(new char[numSpacesName + numSpacesCost]).replace("\0", " "));

        sb.append(cost);

        return sb.toString();
    }
}