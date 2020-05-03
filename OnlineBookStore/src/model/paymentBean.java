package model;
/**
 * The backing bean for the payment operation
 * It interacts with the CartBean and AnalyticBean
 * to create the needed db records for a payment
 * process
 * 
 *  @author Zaeem
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import data.Book;

public class paymentBean {
	
	private String cardNum;
	private String date;
	private String cvv;
	private String name;
	private String address;
	private String zip;
	private String province;
	private String country;
	private String phone;
	static int failureCounter;
	public String message;
	private DataSource ds;
	private CartBean cart;
	private AnalyticBean stats;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public CartBean getCart() {
		return cart;
	}
	public void setCart(CartBean cart) {
		this.cart = cart;
	}
	public AnalyticBean getStats() {
		return stats;
	}
	public void setStats(AnalyticBean stats) {
		this.stats = stats;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCvv() {
		return cvv;
	}
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/*
	 * Takes the inputs on the form 
	 * and creates a DB record based on the info.
	 * It also creates a POitem record for all the 
	 * items in the cart (if credit card is passed)
	 */
	public void pay() {
		failureCounter++;
		String fName = this.getName().split(" ")[0];
		String lName = this.getName().split(" ")[1];
		String query = "";
		Boolean pass = false;
		if (failureCounter % 3 == 0) {
			message = "Credit card authorization failed.";
			query = String.format("insert into PO (lname, fname, status, street, province, country, zip, phone) values ('%s', '%s', 'DENIED', '%s', '%s', '%s', '%s', '%s')"
					,lName,fName,getAddress(),getProvince(),getCountry(),getZip(),getPhone());
		}
		else {
			message = "Order Successfully Completed.";
			pass = true;
			query = String.format("insert into PO (lname, fname, status, street, province, country, zip, phone) values ('%s', '%s', 'ORDERED', '%s', '%s', '%s', '%s', '%s')"
					,lName,fName,getAddress(),getProvince(),getCountry(),getZip(),getPhone());
		}
		try {
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/Db2-4413");
			Connection con = ds.getConnection();
			PreparedStatement p = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			p.execute();
			ResultSet r = p.getGeneratedKeys();
			
			if(r.next() && pass) {
				String id = r.getString("id");
				System.out.println("here: "+id);
				HashMap<Book, Integer> books = cart.getCart();
				for (Book b : books.keySet()) {
					String queryitem = String.format("insert into POItem (id, bid, price) values ('%s','%s','%s')",id,b.getBid(),b.getPrice());
					try {
						p.execute(queryitem);
					}catch(Exception e) {
						//do nothing
					}
					stats.purchaseEvent(b.getBid());
				}
				cart.clearCart();
			}
			r.close();
			p.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			message = "Error: unable to record order.";
		}
	}
}
