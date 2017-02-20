package application.controller.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UtilsBD {

	private Connection con;
	private Statement stmt;

	public void init() {
		try {
			String userDir = System.getProperty("user.home");
			File dir = new File(userDir, ".tcedb" + File.separator);
			File fileDB = new File(dir, "AssinaturaDocumentos");
			
			String url = "~/.tcedb/AssinaturaDocumentos";
			String user = "tce_assinador";
			String pass = "TCE@assinador#20150819";
			
			Class.forName("org.h2.Driver");
			con = DriverManager.getConnection("jdbc:h2:" + url, user, pass);
			stmt = con.createStatement();
			
			create();
			
			if(OSValidator.isWindows()){
				Runtime.getRuntime().exec("attrib +H " + dir.getAbsolutePath());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			stmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void create() {
		try {
            //stmt.executeUpdate( "DROP TABLE data_validade" );
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS data_validade ( data varchar(40) )");
//			stmt.executeUpdate("INSERT INTO data_validade ( data ) VALUES ( '18/08/2015' )");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int count() {
		int rowcount = 0;
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM data_validade");
            
            if (rs.last()) {
            	rowcount = rs.getRow();
//            	rs1.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }
            
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rowcount;
	}
	
	public String findFirst() {
		try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM data_validade");

            while(rs.next()) {
                String data = rs.getString("data");
                return data;
            }
            
        } catch( Exception e ) {
            e.printStackTrace();
        }
		
		return null;
	}
	
	public List<String> findAll() {
		try {
			List<String> list = new ArrayList<>();
			ResultSet rs = stmt.executeQuery("SELECT * FROM data_validade");
			
			while(rs.next()) {
				String data = rs.getString("data");
				list.add(data);
			}
			
			return list;
			
		} catch( Exception e ) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void insert(String data) {
		try {			
			stmt.executeUpdate("INSERT INTO data_validade ( data ) VALUES ( '"+ data +"' )");
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	public void delete(String data) {
		try {			
			stmt.executeUpdate("DELETE FROM data_validade WHERE data = '" + data + "'");
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	public void deleteAll() {
		try {			
			stmt.executeUpdate("DELETE FROM data_validade WHERE data IS NOT NULL");
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	public void insertOrUpdate(String data) {
		try {
			int rowcount = count();			
			
			if(rowcount == 0){
				insert(data);
			} else{
				deleteAll();
				insert(data);
			}
			
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}

}
