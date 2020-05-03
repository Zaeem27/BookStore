package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import data.Book;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The backing bean for the Signup operation The password is not stored and is
 * deleted after the Signup action.
 * 
 * @author Gagen
 *
 */
public class SignUpBean {
	private String name;
	private String password;
	private Boolean failed;
	private String accesslevel;
	private String confirmedPassword;
	private String userType;

	private DataSource ds;
	
	// getter and setters for JSF to use
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getAccesslevel() {
		return accesslevel;
	}

	public void setAccesslevel(String accesslevel) {
		this.accesslevel = accesslevel;
	}

	public Boolean getFailed() {
		return failed;
	}

	public void setFailed(Boolean failed) {
		this.failed = failed;
	}

	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
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
	
	/*
	 * The signUp operation it checks the password and creates a db
	 * record for the user.
	 */
	public String signUp() {
		if (this.getPassword().equals(this.getConfirmedPassword())) {
			MessageDigest messageDigest;
			String encryptedPassword = "";
			try {
				messageDigest = MessageDigest.getInstance("SHA-256");
				messageDigest.update(this.getPassword().getBytes());
				encryptedPassword = new String(messageDigest.digest());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			String alteredPasswordForSQL = "";
			alteredPasswordForSQL += encryptedPassword.charAt(0);
			for (int i = 0; i < encryptedPassword.length() - 1; i++) {
				char c = encryptedPassword.charAt(i + 1);
				if (c == '\'') {
					alteredPasswordForSQL += "'";
				}
				alteredPasswordForSQL += c;
			}
			if (this.getUserType().equals("") || this.getUserType().equals(null)) {
				this.setUserType("Visitor");
			}
			if (this.createUser(this.getName(), alteredPasswordForSQL, this.getUserType())) {
				setFailed(false);
				setPassword("");
				setConfirmedPassword("");
				return "created";
			}else {
				setFailed(true);
				setPassword("");
				setConfirmedPassword("");
				setName("");
				return "failed";
			}
			
		}else {
			setFailed(true);
			setPassword("");
			setConfirmedPassword("");
			return "failed";
		}
	}
	
	/*
	 * method to do the actual db record creation
	 */
	public Boolean createUser(String username, String password, String type) {
		try {
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/Db2-4413");
			String query = "insert into users (user_name, password_hash, user_type) values ('" + username + "', '" + password + "', '" + type + "')";
			Connection con = ds.getConnection();
			PreparedStatement p = con.prepareStatement(query);
			boolean r = p.execute();

			// make sure to release the db resources
			p.close();
			con.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
	}

}
