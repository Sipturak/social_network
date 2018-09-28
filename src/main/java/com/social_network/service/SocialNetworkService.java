package com.social_network.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.social_network.model.User;
import com.social_network.repository.SocialNetworkRepositoryInterface;

public class SocialNetworkService implements SocialNetwrokServiceInterface {
	
	private SocialNetworkRepositoryInterface repo;
	
	public SocialNetworkService(SocialNetworkRepositoryInterface repo) {
		this.repo = repo;
	}
	
	@Override
	public void create () throws IOException, SQLException {
		repo.create();
	}
	
	@Override
	public List<User> getDirectFriends (String sql, int id) throws SQLException{
		return repo.getDirectFriends(sql, id);
	}
	
	@Override
	public List<User> getFriendsOfFriends(String sql, int id) throws SQLException{
		return repo.getFriendsOfFriends(sql, id);
	}
	
	@Override
	public List<User> getSuggestedFriends(int id) throws SQLException{
		return repo.getSuggestedFriends(id);
	}
	
}
