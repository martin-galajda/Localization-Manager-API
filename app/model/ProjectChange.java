package model;

import java.util.Date;


public class ProjectChange extends BaseModelClass {

	public static ProjectChange create(Project oldProjectVersion){
		ProjectChange projectChange = new ProjectChange();
		projectChange.setOldProjectVersion(oldProjectVersion);
		projectChange.setCreatedAtTimestamp(new Date().getTime());
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

	public Long getCreatedAtTimestamp() {
		return this.CreatedAtTimestamp;
	}

	public void setCreatedAtTimestamp(Long timestamp) {
		CreatedAtTimestamp = timestamp;
	}
}
