package model;

import java.util.Date;


public class ProjectChange extends BaseModelClass {

	public static ProjectChange create(Project oldProjectVersion){
		ProjectChange projectChange = new ProjectChange();
		projectChange.setOldProjectVersion(oldProjectVersion);
		projectChange.setCreatedAt(new Date());
		return projectChange;
	}

	private Project OldProjectVersion;

	private Date CreatedAt;

	public Project getOldProjectVersion() {
		return OldProjectVersion;
	}

	public void setOldProjectVersion(Project oldProjectVersion) {
		OldProjectVersion = oldProjectVersion;
	}

	public Date getCreatedAt() {
		return CreatedAt;
	}

	public void setCreatedAt(Date createdAt) {
		CreatedAt = createdAt;
	}
}
