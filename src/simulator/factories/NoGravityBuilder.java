package simulator.factories;

import org.json.JSONObject;

import simulator.model.GravityLaws;
import simulator.model.NoGravity;

public class NoGravityBuilder extends Builder<GravityLaws> {
	
	public NoGravityBuilder() {
		this.typeTag = "ng";
		this.desc = "No Gravity";
	}

	@Override
	protected GravityLaws createTheInstance(JSONObject jsonObject) {
		return new NoGravity();
	}

}
