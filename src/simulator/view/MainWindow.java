package simulator.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.FileNotFoundException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;


import simulator.control.Controller;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	
	ControlPanel control_panel; //toolbar
	BodiesTable bodies_table;
	Viewer viewer;
	StatusBar status_bar;
	Controller _ctrl;
	
	public MainWindow(Controller ctrl) throws FileNotFoundException {
		super("Physics Simulator");
		_ctrl=ctrl;
		initGUI();
		setExtendedState(MAXIMIZED_BOTH);
	}
	
	
	
	private void initGUI() throws FileNotFoundException {
		
		JPanel panelPrincipal = this.creaPanelPrincipal(); 
		this.setContentPane(panelPrincipal);
		this.addControlPanel(panelPrincipal);
	
		JPanel panelCentral = this.createPanelCentral(); 
		
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

		panelPrincipal.add(panelCentral,BorderLayout.CENTER);
		
		this.createPanelSuperior(panelCentral);
		
		this.createPanelInferior(panelCentral); 
		
		this.addStatusBar(panelPrincipal);
		
		this.pack();
		this.setVisible(true);
	}
	
	private void addControlPanel(JPanel panelPrincipal) {
		this.control_panel = new ControlPanel(this._ctrl);
		panelPrincipal.add(this.control_panel, BorderLayout.PAGE_START);

	}
	
	private void createPanelSuperior(JPanel panelCentral) throws FileNotFoundException {
		JPanel panelSuperior = new JPanel();
		panelSuperior.setLayout(new BoxLayout (panelSuperior,BoxLayout.Y_AXIS));
		
		this.bodies_table = new BodiesTable(_ctrl);
		
		panelSuperior.add(this.bodies_table);
		
		panelCentral.add(panelSuperior);
	}
	
	private void createPanelInferior(JPanel panelCentral) {
		
		JPanel panelInferior = new JPanel();
		panelInferior.setLayout(new BoxLayout (panelInferior,BoxLayout.Y_AXIS));
		this.viewer = new Viewer(_ctrl);
		
		panelInferior.add(this.viewer);
		
		panelCentral.add(panelInferior);
		
	}
	
	private void addStatusBar(JPanel panelPrincipal) {
		this.status_bar = new StatusBar(_ctrl);
		panelPrincipal.add(this.status_bar, BorderLayout.PAGE_END);
	}
	
	private JPanel creaPanelPrincipal() {
		JPanel panelSuperior = new JPanel();
		panelSuperior.setLayout(new BorderLayout());
		return panelSuperior;
	}
	
	private JPanel createPanelCentral() {
		JPanel panelCentral = new JPanel();
		panelCentral.setLayout(new GridLayout(2,1)); 
		return panelCentral;
	}
}
