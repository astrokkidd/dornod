package src;
/**
 * Case class that has a number,
 * an amount, and the ability to
 * determine if the case has already
 * been picked.
 */

public class Case {
    int num;
    double amount;
    boolean canBePicked;

    /**
     * An initializing constructor
     */
    public Case() {
        this.num = 0;
        this.amount = 0;
        this.canBePicked = false;
    }

    /**
     * A constructor that sets the number and
     * the amount
     *
     * @param num   Number of the case
     * @param amount    Amount of the case
     */
    public Case(int num, double amount) {
        this.num = num;
        this.amount = amount;
        this.canBePicked = true;
    }

    /**
     * Sets the number of a case
     *
     * @param num   Number to set
     */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     * Sets the amount of a case
     *
     * @param amount    Amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Sets if the case can be picked
     *
     * @param canBePicked    Can it be picked
     */
    public void setCanBePicked(boolean canBePicked) {
        this.canBePicked = canBePicked;
    }

    public boolean getCanBePicked() {
            return canBePicked;
    }

    /**
     * Gets a cases number
     *
     * @return  Number of case
     */
    public int getNum() {
        return num;
    }

    /**
     * Gets a cases amount
     *
     * @return  Amount of case
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sends case to string
     *
     * @return  String
     */
    public String toString() {
        return "Case number " + num +
               " has $" + amount +
               "!";
    }
}