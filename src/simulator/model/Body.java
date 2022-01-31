package simulator.model;

import simulator.misc.Vector;

public class Body {
	
	protected String id;
	protected Vector velocity;
	protected Vector acceleration;
	protected Vector possition;
	protected double mass;
	
	public Body(String id, Vector possition, Vector velocity, double mass, Vector acceleration) {
		this.id = id;
		this.possition = possition;
		this.velocity = velocity;
		this.mass = mass;
		this.acceleration = acceleration;
	}
	
	//------------------------------------------------------------------------------------------------------------
	
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Vector getVelocity() {
		return new Vector(velocity);
	}
	public void setVelocity(Vector velocity) {
		this.velocity = new Vector(velocity);
	}
	public Vector getAcceleration() {
		return new Vector(acceleration);
	}
	public void setAcceleration(Vector aceleration) {
		this.acceleration = new Vector(aceleration);
	}
	public Vector getPossition() {
		return new Vector(possition);
	}
	public void setPossition(Vector possition) {
		this.possition = new Vector(possition);
	}
	public double getMass() {
		return this.mass;
	}
	public void setMass(double mass) {
		this.mass = mass;
	
	}
	//---------------------------------------------------------------------------------------------------------------
	
	void move(double t) {
		this.possition = this.possition.plus(this.velocity.scale(t)).plus(this.acceleration.scale((t*t)/2));
		this.velocity = this.velocity.plus(this.acceleration.scale(t));
	}
	
	public String toString() {
		
		return " { \"id\": \"" + this.id + "\", \"mass\": "+ this.mass + ", \"pos\": " + this.possition.toString() + 
				", \"vel\": " + this.velocity.toString() + ", \"acc\": " + this.acceleration.toString() + " }";
	}
	
}
