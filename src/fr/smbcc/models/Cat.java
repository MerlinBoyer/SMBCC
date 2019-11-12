package fr.smbcc.models;

import java.awt.event.KeyEvent;

public class Cat extends Player {


    private final static String spriteSheetPath = "C:\\Users\\IN-BR-012\\Documents\\adamingJEE\\code\\workspaces\\01_JAVASE\\SMBCC\\src\\fr\\smbcc\\resources\\cat_sprites.png";
    private final static int catSpriteOffsetX = 2 * 3;  // (0 -> 3)
    private final static int catSpriteOffsetY = 1 * 4;  // (0 -> 1)
    protected static final int HITBOX_SIZE = 40;       // boundaries inside offset in px
    protected static final int Y_CORRECTION = 4;         // correction of hitbox y placement with rectangular sprite
    protected static final int X_CORRECTION = 4;         // correction of hitbox y placement with rectangular sprite
    
    public Cat(int x, int y) {
        super(x, y, spriteSheetPath, catSpriteOffsetX, catSpriteOffsetY);
        this.setHitbox(X_CORRECTION, Y_CORRECTION, HITBOX_SIZE, HITBOX_SIZE);
    }





    /*
    *  keyboard stuff
    */

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_Q) {
            dx = -1 * (X_SPEED + bonus_speed);
        }

        if (key == KeyEvent.VK_D) {
            dx = X_SPEED + bonus_speed;
        }

        if (key == KeyEvent.VK_Z) {
            dy = -1 * (Y_SPEED + bonus_speed);
        }

        if (key == KeyEvent.VK_S) {
            dy = Y_SPEED + bonus_speed;
        }

        if (key == 32) {
            this.placeBomb();
        }

    }

    public void keyReleased(KeyEvent e) {
        
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_Q) {
            dx = 0;
        }

        if (key == KeyEvent.VK_D) {
            dx = 0;
        }

        if (key == KeyEvent.VK_Z) {
            dy = 0;
        }

        if (key == KeyEvent.VK_S) {
            dy = 0;
        }
    }
}