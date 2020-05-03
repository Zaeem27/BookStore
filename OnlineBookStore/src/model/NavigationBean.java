package model;
/**
 * Simple backing bean to store page navigation
 * Allows us to create buttons that pass a number
 * and it responds with the page they should go to.
 * 
 * Used mainly for general navigation and not conditional
 * navigation.
 * 
 * @author Gagen
 *
 */
public class NavigationBean {
	
	private String pageId;

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	
	//switch case for the page to redirect too
	public String gotoPage() {
		switch (pageId) {
		case "1":
			return "StoreFront?faces-redirect=true";
		case "2":
			return "ShoppingCart?faces-redirect=true";
		case "3":
			return "LoginPage?faces-redirect=true";
		case "4":
			return "SignUp?faces-redirect=true";
		case "5":
			return "Payment?faces-redirect=true";
		case "6":
			return "Analytics?faces-redirect=true";
		default:
			return null;
		}
	}

}
