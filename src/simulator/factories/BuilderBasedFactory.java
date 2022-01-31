package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T>{
	
	List<Builder<T>> builders;
	List<JSONObject> factElem;

	
	public BuilderBasedFactory(List<Builder<T>> builders) {	
		this.builders = new ArrayList<Builder<T>>(builders);		
		
	}
	
	@Override
	public T createInstance(JSONObject info) {
		try {
			//ver si info es correcto
			T enc = null;
			for(Builder<T> b: this.builders) {
				enc = b.createInstance(info);
				if(enc != null) return enc;
			}
			
		}catch(IllegalArgumentException e) {
			System.out.println("Informacion incorrecta");
		}
		return null;
	}

	
	@Override
	public List<JSONObject> getInfo() {
		this.factElem = new ArrayList<JSONObject>();
		for(Builder<T> b: this.builders)
			this.factElem.add(b.getBuilderInfo());
		return this.factElem;
	}

}
