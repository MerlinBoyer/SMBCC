package fr.smbcc.models;

import java.awt.event.KeyEvent;

/*
*   Corgi class represent player 1.
*   it has a sprite path, hitbox params & a keyboard manager
*/
public class Corgi extends Player {


    private final static String spriteSheetPath = "corgi_sprites.png";
    private final static int corgiSpriteOffsetX = 0 * 3;    // (0 -> 3) select a cat in sprite sheet
    private final static int corgiSpriteOffsetY = 1 * 4;    // (0 -> 1)
    protected static final int HITBOX_SIZE = 40;            // boundaries inside offset in px
    protected static final int Y_CORRECTION  = 4;           // correction of hitbox y placement with rectangular sprite
    protected static final int X_CORRECTION  = 4;           // correction of hitbox y placement with rectangular sprite
    
    public Corgi(int x, int y) {

        super(x, y, spriteSheetPath, corgiSpriteOffsetX, corgiSpriteOffsetY);
        this.setHitbox(X_CORRECTION, Y_CORRECTION, HITBOX_SIZE, HITBOX_SIZE);

    }




    /********************
    *  keyboard stuff
    ********************/

    /*
    *  add dx or dy speed when a key is pressed (arrows + ENTER pattern)
    *  changing direction cancel previous dy or dx speed (otherwise player control is awfull)
    */
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -1 * (X_SPEED + bonus_speed);
            dy = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = X_SPEED + bonus_speed;
            dy = 0;
        }

        if (key == KeyEvent.VK_UP) {
            dy = -1 * (Y_SPEED + bonus_speed);
            dx = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = Y_SPEED + bonus_speed;
            dx = 0;
        }

        if(key == 17 || key == 10) {
            this.placeBomb();
        }
    }

    public void keyReleased(KeyEvent e) {
        
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_UP) {
            dy = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }

}