/*
 * Investment.java 
 * 
 */
public class Investment {
	// declare variables
	private String symbol;
	private String name;
	private int quantity;
	private double price;
	private double bookValue;

	public Investment(String symbol, String name, int quantity, double price, double bookValue) {
		if (symbol == null) {
			this.symbol = "";
		} else {
			this.symbol = symbol;
		}
		if (name == null) {
			this.name = "";
		} else {
			this.name = name;
		}
		if (quantity < 0) {
			this.quantity = 0;
		} else {
			this.quantity = quantity;
		}
		if (price < 0.0) {
			this.price = 0.0;
		} else {
			this.price = price;
		}
		this.bookValue = bookValue;
	}

	public Investment() {
		this.symbol = "";
		this.name = "";
		this.quantity = 0;
		this.price = 0.0;
		this.bookValue = 0.0;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Investment))
			return false;
		else {
			Investment d = (Investment) o;
			if (!(this.getSymbol().equals(d.getSymbol())))
				return false;
			if (!(this.getName().equals(d.getName())))
				return false;
			if (!(this.getQuantity() == d.getQuantity()))
				return false;
			if (!(this.getPrice() == d.getPrice()))
				return false;
			return true;
		}
	}

	@Override
	public String toString() {
		return "symbol = " + symbol + "\nname = " + name + "\nquantity = " + quantity + "\n" + "price = " + price + "\n"
				+ "bookValue = " + bookValue + "\n";
		// return ("symbol = \"" + symbol + "\"" + "\name = \"" + name+ "\"" + "quantity
		// = \"" + quantity + "\"" + "price = \"" + price + "\"" + "bookValue = \"" +
		// bookValue + "\"");
	}

	/*
	 * 
	 * getters and setters
	 * 
	 */

	public void setSymbol(String symbol) {
		if (symbol != null && !"".equals(symbol)) {
			this.symbol = symbol;
		} else {
			System.out.println("Error: Improper symbol");
			System.exit(0);
		}
	}

	public void setName(String name) {
		if (name != null && !"".equals(name)) {
			this.name = name;
		} else {
			System.out.println("Error: Improper name");
			System.exit(0);
		}
	}

	public void setQuantity(int quantity) {
		if (quantity >= 0) {
			this.quantity = quantity;
		} else {
			System.out.println("Error: Negative quantity");
			System.exit(0);
		}
	}

	public void setPrice(Double price) {
		if (price >= 0.0) {
			this.price = price;
		} else {
			System.out.println("Error: Negative price");
			System.exit(0);
		}
	}

	public void setBookValue(Double bookValue) {
		this.bookValue = bookValue;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getName() {
		return name;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getPrice() {
		return price;
	}

	public double getBookValue() {
		return bookValue;
	}

}// end investment class
