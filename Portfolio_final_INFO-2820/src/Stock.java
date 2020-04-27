/*
 * 
 * Stock.java
 * 
 * 
 */
public class Stock extends Investment {

	public static final double COMMISSION = 9.99;

	/**
	 * requires all instances to be provided as input and prevents null assignments
	 * of class types and negative for int and double variables
	 * 
	 * super invokes constructor to immediate parent class
	 * 
	 * @param symbol    The symbol of the stock investment
	 * @param name      The name of the stock investment
	 * @param quantity  The quantity of stock investment
	 * @param price     The price of stock investment
	 * @param bookValue
	 */
	public Stock(String symbol, String name, int quantity, double price, double bookValue) {
		super(symbol, name, quantity, price, bookValue);
	}

	/**
	 * default constructor
	 */
	public Stock() {
		super();
	}

	/**
	 * compares fields of stock investment symbol
	 * 
	 * @param other the object instance to be compared to this instance
	 * @return true if both symbol matches
	 */

	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (!this.getClass().equals(other.getClass()))
			return false;
		Stock st = (Stock) other;
		return super.equals(other);
	}

	/**
	 * represents all instances
	 * 
	 * @return a string containing all the instances
	 */
	@Override
	public String toString() {
		return super.toString();
	}

	/**
	 * This method calculates the book value of a stock when buying
	 * 
	 * @param amount  the quantity of stocks to be bought
	 * @param pricing the price at which they are being bought
	 * @return the bookvalue of the stock
	 */
	public double computeBookValueForBuying(int amount, double pricing) {
		double theBookValue = amount * pricing + COMMISSION;
		return theBookValue;
	}

	/**
	 * method calculates bookValue of a stock when being sold
	 * 
	 * @param initialAmount    The quantity of stocks before selling
	 * @param remainingAmount  The remaining stock after selling
	 * @param initialBookValue The value of book value at the time when it was
	 *                         bought
	 * @return the new value of the book value
	 */

	public double computeBookValueForSelling(int initialAmount, int remainingAmount, double initialBookValue) {
		double finalBookValue = initialBookValue * ((double) remainingAmount / (double) initialAmount);
		return finalBookValue;
	}

	/**
	 * computes payment for each selling
	 * 
	 * @param amount  The quantity of stocks being sold
	 * @param pricing The price at which the stock is being sold
	 * @return the payment after selling stocks
	 */
	public double computePayment(int amount, double pricing) {
		double payment = amount * pricing - COMMISSION;

		return payment;
	}

	/**
	 * calculate the stock gain
	 * 
	 * FIXME: Doesn't seem to update...
	 * 
	 * @param currentQty   amount of a particular stock
	 * @param updatedPrice new price at which it is bought or sold
	 * @param theBookValue current book value of the stock
	 * @return gain of all stocks
	 */
	public double computeStockGain(int currentQty, double updatedPrice, double theBookValue) {
		double gain = (currentQty * updatedPrice - COMMISSION) - theBookValue;

		return gain;
	}

}// end stock class
