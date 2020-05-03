package model;

import java.io.Serializable;
import java.util.HashMap;

import data.Book;
/**
 * The backing bean for the cart
 * it contains the data for the cart
 * and changes it as needed
 * 
 * @author Gagen
 *
 */
public class CartBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//map of books and number in cart
	private final HashMap<Book, Integer> cart = new HashMap<Book, Integer>();
	private boolean emptycart = true;
	private int GrandTotal = 0;
	
	//for bean injection
	private LoginBean Logbean;
	public LoginBean getLogbean() {
		return Logbean;
	}
	public void setLogbean(LoginBean logbean) {
		Logbean = logbean;
	}
	
	
	public CartBean() {
		//empty 
	}
	//empty cart after a payment
	public void clearCart() {
		cart.clear();
		GrandTotal = 0;
		emptycart = cart.isEmpty();
	}
	//add a book
	public void additem(Book b) {
		if (cart.containsKey(b)) {
			cart.replace(b, cart.get(b)+1);
		}else {
			cart.put(b, 1);
		}
		GrandTotal += b.getPrice();
		emptycart = cart.isEmpty();
	}
	//remove a book
	public void removeItem(Book b) {
		if (cart.containsKey(b)) {
			if (cart.get(b) > 1) {
				cart.replace(b, cart.get(b)-1);
			}else {
				cart.remove(b);
			}
			GrandTotal -= b.getPrice();
		}
		emptycart = cart.isEmpty();
		
	}
	
	//delete a item
	public void deleteItem(Book b) {
		if(cart.containsKey(b)) {
			GrandTotal -= b.getPrice()*cart.get(b);
			cart.remove(b);
		}
		emptycart = cart.isEmpty();
	}
	
	//getters and setters
	public HashMap<Book, Integer> getCart() {
		return cart;
	}
	public boolean isEmptycart() {
		return emptycart;
	}
	public int getGrandTotal() {
		return GrandTotal;
	}
	public void setGrandTotal(int grandTotal) {
		GrandTotal = grandTotal;
	}
	
	/*
	 * see if person is logged in
	 * before going to payment page
	 */
	public String checkOut() {
		if (Logbean.getLoggedin()) {
			return "paymentPage";
		} else {
			return "loginPage";
		}
	}
}
