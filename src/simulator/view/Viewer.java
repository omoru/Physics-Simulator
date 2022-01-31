package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;
import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

@SuppressWarnings("serial")
public class Viewer extends JComponent implements SimulatorObserver {
	private static final int _WIDTH = 1000;
	private static final int _HEIGHT = 1000;
	// A�ade constantes para los colores
	private int _centerX;
	private int _centerY;
	private double _scale;
	private List<Body> _bodies;
	private boolean _showHelp;
	private boolean first = true;
	
	Viewer(Controller ctrl) {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(	BorderFactory.createLineBorder(Color.black, 2), "Viewer",TitledBorder.LEFT
		,TitledBorder.TOP));
		
		initGUI();
		ctrl.addObserver(this);
		
	}
	
	public  int getStaticWidth() {
		return _WIDTH;
	}

	public  int getStaticHeight() {
		return _HEIGHT;
	}
	
	private void initGUI() {
		// Suma border con title
		//autoScale();
		_bodies = new ArrayList<>();
		_scale = 1.0;
		_showHelp = true;
		addKeyListener(new KeyListener() {
			// Completa con m�todos de la interfaz
			@Override
			public void keyPressed(KeyEvent e) {
			switch (e.getKeyChar()) {
				case '-': _scale = _scale * 1.1;break;
				case '+': _scale = Math.max(1000.0, _scale / 1.1);break;
				case '=': autoScale();break;
				case 'h': _showHelp = !_showHelp;break;
				default:
			}
			
			repaint();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		addMouseListener(new MouseListener() {
			// Completa con m�todos de la interfaz
			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			});
		
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		// Usa �gr� para dibujar, no �g�
		_centerX = getWidth() / 2;
		_centerY = getHeight() / 2;
		
		// Dibuja una cruz en el centro
		gr.setColor(Color.red);
		gr.drawLine(_centerX, _centerY + 5, _centerX, _centerY - 5);
		gr.drawLine(_centerX + 5, _centerY, _centerX - 5, _centerY);
		// Dibuja los bodies
		for(Body b: this._bodies) {
			gr.setColor(Color.blue);
			int x =  _centerX + (int) (b.getPossition().coordinate(0)/_scale);
			int y = _centerY - (int) (b.getPossition().coordinate(1)/_scale);
			gr.fillOval(x - 5, y - 5, 10, 10 );
			gr.setColor(Color.black);
			gr.drawString(b.getId(), x - 5, y - 5);
			
		}
		
		// Dibuja help si _showHelp es true
		if(this._showHelp) {
			gr.setColor(Color.red);
			gr.drawString("h: toggle help, +: zoom-in, -: zoom-out, =: fit", 10, 25);
			gr.drawString("Scaling ratio: " + this._scale, 10, 40);
		}
		
		if(this.first) {
			autoScale();
			this.first = false;
		}
		
	}
	
	// A�ade otros metodos
	private void autoScale() {
		double max = 1.0;
		for (Body b : _bodies) {
			Vector p = b.getPossition();
			for (int i = 0; i < p.dim(); i++)
			max = Math.max(max,
			Math.abs(b.getPossition().coordinate(i)));
		}
		double size = Math.max(1.0, Math.min((double) getWidth(), (double) getHeight()));
		_scale = max > size ? 4.0 * max / size : 2.0;
	}
	
	
	
	// A�ade metodos de SimulatorObserver

	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				_bodies = bodies;
				autoScale();
				repaint();	
			}
		});	
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				_bodies = bodies;
				autoScale();
				repaint();	
			}
		});	
			
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				_bodies = bodies;
				autoScale();
				repaint();	
			}
		});	
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				_bodies = bodies;
				repaint();	
			}
		});	
		
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		
	}

	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		
	}
}
