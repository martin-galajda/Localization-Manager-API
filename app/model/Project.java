package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.CompareProjectException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Project extends BaseModelClass {

	private String name;

	private String projectKey;

	private boolean syncVersions;

	private String git;

	private String resourcePath;

	private User assignee;

	private Converter converter;

	private List<String> branches;

	private Integer wordCount = 0;

	private String status = "NONE";

	private String hashMapIdentifier;

	private Double price = 0.0;

	private String currency = "CZK";

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String newCurrency) {
		this.currency = newCurrency;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double newPrice) {
		price = newPrice;
	}

	public String getHashMapIdentifier() {
		return hashMapIdentifier;
	}

	public void setHashMapIdentifier(String hashMapIdentifier) {
		this.hashMapIdentifier = hashMapIdentifier;
	}

	public Integer getWordCount() {
		return wordCount;
	}

	public void setWordCount(Integer wordCount) {
		this.wordCount = wordCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean getSyncVersions() {
		return syncVersions;
	}

	public List<String> getBranches() {
		return branches;
	}

	public void setBranches(List<String> branches) {
		this.branches = branches;
	}

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	public void setSyncVersions(boolean syncVersions) {
		this.syncVersions = syncVersions;
	}

	public String getGit() {
		return git;
	}

	public void setGit(String git) {
		this.git = git;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	public User getAssignee() {

		return assignee;
	}

	public void setConverter(model.Converter converter) {
		this.converter = converter;
	}

	public model.Converter getConverter() {
		return converter;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
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


