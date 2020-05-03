package model;

/**
 * The backing bean for the store front
 * it gets our books and lets us view and 
 * add them to our cart.
 * 
 * @author Gagen
 */

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import data.Book;

public class StoreBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private final ArrayList<Book> bookList = new ArrayList<Book>();
	//search vars
	private final ArrayList<Book> searchList = new ArrayList<Book>();
	private String searchitem;
	private Boolean searched = false;
	//other vars
	private DataSource ds;
	private CartBean cartbean;
	private AnalyticBean analysisbean;
	//for detailview
	private Book detailBook;
	
	//some getters and setters
	public void setCartbean(CartBean cartbean) {
		this.cartbean = cartbean;
	}

	public CartBean getCartbean() {
		return cartbean;
	}

	public AnalyticBean getAnalysisbean() {
		return analysisbean;
	}

	public void setAnalysisbean(AnalyticBean analysisbean) {
		this.analysisbean = analysisbean;
	}

	public StoreBean(){
		try {
			//lookup the books in the db and store them in a booklist.
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/Db2-4413");
			String query = "select * from Book";
			
			Connection con = ds.getConnection();
			PreparedStatement p = con.prepareStatement(query);
			ResultSet r = p.executeQuery();
			while (r.next()) {
				String bid = r.getString("bid");
				String title = r.getString("title");
				int price = Integer.parseInt(r.getString("price"));
				String cat = r.getString("category");
				Book b = new Book(bid,title,price,cat);
				bookList.add(b);
			}
			//make sure to release the db resources
			r.close();
			p.close();
			con.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//add the book to the cart
	public String addAction(Book x) {
		cartbean.additem(x);
		analysisbean.cartEvent(x.getBid());
		return "ShoppingCart";
	}
	
	//view the item in detail
	public String viewAction(Book x) {
		setDetailBook(x);
		analysisbean.viewEvent(x.getBid());
		return "DetailsPage";
	}
	
	//search for a item
	public String searchAction() {
		searched = true;
		searchList.clear();
		for (Book b : bookList) {
			if(b.getBid().equalsIgnoreCase(searchitem)) {
				searchList.add(b);
			}else if(b.getTitle().equalsIgnoreCase(searchitem)) {
				searchList.add(b);
			}else if(b.getBid().contains(searchitem.toLowerCase())) {
				searchList.add(b);
			}else if(b.getTitle().toLowerCase().contains(searchitem.toLowerCase())) {
				searchList.add(b);
			}
		}
		return null;
	}
	//clear the search
	public String clearSearch() {
		searched = false;
		searchitem = "";
		return null;
	}
	
	/*
	 * Getters and setters for
	 * the backing bean
	 */
	public ArrayList<Book> getBookList() {
		return bookList;
	}

	public String getSearchitem() {
		return searchitem;
	}

	public void setSearchitem(String searchitem) {
		this.searchitem = searchitem;
	}

	public boolean isSearched() {
		return searched;
	}

	public ArrayList<Book> getSearchList() {
		return searchList;
	}

	public Book getDetailBook() {
		return detailBook;
	}

	public void setDetailBook(Book detailBook) {
		this.detailBook = detailBook;
	}
	
	//image file name for books
	public String bookimage() {
		return detailBook.getBid()+".jpg";
	}
}
