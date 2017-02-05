package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Converter extends BaseModelClass {

	private String Name;

	private String[] convertFromXliff;

	private String[] convertToXliff;


	public Converter()
	{

	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String[] getConvertFromXliff() {
		return convertFromXliff;
	}

	public void setConvertFromXliff(String[] convertFromXliff) {
		this.convertFromXliff = convertFromXliff;
	}

	public String[] getConvertToXliff() {
		return convertToXliff;
	}

	public void setConvertToXliff(String[] convertToXliff) {
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
}
