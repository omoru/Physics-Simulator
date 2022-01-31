package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Builder<T> {
	protected String typeTag;
	protected String desc;
	
	//----------------------------------------------------------------------------------------------------------------
	
	public String getTypeTag() {
		return typeTag;
	}
	public String getDesc() {
		return desc;
	}
	
	//----------------------------------------------------------------------------------------------------------------
	
	public Builder(){}
	
	public Builder(String typeTag, String desc) {
		this.typeTag = typeTag;
		this.desc = desc;
	}
	
	//----------------------------------------------------------------------------------------------------------------
	
	protected double[] jsonArrayTodoubleArray(JSONArray array) {
		double[] datos = new double[array.length()];
		for(int i = 0; i < datos.length; i++) 
			datos[i] = array.getDouble(i);
		return datos;
	}
	
	public T createInstance(JSONObject info) {
		T b = null;
		if(this.typeTag != null && this.typeTag.equals(info.getString("type")))
			b = createTheInstance(info.getJSONObject("data"));
		return b;
		
	}
	
	public JSONObject getBuilderInfo() {
		JSONObject info = new JSONObject();
		info.put("type", this.typeTag);
		info.put("data", createData());
		info.put("desc", this.desc);
		return info;
	}
	
	protected JSONObject createData() {
		return new JSONObject();
	}
	
	protected abstract T createTheInstance(JSONObject jsonObject);
	
}
