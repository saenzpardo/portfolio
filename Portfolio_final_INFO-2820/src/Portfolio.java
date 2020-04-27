
/*
 * A simple program to manage stock/mutual fund invesments using the following:
 * Stock symbol, name, shares, price per share and current price
 * User should be able to view/update current price and profit/loss
 * Requirements:
 * 1) Must use user defined classes and inheritance
 * 2) GUI interface must be used for I/O
 * 3) May own multiple stocks and participate in multiple mutual funds
 * 4) Stock object should only have information about one asses - stock, divident stock or mutual fund
 * 5) Exception handling should be used where appropriate
 * 6) The stock and mutual fund information stored in text file
 * 7) Option to display portfolio information in text file --> (I'll use a search option here I think...display in text area)
 * 
 * Author: Adam Saenzpardo
 * INFO-2820 Java Final
 * Date: April, 20 2020
 * 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class Portfolio extends JFrame {// and away we go...
	// declare variables
	private static final long serialVersionUID = 1L;
	public static final int HEIGHT = 400;
	public static final int WIDTH = 590;
	public static final int SF_NONEMPTY = 0;
	public static final int SF_ISINT = 1;
	public static final int SF_ISDOUBLE = 2;
	public static final int SF_ISDOUBLE2 = 3;
	// declare GUI
	private JTextArea textArea;
	private JTextArea messagesArea;
	private JScrollPane scrolledMessages;
	private JPanel commandsPanel;
	private JPanel buyPanel;
	private JPanel sellPanel;
	private JPanel updatePanel;
	private JPanel getGainPanel;
	private JPanel searchPanel;
	private JPanel bigPanel;
	private JComboBox<String> investmentsType;
	private JTextField symbolField;
	private JTextField nameField;
	private JTextField quantityField;
	private JTextField priceField;
	private JTextField keywordsField;
	private JTextField lowPriceField;
	private JTextField highPriceField;
	private JTextField getGainField;
	private JButton prevButton;
	private JButton nextButton;
	// declared when I needed...
	private String type;
	private String symbol;
	private String name;
	private int quantity;
	private double price;
	private String keywords;
	private double lowPrice;
	private double highPrice;
	private int count = 0;
	private static String filename;
	// declare arrayList
	private ArrayList<Investment> investment;

	// buy
	private class BuyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			// buying interface
			buyGui();

		}
	}

	// sell
	private class SellListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			// selling interface
			sellGui();
		}
	}

	// update
	private class UpdateListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			// updates interface
			updateGui();
		}
	}

	// gains
	private class GetGainListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			// gains interface
			GainGui();
		}
	}

	// search
	private class SearchListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			// search interface
			searchGui();
		}
	}

	// end program
	private class QuitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			print();
			saveInvestments(filename);
			System.exit(0);
		}
	}

	// print method
	public void print() {
		for (int i = 0; i < this.investment.size(); i++) {
			System.out.println(this.investment.get(i));
		}
	}

	// form reset
	private class ResetBListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			investmentsType.setSelectedIndex(0);
			symbolField.setText("");
			nameField.setText("");
			quantityField.setText("");
			priceField.setText("");
			messagesArea.setText("");
		}
	}

	// form reset
	private class ResetSListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			symbolField.setText("");
			quantityField.setText("");
			priceField.setText("");
			messagesArea.setText("");
		}
	}

	// form reset
	private class ResetSearchListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			symbolField.setText("");
			keywordsField.setText("");
			lowPriceField.setText("");
			highPriceField.setText("");
			messagesArea.setText("");
		}
	}

	// type drop box (stock/mutual funds)
	private class TypeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			type = null;
			JComboBox tempType = (JComboBox) ae.getSource();
			type = (String) tempType.getSelectedItem();
		}
	}

	// buy
	private class BuyButtonListener implements ActionListener {// BuyButton class

		@Override
		public void actionPerformed(ActionEvent ae) {
			symbol = null;
			name = null;
			quantity = 0;
			price = 0.0;
			// In case the user did not change the type button because they just want stock
			if (type == null) {
				type = "Stock";
			}
			if (symbol == null) {
				if (!validateFormat(symbolField.getText(), SF_NONEMPTY)) {
					investmentsType.setSelectedIndex(0);
					symbolField.setText("");
					nameField.setText("");
					quantityField.setText("");
					priceField.setText("");
					messagesArea.setText("Error Messages.\nSymbol field cannot be empty\nPlease try again!");
				} else {
					symbol = symbolField.getText();
					if (name == null) {
						if (!validateFormat(nameField.getText(), SF_NONEMPTY)) {
							investmentsType.setSelectedIndex(0);
							symbolField.setText("");
							nameField.setText("");
							quantityField.setText("");
							priceField.setText("");
							messagesArea.setText("Error Messages.\nName field cannot be empty\nPlease try again!");
						} else {
							name = nameField.getText();
							if (quantity == 0) {
								if (!validateFormat(quantityField.getText(), SF_ISINT)) {
									investmentsType.setSelectedIndex(0);
									symbolField.setText("");
									nameField.setText("");
									quantityField.setText("");
									priceField.setText("");
									messagesArea.setText(
											"Error Messages.\nQuantity field cannot be empty or invalid input\nPlease try again!");
								} else {
									quantity = Integer.parseInt(quantityField.getText().trim());
									if (price == 0.0) {
										if (!validateFormat(priceField.getText(), SF_ISDOUBLE)) {
											investmentsType.setSelectedIndex(0);
											symbolField.setText("");
											nameField.setText("");
											quantityField.setText("");
											priceField.setText("");
											messagesArea.setText(
													"Error Messages.\nPrice field cannot be empty or invalid input\nPlease try again!");
										} else {
											price = Double.parseDouble(priceField.getText().trim());
											addInvestment(type, symbol, name, quantity, price);
										}
									}
								}
							}
						}
					}
				}
			} // end nested if
		}// end method
	}// end class

	/**
	 * takes 5 parameters which are attributes of investment. checks if arrayList is
	 * empty, then verifies if type chosen by user is stock or mutual. Adds
	 * investment to arrayList if does not exist, otherwise updates price and
	 * quantity of investment, bookValue is updated.
	 * 
	 * @param type     stock investment or mutual fund investment
	 * @param symbol   symbol of the investment
	 * @param name     name of the investment
	 * @param quantity quantity to purchase
	 * @param price    purchase price of the investment
	 */
	public void addInvestment(String type, String symbol, String name, int quantity, double price) {

		// list isn't empty
		if (!this.investment.isEmpty()) {
			if ("stock".contains(type.toLowerCase().trim())) {
				for (int i = 0; i < this.investment.size(); i++) {
					if (this.investment.get(i).getSymbol().equals(symbol)) {
						// Updating quantity and price
						Stock addStock = new Stock();
						int tempQuantity = this.investment.get(i).getQuantity();
						tempQuantity = quantity + tempQuantity;
						this.investment.get(i).setQuantity(tempQuantity);
						this.investment.get(i).setPrice(price);
						double newBookValue = addStock.computeBookValueForBuying(quantity, price);
						double tempBookValue = this.investment.get(i).getBookValue();
						tempBookValue = tempBookValue + newBookValue;
						this.investment.get(i).setBookValue(tempBookValue);
						messagesArea.setText("Stock's quantity has been successfully updated to Portfolio");
						return;
					}
				}
				// existing match check
				Stock temp = new Stock();
				double tempBookValue = temp.computeBookValueForBuying(quantity, price);
				Stock tempStock = new Stock(symbol, name, quantity, price, tempBookValue);
				this.investment.add(tempStock);
				messagesArea.setText("Stock has been successfully added to Portfolio");
			} else if ("mutual funds".contains(type.toLowerCase().trim())) {
				for (int i = 0; i < this.investment.size(); i++) {
					if (this.investment.get(i).getSymbol().equals(symbol)) {
						// Updating quantity and price
						MutualFunds addFunds = new MutualFunds();
						int tempQuantity = this.investment.get(i).getQuantity();
						tempQuantity = quantity + tempQuantity;
						this.investment.get(i).setQuantity(tempQuantity);
						this.investment.get(i).setPrice(price);
						double newBookValue = addFunds.computeBookValueForBuying(quantity, price);
						double tempBookValue = this.investment.get(i).getBookValue();
						tempBookValue = tempBookValue + newBookValue;
						this.investment.get(i).setBookValue(tempBookValue);
						messagesArea.setText("Mutual Funds' quantity has been successfully updated to Portfolio");
						return;
					}
				}
				// does not match any, just add the new stock already
				MutualFunds temp = new MutualFunds();
				double tempBookValue = temp.computeBookValueForBuying(quantity, price);
				MutualFunds tempFund = new MutualFunds(symbol, name, quantity, price, tempBookValue);
				this.investment.add(tempFund);
				messagesArea.setText("Mutual Funds has been successfully added to Portfolio");
			}
		} else { // to add to empty list
			if ("stock".contains(type.toLowerCase().trim())) {
				Stock temp = new Stock();
				double tempBookValue = temp.computeBookValueForBuying(quantity, price);
				Stock tempStock = new Stock(symbol, name, quantity, price, tempBookValue);
				this.investment.add(tempStock);
				messagesArea.setText("Stock has been successfully added to Portfolio");
			} else if ("mutual funds".contains(type.toLowerCase().trim())) {
				MutualFunds temp = new MutualFunds();
				double tempBookValue = temp.computeBookValueForBuying(quantity, price);
				MutualFunds tempFund = new MutualFunds(symbol, name, quantity, price, tempBookValue);
				this.investment.add(tempFund);
				messagesArea.setText("Mutual Funds has been successfully added to Portfolio");
			} else {
				System.out.println("Unexpected type of investment");
			}
		}
	}// end addInvesment

	/**
	 * listener for sell buttons selling interface. Gets symbol, quantity/price of
	 * investment to be sold and calls the sellInvestments method to update
	 * portfolio
	 */
	private class SellButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			symbol = null;
			quantity = 0;
			price = 0.0;

			if (symbol == null) {
				if (!validateFormat(symbolField.getText(), SF_NONEMPTY)) {
					symbolField.setText("");
					quantityField.setText("");
					priceField.setText("");
					messagesArea.setText("Error Messages.\nSymbol field cannot be empty\nPlease try again!");
				} else {
					symbol = symbolField.getText();
					if (quantity == 0) {
						if (!validateFormat(quantityField.getText(), SF_ISINT)) {
							symbolField.setText("");
							quantityField.setText("");
							priceField.setText("");
							messagesArea.setText(
									"Error Messages.\nQuantity field cannot be empty or invalid input\nPlease try again!");
						} else {
							quantity = Integer.parseInt(quantityField.getText().trim());
							if (price == 0.0) {
								if (!validateFormat(priceField.getText(), SF_ISDOUBLE)) {
									symbolField.setText("");
									quantityField.setText("");
									priceField.setText("");
									messagesArea.setText(
											"Error Messages.\nPrice field cannot be empty or invalid input\nPlease try again!");
								} else {
									price = Double.parseDouble(priceField.getText().trim());
									sellInvestments(symbol, quantity, price);
								}
							}
						}
					}
				}
			} // end nested if block
		}// end method
	}// end sellButton class

	/**
	 * takes in 3 parameters to update the quantity and bookValue to be sold. if all
	 * is sold --> remove from arrayList
	 * 
	 * @param symbol   represents the symbol of the investment to be sold
	 * @param quantity represents the quantity to be sold
	 * @param price    represents the price at which the investment is to be sold
	 */
	public void sellInvestments(String symbol, int quantity, double price) {
		if (!this.investment.isEmpty()) {
			for (int i = 0; i <= this.investment.size() - 1; i++) {
				if (this.investment.get(i).getSymbol().equals(symbol)) {
					int availableQuantity = this.investment.get(i).getQuantity();
					if (!checkAvailability(quantity, i)) {
						symbolField.setText("");
						quantityField.setText("");
						priceField.setText("");
						messagesArea.setText("Error Message.\nThe quantity you input is invalid.Reason: "
								+ "Current quantity is less than the quantity you want to sell\nPlease try again!");
						return;
					}
					int remainingQuantity = availableQuantity - quantity;
					Investment tempInvestment = new Investment();
					if (remainingQuantity > 0) {
						// Update price
						this.investment.get(i).setPrice(price);
						// update bookValue, need to know whether is it a stock or funds
						if (this.investment.get(i).getClass().getCanonicalName().equals("assignment2.Stock")) {
							Stock s = new Stock();
							double initialBkValue = this.investment.get(i).getBookValue();
							double finalBkValue = s.computeBookValueForSelling(availableQuantity, remainingQuantity,
									initialBkValue);
							this.investment.get(i).setBookValue(finalBkValue);
							this.investment.get(i).setQuantity(remainingQuantity);
							double stockPayment = s.computePayment(quantity, price);
							messagesArea.setText("Selling of stock has been successfully");
							return;
						} else if (this.investment.get(i).getClass().getCanonicalName()
								.equals("assignment2.MutualFunds")) {
							MutualFunds m = new MutualFunds();
							double initialBkValue = m.getBookValue();
							double finalBkValue = m.computeBookValueForSelling(availableQuantity, remainingQuantity,
									initialBkValue);
							this.investment.get(i).setBookValue(finalBkValue);
							this.investment.get(i).setQuantity(remainingQuantity);
							double stockPayment = m.computePayment(quantity, price);
							messagesArea.setText("Selling of mutual fund has been successfully");
							return;
						}
					} else {
						// remove from list
						boolean remove = this.investment.remove(this.investment.get(i));
						if (this.investment.get(i).getClass().getCanonicalName().equals("assignment2.Stock")) {
							Stock s = (Stock) tempInvestment;
							double stockPayment = s.computePayment(quantity, price);
							return;
						} else if (this.investment.get(i).getClass().getCanonicalName()
								.equals("assignment2.MutualFunds")) {
							MutualFunds m = (MutualFunds) tempInvestment;
							double stockPayment = m.computePayment(quantity, price);
							return;
						}
					}
				} // end nested if block
			} // end for loop
			symbolField.setText("");
			quantityField.setText("");
			priceField.setText("");
			messagesArea.setText("Error Message.\nThis investment does not exist\nPlease try again!");
		} else {
			symbolField.setText("");
			quantityField.setText("");
			priceField.setText("");
			messagesArea.setText(
					"Error Message.\nSelling cannot be done because " + "there is no investments in the portfolio\n");
		}
	}// end sellInvestments method

	// FIXME: need to add a greater than available stock here...

	/**
	 * checks quantity to be sold is less than the quantity available in the
	 * portfolio
	 * 
	 * @param toSell an integer which represent the amount of stock the user wishes
	 *               to sell
	 * @param k      an integer to indicate which stock we are selling in the
	 *               arrayList
	 * @return true is the amount of available stock is greater than what is
	 *         requested
	 */
	public boolean checkAvailability(int toSell, int k) {
		int availableQuantity = this.investment.get(k).getQuantity();
		return availableQuantity >= toSell;
	}

	/**
	 * PrevButtonListener class
	 */
	private class PrevButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			int lastIndex = lastInvestmentIndex();
			count--;
			if (count == 0) {
				prevButton.setEnabled(false);
				nextButton.setEnabled(true);
				getPrevious(count);
			} else {
				nextButton.setEnabled(true);
				prevButton.setEnabled(true);
				getPrevious(count);
			}
		}// end actionPerformed
	}// end PrevButton

	/**
	 * gets symbol/name of investment and sets value to text field for symbol/name
	 * 
	 * @param i indicates the location of the investment retrieved from the
	 *          arrayList
	 */
	private void getPrevious(int i) {
		symbolField.setText(this.investment.get(i).getSymbol());
		nameField.setText(this.investment.get(i).getName());
		priceField.setText("");
	}

	/**
	 * NextButtonListener inner class for nextButton. Allows user to view next
	 * investment
	 */
	private class NextButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			int lastIndex = lastInvestmentIndex();
			count++;
			if (lastIndex - 1 == count) {
				nextButton.setEnabled(false);
				prevButton.setEnabled(true);
				getNext(count);
			} else if (count == 0) {
				prevButton.setEnabled(false);
				nextButton.setEnabled(true);
				getNext(count);
			} else if (count > 0 && count < lastIndex - 1) {
				prevButton.setEnabled(true);
				nextButton.setEnabled(true);
				getNext(count);
			}
		}
	}// end NextButton

	/**
	 * returns the size of investment arraList
	 * 
	 * @return the size of the arrayList investment
	 */
	private int lastInvestmentIndex() {
		return this.investment.size();
	}

	/**
	 * gets symbol and name of investment and sets to text field for symbol/name
	 * 
	 * @param i indicates the location of the investment in the arrayList
	 */
	private void getNext(int i) {
		symbolField.setText(this.investment.get(i).getSymbol());
		nameField.setText(this.investment.get(i).getName());
		priceField.setText("");
	}

	/**
	 * SaveButtonListener inner class for saveButton gets updated price from the
	 * text field, verifies double and calls the updatePrice method
	 */
	private class SaveButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			price = 0.0;
			if (price == 0.0) {
				if (!validateFormat(priceField.getText(), SF_ISDOUBLE)) {
					priceField.setText("");
					messagesArea.setText(
							"Error Messages.\nPrice field cannot be empty or invalid input while trying to update the price\nPlease try again!");
				} else {
					messagesArea.setText("");
					price = Double.parseDouble(priceField.getText().trim());
					updatePrice(count);
				}
			}
		}
	}

	/**
	 * sets the price of investment to new price and displays details
	 * 
	 * @param i indicates the location of the investment to be updated
	 */
	private void updatePrice(int i) {
		this.investment.get(i).setPrice(price);
		messagesArea.append("Investment's price has been successfully updated\n");
		messagesArea.append(this.investment.get(i).toString());
		priceField.setText("");
	}

	/**
	 * SearchButtonListener inner class for the searchButton Gets the values for
	 * symbol, keywords, low price and high price
	 */
	private class SearchButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			symbol = null;
			keywords = null;
			lowPrice = 0.0;
			highPrice = 0.0;

			if (symbol == null) {
				symbol = symbolField.getText();
				if (keywords == null) {
					keywords = keywordsField.getText();
					if (lowPrice == 0.0) {
						if (!validateFormat(lowPriceField.getText(), SF_ISDOUBLE2)) {
							symbolField.setText("");
							keywordsField.setText("");
							lowPriceField.setText("");
							highPriceField.setText("");
							messagesArea.setText(
									"Error Messages.\nLow Price field cannot be empty or invalid input\nPlease try again!");
						} else {
							if (lowPriceField.getText().isEmpty()) {
								lowPrice = 0.0;
							} else {
								lowPrice = Double.parseDouble(lowPriceField.getText().trim());
							}
							if (highPrice == 0.0) {
								if (!validateFormat(highPriceField.getText(), SF_ISDOUBLE2)) {
									symbolField.setText("");
									keywordsField.setText("");
									lowPriceField.setText("");
									highPriceField.setText("");
									messagesArea.setText(
											"Error Messages.\nHigh Price field cannot be empty or invalid input\nPlease try again!");
								} else {
									if (highPriceField.getText().isEmpty()) {
										highPrice = 0.0;
									} else {
										highPrice = Double.parseDouble(highPriceField.getText().trim());
									}
									if (lowPrice > highPrice && highPrice != 0.0) {
										symbolField.setText("");
										keywordsField.setText("");
										lowPriceField.setText("");
										highPriceField.setText("");
										messagesArea.setText(
												"Error Messages.\nHigh Price field cannot be less than Low Price field\nPlease try again!");
									} // this
								} // looks
							} // ridiculous
						} // but
					} // it
				} // works
			} else {
				System.out.println("Error in SearchButtonListener in getting symbol...");
			} // end nested if block
			searchInvestment(symbol, keywords, lowPrice, highPrice);
		}// end method
	}// end Search method

	/**
	 * goes through the arrayList to see if there are any investments matching
	 * search
	 * 
	 * @param symbol    is used to search if there is any investment which has the
	 *                  same symbol
	 * @param keywords  represents keywords that would be used to see if there is
	 *                  investments which correspond to them
	 * @param lowPrice  represents the lower bound of price in which investments
	 *                  should be
	 * @param highPrice represents the upper bound of price in which investments
	 *                  should be
	 */
	public void searchInvestment(String symbol, String keywords, double lowPrice, double highPrice) {
		messagesArea.setText("");
		int rangeSwitch = 0; // 0 - not empty and 1 - is empty

		String[] tokens;
		if (lowPrice == 0.0 && highPrice == 0.0) {
			rangeSwitch = 1;
		}

		// first case when all fields are empty
		if ("".equals(symbol) && "".equals(keywords) && lowPrice == 0.0 && highPrice == 0.0) {
			printAllToArea();
		}
		// 2nd case where only symbol is filled in
		if (symbol.length() != 0 && "".equals(keywords) && rangeSwitch == 1) {
			int a = 0;
			for (int i = 0; i <= this.investment.size() - 1; i++) {
				if (this.investment.get(i).getSymbol().equals(symbol)) {
					printInvestmentToArea(i);
					a++;
				}
			}
			if (a == 0) {
				messagesArea.setText("No investments corresponding to the search criteria");
			}
		}
		// 3rd case, keywords filled in
		if ("".equals(symbol) && keywords.length() != 0 && rangeSwitch == 1) {
			int b = 0;
			int k = 0;
			int j = 0;
			StringTokenizer words = new StringTokenizer(keywords);
			tokens = new String[words.countTokens()];
			while (words.hasMoreTokens()) {
				tokens[k++] = words.nextToken();
			}
			for (int i = 0; i <= this.investment.size() - 1; i++) {
				boolean stillMatch = true;
				for (j = 0; j < k; j++) {
					if (stillMatch == true) {
						int matching = this.investment.get(i).getName().toLowerCase().indexOf(tokens[j].toLowerCase());
						if (matching == -1) {
							stillMatch = false;
						}
					}
				}
				if (stillMatch == true) {
					printInvestmentToArea(i);
					b++;
				} else {
					System.out.println("This current investment does not match");
				}
			}
			if (b == 0) {
				messagesArea.setText("No investments corresponding to the search criteria");
			}

		}
		// 4th case, price is filled in
		if ("".equals(symbol) && keywords.length() == 0 && rangeSwitch == 0) {
			// low price and high price
			int c = 0;
			if (highPrice == 0.0) { // price = lowPrice
				for (int i = 0; i <= this.investment.size() - 1; i++) {
					double tempPrice = this.investment.get(i).getPrice();
					if (tempPrice == lowPrice) {
						printInvestmentToArea(i);
						c++;
					}
				}
				if (c == 0) {
					messagesArea.setText("No investments corresponding to the search criteria");
				}
			} else { // lowPrice <price < highPrice
				for (int i = 0; i <= this.investment.size() - 1; i++) {
					double tempPrice = this.investment.get(i).getPrice();
					if (tempPrice >= lowPrice && tempPrice <= highPrice) {
						printInvestmentToArea(i);
						c++;
					}
				}
				if (c == 0) {
					messagesArea.setText("No investments corresponding to the search criteria");
				}
			}
		}
		// 5th, symbol and keywords are filled in
		if (symbol.length() != 0 && keywords.length() != 0 && rangeSwitch == 1) {
			int d = 0;
			int k = 0;
			int j = 0;
			StringTokenizer words = new StringTokenizer(keywords);
			tokens = new String[words.countTokens()];
			while (words.hasMoreTokens()) {
				tokens[k++] = words.nextToken();
			}

			for (int i = 0; i <= this.investment.size() - 1; i++) {
				boolean stillMatch = true;
				for (j = 0; j < k; j++) {
					if (stillMatch == true) {
						int matching = this.investment.get(i).getName().toLowerCase().indexOf(tokens[j].toLowerCase());
						if (matching == -1) {
							stillMatch = false;
						}
					}
				}
				if (stillMatch == true) {
					if (this.investment.get(i).getSymbol().equals(symbol)) {
						printInvestmentToArea(i);
						d++;
					} else {
						System.out.println("This current investment does not match");
					}
				}
			}
			if (d == 0) {
				messagesArea.setText("No investments corresponding to the search criteria");
			}

		}
		// 6th, symbol and range are filled in
		if (symbol.length() != 0 && keywords.length() == 0 && rangeSwitch == 0) {
			int e = 0;
			for (int i = 0; i <= this.investment.size() - 1; i++) {
				if (this.investment.get(i).getSymbol().equals(symbol)) {
					if (highPrice == 0.0) { // price = lowPrice
						double tempPrice = this.investment.get(i).getPrice();
						if (tempPrice == lowPrice) {
							printInvestmentToArea(i);
							e++;
						}
					} else {
						double tempPrice = this.investment.get(i).getPrice();
						if (tempPrice >= lowPrice && tempPrice <= highPrice) {
							printInvestmentToArea(i);
							e++;
						}
					}
				}
			}
			if (e == 0) {
				messagesArea.setText("No investments corresponding to the search criteria");
			}
		}
		// 7th, keywords and range are filled in
		if (symbol.length() == 0 && keywords.length() != 0 && rangeSwitch == 0) {
			int f = 0;
			int k = 0;
			int j = 0;
			if (highPrice == 0.0) { // price = lowPrice
				for (int i = 0; i <= this.investment.size() - 1; i++) {
					double tempPrice = this.investment.get(i).getPrice();
					if (tempPrice == lowPrice) {
						StringTokenizer words = new StringTokenizer(keywords);
						tokens = new String[words.countTokens()];
						while (words.hasMoreTokens()) {
							tokens[k++] = words.nextToken();
						}

						boolean stillMatch = true;
						for (j = 0; j < k; j++) {
							if (stillMatch == true) {
								int matching = this.investment.get(i).getName().toLowerCase()
										.indexOf(tokens[j].toLowerCase());
								if (matching == -1) {
									stillMatch = false;
								}
							}
						}
						if (stillMatch == true) {
							printInvestmentToArea(i);
							f++;
						} else {
							System.out.println("This current investment does not match");
						}
					}
				}
				if (f == 0) {
					messagesArea.setText("No investments corresponding to the search criteria");
				}
			} else { // lowPrice <price < highPrice
				for (int i = 0; i <= this.investment.size() - 1; i++) {
					double tempPrice = this.investment.get(i).getPrice();
					if (tempPrice >= lowPrice && tempPrice <= highPrice) {
						StringTokenizer words = new StringTokenizer(keywords);
						tokens = new String[words.countTokens()];
						while (words.hasMoreTokens()) {
							tokens[k++] = words.nextToken();
						}

						boolean stillMatch = true;
						for (j = 0; j < k; j++) {
							if (stillMatch == true) {
								// System.out.println("still match true");
								int matching = this.investment.get(i).getName().toLowerCase()
										.indexOf(tokens[j].toLowerCase());
								if (matching == -1) {
									stillMatch = false;
								}
							}
						}
						if (stillMatch == true) {
							printInvestmentToArea(i);
							f++;
						} else {
							System.out.println("This current investment does not match");
						}
					}
				}
				if (f == 0) {
					messagesArea.setText("No investments corresponding to the search criteria");
				}
			}

		}
		// 8th, all filled in
		if (symbol.length() != 0 && keywords.length() != 0 && rangeSwitch == 0) {
			int g = 0;
			int k = 0;
			int j = 0;
			if (highPrice == 0.0) { // price = lowPrice
				for (int i = 0; i <= this.investment.size() - 1; i++) {
					double tempPrice = this.investment.get(i).getPrice();
					if (tempPrice == lowPrice) {
						StringTokenizer words = new StringTokenizer(keywords);
						tokens = new String[words.countTokens()];
						while (words.hasMoreTokens()) {
							tokens[k++] = words.nextToken();
						}

						boolean stillMatch = true;
						for (j = 0; j < k; j++) {
							if (stillMatch == true) {
								int matching = this.investment.get(i).getName().toLowerCase()
										.indexOf(tokens[j].toLowerCase());
								if (matching == -1) {
									stillMatch = false;
								}
							}
						}
						if (stillMatch == true) {
							if (this.investment.get(i).getSymbol().equals(symbol)) {
								printInvestmentToArea(i);
								g++;
							} else {
								System.out.println("This current investment does not match");
							}
						} else {
							System.out.println("This current investment does not match");
						}
					}
				}
				if (g == 0) {
					messagesArea.setText("No investments corresponding to the search criteria");
				}
			} else { // lowPrice <price < highPrice
				for (int i = 0; i <= this.investment.size() - 1; i++) {
					double tempPrice = this.investment.get(i).getPrice();
					if (tempPrice >= lowPrice && tempPrice <= highPrice) {
						StringTokenizer words = new StringTokenizer(keywords);
						tokens = new String[words.countTokens()];
						while (words.hasMoreTokens()) {
							tokens[k++] = words.nextToken();
						}

						boolean stillMatch = true;
						for (j = 0; j < k; j++) {
							if (stillMatch == true) {
								int matching = this.investment.get(i).getName().toLowerCase()
										.indexOf(tokens[j].toLowerCase());
								if (matching == -1) {
									stillMatch = false;
								}
							}
						}
						if (stillMatch == true) {
							if (this.investment.get(i).getSymbol().equals(symbol)) {
								printInvestmentToArea(i);
								g++;
							} else {
								System.out.println("This current investment does not match");
							}

						} else {
							System.out.println("This current investment does not match");
						}
					}
				}
				if (g == 0) {
					messagesArea.setText("No investments corresponding to the search criteria");
				}
			}
		}
	}// end search block from hell

	/**
	 * displays all investments to the textArea
	 */
	public void printAllToArea() {
		for (int i = 0; i <= this.investment.size() - 1; i++) {
			messagesArea.append(this.investment.get(i).toString() + "\n");
		}
	}

	/**
	 * displays selected investment to the text area
	 * 
	 * @param i represents the location of the investment to be retrieved in the
	 *          arrayList
	 */
	public void printInvestmentToArea(int i) {
		messagesArea.append(this.investment.get(i).toString() + "\n");
	}

	/**
	 * String validation, yay!
	 * 
	 * @param input  string that verify
	 * @param format case we want to branch on
	 * @return true if good...false if bad mmmmkkkaaaayyyy...
	 */
	public boolean validateFormat(String input, int format) {
		switch (format) {
		case SF_NONEMPTY:
			return (!input.isEmpty());
		case SF_ISINT:
			if (input.isEmpty()) {
				return false;
			} else {
				try {
					int temp = Integer.parseInt(input.trim());
					if (temp >= 0) {
						return true;
					} else {
						return false;
					}
				} catch (NumberFormatException e) {
					System.out.println("Uh oh integer?");
					return false;
				}
			}
		case SF_ISDOUBLE:
			if (input.isEmpty()) {
				return false;
			} else {
				try {
					double temp = Double.parseDouble(input.trim());
					if (temp >= 0.0) {
						return true;
					} else {
						return false;
					}
				} catch (NumberFormatException e) {
					System.out.println("Uh oh double?");
					return false;
				}
			}
		case SF_ISDOUBLE2:
			if (input.isEmpty()) {
				return true;
			} else {
				try {
					double temp = Double.parseDouble(input.trim());
					if (temp >= 0.0) {
						return true;
					} else {
						return false;
					}
				} catch (NumberFormatException e) {
					System.out.println("Uh oh double?");
					return false;
				}
			}
		default:
			System.out.print("Something is wrong!");
			return false;
		}
	}

	/**
	 * interface for buying
	 */
	public void buyGui() {
		buyPanel = new JPanel();
		// buyPanel.setBackground(Color.red); // tried to add color color in different
		// areas but looks tacky...
		commandsPanel.setVisible(false);
		sellPanel.setVisible(false);
		updatePanel.setVisible(false);
		getGainPanel.setVisible(false);
		searchPanel.setVisible(false);
		bigPanel.add(buyPanel, BorderLayout.CENTER);

		buyPanel.setLayout(new BorderLayout());
		JLabel buyInvest = new JLabel("Buying an investment");
		buyPanel.add(buyInvest, BorderLayout.NORTH);

		String[] labels = { "Symbol ", "Name ", "Quantity ", "Price " };
		int numPairs = labels.length;

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;

		JLabel type = new JLabel("Type ");
		infoPanel.add(type, gbc);
		gbc.gridy++;

		for (int i = 0; i < numPairs; i++) {
			JLabel label = new JLabel(labels[i]);
			infoPanel.add(label, gbc);
			gbc.gridy++;
		}

		gbc.gridx++;
		gbc.gridy = 0;
		investmentsType = new JComboBox<>();
		investmentsType.addItem("Stock");
		investmentsType.addItem("Mutual Funds");
		investmentsType.addActionListener(new TypeListener());
		infoPanel.add(investmentsType, gbc);
		gbc.gridy++;

		symbolField = new JTextField(20);
		infoPanel.add(symbolField, gbc);
		gbc.gridy++;
		nameField = new JTextField(20);
		infoPanel.add(nameField, gbc);
		gbc.gridy++;
		quantityField = new JTextField(20);
		infoPanel.add(quantityField, gbc);
		gbc.gridy++;
		priceField = new JTextField(20);
		infoPanel.add(priceField, gbc);
		gbc.gridy++;

		buyPanel.add(infoPanel, BorderLayout.CENTER);

		JPanel outerPanel = new JPanel();
		JPanel innerPanel = new JPanel();
		// outerPanel.setPreferredSize(new Dimension(160,160));
		outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.LINE_AXIS));
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.PAGE_AXIS));

		outerPanel.add(Box.createHorizontalStrut(20));
		outerPanel.add(innerPanel);
		outerPanel.add(Box.createHorizontalStrut(20));

		innerPanel.add(Box.createVerticalStrut(20));
		JButton resetButton = new JButton("Reset");
		Dimension d = new Dimension(75, 20);
		resetButton.setSize(d);
		resetButton.setMinimumSize(d);
		resetButton.setMaximumSize(d);
		resetButton.setPreferredSize(d);
		resetButton.addActionListener(new ResetBListener());
		JButton buyButton = new JButton("Buy");
		buyButton.setSize(d);
		buyButton.setMinimumSize(d);
		buyButton.setMaximumSize(d);
		buyButton.setPreferredSize(d);
		buyButton.addActionListener(new BuyButtonListener());
		innerPanel.add(resetButton);
		innerPanel.add(Box.createVerticalStrut(40));
		innerPanel.add(buyButton);
		buyPanel.add(outerPanel, BorderLayout.EAST);
		scrollDisplay(1);
	}

	/**
	 * interface for selling
	 */
	public void sellGui() {
		sellPanel = new JPanel();
		commandsPanel.setVisible(false);
		buyPanel.setVisible(false);
		updatePanel.setVisible(false);
		getGainPanel.setVisible(false);
		searchPanel.setVisible(false);
		bigPanel.add(sellPanel, BorderLayout.CENTER);

		sellPanel.setLayout(new BorderLayout());
		JLabel sellInvest = new JLabel("Selling an investment");
		sellPanel.add(sellInvest, BorderLayout.NORTH);

		String[] labels = { "Symbol ", "Quantity ", "Price " };
		int numPairs = labels.length;

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;

		for (int i = 0; i < numPairs; i++) {
			JLabel label = new JLabel(labels[i]);
			infoPanel.add(label, gbc);
			gbc.gridy++;
		}

		gbc.gridx++;
		gbc.gridy = 0;

		symbolField = new JTextField(20);
		infoPanel.add(symbolField, gbc);
		gbc.gridy++;
		quantityField = new JTextField(20);
		infoPanel.add(quantityField, gbc);
		gbc.gridy++;
		priceField = new JTextField(20);
		infoPanel.add(priceField, gbc);
		gbc.gridy++;

		sellPanel.add(infoPanel, BorderLayout.CENTER);

		JPanel outerPanel = new JPanel();
		JPanel innerPanel = new JPanel();
		// outerPanel.setPreferredSize(new Dimension(160,160));
		outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.LINE_AXIS));
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.PAGE_AXIS));

		outerPanel.add(Box.createHorizontalStrut(20));
		outerPanel.add(innerPanel);
		outerPanel.add(Box.createHorizontalStrut(20));

		innerPanel.add(Box.createVerticalStrut(20));
		JButton resetButton = new JButton("Reset");
		Dimension d = new Dimension(75, 20);
		resetButton.setSize(d);
		resetButton.setMinimumSize(d);
		resetButton.setMaximumSize(d);
		resetButton.setPreferredSize(d);
		resetButton.addActionListener(new ResetSListener());
		JButton sellButton = new JButton("Sell");
		sellButton.setSize(d);
		sellButton.setMinimumSize(d);
		sellButton.setMaximumSize(d);
		sellButton.setPreferredSize(d);
		sellButton.addActionListener(new SellButtonListener());
		innerPanel.add(resetButton);
		innerPanel.add(Box.createVerticalStrut(40));
		innerPanel.add(sellButton);
		sellPanel.add(outerPanel, BorderLayout.EAST);
		// FIXME: need to set up something to scroll...

		scrollDisplay(2);

	}

	/**
	 * scrolled display
	 * 
	 * @param choice scrolled text area for buying, selling, updating or searching
	 */
	public void scrollDisplay(int choice) {
		JPanel messagesPanel = new JPanel();
		JPanel messagesBox = new JPanel();
		messagesPanel.setBackground(Color.LIGHT_GRAY);
		messagesPanel.setLayout(new BorderLayout());
		messagesBox.setLayout(new BorderLayout());
		// messagesPanel.setPreferredSize(new Dimension(75,75));
		JLabel messagesLabel = new JLabel("Messages");
		messagesPanel.add(messagesLabel, BorderLayout.NORTH);
		messagesPanel.add(messagesBox, BorderLayout.SOUTH);

		messagesArea = new JTextArea(15, 25);
		messagesArea.setEditable(false);
		messagesArea.setBackground(Color.WHITE);
		messagesBox.setPreferredSize(new Dimension(175, 175));
		scrolledMessages = new JScrollPane(messagesArea);
		scrolledMessages.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrolledMessages.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrolledMessages.setPreferredSize(new Dimension(175, 175));
		messagesBox.add(scrolledMessages, BorderLayout.SOUTH);
		if (choice == 1) {
			buyPanel.add(messagesPanel, BorderLayout.SOUTH);
		} else if (choice == 2) {
			sellPanel.add(messagesPanel, BorderLayout.SOUTH);
		} else if (choice == 3) {
			updatePanel.add(messagesPanel, BorderLayout.SOUTH);
		} else if (choice == 4) {
			JLabel newLabel = new JLabel("Search results");
			messagesPanel.add(newLabel, BorderLayout.NORTH);
			searchPanel.add(messagesPanel, BorderLayout.SOUTH);
		}
	}// end scroll option

	/**
	 * update interface
	 */
	public void updateGui() {
		this.type = null;
		updatePanel = new JPanel();
		commandsPanel.setVisible(false);
		buyPanel.setVisible(false);
		sellPanel.setVisible(false);
		getGainPanel.setVisible(false);
		searchPanel.setVisible(false);
		bigPanel.add(updatePanel, BorderLayout.CENTER);

		updatePanel.setLayout(new BorderLayout());
		JLabel updateInvest = new JLabel("Updating investments");
		updatePanel.add(updateInvest, BorderLayout.NORTH);

		String[] labels = { "Symbol ", "Name ", "Price " };
		int numPairs = labels.length;

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;

		for (int i = 0; i < numPairs; i++) {
			JLabel label = new JLabel(labels[i]);
			infoPanel.add(label, gbc);
			gbc.gridy++;
		}

		gbc.gridx++;
		gbc.gridy = 0;

		symbolField = new JTextField(20);
		symbolField.setEditable(false);
		infoPanel.add(symbolField, gbc);
		gbc.gridy++;
		nameField = new JTextField(20);
		nameField.setEditable(false);
		infoPanel.add(nameField, gbc);
		gbc.gridy++;
		priceField = new JTextField(20);
		infoPanel.add(priceField, gbc);
		gbc.gridy++;
		updatePanel.add(infoPanel, BorderLayout.CENTER);

		JPanel outerPanel = new JPanel();
		JPanel innerPanel = new JPanel();
		// outerPanel.setPreferredSize(new Dimension(160,160));
		outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.LINE_AXIS));
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.PAGE_AXIS));

		outerPanel.add(Box.createHorizontalStrut(10));
		outerPanel.add(innerPanel);
		outerPanel.add(Box.createHorizontalStrut(10));

		innerPanel.add(Box.createVerticalStrut(10));
		prevButton = new JButton("Prev");
		Dimension d = new Dimension(75, 20);
		prevButton.setSize(d);
		prevButton.setMinimumSize(d);
		prevButton.setMaximumSize(d);
		prevButton.setPreferredSize(d);
		prevButton.addActionListener(new PrevButtonListener());
		nextButton = new JButton("Next");
		nextButton.setSize(d);
		nextButton.setMinimumSize(d);
		nextButton.setMaximumSize(d);
		nextButton.setPreferredSize(d);
		nextButton.addActionListener(new NextButtonListener());
		JButton saveButton = new JButton("Save");
		saveButton.setSize(d);
		saveButton.setMinimumSize(d);
		saveButton.setMaximumSize(d);
		saveButton.setPreferredSize(d);
		saveButton.addActionListener(new SaveButtonListener());
		innerPanel.add(prevButton);
		innerPanel.add(Box.createVerticalStrut(20));
		innerPanel.add(nextButton);
		innerPanel.add(Box.createVerticalStrut(20));
		innerPanel.add(saveButton);
		updatePanel.add(outerPanel, BorderLayout.EAST);

		scrollDisplay(3);
		count = 0;
		if (this.investment.isEmpty()) {
			messagesArea.setText("MESSAGE: The Portfolio is currently empty.\nYou won't be able to search!");
			prevButton.setEnabled(false);
			nextButton.setEnabled(false);
			saveButton.setEnabled(false);
		} else {
			symbolField.setText(this.investment.get(0).getSymbol());
			nameField.setText(this.investment.get(0).getName());
			prevButton.setEnabled(false);
			if (this.investment.size() == 1) {
				nextButton.setEnabled(false);
			}
		}
	}

	/**
	 * search interface
	 */
	public void searchGui() {
		searchPanel = new JPanel();
		commandsPanel.setVisible(false);
		buyPanel.setVisible(false);
		sellPanel.setVisible(false);
		getGainPanel.setVisible(false);
		updatePanel.setVisible(false);
		bigPanel.add(searchPanel, BorderLayout.CENTER);

		searchPanel.setLayout(new BorderLayout());
		JLabel searchLabel = new JLabel("Searching investments");
		searchPanel.add(searchLabel, BorderLayout.NORTH);

		String[] labels = { "Symbol ", "Name keywords ", "Low Price ", "High Price " };
		int numPairs = labels.length;

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;

		for (int i = 0; i < numPairs; i++) {
			JLabel label = new JLabel(labels[i]);
			infoPanel.add(label, gbc);
			gbc.gridy++;
		}

		gbc.gridx++;
		gbc.gridy = 0;
		symbolField = new JTextField(20);
		infoPanel.add(symbolField, gbc);
		gbc.gridy++;
		keywordsField = new JTextField(20);
		infoPanel.add(keywordsField, gbc);
		gbc.gridy++;
		lowPriceField = new JTextField(20);
		infoPanel.add(lowPriceField, gbc);
		gbc.gridy++;
		highPriceField = new JTextField(20);
		infoPanel.add(highPriceField, gbc);
		gbc.gridy++;
		searchPanel.add(infoPanel, BorderLayout.CENTER);

		JPanel outerPanel = new JPanel();
		JPanel innerPanel = new JPanel();
		// outerPanel.setPreferredSize(new Dimension(160,160));
		outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.LINE_AXIS));
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.PAGE_AXIS));

		outerPanel.add(Box.createHorizontalStrut(20));
		outerPanel.add(innerPanel);
		outerPanel.add(Box.createHorizontalStrut(20));

		innerPanel.add(Box.createVerticalStrut(20));
		JButton resetButton = new JButton("Reset");
		Dimension d = new Dimension(75, 20);
		resetButton.setSize(d);
		resetButton.setMinimumSize(d);
		resetButton.setMaximumSize(d);
		resetButton.setPreferredSize(d);
		resetButton.addActionListener(new ResetSearchListener());
		JButton searchButton = new JButton("Search");
		searchButton.setSize(d);
		searchButton.setMinimumSize(d);
		searchButton.setMaximumSize(d);
		searchButton.setPreferredSize(d);
		searchButton.addActionListener(new SearchButtonListener());
		innerPanel.add(resetButton);
		innerPanel.add(Box.createVerticalStrut(40));
		innerPanel.add(searchButton);
		searchPanel.add(outerPanel, BorderLayout.EAST);

		scrollDisplay(4);

		if (this.investment.isEmpty()) {
			searchButton.setEnabled(false);
			messagesArea.setText("MESSAGE: The portfolio is empty and thus you cannot search through the portfolio");
		}
	}

	/**
	 * total gains interface
	 * 
	 * FIXME: Gains aren't working now, but I'm not sure if they ever did
	 */
	public void GainGui() {
		getGainPanel = new JPanel();
		commandsPanel.setVisible(false);
		buyPanel.setVisible(false);
		sellPanel.setVisible(false);
		updatePanel.setVisible(false);
		searchPanel.setVisible(false);
		bigPanel.add(getGainPanel, BorderLayout.CENTER);

		getGainPanel.setLayout(new BorderLayout());
		JLabel gainLabel = new JLabel("Getting total gain");
		getGainPanel.add(gainLabel, BorderLayout.NORTH);

		String[] labels = { "Total gain " };
		int numPairs = labels.length;

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;

		for (int i = 0; i < numPairs; i++) {
			JLabel label = new JLabel(labels[i]);
			infoPanel.add(label, gbc);
			gbc.gridy++;
		}

		gbc.gridx++;
		gbc.gridy = 0;
		getGainField = new JTextField(20);
		getGainField.setEditable(false);
		infoPanel.add(getGainField, gbc);
		gbc.gridy++;

		getGainPanel.add(infoPanel, BorderLayout.CENTER);

		JPanel messagesPanel = new JPanel();
		JPanel messagesBox = new JPanel();
		messagesPanel.setBackground(Color.LIGHT_GRAY);
		messagesPanel.setLayout(new BorderLayout());
		messagesBox.setLayout(new BorderLayout());
		// messagesPanel.setPreferredSize(new Dimension(75,75));
		JLabel messagesLabel = new JLabel("Individual gains");
		messagesPanel.add(messagesLabel, BorderLayout.NORTH);
		messagesPanel.add(messagesBox, BorderLayout.SOUTH);

		messagesArea = new JTextArea(400, 400);
		messagesArea.setEditable(false);
		messagesArea.setBackground(Color.WHITE);
		messagesBox.setPreferredSize(new Dimension(250, 250));
		messagesArea.setLineWrap(true);
		messagesArea.setWrapStyleWord(true);
		scrolledMessages = new JScrollPane(messagesArea);
		scrolledMessages.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrolledMessages.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrolledMessages.setPreferredSize(new Dimension(250, 250));
		messagesBox.add(scrolledMessages, BorderLayout.SOUTH);

		getGainPanel.add(messagesPanel, BorderLayout.SOUTH);

		double sumOfStockGain = 0.0;
		double sumOfFundsGain = 0.0;
		double totalGain = 0.0;

		for (int i = 0; i <= this.investment.size() - 1; i++) {
			int tempQty = this.investment.get(i).getQuantity();
			double tempPrice = this.investment.get(i).getPrice();
			double tempBkValue = this.investment.get(i).getBookValue();

			if (this.investment.get(i).getClass().getCanonicalName().equals("assignment2.Stock")) {
				Stock s = new Stock();
				double tempGain = s.computeStockGain(tempQty, tempPrice, tempBkValue);
				messagesArea.append(this.investment.get(i).toString() + "Gain = " + tempGain + "\n\n");
				sumOfStockGain = sumOfStockGain + tempGain;
			}
			if (this.investment.get(i).getClass().getCanonicalName().equals("assignment2.MutualFunds")) {
				MutualFunds m = new MutualFunds();
				double tempGain = m.computeFundsGain(tempQty, tempPrice, tempBkValue);
				messagesArea.append(this.investment.get(i).toString() + "Gain = " + tempGain + "\n\n");
				sumOfFundsGain = sumOfFundsGain + tempGain;
			}
		}
		totalGain = sumOfStockGain + sumOfFundsGain;
		String total = Double.toString(totalGain);
		getGainField.setText(total);

	}

	/**
	 * init JFrame and to set up welcome window implement menu bar to switch screens
	 * 
	 * FIXME: doesn't start in the center of the screen anymore...not sure what I
	 * did
	 * 
	 */
	public Portfolio() {
		super("Investment Portfolio");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		JMenu commandsMenu = new JMenu("Commands");

		JMenuItem buy = new JMenuItem("Buy");
		buy.addActionListener(new BuyListener());
		commandsMenu.add(buy);

		JMenuItem sell = new JMenuItem("Sell");
		sell.addActionListener(new SellListener());
		commandsMenu.add(sell);

		JMenuItem update = new JMenuItem("Update");
		update.addActionListener(new UpdateListener());
		commandsMenu.add(update);

		JMenuItem getGain = new JMenuItem("GetGain");
		getGain.addActionListener(new GetGainListener());
		commandsMenu.add(getGain);

		JMenuItem search = new JMenuItem("Search");
		search.addActionListener(new SearchListener());
		commandsMenu.add(search);

		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener(new QuitListener());
		commandsMenu.add(quit);

		JMenuBar commandsBar = new JMenuBar();
		commandsBar.add(commandsMenu);
		setJMenuBar(commandsBar);

		bigPanel = new JPanel();
		bigPanel.setLayout(new BorderLayout());
		commandsPanel = new JPanel();
		buyPanel = new JPanel();
		sellPanel = new JPanel();
		updatePanel = new JPanel();
		getGainPanel = new JPanel();
		searchPanel = new JPanel();
		commandsPanel.setLayout(new GridLayout(2, 1));
		JLabel welcomeLabel = new JLabel("Welcome to the Investment Portfolio");
		commandsPanel.setBackground(Color.white);
		commandsPanel.add(welcomeLabel);

		JTextArea welcomeArea = new JTextArea(10, 10);
		welcomeArea.setEditable(false);
		welcomeArea.setLineWrap(true);
		welcomeArea.setText(
				"Choose a command from the \"Commands\" menu to buy or sell an investment, \nupdate prices, get gain for the portfolio, "
						+ "search for relevant investment, or quit the program");
		commandsPanel.add(welcomeArea);
		bigPanel.add(commandsPanel, BorderLayout.CENTER);
		add(bigPanel, BorderLayout.CENTER);

		// all related invesments...
		this.investment = new ArrayList<>();
	}

	/**
	 * accessor to the arrayList investment
	 *
	 * @return the content of investment
	 */
	public ArrayList<Investment> getInvestment() {
		return this.investment;
	}

	/**
	 * writes investments to a file
	 * 
	 * FIXME: not writing to file....
	 *
	 * @param tempFilename
	 * @return denotes whether the investments have been saved to file
	 *         investments.txt
	 */
	public String saveInvestments(String tempFilename) {

		BufferedWriter writer;

		System.out.println("Quiting and saving the contents in a file. . .");
		if (tempFilename.length() == 0) {
			tempFilename = "Investments.txt";
		}
		try {// exception handling
			writer = new BufferedWriter(new FileWriter(tempFilename));
			for (int i = 0; i < this.investment.size(); i++) {
				if (this.investment.get(i).getClass().getCanonicalName().equals("assignment2.Stock")) {
					writer.write("type = stock\n");
					writer.newLine();
					writer.write("symbol = " + investment.get(i).getSymbol());
					writer.newLine();
					writer.write("name = " + investment.get(i).getName());
					writer.newLine();
					writer.write("quantity = " + investment.get(i).getQuantity());
					writer.newLine();
					writer.write("price = " + investment.get(i).getPrice());
					writer.newLine();
					writer.write("bookValue = " + investment.get(i).getBookValue());
					writer.newLine();
					writer.newLine();
				} else if (this.investment.get(i).getClass().getCanonicalName().equals("assignment2.MutualFunds")) {
					writer.write("type = mutualfund");
					writer.newLine();
					writer.write("symbol = " + investment.get(i).getSymbol());
					writer.newLine();
					writer.write("name = " + investment.get(i).getName());
					writer.newLine();
					writer.write("quantity = " + investment.get(i).getQuantity());
					writer.newLine();
					writer.write("price = " + investment.get(i).getPrice());
					writer.newLine();
					writer.write("bookValue = " + investment.get(i).getBookValue());
					writer.newLine();
					writer.newLine();
				}
			}
			writer.close();
		} catch (IOException e) {
			return "Failed to write to " + tempFilename;
		}
		return "Sucessfully wrote to the file. . . " + tempFilename;
	}

	/**
	 * Method loads the contents of a file and save it to the arrayList for all the
	 * investments
	 *
	 * @param filename which represents the file name which obtained from the
	 *                 command line
	 * @return a string which denotes whether the file has been successfully opened
	 *         or not
	 */
	public String loadInvestments(String filename) {
		BufferedReader reader;
		int counter = 0;
		int typeFile = 0;
		String symbolFile = "";
		String nameFile = "";
		String tempQuantity = "";
		String tempPrice = "";
		String tempBookValue = "";
		int quantityFile = 0;
		double priceFile = 0.0;
		double bookValue = 0.0;

		System.out.println("Loading the contents of the file. . .");

		try {// exception handling
			reader = new BufferedReader(new FileReader(filename));
			String line = "";
			while (line != null) {
				line = reader.readLine();
				if (line != null && line.length() != 0) {
					counter++;
					switch (counter) {
					case 1:
						typeFile = getTypeFromLine(line);
						break;
					case 2:
						symbolFile = getDataFromLine(line);
						// System.out.println("symbol is " + symbol);
						break;
					case 3:
						nameFile = getDataFromLine(line);
						// System.out.println("name is " + name);
						break;
					case 4:
						tempQuantity = getDataFromLine(line);
						// System.out.println("tempQuantity is " + tempQuantity);
						try {
							quantityFile = Integer.parseInt(tempQuantity);
						} catch (NumberFormatException n) {
							// System.out.println("LoadInvestment function problem with quantity");
						}
						break;
					case 5:
						tempPrice = getDataFromLine(line);
						// System.out.println("tempPrice is " + tempPrice);
						try {
							priceFile = Double.parseDouble(tempPrice);
						} catch (NumberFormatException n) {
							// System.out.println("LoadInvestment function problem with price");
						}
						break;
					case 6:
						tempBookValue = getDataFromLine(line);
						// System.out.println("tempBook is " + tempBookValue);
						try {
							bookValue = Double.parseDouble(tempBookValue);
						} catch (NumberFormatException n) {
							// System.out.println("LoadInvestment function problem with bookvalue");
						}

						if (typeFile == 1) { // means stock
							Investment in = getBook(symbolFile, nameFile, quantityFile, priceFile, bookValue);
							this.investment.add(in);
							// System.out.print(this.investment.get(0).toString());
							// System.out.println("OKKK");
						} else if (typeFile == 2) { // mutualfund
							MutualFunds tempFund = new MutualFunds(symbolFile, nameFile, quantityFile, priceFile,
									bookValue);
							this.investment.add(tempFund);
						} else {
							System.out.println("Error in getting the type 1 is for stock and 2 is for funds");
						}
						counter = 0;
						break;
					default:
						System.out.println("Not supposed to branch here while reading file");
						counter = 0;
						break;
					}
				}
			} // end while loop
			reader.close();
		} catch (IOException e) {
			return "Failed to read this file or file does not exist yet.";
		}
		return "Successfully read the file. . .";
	}

	/**
	 * Creates the stock object more work needed here...
	 *
	 * @param symbol    represents the symbol of the investment
	 * @param name      represents the name of the investment
	 * @param quantity  represents the quantity of the stock investment
	 * @param price     denotes the price at which the stocks were bought
	 * @param bookValue represents the boolValue of the stock investment
	 * @return invesmtent object which is actually a stock
	 */
	private Investment getBook(String symbol, String name, int quantity, double price, double bookValue) {
		return new Stock(symbol, name, quantity, price, bookValue);
	}

	/**
	 * Allows the user to know whether this investment is a stock or an mutual fund
	 *
	 * @param line which is read from the file
	 * @return an integer to denote whether it is a stock or a mutual fund; 1 for
	 *         stock and 2 for fund
	 */
	public int getTypeFromLine(String line) {
		String[] splitLine = line.split("=");
		// change to switch case here
		switch (splitLine[1].trim()) {
		case "stock":
			return 1;
		case "mutualfund":
			return 2;
		default:
			System.out
					.println("Error. This type of investment does not exist. Investment read: " + splitLine[1].trim());
		}
		return 0;
	}

	/**
	 * Allows the user to retrieve input data
	 *
	 * @param line which is read from the file
	 * @return a respective string if it is a symbol, name, quantity or price or
	 *         bookValue
	 */
	public String getDataFromLine(String line) {
		String[] splitLine = line.split("=");

		switch (splitLine[0].trim()) {
		case "symbol":
			String tempSymbol = splitLine[1].trim();
			return tempSymbol;
		case "name":
			String tempName = splitLine[1].trim();
			return tempName;
		case "quantity":
			String tempQuantity = splitLine[1].trim();
			return tempQuantity;
		case "price":
			String tempPrice = splitLine[1].trim();
			return tempPrice;
		case "bookValue":
			String bookValue = splitLine[1].trim();
			return bookValue;
		default:
			break;
		}
		return "There is an error somewhere in getDataFromLine Function";
	}

	

		








	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Portfolio gui = new Portfolio();
		gui.setVisible(true);
		filename = "Investments.txt";
		if (args.length != 0) {
			filename = args[0];
			System.out.println("the filename: " + filename + " and thre args: " + args[0]);
			gui.loadInvestments(filename);
		}
		/////////// ok...that was a bitch, but it's working...kinda ///////////

	}// end main method
}// end class (JFrame)



