package model;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by martin on 12/8/16.
 */
public class BaseModelClass {
	public BaseModelClass() {

	}

	protected String Id;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}
}
