package fr.smbcc.models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * Bomb
 */
public class Bomb extends Sprite implements ActionListener {

    private static int DELAY_BOMB = 3000;  // time before explosion
    private static int SPRITE_SIZE = 48;  // time before explosion
    private Timer ttl;
    private Tile tile;
    private int x, y;
    private int range;
    private boolean has_exploded;

    private static final String spriteSheetPath = "C:\\Users\\IN-BR-012\\Documents\\adamingJEE\\code\\workspaces\\01_JAVASE\\SMBCC\\src\\fr\\smbcc\\resources\\bombs.png";

    public Bomb(int x, int y, int range) {
        super(x, y, spriteSheetPath);
        this.sprite_size = SPRITE_SIZE;
        this.ttl = new Timer(DELAY_BOMB, this);
        this.has_exploded = false;
        this.range = range;
        this.setSprite(0, 0);  // only one bomb sprite
    }



    /*
    *  timer stuff
    */
    public void startTimer(){
        this.ttl.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.explode();
    }

    public void explode(){
        System.out.println("bomb EXPLOSION");
        this.has_exploded = true;
        this.ttl.stop();
    }





    /*
    * Getters and setters
    */
    public Timer getTtl() {
        return this.ttl;
    }

    public void setTtl(Timer ttl) {
        this.ttl = ttl;
    }

    public Tile getTile() {
        return this.tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean hasExploded(){
        return this.has_exploded;
    }

    public void setRange( int range ) {
        this.range = range;
    }

    public int getRange() {
        return this.range;
    }
    
    
}