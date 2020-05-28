package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.CoppiaArtisti;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Possibili ruoli degli artisti 
	 */
	public List<String> getRuoli(){
		String sql ="SELECT DISTINCT role " + 
				"FROM authorship "
				+ "ORDER BY role ASC "; 
		
		List<String> ruoli= new ArrayList<>(); 
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				String s= new String(res.getString("role")); 
				ruoli.add(s); 
			}
			conn.close();
			return ruoli;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Tutti gli Artisti con quel ruolo
	 */
	public List<Integer> getArtisti(String ruolo){
		String sql="SELECT a.artist_id "+
	               "FROM artists as a, authorship au "+
				"WHERE a.artist_id=au.artist_id AND au.role=? "; 
		
		List<Integer> artisti= new ArrayList<>(); 
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1,  ruolo);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				artisti.add(res.getInt("artist_id"));
				
			}
			conn.close();
			return artisti;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Coppie di Artisti
	 */
	public List<CoppiaArtisti> getCoppie(String ruolo){
		String sql="SELECT a1.artist_id as id1, a2.artist_id as id2, COUNT(distinct eo1.exhibition_id) AS peso " + 
				"FROM artists AS a1, artists AS a2, authorship AS au1,authorship AS au2, exhibition_objects AS eo1, exhibition_objects AS eo2 " + 
				"WHERE au1.role=?  AND au2.role=? " + 
				"AND au1.artist_id=a1.artist_id AND au2.artist_id=a2.artist_id " + 
				"AND au1.object_id=eo1.object_id AND au2.object_id=eo2.object_id " + 
				"AND eo1.exhibition_id=eo2.exhibition_id " + 
				"AND a1.artist_id>a2.artist_id " + 
				"GROUP BY a1.artist_id, a2.artist_id ";
		
		List<CoppiaArtisti> risultato= new ArrayList<>(); 
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1,  ruolo);
			st.setString(2,  ruolo);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {

				CoppiaArtisti c= new CoppiaArtisti(res.getInt("id1"), res.getInt("id2"), res.getInt("peso")); 
				risultato.add(c); 
			}
			conn.close();
			return risultato;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
