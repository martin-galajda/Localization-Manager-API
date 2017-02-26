package model;

import java.util.List;

import java.util.Date;


public class ProjectChange extends BaseModelClass {

	public static ProjectChange create(String projectId, List<FieldChange> fieldChangeList, String createdBy){
		Date date = new Date();
		long timeStamp = date.getTime();

		ProjectChange projectChange = new ProjectChange();
		projectChange.setCreatedAtTimestamp(timeStamp);
		projectChange.setCreatedBy(createdBy);
		projectChange.setFieldChangeList(fieldChangeList);
		projectChange.setProjectId(projectId);

		return projectChange;
	}

	private List<FieldChange> fieldChangeList;

	private long CreatedAtTimestamp;

	private String CreatedBy;

	private String projectId;

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getCreatedBy() {
		return CreatedBy;
	}

	public void setCreatedBy(String createdBy) {
		this.CreatedBy = createdBy;
	}

	public Long getCreatedAtTimestamp() {
		return this.CreatedAtTimestamp;
	}

	public void setCreatedAtTimestamp(Long timestamp) {
		CreatedAtTimestamp = timestamp;
	}

	public List<FieldChange> getFieldChangeList() {
		return fieldChangeList;
	}

	public void setFieldChangeList(List<FieldChange> fieldChangeList) {
		this.fieldChangeList = fieldChangeList;
	}
}
