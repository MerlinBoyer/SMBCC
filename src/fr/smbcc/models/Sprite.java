package fr.smbcc.models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


/*
 * Sprite class define methods to load and store images
 * game entities herites from this class
 */
public class Sprite {

    // this prefix is used in vscode to get resources folder path
    private static final String RESOURSES_PATH = "C:\\Users\\IN-BR-012\\Documents\\adamingJEE\\code\\workspaces\\01_JAVASE\\SMBCC\\resources\\";
    
    protected int x;                        // a sprite is an image with x,y top left corner coor
    protected int y;                        //    and width,height in pixels
    protected int w = 48;
    protected int h = 48;

    protected boolean visible;              // if !visible -> delete this sprite (dead in game)
    protected String spriteSheetPath;       // sprite sheet path where all sprites are stored
    protected BufferedImage spriteSheet;    // all sprites are on a single spritesheet image
    protected BufferedImage sprite;         // specific sprite to print
    protected int sprite_size;              // size of a sprite on spritesheet
    

    public Sprite(int x, int y, String spriteSheetPath) {

        this.x = x;
        this.y = y;
        this.spriteSheetPath = spriteSheetPath;
        visible = true;
    }





    /*
    *  load a sprite sheet and cut a sub-image within corresponding to the sprite 
    */

    public BufferedImage loadSpriteSheet() {

        BufferedImage image = null;

        try {
            image = ImageIO.read(new File( RESOURSES_PATH + this.spriteSheetPath ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    /*
    *  cut the sprite in the global spritesheet
    */
    public BufferedImage getSpriteFromSheet(int xGrid, int yGrid) {
        if (spriteSheet == null) {
            spriteSheet = loadSpriteSheet( );
        }

        return spriteSheet.getSubimage(xGrid, yGrid, this.sprite_size, this.sprite_size);
    }

    /*
    * set the sprite image from spritesheet at x,y params
    */
    public void setSprite(int col, int row){
        sprite = getSpriteFromSheet(col, row);
    }

    /*
    * set the sprite image from image param
    */
    public void setSprite(BufferedImage img){
        if(img.getWidth() != this.w || img.getHeight() != this.h) return;
        sprite = img;
    }

    /*
    * used if spritesheet only have one sprite 
    */
    public void setGlobalSprite(int col, int row){
        sprite = loadSpriteSheet();
    }



    /***********************
    *  getters & setters
    ***********************/

    public BufferedImage getSprite(){
        return sprite;
    }

    public int getX() {
        
        return x;
    }

    public int getY() {
        
        return y;
    }

    protected int getSpriteWidth() {
        return this.w;
    }

    protected int getSpriteHeight() {
        return this.h;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

}