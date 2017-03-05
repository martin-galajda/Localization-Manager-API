package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.CompareProjectException;
import play.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Project extends BaseModelClass {

	private String Name;

	private String ProjectKey;

	private boolean SyncVersions;

	private String Git;

	private String ResourcePath;

	private User Assignee;

	private Converter Converter;

	private List<String> Branches;

	private Integer WordCount = 0;

	private TranslationStatus Status = TranslationStatus.NONE;

	private String HashMapIdentifier;

	public String getHashMapIdentifier() {
		return HashMapIdentifier;
	}

	public void setHashMapIdentifier(String hashMapIdentifier) {
		HashMapIdentifier = hashMapIdentifier;
	}

	public Integer getWordCount() {
		return WordCount;
	}

	public void setWordCount(Integer wordCount) {
		WordCount = wordCount;
	}

	public Integer getStatus() {
		return Status.value;
	}

	public void setStatus(Integer status) {
		Status = TranslationStatus.fromInt(status);
	}

	public boolean getSyncVersions() {
		return SyncVersions;
	}

	public List<String> getBranches() {
		return Branches;
	}

	public void setBranches(List<String> branches) {
		Branches = branches;
	}

	public String getProjectKey() {
		return ProjectKey;
	}

	public void setProjectKey(String projectKey) {
		ProjectKey = projectKey;
	}

	public void setSyncVersions(boolean syncVersions) {
		this.SyncVersions = syncVersions;
	}

	public String getGit() {
		return Git;
	}

	public void setGit(String git) {
		Git = git;
	}

	public String getResourcePath() {
		return ResourcePath;
	}

	public void setResourcePath(String resourcePath) {
		ResourcePath = resourcePath;
	}

	public void setAssignee(User assignee) {
		Assignee = assignee;
	}

	public User getAssignee() {

		return Assignee;
	}

	public void setConverter(model.Converter converter) {
		Converter = converter;
	}

	public model.Converter getConverter() {
		return Converter;
	}


	public void setName(String name) {
		Name = name;
	}

	public String getName() {
		return Name;
	}

	public Project()
	{

	}

	public static Project create(JsonNode newProjectJson) {
		ObjectMapper objMapper = new ObjectMapper();
		Project newProject = null;
		try {
			newProject = objMapper.treeToValue(newProjectJson, Project.class);
		}
		catch (JsonProcessingException e) {
			System.err.println("Error parsing project json into Project model: " + e.getMessage());
		}
		return newProject;
	}

	public List<FieldChange> getChangedFields(Project oldProject) throws CompareProjectException {
		Field[] fields = Project.class.getDeclaredFields();
		List<FieldChange> fieldChangeList = new ArrayList<>();

		try {
			for (Field field: fields) {
				Object oldField = field.get(oldProject);
				Object newField = field.get(this);
				String valueOfCurrentProjectField;
				String valueOfOldProjectField;

				if (isListType(field)) {
					valueOfCurrentProjectField = convertListPropertyToString(newField);
					valueOfOldProjectField = convertListPropertyToString(oldField);
				} else {
					valueOfCurrentProjectField = newField != null ? newField.toString() : "";
					valueOfOldProjectField = oldField != null ? oldField.toString() : "";
				}
				Logger.debug("Field name: " + field.getName());
				Logger.debug("Old value field: " + valueOfOldProjectField);
				Logger.debug("New value field: " + valueOfCurrentProjectField);

				if (!valueOfCurrentProjectField.equals(valueOfOldProjectField)) {
					FieldChange newFieldChange = new FieldChange(
							field.getName(),
							valueOfOldProjectField,
							valueOfCurrentProjectField
					);

					fieldChangeList.add(newFieldChange);
				}
			}
		} catch (IllegalArgumentException|IllegalAccessException e) {
			throw new CompareProjectException("Error occured while comparing projects: ", e);
		}

		return fieldChangeList;
	}

	@SuppressWarnings("unchecked")
	private static String convertListPropertyToString(Object printableListObject) {
		StringBuilder stringBuilder = new StringBuilder("");

		if (printableListObject instanceof List) {
			List<Object> printableList = (List) printableListObject;
			for (Object property : printableList) {
				stringBuilder.append(",");
				stringBuilder.append(property.toString());
			}
		}


		return stringBuilder.toString();
	}

	private static boolean isListType(Field field) {
		return List.class.isAssignableFrom(field.getType());
	}
}


