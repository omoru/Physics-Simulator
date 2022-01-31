package simulator.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class PhysicsSimulator {

	private GravityLaws gravityLaws;
	private List<Body> bodies;
	private List<SimulatorObserver> observers;
	private double currentTime;
	private double timeSteps;//dt
	
	public PhysicsSimulator(GravityLaws gravityLaws, double timeSteps) {
		this.gravityLaws = gravityLaws;
		this.timeSteps = timeSteps;
		
		this.currentTime = 0;
		this.bodies = new ArrayList<Body>();
		this.observers=new ArrayList<SimulatorObserver>();
	}
	
	public PhysicsSimulator(GravityLaws gravityLaws) {
		this.gravityLaws = gravityLaws;
		this.currentTime = 0;
		this.bodies = new ArrayList<Body>();
		this.observers=new ArrayList<SimulatorObserver>();
	}
	
	//----------------------------------------------------------------------------------------------------------------
	
	public double getTimeSteps() {
		return timeSteps;
	}
	
	public void advance() {
		this.gravityLaws.apply(this.bodies);
		for(Body b: this.bodies)
			b.move(this.timeSteps);
		this.currentTime += this.timeSteps;
		for(int i=0; i< observers.size();++i) observers.get(i).onAdvance(bodies,currentTime);
	}
	
	public void reset() {
		bodies.clear();
		currentTime=0.0;
		for(int i=0; i< observers.size();++i) observers.get(i).onReset(bodies, currentTime, timeSteps, gravityLaws.toString());
	}
	
	public void setDeltaTime(double dt) {
		if(dt < 0) JOptionPane.showMessageDialog(null,"Delta-Time debe de ser mayor que 0.");
		else{
			this.timeSteps=dt;
			for(int i=0; i< observers.size();++i) observers.get(i).onDeltaTimeChanged(dt);
		}
		
	}
	
	public void setGravityLaws(GravityLaws gravityLaws) {
		if(gravityLaws==null) JOptionPane.showMessageDialog(null,"No se ha cambiado la ley de la gravedad.");
		else {
			this.gravityLaws=gravityLaws;
			for(int i=0; i< observers.size();++i) observers.get(i).onGravityLawChanged(gravityLaws.toString());
		}
	}
	
	
	public void addBody(Body b) {
		if(this.bodies.contains(b))  JOptionPane.showMessageDialog(null,"Un cuerpo ya pertenece a la lista de bodies");
		else {
			this.bodies.add(b);
			for(int i=0; i< observers.size();++i) observers.get(i).onBodyAdded(bodies,b);
		}
	}
	
	public void addObserver(SimulatorObserver o){
		if(!this.observers.contains(o)) {
			observers.add(o);
			observers.get(observers.size()-1).onRegister(bodies, currentTime, timeSteps, gravityLaws.toString());
		}
	}
	
	@Override
	public String toString() {
		return "\"time\": " + this.currentTime + ", \"bodies\": " + this.bodies.toString();
	}

	
}
