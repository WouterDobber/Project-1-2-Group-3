import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class RunLanderGuiClosedLoop {

    public static void main(String[] args) {
        new RunLanderGuiClosedLoop();
    }

    private LandSystem landingModule = new LandSystem("lander", 300, -300/1000D, 600/1000D, -10/1000D,-10/1000D);
    private UI ui = new UI();

    public RunLanderGuiClosedLoop() {
        update();
    }

    public void update() {
        while (true) {
            landingModule.updateVelocity();
            landingModule.updatePosition();
            landingModule.update();
            if(landingModule.getLocation().getY() < 0.001){
                //System.out.println(landingModule.getLocation().getX());
                //System.out.println(landingModule.getVelocity().getY());
                break;
            }
            ui.repaint();
            try {
                Thread.sleep(120);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class UI extends JPanel {

        private final int SCREEN_WIDTH = 750;
        private final int SCREEN_HEIGHT = 750;
        private final int LOWER_END_DRAWINGS = 600;

        private Image backgroundImage;

        private JFrame frame = new JFrame();

        public UI() {
            this.frame.add(this);
            this.frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
            this.frame.setVisible(true);
            this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        @Override
        public void paintComponent(Graphics g) {
            try {backgroundImage = ImageIO.read(new File("solarimages/sad.jpg"));}
            catch(Exception e) {e.printStackTrace();}

            g.drawImage(backgroundImage, 0, 0, this);
            g.setColor(new Color((float) 0.5, (float) 0.2, (float) 0.0));
            g.fillRoundRect(0, LOWER_END_DRAWINGS, frame.getWidth(), SCREEN_HEIGHT, 1000000000,150);


            Vector screen = toScreenCoordinates((Vector) landingModule.getLocation());
            g.setColor(Color.RED);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            {
                if (landingModule.getLocation().getY() >= 0.001) {
                    if (landingModule.leftThruster.getForce(1.0).getX() > 0) {
                        Vector3dInterface left = screen.add(new Vector(-landingModule.leftThruster.getForce(1.0).getX()/5, 0, 0));
                        g2.drawLine((int) screen.getX(), (int) screen.getY() - 20, (int) left.getX(), (int) left.getY() - 20);
                    }
                    if (landingModule.rightThruster.getForce(1.0).getX() < 0) {
                        Vector3dInterface right = screen.add(new Vector(-landingModule.rightThruster.getForce(1.0).getX()/5, 0, 0));
                        g2.drawLine((int) screen.getX(), (int) screen.getY() - 20, (int) right.getX(), (int) right.getY() - 20);
                    }

                    if (landingModule.bottomThruster.getForce(1.0).getY() > 0) {
                        Vector3dInterface bottom = screen.add(new Vector(0, landingModule.bottomThruster.getForce(1.0).getY()/15, 0));
                        g2.drawLine((int) screen.getX(), (int) screen.getY(), (int) bottom.getX(), (int) bottom.getY());
                    }
                }
            }
            try{
                Image landerImage = ImageIO.read(new File("solarimages/lander.png"));
                Image scaledLanderImage = landerImage.getScaledInstance(50,50, Image.SCALE_DEFAULT);
                g.drawImage(scaledLanderImage, (int) (screen.getX() - (25)), (int) (screen.getY() - (45)), this);
            }
            catch(Exception e) {
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect((int) (screen.getX() - (1.5)), (int) (screen.getY() - (1.5)), (int) 5, (int) 5);
            }
        }

        public Vector toScreenCoordinates(Vector vec) {
            return new Vector((vec.getX() + 300.0), (600 - vec.getY()),0);
        }
    }
}
