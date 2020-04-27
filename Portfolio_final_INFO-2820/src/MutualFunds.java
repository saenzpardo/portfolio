/*
 * 
 * MutualFunds.java
 * 
 * 
 */
public class MutualFunds extends Investment {

	public static final double REDEMPTION = 9.99;

	public MutualFunds(String symbol, String name, int quantity, double price, double bookValue) {
		super(symbol, name, quantity, price, bookValue);
	}

	public MutualFunds() {
		super();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (!this.getClass().equals(other.getClass()))
			return false;

		MutualFunds mf = (MutualFunds) other;
		return super.equals(other);
	}

	@Override
	public String toString() {

		return super.toString();
	}

	public double computeBookValueForBuying(int amount, double pricing) {
		double theBookValue = amount * pricing;
		return theBookValue;
	}

	public double computeBookValueForSelling(int initialAmount, int remainingAmount, double initialBookValue) {
		double finalBookValue = initialBookValue * (remainingAmount / initialAmount);
		return finalBookValue;
	}

	public double computePayment(int amount, double pricing) {
		double payment = amount * pricing - REDEMPTION;
		return payment;
	}

	public double computeFundsGain(int currentQty, double updatedPrice, double theBookValue) {
		double gain = (currentQty * updatedPrice - REDEMPTION) - theBookValue;
		return gain;
	}

}// end mutual funds