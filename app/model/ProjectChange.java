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

	private long CreatedAtTimestamp;

	public Project getOldProjectVersion() {
		return OldProjectVersion;
	}

	public void setOldProjectVersion(Project oldProjectVersion) {
		OldProjectVersion = oldProjectVersion;
	}

	public Date getCreatedAt() {
		return new Date(this.CreatedAtTimestamp);
	}

	public void setCreatedAt(Date createdAt) {
		CreatedAtTimestamp = createdAt.getTime();
	}

	public void setCreatedAtTimestamp(Long timestamp) {
		CreatedAtTimestamp = timestamp;
	}
}
