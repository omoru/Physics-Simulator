package simulator.model;

import java.util.List;

public class FallingToCenterGravity implements GravityLaws {
	
	private final double GRAVITY = 9.81;
	
	public FallingToCenterGravity() {};
	
	public void apply(List<Body> bodies){
		
		for(Body b: bodies)	b.setAcceleration(b.getPossition().direction().scale(-GRAVITY));
		
	}
	
public String toString() {
		
		return "Falling to center";
		
	}
	
}
