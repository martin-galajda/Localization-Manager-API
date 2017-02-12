package model;


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

}
