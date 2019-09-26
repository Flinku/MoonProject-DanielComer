import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

public class SpriteObj {
    int x;
    int y;
    String spriteName;
    BufferedImage ogSprite;
    BufferedImage ogSprite2;
    BufferedImage cSprite;
    BufferedImage sprite;

    SpriteObj(String imageName, String imgLocation, int startX, int startY){
        spriteName = imageName;
        x = startX;
        y = startY;

        try {
            sprite = ImageIO.read(new File(imgLocation));
            ogSprite = sprite;
            cSprite = sprite;
        }
        catch (Exception e) {
            System.out.println("An image appears to missing");
        }

    }

    SpriteObj(String imageName, String imgLocation, String imgLocation2, int startX, int startY){
        spriteName = imageName;
        x = startX;
        y = startY;

        try {
            sprite = ImageIO.read(new File(imgLocation));
            ogSprite = sprite;
            cSprite = sprite;
            ogSprite2 = ImageIO.read(new File(imgLocation2));
        }
        catch (Exception e) {
            System.out.println("An image appears to missing");
        }

    }

    //Changes the sprite's x and y values, respectively
    void changeX(int change) {
        x += change;
    }
    void changeY(int change) {
        y += change;
    }

    void updateSprite(int spriteNum){
        if (spriteNum == 0){
            cSprite = ogSprite;
        }
        else {
            cSprite = ogSprite2;
        }
    }

    //Rotates the image, assisted by this website: https://blog.idrsolutions.com/2019/05/image-rotation-in-java/
    void rotateImg(int degrees){
        //Converts the degrees to radians, and determines the sin/cos of it
        double rads = Math.toRadians(degrees);
        double sin = Math.abs(Math.sin(rads));
        double cos = Math.abs(Math.cos(rads));

        AffineTransform myTF = new AffineTransform();
        //Determines the new dimensions of the image
        int newWidth = (int) (cSprite.getWidth() * cos + cSprite.getHeight() * sin);
        int newHeight = (int) (cSprite.getHeight() * cos + cSprite.getWidth() * sin);

        myTF.translate(newWidth / 2, newHeight / 2);
        myTF.rotate(rads,0, 0);
        myTF.translate(-cSprite.getWidth() / 2, -cSprite.getHeight() / 2);
        final AffineTransformOp rotateOp = new AffineTransformOp(myTF, AffineTransformOp.TYPE_BILINEAR);
        //To be perfectly honest I don't know what this does but the method to apply the rotation needs it
        BufferedImage dest = new BufferedImage(50, 50, 1);
        //Applies the rotation and updates sprite
        sprite = rotateOp.filter(cSprite, dest);

    }
    //Gets the center of the sprite relative to itself
    int[] getCenter(){
        int[] points = {sprite.getHeight()/2, sprite.getWidth()/2};
        return points;
    }

}