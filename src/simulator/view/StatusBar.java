package simulator.view;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

@SuppressWarnings("serial")
public class StatusBar extends JPanel implements SimulatorObserver {
	
	private JLabel _currTime;
	private JLabel _currLaws;
	private JLabel _numOfBodies;
	
	StatusBar(Controller ctrl){
		initGUI();
		ctrl.addObserver(this);
	}
	
	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		this._currLaws=new JLabel();
		this._currTime=new JLabel();
		this._numOfBodies=new JLabel();
		
		add(_currTime);
		add(_numOfBodies);
		add(_currLaws);
		
		
	}


	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				_currTime.setText("Time:  "+ Double.toString(time)+"    ");
				_numOfBodies.setText("Bodies:  "+ bodies.size()+"    ");
				_currLaws.setText("Laws:  "+gLawsDesc+"    ");
			}
		});
		
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				_currTime.setText("Time:  "+ Double.toString(time)+"    ");
				_numOfBodies.setText("Bodies:  "+ bodies.size()+"    ");
				_currLaws.setText("Laws:  "+gLawsDesc+"    ");
			}
		});


	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				_numOfBodies.setText("Bodies:  "+ bodies.size()+"    ");
			}
		});
		
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				_currTime.setText("Time:  "+ Double.toString(time)+"    ");
			}
		});

	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				_currLaws.setText("Laws:  "+gLawsDesc+"    ");
			}
		});
		
	}

}
