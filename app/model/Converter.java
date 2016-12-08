package model;

public class Converter extends BaseModelClass {

	private String Name;

	private String InputFormat;

	private String Script;

	private String OutputFormat;

	public void setName(String name) {
		Name = name;
	}

	public void setInputFormat(String inputFormat) {
		InputFormat = inputFormat;
	}

	public void setScript(String script) {
		Script = script;
	}

	public void setOutputFormat(String outputFormat) {
		OutputFormat = outputFormat;
	}



	public String getName() {
		return Name;
	}

	public String getInputFormat() {
		return InputFormat;
	}

	public String getScript() {
		return Script;
	}

	public String getOutputFormat() {
		return OutputFormat;
	}

	public Converter()
	{

	}
}
