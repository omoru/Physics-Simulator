package simulator.view;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONObject;

import simulator.model.Body;
import simulator.model.SimulatorObserver;
import simulator.control.*;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel implements SimulatorObserver {
	
	private Controller _ctrl;
	private JButton loadBodies;
	private JButton gravityLaws;
	private JButton start;
	private JButton reset;
	private JButton stop;
	private JButton exit;
	private JSpinner steps;
	private JSpinner delay;
	private JTextField dt;
	private volatile Thread _thread;
	
	public ControlPanel(Controller c) {
		_ctrl=c;
		initGUI();
		_ctrl.addObserver(this);
		
	}
	
	class GestorBoton implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			JButton botonPulsado=(JButton)e.getSource();
			if(botonPulsado==loadBodies) {
				try {
					loadButtonAction();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			else if(botonPulsado==gravityLaws) {
				gLawsButtonAction();
			}
			
			else if(botonPulsado==start) {
				try {
					RunButton();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			else if(botonPulsado==stop) {
				stopButton();
			}
			else if(botonPulsado==exit) {
				exitButton();
			}
			
			else if(botonPulsado==reset) {
				resetButton();
			}
			
		}
	}

	 
	private void loadButtonAction() throws FileNotFoundException {
		
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		    "txt & json", "txt", "JSONObject");
		chooser.setCurrentDirectory(new File("resources/examples"));
		chooser.setFileFilter(filter);
		
		int returnVal = chooser.showOpenDialog(loadBodies.getParent());
		if(returnVal == JFileChooser.APPROVE_OPTION) {
		   System.out.println("You chose to open this file: " +
		        chooser.getSelectedFile().getName());
		}
		
		_ctrl.reset();
		InputStream is = new FileInputStream(chooser.getSelectedFile());
		_ctrl.loadBodies(is);
		
	}
	
	private void gLawsButtonAction() {
		
		final String[] posiblevalues = { "ftcg" , "nlug", "ng" };
		
		String option=(String) JOptionPane.showInputDialog(null,"Select a law", "Gravity Laws",
				JOptionPane.QUESTION_MESSAGE, new ImageIcon("src/icons/physics.png"),posiblevalues,posiblevalues[0]);
		List<JSONObject> factElem = this._ctrl.getGravityLawsFactory().getInfo();
		for(JSONObject jO: factElem) {
			if(jO.get("type").equals(option)) this._ctrl.setGravityLaws(jO);
		}
		
		
	}
	
	private void RunButton() throws InterruptedException {
		
		disableButtons(); 
		this._thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Integer n= (Integer)steps.getValue();
					Double delta= Double.parseDouble(dt.getText());
					_ctrl.setDeltaTime(delta);
					Integer d = (Integer)delay.getValue();
					run_sim(n, d);
					enableButtons();
					_thread = null;				
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		_thread.start();
		
	}
	
	private void stopButton() {
		
		if(this._thread != null) {
			this._thread.interrupt();
		}
		
	}
	
	private void exitButton() {
		int dialog=JOptionPane.YES_NO_OPTION;
		int dialogResult= JOptionPane.showConfirmDialog(null,"¿Está seguro de que desea salir del programa?","EXIT",dialog);
		
		if(dialogResult==JOptionPane.YES_OPTION) {
			System.exit(0);
		}
		
		
	}
	
	private void resetButton() {
		this._ctrl.reset();
	}
	
	private void initGUI() {
		
		JToolBar toolBar = new JToolBar();
		this.loadBodies = new JButton(new ImageIcon("src/icons/open.png"));
		this.gravityLaws = new JButton(new ImageIcon("src/icons/physics.png"));
		this.start = new JButton(new ImageIcon("src/icons/run.png"));
		this.stop = new JButton(new ImageIcon("src/icons/stop.png"));
		this.reset = new JButton(new ImageIcon("src/icons/clear.png"));
		this.exit = new JButton(new ImageIcon("src/icons/exit.png"));
		this.steps = new JSpinner();
		this.steps.setPreferredSize(new Dimension(80,10));
		this.dt = new JTextField(Double.toString(this._ctrl.getDeltaTime()));
		this.dt.setPreferredSize(new Dimension(80,10));
		SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 1000, 1);
		this.delay = new JSpinner(model);
		this.delay.setPreferredSize(new Dimension(80,10));
		
	
		
		this.loadBodies.setToolTipText("Load bodies file into the editor");
		this.gravityLaws.setToolTipText("Choose gravity law");
		this.start.setToolTipText("Start the simulation");
		this.stop.setToolTipText("Stop the simulation");
		this.reset.setToolTipText("Clean data");
		this.exit.setToolTipText("Exit");
		JLabel s = new JLabel("Steps: ");
		JLabel r= new JLabel("Delta-time: ");
		JLabel d = new JLabel("Delay: ");
		
		toolBar.add(this.loadBodies);
		toolBar.add(this.gravityLaws);
		toolBar.add(this.start);
		toolBar.add(this.stop);
		toolBar.add(this.reset);
		toolBar.add(d);
		toolBar.add(this.delay);
		toolBar.add(s);
		toolBar.add(this.steps);
		toolBar.add(r);
		toolBar.add(this.dt);
		toolBar.add(this.exit);
		
		GestorBoton gestor= new GestorBoton();
		
		loadBodies.addActionListener(gestor);
		gravityLaws.addActionListener(gestor);
		start.addActionListener(gestor);
		stop.addActionListener(gestor);
		reset.addActionListener(gestor);
		exit.addActionListener(gestor);
		
		add(toolBar);

	}
	
	private void run_sim(int n, int delay) throws InterruptedException {
		if(n<=0)  JOptionPane.showMessageDialog(null,"El numero de 'steps' debe de ser mayor que 0.");

		while(n>0 && !Thread.interrupted()) {
			try{
				_ctrl.run(1);
				Thread.sleep(delay);
				n--;
			} catch(Exception e) {
				enableButtons();
				return;
			}
			
		}
		
	}
	
	private void enableButtons() {
		this.loadBodies.setEnabled(true);
		this.gravityLaws.setEnabled(true);
		this.start.setEnabled(true);
		this.stop.setEnabled(true);
		this.reset.setEnabled(true);
		this.delay.setEnabled(true);
		this.steps.setEnabled(true);
		this.dt.setEnabled(true);
		
	}
	
	private void disableButtons() {
		this.loadBodies.setEnabled(false);
		this.gravityLaws.setEnabled(false);
		this.start.setEnabled(false);
		this.reset.setEnabled(false);
		this.delay.setEnabled(false);
		this.steps.setEnabled(false);
		this.dt.setEnabled(false);
		
	}
	

	
	
	private void update(double time) {
		
		SwingUtilities.invokeLater(new Runnable() {
				
			@Override
			public void run() {
				// TODO Auto-generated method stub
				_ctrl.setDeltaTime(time);
			}
		});
			
	}
	

	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		update(time);
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		update(time);	
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		update(dt);
	}

	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		// TODO Auto-generated method stub
		
	}

}
