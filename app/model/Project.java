package model;


public class Project extends BaseModelClass {
	private String Name;

	private String Description;

	private String GitUrl;

	private String Format;

	private String PathToResources;

	private String ResourcePrefix;

	private String StashId;

	private User Assignee;

	private User Reviewer;

	private Converter Converter;

	public void setAssignee(User assignee) {
		Assignee = assignee;
	}

	public void setReviewer(User reviewer) {
		Reviewer = reviewer;
	}

	public void setConverter(model.Converter converter) {
		Converter = converter;
	}

	public User getAssignee() {

		return Assignee;
	}

	public User getReviewer() {
		return Reviewer;
	}

	public model.Converter getConverter() {
		return Converter;
	}


	public void setName(String name) {
		Name = name;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public void setGitUrl(String gitUrl) {
		GitUrl = gitUrl;
	}

	public void setFormat(String format) {
		Format = format;
	}

	public void setPathToResources(String pathToResources) {
		PathToResources = pathToResources;
	}

	public void setResourcePrefix(String resourcePrefix) {
		ResourcePrefix = resourcePrefix;
	}

	public void setStashId(String stashId) {
		StashId = stashId;
	}


	public String getName() {
		return Name;
	}

	public String getDescription() {
		return Description;
	}

	public String getGitUrl() {
		return GitUrl;
	}

	public String getFormat() {
		return Format;
	}

	public String getPathToResources() {
		return PathToResources;
	}

	public String getResourcePrefix() {
		return ResourcePrefix;
	}

	public String getStashId() {
		return StashId;
	}

	public Project()
	{

	}
}
