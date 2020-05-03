package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import java.security.MessageDigest;

/**
 * The backing bean for the login operation The password is not stored and is
 * deleted after the login action.
 * 
 * we keep the username and the access level for the user for the session.
 * 
 * @author Gagen
 *
 */
public class LoginBean {
	private String name;
	private String password;
	private Boolean failed;
	private Boolean loggedin;
	private String accesslevel;

	private DataSource ds;
	
	public LoginBean() {
		this.loggedin = false;
	}
	// getter and setters for JSF to use
	public String getAccesslevel() {
		return accesslevel;
	}

	public void setAccesslevel(String accesslevel) {
		this.accesslevel = accesslevel;
	}

	public Boolean getLoggedin() {
		return loggedin;
	}

	public void setLoggedin(Boolean loggedin) {
		this.loggedin = loggedin;
	}

	public Boolean getFailed() {
		return failed;
	}

	public void setFailed(Boolean failed) {
		this.failed = failed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// Login code
	public String login() {
		String username = this.getName();
		MessageDigest messageDigest;
		String encryptedPassword;
		try {

			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(this.getPassword().getBytes());
			encryptedPassword = new String(messageDigest.digest());

			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/Db2-4413");
			String query = "select * from users where USER_NAME = '" + username + "'";

			Connection con = ds.getConnection();
			PreparedStatement p = con.prepareStatement(query);
			ResultSet r = p.executeQuery();
			r.next();
			String name = r.getString("user_name");
			String dbPass = r.getString("password_hash");
			this.setAccesslevel(r.getString("user_type"));
			r.close();
			p.close();
			con.close();
			if (dbPass.equals(encryptedPassword)) {
				this.password="";
				setLoggedin(true);
				setFailed(false);
				return "true";
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		this.password="";
		setLoggedin(false);
		setFailed(true);
		return "false";
	}
	
	//logout code
	public String logout() {
		setAccesslevel("Visitor");
		setLoggedin(false);
		setName("");
		return null;
	}
	
	//Access level boolean checks
	public Boolean isAdmin() {
		return "Admin".equalsIgnoreCase(getAccesslevel());
	}
	
	public Boolean isPartner() {
		return "Partner".equalsIgnoreCase(getAccesslevel());
	}
}