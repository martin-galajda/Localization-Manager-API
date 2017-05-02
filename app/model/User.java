package model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.UserRole;

public class User extends BaseModelClass {

	private String name;

	private String teamName;

	private boolean isAssignable;

	protected String idFromProvider;

	private String role = UserRole.GUEST;

	private String pictureUrl;

	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPictureUrl()
	{
		return pictureUrl;
	}

	public void setPictureUrl(String newPictureUrl)
	{
		pictureUrl = newPictureUrl;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setIdFromProvider(String idFromProvider) {
		this.idFromProvider = idFromProvider;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public void setIsAssignable(boolean assignable) {
		isAssignable = assignable;
	}

	public String getName() {
		return name;
	}

	public String getTeamName() {
		return teamName;
	}

	public String getIdFromProvider() {
		return idFromProvider;
	}

	public boolean getIsAssignable() {
		return isAssignable;
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

	public String toString() {
		return this.getName();
	}
}
