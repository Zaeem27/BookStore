package rest;
/**
 * Class that handles the rest operations
 * 
 * @author Zaeem
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.json.Json;
import javax.json.JsonObject;

@Path("book")
public class Book {

	@GET
	@Path("/getProductInfo/")
	@Produces("text/plain")
	public String getProductInfo(@QueryParam("bid") String name) throws Exception {
		String bid = "";
		String title = "";
		String price = "";
		String cat = "";
		String serializedJson = "";
		try {
			// lookup the books in the db and store them in a booklist.
			DataSource ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/Db2-4413");
			String query = "select * from Book where bid = '" + name + "'";

			Connection con = ds.getConnection();
			PreparedStatement p = con.prepareStatement(query);
			ResultSet r = p.executeQuery();
			r.next();
			bid = r.getString("bid");
			title = r.getString("title");
			price = r.getString("price");
			cat = r.getString("category");
			// Book b = new Book(bid,title,price,cat);

			r.close();
			p.close();
			con.close();

			JsonObject value = Json.createObjectBuilder().add("SBN", bid).add("Title", title).add("Price", price).add("Category", cat).build();
			serializedJson = value.toString() + "\n";

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serializedJson;
	}
	
	

	@GET
	@Path("/getOrdersByPartNumber/")
	@Produces("text/plain")
	public String getOrdersByPartNumber(@QueryParam("partNumber") String name) throws Exception {
		DataSource ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/Db2-4413");
		String query = "select * from POItem where bid = '" + name + "'";
		
		Connection con = ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		ResultSet r = p.executeQuery();
		
		String serializedJson = "";
		while (r.next()) {
			String id = r.getString("id");
			String bid = r.getString("bid");
			String price = r.getString("price");
			JsonObject value = Json.createObjectBuilder().add("ID", id).add("BID", bid).add("Price", price).build();
			serializedJson += value.toString() + "\n";
		}

		r.close();
		p.close();
		con.close();
		return serializedJson;
	}
}
