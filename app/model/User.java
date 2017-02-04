package model;


public class User extends BaseModelClass {

	private String Name;

	private String TeamName;

	protected String IdFromProvider;

	public String getIdFromProvider() {
		return IdFromProvider;
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

	public String getName() {
		return Name;
	}

	public String getTeamName() {
		return TeamName;
	}

	public User()
	{

	}

}
