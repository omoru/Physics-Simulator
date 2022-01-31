package simulator.model;

import simulator.misc.Vector;

public class MassLossingBody extends Body{
	private double lossFactor;
	private	double lossFrequency;
	private double c;
	
	public MassLossingBody(String id, Vector possition, Vector velocity, double mass, Vector acceleration, 
			double lossFrequency, double lossFactor) {
		super(id, possition, velocity, mass, acceleration);
		this.lossFrequency = lossFrequency;
		this.lossFactor = lossFactor;
		this.c = 0.0;
		
	}
	
	//-----------------------------------------------------------------------------------------------------------
	
	public double getLossFactor() {
		return lossFactor;
	}

	public void setLossFactor(double lossFactor) {
		this.lossFactor = lossFactor;
	}

	public double getLossFrequency() {
		return lossFrequency;
	}

	public void setLossFrequency(double lossFrequency) {
		this.lossFrequency = lossFrequency;
	}
	
	//-----------------------------------------------------------------------------------------------------------

	void move (double t) {
		super.move(t);
		if(this.c >= this.lossFrequency) {
			this.mass = this.mass * (1 - this.lossFactor);
			this.c = 0.0;
		}
		else this.c++;
		
	}
	/*
	public String toString1() {
		return super.toString().substring(0, super.toString().length() - 1) + 
				", \"lossFac\": " + this.lossFactor + ", \"lossFreq\": " + this.lossFrequency + " }";
	}
	*/
}