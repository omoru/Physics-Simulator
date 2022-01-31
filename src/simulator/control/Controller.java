package simulator.control;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

public class Controller {
	private PhysicsSimulator physicsSimulator;
	private Factory<Body> bodies;
	private Factory<GravityLaws> glaws;
	
	public Controller(PhysicsSimulator physicsSimulator, Factory<Body> bodies,Factory<GravityLaws> glaws) {
		this.physicsSimulator = physicsSimulator;
		this.bodies = bodies;
		this.glaws=glaws;
	}

	public double getDeltaTime() {
		return physicsSimulator.getTimeSteps();
	}
	
	public void reset() {
		physicsSimulator.reset();
	}
	
	public void setDeltaTime(double dt) {
		physicsSimulator.setDeltaTime(dt);
	}
	
	public void addObserver(SimulatorObserver o) {
		physicsSimulator.addObserver(o);
	}
	
	public void run(int n) {
		if(n<=0)JOptionPane.showMessageDialog(null,"El numero de pasos debe de ser mayor que 0");
		else {
			
			 for(int i=0; i <= n;++i) physicsSimulator.advance();	
		}
		
	}
	
	public Factory<GravityLaws> getGravityLawsFactory(){
		return this.glaws;
	}
	
	public void setGravityLaws(JSONObject info) {
		this.physicsSimulator.setGravityLaws(glaws.createInstance(info));
	}
	
	
	//With output
	public void run(int n, OutputStream out) throws IOException {
		OutputStreamWriter w = new OutputStreamWriter(out);
		BufferedWriter bw = new BufferedWriter(w);
		String report = "{\n" + "\"states\": [\n";
		if(n<=0)JOptionPane.showMessageDialog(null,"El numero de pasos debe de ser mayor que 0");
		for(int i=0; i <= n;++i) {
			try {
				report += ("{ "+ physicsSimulator.toString()+" },\n");
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			physicsSimulator.advance();			
		}
		report = report.substring(0, report.length() - 2) + "\n]\n}";
		bw.write(report);
		bw.flush();
	}
		
	
	public void loadBodies(InputStream in) {
		JSONObject jsonIn = new JSONObject(new JSONTokener(in));
		JSONArray bodies = jsonIn.getJSONArray("bodies");
		for(int i = 0; i < bodies.length(); i++)
			this.physicsSimulator.addBody(this.bodies.createInstance(bodies.getJSONObject(i)));
	}
	
}
