package com.social_network.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;

public class JsonReader {
	
	private BufferedReader jsonReader;
	private Gson gson;
	
	public User[] getUsersFromGsonFile(String path) throws IOException {
		this.jsonReader = new BufferedReader(new FileReader(path));		
		this.gson = new Gson();
		User [] user = gson.fromJson(jsonReader, User [].class);
		jsonReader.close();
		return user;
		
	}
}
