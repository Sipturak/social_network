package com.social_network.repository;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.social_network.model.JsonReader;
import com.social_network.model.User;
import com.social_network.repository.Provider.DBConnection.DbTypes;

public class SocialNetworkRepository extends Provider implements SocialNetworkRepositoryInterface{
	
	//CONSTANTS FOR CREATE METHOD
	interface Constants{
		public static final String UPDATE_USERS = "update users set first_name = ?, surname = ?, age = ? , gender = ? where id = ?";
		public static final String UPDATE_FRIENDSHIPS = "update friendships set friends_id = ? where user_id = ?";
		public static final String INSERT_INTO_USERS = "insert into users values (?,?,?,?,?)";
		public static final String INSERT_INTO_FRIENDSHIPS = "insert into friendships (user_id, friends_id) values (?,?)";
		public static final String SELECT_ALL_FOR_SPECIFIC_USER = "select * from users where id = ?";
		public static final String SELECT_ID_FROM_FRIENDSHIP_WHERE_USERID = "select id from friendships where user_id = ?";
	}
	
	private ResultSet rs;
	private PreparedStatement prStatement;
	private JsonReader jsonReader;
	
	public SocialNetworkRepository() throws SQLException {
		// TODO Auto-generated constructor stub
		setConnection(DBConnection.getConnection(DbTypes.Mysql));
		jsonReader = new JsonReader();
	}	
	
	@Override
	public void create () throws IOException, SQLException { //insert input parameters path of f file, arg0 sql and arg1 sql
		
		User [] usersArray = jsonReader.getUsersFromGsonFile("C:\\Users\\pc\\Downloads\\data.json");
		
		for(User item : usersArray) {	
			rs = getResultSetCheckingUser(Constants.SELECT_ALL_FOR_SPECIFIC_USER, item.getId());
			if(rs.next()) {
				rs = getResultSetCheckingUser(Constants.SELECT_ID_FROM_FRIENDSHIP_WHERE_USERID, item.getId());
				for(Integer friendId : item.getFriends()) {
					int id = 0;
					if(rs.next())
						id = rs.getInt("id");
					prStatement = getPreparedStatament(Constants.UPDATE_FRIENDSHIPS);
					prStatement.setInt(1, friendId);
					prStatement.setInt(2, id);
					prStatement.execute();
				}	
				prStatement = getPreparedStatament(Constants.INSERT_INTO_USERS);
				prStatement.setString(1, item.getFirstName());
				prStatement.setString(2, item.getSurname());
				prStatement.setInt(3, item.getAge());
				prStatement.setString(4, item.getGender());
				prStatement.setInt(5, item.getId());
				prStatement.execute();
			}
			
			else {
				setPreparedStatament(item,Constants.INSERT_INTO_USERS,Constants.INSERT_INTO_FRIENDSHIPS);
			}
		}
		prStatement.close();
		rs.close();
	}
	
	@Override
	public List<User> getDirectFriends(String sql, int id) throws SQLException { //add input parameter from sql
		
		List<User> users = new LinkedList<>();
		rs = getResultSetCheckingUser(sql, id);
			while(rs.next()) {
				users.add(new User(rs.getInt("id"), rs.getString("first_name"), rs.getString("surname"), 
						+ rs.getInt("age"), rs.getString("gender")));
			}
		rs.close();
		return users;
	}
	
	@Override
	public List<User> getFriendsOfFriends(String sql, int id) throws SQLException{
		List<User> users = new LinkedList<>();
		
		prStatement = getPreparedStatament(sql);
		prStatement.setInt(1, id);
		prStatement.setInt(2, id);
		
		rs = prStatement.executeQuery();
			while(rs.next()) {
				users.add(new User(rs.getInt("id"), rs.getString("first_name"), rs.getString("surname"),
						rs.getInt("age"), rs.getString("gender")));
			}
		prStatement.close();
		rs.close();
		return users;
	}
	
	@Override
	public List<User> getSuggestedFriends(int id) throws SQLException{
		List<Integer> suggestionFrineds = new ArrayList<>();
		String sql = "select friends_id from friendships where user_id = ?";
		List<Integer> myFriends = getMyFriends(id);
		List<Integer> potentialSuggeestionFriends = new ArrayList<>();
		potentialSuggeestionFriends.addAll(getPotentialSuggestionFriends(id));
		for(int i = 0; i < potentialSuggeestionFriends.size();i++) {
				int count = 0;
				for(int k = 0; k < myFriends.size(); k++) {
				prStatement = getPreparedStatament(sql);
				prStatement.setInt(1, myFriends.get(k));
				rs = prStatement.executeQuery();
				List<Integer> frinedsOfMyFriends = null;
				frinedsOfMyFriends = new ArrayList<>();
				while(rs.next()) {
					frinedsOfMyFriends.add(rs.getInt("friends_id"));
				}
				for(int j = 0; j < frinedsOfMyFriends.size(); j++) {
					if(potentialSuggeestionFriends.get(i) == frinedsOfMyFriends.get(j)) {
						count++;
						break;
					}
				}
				if(count >= 2) {
					suggestionFrineds.add(potentialSuggeestionFriends.get(i));
					break;
				}
			}
		}
		String arg0 = "select * from users where id = ?";
		List<User> suggestedUsers = new LinkedList<>();
		for(Integer item : suggestionFrineds) {
			rs = getResultSetCheckingUser(arg0, item);
			if(rs.next()) {
				suggestedUsers.add(new User(rs.getInt("id"), rs.getString("first_name"), rs.getString("surname"), 
						rs.getInt("age"), rs.getString("gender")));
			}
		}
		return suggestedUsers;
	}
	
	private List<Integer> getMyFriends(int id) throws SQLException{
		String sql = "select friends_id from friendships where user_id = ?";
		List<Integer> friendsList = getFriends(id, sql);		
		return friendsList;
	}
	
	private List<Integer> getFriends(int id, String sql) throws SQLException {
		List<Integer> friendsList = new ArrayList<>();
		prStatement = getPreparedStatament(sql);
		prStatement.setInt(1, id);
		rs = prStatement.executeQuery();
		while(rs.next()) {
			friendsList.add(rs.getInt("friends_id"));
		}
		return friendsList;
	}
	
	private Set<Integer> getPotentialSuggestionFriends(int id) throws SQLException {
		List<Integer> myFriends = getMyFriends(id);
		String sql = "select friends_id from friendships where user_id = ?";
		Set<Integer> friendsThatIDontHave = new HashSet<>();
		for(int i = 0; i < myFriends.size(); i++) {		
			prStatement = getPreparedStatament(sql);
			prStatement.setInt(1, myFriends.get(i));
			rs = prStatement.executeQuery();
			List<Integer> friendsOfMyFriends = null;
			friendsOfMyFriends = new ArrayList<>();
			while(rs.next()) {
				//get all friends of my friends and add to list
				friendsOfMyFriends.add(rs.getInt("friends_id"));
			}
			//check if that i have specific friend of  friends from my friends
			for(int k = 0; k < friendsOfMyFriends.size(); k ++) {
				int element = friendsOfMyFriends.get(k);
				for(int j = 0; j < myFriends.size(); j++) {
					if(element == myFriends.get(j) || element == id) {
						friendsOfMyFriends.set(k, -1);
						break;
					}
				}
			}
			//which i don't have that are potential suggested friends
			for(int z = 0 ; z < friendsOfMyFriends.size(); z++) {
				if(friendsOfMyFriends.get(z) != -1)
					friendsThatIDontHave.add(friendsOfMyFriends.get(z));
			}
		}
		return friendsThatIDontHave;
	}
	
	private void setPreparedStatament(User item,String sql0, String sql1) throws SQLException {
		prStatement = getPreparedStatament(sql0);
		prStatement.setInt(1, item.getId());
		prStatement.setString(2, item.getFirstName());
		prStatement.setString(3, item.getSurname());
		prStatement.setInt(4, item.getAge());
		prStatement.setString(5, item.getGender());
		prStatement.execute();
		
		for(Integer friendId : item.getFriends()) {
			prStatement = getPreparedStatament(sql1);
			prStatement.setInt(1, item.getId());
			prStatement.setInt(2, friendId);
			prStatement.execute();
		}
	}
}
