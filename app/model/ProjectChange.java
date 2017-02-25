package model;

import java.util.Date;


public class ProjectChange extends BaseModelClass {

	public static ProjectChange create(Project oldProjectVersion, String createdBy){
		Date date = new Date();
		long timeStamp = date.getTime();

		ProjectChange projectChange = new ProjectChange();
		projectChange.setOldProjectVersion(oldProjectVersion);
		projectChange.setCreatedAtTimestamp(timeStamp);
		projectChange.setCreatedBy(createdBy);

		System.err.println("Adding change inside projectChange: create");
		System.err.println("Adding change inside timestamp: " + timeStamp);
		System.err.println("Adding createdBy: " + createdBy);


		return projectChange;
	}

	private Project OldProjectVersion;

	private long CreatedAtTimestamp;

	private String CreatedBy;

	public String getCreatedBy() {
		return CreatedBy;
	}

	public void setCreatedBy(String createdBy) {
		this.CreatedBy = createdBy;
	}

	public Project getOldProjectVersion() {
		System.err.println("Calling getOldProjectVersion");
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
