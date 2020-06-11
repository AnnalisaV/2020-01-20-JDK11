package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.CoupleArtists;
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
	 * Possibili ruoli degli Artists
	 * @return lista di ruoli possibili
	 */
	public List<String> getRoles(){
		String sql="SELECT DISTINCT role " + 
				"FROM authorship " + 
				"ORDER BY role asc"; 
		List<String> lista= new ArrayList<>(); 
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				lista.add(new String(res.getString("role"))); 
			}
			conn.close();
			return lista;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public void getAllArtists(Map<Integer, Artist> idMapArtists) {
		String sql="SELECT artist_id, NAME " + 
				"FROM artists"; 
		
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMapArtists.containsKey(res.getInt("artist_id"))) {
					Artist a = new Artist(res.getInt("artist_id"), res.getString("name"));
					idMapArtists.put(a.getId(), a); 
				}
			}
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		
	}
	
	public List<Artist> getArtistByRole(String role, Map<Integer, Artist> idMap){
		String sql="SELECT DISTINCT (artist_id) " + 
				"FROM authorship " + 
				"WHERE role=?"; 
		List<Artist> lista= new ArrayList<>(); 
		
		
try {
			Connection conn = DBConnect.getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, role);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Artist a = idMap.get(res.getInt("artist_id")); 
				lista.add(a);
			}
			conn.close();
			return lista; 
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null; 
			
		}
		
	}
	
	public List<CoupleArtists> getCouples(Map<Integer, Artist> idMap, String role){
		String sql="SELECT a1.artist_id, a2.artist_id, COUNT(DISTINCT ob1.exhibition_id) AS peso " + 
				"FROM exhibition_objects ob1, exhibition_objects ob2, authorship a1, authorship a2 " + 
				"WHERE ob1.exhibition_id=ob2.exhibition_id AND " + 
				"ob1.object_id=a1.object_id AND " + 
				"ob2.object_id=a2.object_id AND " + 
				"a1.artist_id>a2.artist_id AND " + 
				"a1.role=a2.role AND a1.role=? " + 
				"GROUP BY a1.artist_id, a2.artist_id"; 
		List<CoupleArtists> lista= new ArrayList<>(); 
		
		
       try {
			Connection conn = DBConnect.getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1,  role);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Artist a1= idMap.get(res.getInt("a1.artist_id"));
				Artist a2= idMap.get(res.getInt("a2.artist_id"));
				
				CoupleArtists c= new CoupleArtists(a1, a2, res.getInt("peso")); 
				lista.add(c); 
			}
			conn.close();
			return lista; 
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null; 
			
		}
		
	}
	
	
	
}
