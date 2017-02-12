package model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class User extends BaseModelClass {

	private String Name;

	private String TeamName;

	private boolean IsAssignable;

	protected String IdFromProvider;

	private String Role;

	public String getRole() {
		return Role;
	}

	public void setRole(String role) {
		Role = role;
	}

	public void setIdFromProvider(String idFromProvider) {
		IdFromProvider = idFromProvider;
	}

	public void setName(String name) {
		Name = name;
	}

	public void setTeamName(String teamName) {
		TeamName = teamName;
	}

	public void setIsAssignable(boolean assignable) {
		IsAssignable = assignable;
	}

	public String getName() {
		return Name;
	}

	public String getTeamName() {
		return TeamName;
	}

	public String getIdFromProvider() {
		return IdFromProvider;
	}

	public boolean getIsAssignable() {
		return IsAssignable;
	}

	public User()
	{

	}

	public static User create(JsonNode newUserAsJson) {
		ObjectMapper objMapper = new ObjectMapper();
		User newUser = null;
		try {
			newUser = objMapper.treeToValue(newUserAsJson, User.class);
		}
		catch (JsonProcessingException e) {
			System.err.println("Error parsing user json into User model: " + e.getMessage());
		}
		return newUser;
	}
}
