
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.*;
import java.awt.*;

public class Run implements ActionListener {
	JPanel buttons = new JPanel();
	private static int scaleFactor = 1;
	private static int maxScale = 12;
	private JButton zoomIn;
	private JButton zoomOut;
	private JComboBox zoomPlanet;
	private JButton selectBody;
	private final String zoomInStr = "Zoom in", zoomOutStr = "Zoom out";
	Image rocket = new ImageIcon("solarimages/rocket.png").getImage();
	public final static double graphicConstant = 8.49598E9; // IDEA: by dividing the coordinates by the same number,
	// they all get scaled down.. this number
	// used to be the astronomic unit, i changed it a bit by playing around.

	public static void main(String[] args) {
		new Run();
	}

	private JFrame frame = new JFrame();
	public SolarSystem solarSystem = new SolarSystem();
	static State state;

	public Run() {
		start();
	}

	public void start() {
		state = new State(solarSystem.bodies);
		frame.setSize(800, 800);
		frame.setContentPane(new UniFrame());
		zoomIn = new JButton(zoomInStr);
		zoomOut = new JButton(zoomOutStr);
		zoomIn.addActionListener(this);
		zoomOut.addActionListener(this);
		buttons.add(zoomIn);
		buttons.add(zoomOut);
		frame.add(buttons);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Adding probe
		CelestialBody Probe = new CelestialBody("rocket", rocket, 15000, -1.471886208478151E11, -2.861522074209465E10, 8170057.668900404, 27962.61762782645, -62349.24395947284, -666.7403073700751);
		solarSystem.bodies.add(Probe);

		SolarSystemSolver solver = new SolarSystemSolver();
		NewtonsFunction function = new NewtonsFunction();

		int numStepsPerUpdate = 5; // more steps planets move faster
		// seconds in 1 year: 31556952
		// run for: j*numStepsPerUpdate= total number of seconds passed
		for (int j = 0; j * numStepsPerUpdate < 31556952; j++) {
			state = (State) solver.step(function, j * numStepsPerUpdate, state, numStepsPerUpdate);
			frame.getContentPane().repaint();
		}

		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		String InOrOut = e.getActionCommand();
		if (InOrOut.equals(zoomInStr) && maxScale != scaleFactor) {
			zoomIn.setEnabled(true);
			++scaleFactor;
			for (int i = 0; i < solarSystem.bodies.size(); i++) {
				solarSystem.bodies.get(i).getLocation().mul(scaleFactor);
				// solarSystem.bodies.get(i).setLocation(solarSystem.bodies.get(i).getLocation().mul(scaleFactor));
			}

		} else if (InOrOut.equals(zoomOutStr)) {
			if (scaleFactor != 1) {
				--scaleFactor;
				zoomOut.setEnabled(true);
			}
			for (int i = 0; i < solarSystem.bodies.size(); i++) {
				solarSystem.bodies.get(i).getLocation().mul(scaleFactor);
			}
		}
	}

	public class UniFrame extends JPanel {

		public UniFrame() {
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			// background black
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 1800, 1280);

			for (CelestialBody body : state.celestialBodies) {
				g.setColor(Color.WHITE);
				// System.out.println(body.toString());

				double positioning = (this.getSize().getWidth() / 2) / scaleFactor; // by adding to this number, where
				// all planets get located changes,
				// so they all move (the centre changes)

				int x = (int) (body.getLocation().getX() / graphicConstant + positioning) * scaleFactor;
				int y = (int) (body.getLocation().getY() / graphicConstant + positioning) * scaleFactor;
				int size = (int) (4 * scaleFactor + body.getRadius());
				g.drawString(body.getName(), x + size - 15, y + 17);
				// g.drawOval(x - 15, y-1, 10, 10);
				g.drawImage(body.getImage(), x - 15, y - 1, size, size, null, this);


			}
		}
	}
}