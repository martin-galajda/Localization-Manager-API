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

	public String toString() {
		return this.getName();
	}
}
