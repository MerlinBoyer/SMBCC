package fr.smbcc.models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * Bomb class is mainly composed by a timer triggering explosion
 */
public class Bomb extends Sprite implements ActionListener {

    private Timer ttl;                     // bomb time to live timer
    private static int DELAY_BOMB = 3000;  // time before explosion
    private static int SPRITE_SIZE = 48;   // bomb sprite size
    private Tile tile;                     // bomb is placed on a specific tile
    private int range;                     // range is tile number to burn in each direction
    private boolean has_exploded;          // indication for Map that bomb has exploded and explosion must be generated 

    private static final String spriteSheetPath = "bombs.png";

    public Bomb(int x, int y, int range) {
        super(x, y, spriteSheetPath);
        this.has_exploded = false;
        this.ttl = new Timer(DELAY_BOMB, this);
        this.range = range;
        this.sprite_size = SPRITE_SIZE;
        this.setSprite(0, 0);                       // only one bomb sprite
    }



    /*
    *  timer stuff
    */
    public void startTimer(){
        this.ttl.start();
    }

    /*
    *  timer end
    */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.explode();
    }


    /*
    *  bomb explosion can be triggered by another explosion burning its tile
    */
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