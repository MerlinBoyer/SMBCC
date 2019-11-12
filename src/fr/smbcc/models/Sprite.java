package fr.smbcc.models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Sprite
 */
public class Sprite {

    protected int x;
    protected int y;
    protected int w = 48;
    protected int h = 48;

    protected boolean visible;              // if !visible -> delete this sprite (dead in game)
    protected String spriteSheetPath;       // sprite sheet with all sprites
    protected BufferedImage sprite;         // sprite printed
    protected BufferedImage spriteSheet;    // all sprites are on a single spritesheet
    protected int sprite_size;              // size of a sprite on spritesheet
    

    public Sprite(int x, int y, String spriteSheetPath) {

        this.x = x;
        this.y = y;
        this.spriteSheetPath = spriteSheetPath;
        visible = true;
    }





    /*
    *  Image stuff
    */

    public BufferedImage loadSpriteSheet() {

        BufferedImage image = null;

        try {
            image = ImageIO.read(new File( this.spriteSheetPath ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public BufferedImage getSpriteFromSheet(int xGrid, int yGrid) {
        if (spriteSheet == null) {
            spriteSheet = loadSpriteSheet( );
        }

        return spriteSheet.getSubimage(xGrid, yGrid, this.sprite_size, this.sprite_size);
    }

    public void setSprite(int col, int row){
        sprite = getSpriteFromSheet(col, row);
    }

    public void setSprite(BufferedImage img){
        if(img.getWidth() != this.w || img.getHeight() != this.h) return;
        sprite = img;
    }

    public void setGlobalSprite(int col, int row){
        sprite = loadSpriteSheet();
    }



    /*
    *  getters & setters
    */
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