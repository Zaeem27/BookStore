package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import data.Book;
import data.VisitItem;;

/**
 * The backing bean for the analytics page
 * we store the stats in the db
 * 
 * @author Gagen
 *
 */
public class AnalyticBean {
	private DataSource ds;
	private final ArrayList<VisitItem> stats = new ArrayList<VisitItem>();
	private final HashMap<String, Integer> monthlySales = new HashMap<String, Integer>();
	private Boolean general;
	private Boolean monthly;
	
	public AnalyticBean(){
		updateStats();
		updateMonthlySales();
	}
	/*
	 * Fill the hashMap of the sales with 
	 * the data from the db 
	 */
	private void updateMonthlySales() {
		monthlySales.clear();
		for (VisitItem x : stats) {
			if ("PURCHASE".equals(x.getEventtype())) {
				String month = x.getDay().substring(0, 2);
				if (monthlySales.containsKey(month)) {
					monthlySales.replace(month, monthlySales.get(month)+1);
				}else {
					monthlySales.put(month, 1);
				}
			}
		}
	}
	
	/*
	 * Fill the stats List with the
	 * visit items from the visit table
	 */
	private void updateStats() {
		stats.clear();
		try {
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/Db2-4413");
			String query = "select * from VisitEvent";
			
			Connection con = ds.getConnection();
			PreparedStatement p = con.prepareStatement(query);
			ResultSet r = p.executeQuery();
			
			while(r.next()) {
				String bid = r.getString("bid");
				String date = r.getString("day");
				String event = r.getString("eventtype");
				VisitItem i = new VisitItem(date, bid, event);
				stats.add(i);
			}
			
			r.close();
			p.close();
			con.close();
		}catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getDate() {
		Calendar d = Calendar.getInstance();
		String date = "";
		if((d.get(Calendar.MONTH)+1)<=9) {
			date += "0"+(d.get(Calendar.MONTH)+1);
		}else {
			date += d.get(Calendar.MONTH);
		}
		if(d.get(Calendar.DATE)<=9) {
			date += "0"+d.get(Calendar.DATE);
		}else {
			date += d.get(Calendar.DATE);
		}
		date += d.get(Calendar.YEAR);
		return date;
	}
	
	//record a view
	public void viewEvent(String bid) {
		String date = getDate();
		try {
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/Db2-4413");
			String query = String.format("INSERT INTO VisitEvent (day, bid, eventtype) VALUES ('%s', '%s', 'VIEW')",
					date,bid);
			Connection con = ds.getConnection();
			PreparedStatement p = con.prepareStatement(query);
			p.executeUpdate();
			
			p.close();
			con.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateStats();
	}
	
	//record a purchase
	public void purchaseEvent(String bid) {
		String date = getDate();
		try {
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/Db2-4413");
			String query = String.format("INSERT INTO VisitEvent (day, bid, eventtype) VALUES ('%s', '%s', 'PURCHASE')",
					date,bid);
			Connection con = ds.getConnection();
			PreparedStatement p = con.prepareStatement(query);
			p.execute();
			
			p.close();
			con.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateStats();
		updateMonthlySales();
	}
	
	//record a cart
	public void cartEvent(String bid) {
		String date = getDate();
		try {
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/Db2-4413");
			String query = String.format("INSERT INTO VisitEvent (day, bid, eventtype) VALUES ('%s', '%s', 'CART')",
					date,bid);
			Connection con = ds.getConnection();
			PreparedStatement p = con.prepareStatement(query);
			p.execute();
			
			p.close();
			con.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateStats();
	}
	//for running page logic
	public String showGeneral() {
		setGeneral(true);
		setMonthly(false);
		return null;
	}
	//for running page logic
	public String showMonthly() {
		setGeneral(false);
		setMonthly(true);
		return null;
	}
	//getters and setters
	public ArrayList<VisitItem> getStats() {
		return stats;
	}

	public HashMap<String, Integer> getMonthlySales() {
		return monthlySales;
	}

	public Boolean getGeneral() {
		return general;
	}

	public void setGeneral(Boolean general) {
		this.general = general;
	}

	public Boolean getMonthly() {
		return monthly;
	}

	public void setMonthly(Boolean monthly) {
		this.monthly = monthly;
	}
}
