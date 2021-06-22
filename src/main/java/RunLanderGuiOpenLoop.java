import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class RunLanderGuiOpenLoop {

    private final static int SCREEN_WIDTH       = 750;
    private final static int SCREEN_HEIGHT      = 750;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Train Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.add(new LanderCanvas());
        frame.setVisible(true);
    }

}

class LanderCanvas extends JComponent {

    private Image backgroundImage;
    private final static int SCREEN_WIDTH       = 750;
    private final static int SCREEN_HEIGHT      = 750;
    private final int LOWER_END_DRAWINGS = 600;
    private Vector landerPos;
    Image landerImage;

    public LanderCanvas() {
        try {
            landerImage = ImageIO.read(new File("solarimages/lander.png"));
            backgroundImage = ImageIO.read(new File("solarimages/sad.jpg"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        double wind = 0;
        Lander[] landerPositions = OpenLoop.getPosCoordinatesForFullFlight(wind);
        Thread animationThread = new Thread(new Runnable() {
            public void run() {
                for (int i=0; i < landerPositions.length;i++) {
                    landerPos = landerPositions[i].getPosition();
                    repaint();
                    try {Thread.sleep(10);} catch (Exception ex) {}
                }
                //System.out.println("Final pos");
                //System.out.println(landerPos);

            }
        });

        animationThread.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, this);
        g.setColor(new Color((float) 0.5, (float) 0.2, (float) 0.0));
        g.fillRoundRect(0, LOWER_END_DRAWINGS, SCREEN_WIDTH, SCREEN_HEIGHT, 1000000000,150);

        Image scaledLanderImage = landerImage.getScaledInstance(50,50, Image.SCALE_DEFAULT);
        int landerX = 3 * (int) (landerPos.getX()/100+200);
        int landerY = (int) SCREEN_HEIGHT - (int) (landerPos.getY()/300+175);

        //System.out.println(landerX);
        //System.out.println(landerY);

        g.drawImage(scaledLanderImage, landerX, landerY, this);



    }


}
