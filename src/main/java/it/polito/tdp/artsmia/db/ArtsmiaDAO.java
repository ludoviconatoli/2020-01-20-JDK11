package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artist;
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
	
	public List<String> getRuoli(){
		String sql = "SELECT DISTINCT role "
				+ "FROM authorship";
		
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while(res.next()) {
				result.add(res.getString("role"));
			}
			
			Collections.sort(result);
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void getArtisti(Map<Integer, Artist> idMap, String ruolo) {
			
		String sql ="SELECT distinct ar.artist_id, ar.name "
				+ "FROM authorship a, artists ar "
				+ "WHERE a.artist_id = ar.artist_id AND a.role = ?";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				if(!idMap.containsKey(res.getInt("ar.artist_id"))) {
					Artist a = new Artist(res.getInt("ar.artist_id"), res.getString("ar.name"));
					
					idMap.put(a.getId(), a);
				}
			}
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Adiacenza> getArchi(Map<Integer, Artist> map, String ruolo){
		String sql="SELECT a1.artist_id, a2.artist_id, COUNT(distinct e1.exhibition_id) AS peso "
				+ "FROM authorship a1, authorship a2, exhibition_objects e1, exhibition_objects e2 "
				+ "WHERE a1.artist_id > a2.artist_id AND a1.role = ? AND a2.role= ? AND "
				+ "		e1.object_id = a1.object_id AND e2.object_id = a2.object_id AND e1.exhibition_id = e2.exhibition_id "
				+ "GROUP BY a1.artist_id, a2.artist_id";
		
		List<Adiacenza> result = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			st.setString(2, ruolo);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				if(map.containsKey(res.getInt("a1.artist_id")) && map.containsKey(res.getInt("a2.artist_id"))) {
					Artist a1 = map.get(res.getInt("a1.artist_id"));
					Artist a2 = map.get(res.getInt("a2.artist_id"));
					
					result.add(new Adiacenza(a1, a2, res.getDouble("peso")));
				}
			}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
