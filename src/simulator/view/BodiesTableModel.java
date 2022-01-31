package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

@SuppressWarnings("serial")
public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver{ 
	
	 String[] columNames = {"id", "Mass", "Position", "Velocity", "Acceleration"};      
	//private Object[][] data;
	private List<Body> bodies; 
	
	BodiesTableModel(Controller ctrl){
		this.bodies=new ArrayList<Body>();
		
		ctrl.addObserver(this); 
	} 
	@Override
	public int getRowCount() { 
		return bodies.size();
	} 
	@Override
	public int getColumnCount() { 
		return this.columNames.length;
	}
	@Override
	public String getColumnName(int column) { 
		String s= new String();
		switch(column) {
			case 0: s=columNames[0];break;
			case 1: s=columNames[1];break;
			case 2: s= columNames[2];break;
			case 3: s=columNames[3];break;
			case 4: s=columNames[4];break;
		}
		return s;
	} 
	
	@Override
	public Object getValueAt(int rowIndex,int columnIndex) { 
		Object b=new Object();
		switch(columnIndex) {
			case 0: b= this.bodies.get(rowIndex).getId();break;
			case 1: b= this.bodies.get(rowIndex).getMass();break;
			case 2: b= this.bodies.get(rowIndex).getPossition();break;
			case 3: b= this.bodies.get(rowIndex).getVelocity();break;
			case 4: b= this.bodies.get(rowIndex).getAcceleration();
		}
		return b;
	}
	 
	
	private void update(List<Body> b) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				bodies=b;
				fireTableStructureChanged();
			}
		});		
		
	}
	
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		update(bodies);
	}
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		update(bodies);
		
	}
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		update(bodies);
		
	}
	@Override
	public void onAdvance(List<Body> bodies, double time) {
		update(bodies);
		
	}
	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		// TODO Auto-generated method stub
		
} 
}