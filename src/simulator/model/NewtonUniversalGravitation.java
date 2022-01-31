package simulator.model;

import java.util.List;
import simulator.misc.Vector;

public class NewtonUniversalGravitation implements GravityLaws {
	
	private final double G = 6.67e-11;
	
	public NewtonUniversalGravitation() {}
	
	public void apply(List<Body> bodies){
		
		double fuerza=0;
		Vector direccion;
		
		for(int i=0; i < bodies.size();++i) {
				
			if(bodies.get(i).getMass() == 0.0) {
				Vector orig= new Vector(bodies.get(i).getPossition().direction().dim());
				bodies.get(i).setAcceleration(orig);
				bodies.get(i).setVelocity(orig);
			}
			
			else {	
				Vector F = new Vector(bodies.get(i).getPossition().direction().dim());
			
				for(int j=0;j< bodies.size();j++) {
				
					if(i!=j) { 
						double distance = bodies.get(j).getPossition().distanceTo(bodies.get(i).getPossition());
						fuerza = G * ((bodies.get(i).getMass() * bodies.get(j).getMass())/Math.pow(distance,2));
						direccion = bodies.get(j).getPossition().minus(bodies.get(i).getPossition()).direction();
						F= F.plus(direccion.scale(fuerza));									
					}
								
				}
				
				bodies.get(i).setAcceleration((F.scale(1/bodies.get(i).getMass())));
			}		
		}	
	}
	

	public String toString() {
		
		return "Newton law of universal gravitation";
		
	}
}