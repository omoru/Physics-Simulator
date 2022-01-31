package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.MassLossingBody;

public class MassLosingBodyBuilder extends Builder<Body>{
	
	public MassLosingBodyBuilder() {
		this.typeTag = "mlb";
		this.desc = "Mass Lossing Body";
	}
	
	@Override
	protected JSONObject createData() {
		JSONObject data= new JSONObject();
		data.put("id", "identidier");
		data.put("pos", "position");
		data.put("vel", "velocity");
		data.put("mass", "mass");
		data.put("freq", "frequency");
		data.put("factor", "factor");
		return data;
	}
	
	@Override
	protected Body createTheInstance(JSONObject jsonObject) {
		String id = jsonObject.getString("id");
		Vector pos = new Vector(jsonArrayTodoubleArray(jsonObject.getJSONArray("pos")));
		Vector vel = new Vector(jsonArrayTodoubleArray(jsonObject.getJSONArray("vel")));
		double mass = jsonObject.getDouble("mass");
		Vector acc = new Vector(pos.dim());
		double freq = jsonObject.getDouble("freq");
		double fac = jsonObject.getDouble("factor");
		return new MassLossingBody(id, pos, vel, mass, acc, freq, fac);
	}

}
