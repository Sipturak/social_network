package com.social_network.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.social_network.model.User;

public interface SocialNetwrokServiceInterface {

	void create() throws IOException, SQLException;

	List<User> getDirectFriends(String sql, int id) throws SQLException;

	List<User> getFriendsOfFriends(String sql, int id) throws SQLException;
	
	List<User> getSuggestedFriends(int id) throws SQLException;
}