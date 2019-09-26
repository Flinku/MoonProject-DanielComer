import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;



public class MainWindow implements KeyListener{
    //Window stuff
    JFrame mainFrame;
    PaintPanel panel;

    //Arrays of objects to be drawn
    ArrayList<SpriteObj> lowerDrawList = new ArrayList<SpriteObj>();
    ArrayList<SpriteObj> upperDrawList = new ArrayList<SpriteObj>();
    String[] text = {"vx: ", "vy: ", "Total speed: ", "dv", "altitude: ", "Speed factor: "};

    //Variables for seeing what keys are being pressed
    boolean aPressed = false;
    boolean dPressed = false;
    boolean spacePressed = false;
    boolean escPressed = false;
    boolean downPressed = false;
    boolean downUsed = false;
    boolean upPressed = false;
    boolean upUsed = false;

    boolean[] keyArray = new boolean[]{aPressed, dPressed, spacePressed, escPressed};

    MainWindow(){
        //Sets up the window
        mainFrame = new JFrame();
        panel = new PaintPanel();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 800);
        mainFrame.add(panel);
        mainFrame.setVisible(true);
        //Lets it listen for key presses
        mainFrame.addKeyListener(this);

    }
    //Adds an image to the upper list, for things that need to go above the forground
    void addUpper(SpriteObj i){
        upperDrawList.add(i);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    void addLower(SpriteObj i){
        lowerDrawList.add(i);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
    //Clears out everything in the two draw list arrays
    void clearDraws(){
        lowerDrawList.clear();
        upperDrawList.clear();
    }
    //Quick way to call repaint() and revalidate()
    void refresh(){
        mainFrame.repaint();
        mainFrame.revalidate();
    }
    //I don't use this but it's necessary for the KeyEvent to work
    @Override
    public void keyTyped(KeyEvent e) {
    }
    //Tells when the key is depressed
    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == 65){
            aPressed = true;
        }
        if (e.getKeyCode() == 68){
            dPressed = true;
        }
        if (e.getKeyCode() == 32){
            spacePressed = true;
        }
        if (e.getKeyCode() == 40) {
            downPressed = true;
        }
        if (e.getKeyCode() == 38) {
            upPressed = true;
        }
    }
    //Tells when the key is released
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 65){
            aPressed = false;
        }
        if (e.getKeyCode() == 68){
            dPressed = false;
        }
        if (e.getKeyCode() == 32){
            spacePressed = false;
        }
        if (e.getKeyCode() == 40) {
            downPressed = false;
            downUsed = false;
        }
        if (e.getKeyCode() == 38) {
            upPressed = false;
            upUsed = false;
        }

    }

    //Draws on the frame using the two sprite list arrays
    public class PaintPanel extends JPanel{
        @Override
        public void paintComponent(Graphics g) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());

            for (SpriteObj i: lowerDrawList) {
                g.drawImage(i.sprite, i.x, i.y, panel);
            }
            for (SpriteObj i: upperDrawList) {
                g.drawImage(i.sprite, i.x, i.y, panel);
            }

            g.setColor(Color.WHITE);
            Font font = new Font("Monospaced", Font.PLAIN, 12);
            g.setFont(font);
            int loc = 10;
            for (String i: text){
                g.drawString(i, 10, loc);
                loc += 15;
            }

        }
    }

}
