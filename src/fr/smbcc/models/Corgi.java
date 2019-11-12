package fr.smbcc.models;

import java.awt.event.KeyEvent;

public class Corgi extends Player {


    private final static String spriteSheetPath = "C:\\Users\\IN-BR-012\\Documents\\adamingJEE\\code\\workspaces\\01_JAVASE\\SMBCC\\src\\fr\\smbcc\\resources\\corgi_sprites.png";
    private final static int corgiSpriteOffsetX = 0 * 3;  // (0 -> 3)
    private final static int corgiSpriteOffsetY = 1 * 4;  // (0 -> 1)
    protected static final int HITBOX_SIZE = 40;       // boundaries inside offset in px
    protected static final int Y_CORRECTION  = 4;         // correction of hitbox y placement with rectangular sprite
    protected static final int X_CORRECTION  = 4;         // correction of hitbox y placement with rectangular sprite
    
    public Corgi(int x, int y) {

        super(x, y, spriteSheetPath, corgiSpriteOffsetX, corgiSpriteOffsetY);
        this.setHitbox(X_CORRECTION, Y_CORRECTION, HITBOX_SIZE, HITBOX_SIZE);

    }




    /*
    *  keyboard stuff
    */
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -1 * (X_SPEED + bonus_speed);
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = X_SPEED + bonus_speed;
        }

        if (key == KeyEvent.VK_UP) {
            dy = -1 * (Y_SPEED + bonus_speed);
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = Y_SPEED + bonus_speed;
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