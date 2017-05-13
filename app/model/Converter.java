package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.data.validation.Constraints;

import java.util.List;

public class Converter extends BaseModelClass {

	@Constraints.Required
	private String name;

	@Constraints.Required
	private List<String> convertFromXliff;

	@Constraints.Required
	private List<String> convertToXliff;


	public Converter()
	{

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getConvertFromXliff() {
		return convertFromXliff;
	}

	public void setConvertFromXliff(List<String> convertFromXliff) {
		this.convertFromXliff = convertFromXliff;
	}

	public List<String> getConvertToXliff() {
		return convertToXliff;
	}

	public void setConvertToXliff(List<String> convertToXliff) {
		this.convertToXliff = convertToXliff;
	}

	public static Converter create(JsonNode newConverterJson) {
		ObjectMapper objMapper = new ObjectMapper();
		Converter newConverter = null;
		try {
			newConverter = objMapper.treeToValue(newConverterJson, Converter.class);
		}
		catch (JsonProcessingException e) {
			System.err.println("Error parsing converter json into Converter model: " + e.getMessage());
		}
		return newConverter;
	}


	public String toString() {
		return this.getName();
	}
}
