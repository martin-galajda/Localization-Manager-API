package model;


public class User {
	private String Id;

	private String Name;

	private String TeamName;

	public void setId(String id) {
		Id = id;
	}

	public void setName(String name) {
		Name = name;
	}

	public void setTeamName(String teamName) {
		TeamName = teamName;
	}

	public String getId() {

		return Id;
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
