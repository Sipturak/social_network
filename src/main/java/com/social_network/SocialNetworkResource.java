package com.social_network;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.social_network.model.User;
import com.social_network.repository.SocialNetworkRepository;
import com.social_network.service.SocialNetworkService;
import com.social_network.service.SocialNetwrokServiceInterface;


@Path("socialnetwork")
public class SocialNetworkResource {

    private static final String SQL_DIRECT_FRIENDS = "select * from users inner join friendships on users.id = friendships.user_id where friends_id = ?";
	private static final String SQL_FRIENDS_OF_FRIENDS = "select distinct u.* from users u inner join friendships ff on u.id = ff.friends_id "
			+ "inner join friendships f on ff.user_id = f.friends_id where f.user_id = ? and ff.friends_id "
			+ "not in (select friends_id from friendships where user_id = ?)";
	/**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */

	private SocialNetwrokServiceInterface service;
	
	public SocialNetworkResource() throws SQLException, IOException {
		// TODO Auto-generated constructor stub
		service = new SocialNetworkService(new SocialNetworkRepository());
		service.create();
	}
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/user/friends/{id}")
    public List<User> getDirectFrineds(@PathParam ("id") int id) throws SQLException{
    	return service.getDirectFriends(SQL_DIRECT_FRIENDS, id);
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/user/friends_of_friends/{id}")
    public List<User> getFriendsOfFriends(@PathParam("id") int id) throws SQLException{
    	return service.getFriendsOfFriends(SQL_FRIENDS_OF_FRIENDS, id);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/user/suggested_friends/{id}")
    public List<User> getSuggestedFriends(@PathParam("id") int id) throws SQLException{
    	return service.getSuggestedFriends(id);
    }
    
}
