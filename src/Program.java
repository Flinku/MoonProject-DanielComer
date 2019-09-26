import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Program {

    Timer fpsTimer;
    MainWindow window = new MainWindow();

    //Sets how strongly to apply speed/acceleration, set to whatever the FPS is for 1:1 ratio
    double cSpeed = 60;
    //Sets the framerate
    int frames = 17;

    int rotation = 0;
    //X and Y values of the rocket relative to the center of the moon (m)
    int rocketPerciseX = 0;
    int rocketPerciseY = 4000000;
    //Rocket's speed tangent to it's orbit (m/s)
    //double rocketSpeed = 1565.89;
    //Rocket's x and y acceleration vectors
    double ax = 0;
    double ay = 0;
    //Rocket's x and y velocity vectors
    double vx = 1000;
    double vy = 0;
    double dv = 0;
    //Angle of the triangle made from the center of the moon to the rocket
    double orbitAngle = Math.toRadians(90);

    //Constants I'm gonna use
    final static int MOON_RADIUS = 1737100;
    final static double MOON_MASS = 7.34767309*Math.pow(10,22);
    //Ratio of real meters to pixels
    final static int CONVERSION_FACTOR = 12867;
    static final double G = 6.674*Math.pow(10,-11);

    SpriteObj rocket = new SpriteObj("Rocket", "Rocket.png", "RocketFlames.png" , 300, 300);
    SpriteObj moon = new SpriteObj("Moon", "Moon.png", 365, 265);

    Program(){
        window.addLower(moon);
        window.addLower(rocket);

        fpsTimer = new Timer(frames, new TimerListener());
        fpsTimer.start();
    }


    //Math that I'll be using enough to justify having methods for

    //Calculates the distance from the rocket to the moon
    int distanceFormula(){
        int d = (int) (Math.sqrt(Math.pow(rocketPerciseX, 2) + Math.pow(rocketPerciseY, 2)));
        if (d > 1737100){
            return d;
        }
        else{
            return 1737100;
        }
    }
    //Converts the precise location to the coordinate system used by the display
    int pointConvertX(long precisePointX){
        return ((int)((Double.valueOf(precisePointX))/Double.valueOf(CONVERSION_FACTOR))) + 500 - 14;
    }
    //Different functions needed for x and y since the y coordinates need to be inverted
    int pointConvertY(int precisePointY){
        return 400 - ((int)((Double.valueOf(precisePointY))/Double.valueOf(CONVERSION_FACTOR)));
    }

    static double gravCalc (int distance){
        double g = ((G*MOON_MASS)/(Math.pow(distance, 2)));
        //System.out.println(g);
        return g;
    }

    double getMoonAngle(){
        if (rocketPerciseX != 0) {
            return Math.atan((Double.valueOf(rocketPerciseY))/(Double.valueOf(rocketPerciseX)));
        }
        else {
            return Math.PI/2;
        }
    }

    double getXAccel(){
        if (rocketPerciseX < 0){
            return Math.abs(Math.cos(orbitAngle) * (gravCalc(distanceFormula())));
        }
        else {
            return -(Math.abs(Math.cos(orbitAngle) * (gravCalc(distanceFormula()))));
        }
    }
    double getYAccel(){
        if (rocketPerciseY < 0) {
            return Math.abs(Math.sin(orbitAngle) * (gravCalc(distanceFormula())));
        }
        else {
            return -(Math.abs(Math.sin(orbitAngle) * (gravCalc(distanceFormula()))));
        }
    }

    class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent event){
            mainLoop();
        }
    }

    void mainLoop(){
        orbitAngle = getMoonAngle();

        //Determines acceleration based on proximity to moon
        ax = getXAccel();
        ay = getYAccel();

        vx += ax/cSpeed;
        vy += ay/cSpeed;

        //The speed is in m/s, but this code runs 60 times a second at normal speed, so it's divided by 60
        rocketPerciseX += (vx/cSpeed);
        rocketPerciseY += (vy/cSpeed);

        //Used for debugging stuff
        /*
        counter++;
        if (counter >= 1000) {
            System.out.println("Percise: " + rocketPerciseX + ", " + rocketPerciseY + ". Actual:" + pointConvertX(rocketPerciseX) + ", " + pointConvertY(rocketPerciseY));
            System.out.println("ax: " + ax + ", ay:" + ay);
            counter = 0;
        }
        */
        if (window.dPressed){
            rotation++;
        }
        if (window.aPressed) {
            rotation--;
        }
        if (window.spacePressed) {
            rocket.updateSprite(1);

            dv += (30/Double.valueOf(cSpeed));
            vx += (30*(Math.sin(Math.toRadians(rotation))))/cSpeed;
            vy += (30*(Math.cos(Math.toRadians(rotation))))/cSpeed;
        }
        else {
            rocket.updateSprite(0);
        }

        if (window.downPressed && !window.downUsed){
            window.downUsed = true;
            cSpeed = cSpeed*2;
        }
        if (window.upPressed && !window.upUsed){
            window.upUsed = true;
            cSpeed = cSpeed/2;
        }

        rocket.rotateImg(rotation);
        rocket.x = pointConvertX(rocketPerciseX);
        rocket.y = pointConvertY(rocketPerciseY);

        //System.out.println("X: " + rocket.x + ", Y: " + rocket.y);

        window.text[0] = "vx: " + vx;
        window.text[1] = "vy: " + vy;
        window.text[2] = "Total speed: " + (Math.sqrt(vx*vx + vy*vy));
        window.text[3] = "dv: " + dv;
        window.text[4] = "altitude: " + (distanceFormula() - MOON_RADIUS);
        window.text[5] = "Time factor: " + 60/cSpeed + "x";

        window.refresh();
    }
}
