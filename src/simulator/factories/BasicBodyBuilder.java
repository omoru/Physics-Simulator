package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body>{
	
	public BasicBodyBuilder() {
		this.typeTag = "basic";
		this.desc = "Default Body";
	}
	
	@Override
	protected JSONObject createData() {
		JSONObject data= new JSONObject();
		data.put("id", "identidier");
		data.put("pos", "possition");
		data.put("vel", "velocity");
		data.put("mass", "mass");
		return data;
	}
	
	@Override
	protected Body createTheInstance(JSONObject jsonObject) {
		String id = jsonObject.getString("id");
		Vector poss = new Vector(jsonArrayTodoubleArray(jsonObject.getJSONArray("pos")));
		Vector vel = new Vector(jsonArrayTodoubleArray(jsonObject.getJSONArray("vel")));
		double mass = jsonObject.getDouble("mass");
		Vector acc = new Vector(poss.dim());
		return new Body(id, poss, vel, mass, acc);
	}

}
