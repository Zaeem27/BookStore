package data;
/**
 * Data storage class for a Visit event row
 * 
 * @author Gagen
 *
 */
public class VisitItem {
	private String day;
	private String bid;
	private String eventtype;
		
	public VisitItem(String day, String bid, String eventtype){
		this.bid = bid;
		this.day = day;
		this.eventtype = eventtype;
	}
	
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getEventtype() {
		return eventtype;
	}
	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
	}
}
