package data;

/**
 * Data object to store and represent the information
 * of a single book.
 * 
 * @author Gagen
 */
public class Book {
	String bid;
	String title;
	int price;
	String category;
	
	public Book(String bid, String title, int price, String category) {
		this.bid = bid;
		this.title = title;
		this.price = price;
		this.category = category;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
}
