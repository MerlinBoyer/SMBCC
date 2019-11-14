package fr.smbcc.models;

import java.util.Random;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Rectangle;

/**
 * Tile class implements a game tile. 
 *   An HARDWALL tile is not walkable and cannot be destroyed
 *   a WALL is not walkable and can hide an item. if a wall is burned by an explosion,
 *      tile become empty and item is revealed.
 *   an EMPTY tile is walkable and can have an pickable item.
 *   an EXPLOSION tile is a state of transition and is walkable but kill player.
 */
public class Tile extends Sprite implements ActionListener{

    
    public static int W = 48;                   // tile width
    public static int H = 48;                   // tile height
    public static int SPRITE_SIZE = 48;
    public static String spriteSheetPath = "tiles.png";   // sprite file name ; prefix added in Sprite.java
    
    public static enum tileType { WALL, HARDWALL, EMPTY, EXPLOSION };
    private tileType type;
    private Item item;                          // WALL tiles can have items inside
    public static int ITEM_CHANCES = 8;         // WALL tiles has item one in ITEM_CHANCES times
    private boolean hasItem;                    // indicate if tile has an item
    private Bomb bomb;                          // when a bom is dropped on tile
    private Timer explosion;                    // explosion timer
    private static int DELAY_EXPLOSION = 800;   // burning time after explosion
    private boolean is_on_fire;                 // when deflagration reach tile
    private boolean shouldBeRedrawn;            // when tile sprite has changed


    public Tile(int x, int y, tileType t) {
        super(x, y, spriteSheetPath);
        this.type = t;
        this.sprite_size = SPRITE_SIZE;
        this.is_on_fire = false;
        if( this.type == tileType.WALL ) { // only breakable walls can have items
            this.generateItem();
        }
        this.updateSprite();
    }



    /*
    *  set tile item according to ITEM_CHANCES provided
    */
    public void generateItem() {
        Random r = new Random();
        if( r.nextInt( ITEM_CHANCES + 1 ) == 0) {
            this.hasItem = true;
            this.item = this.randomItem();
        } else {
            this.hasItem = false;
        }
    }

    /*
    *  Generate a random item randomly chosing his type
    */
    public Item randomItem(){
        Random r = new Random();
        return new Item(Item.Type.values()[ r.nextInt(Item.Type.values().length) ]);
    }


    /*
    *  When player pick up item, send him back. tile item will be removed after player 
    *  ended interact with it (avoid ex nullPointer)
    */
    public Item pickItem(){
        this.hasItem = false;
        this.updateSprite();
        return this.item;
    }

    public void removeItem(){
        this.item = null;
    }

    /*
    *  Explode tile. 2 cases : tile bomb is explosing or a deflagration has reached this tile
    */
    public void explode() {

        // first check if explosion is caused by another bomb
        if( this.bomb != null ){
            if( !this.bomb.hasExploded() ) {  // if our bomb is still there : deflagration ignite it
                this.bomb.explode();
                return;
            }
        }
        // ignite tile
        this.explosion = new Timer(DELAY_EXPLOSION, this);  // explosion last for DELAY_EXPLOSION ms
        this.explosion.start();
        if(this.hasItem && this.type == tileType.EMPTY){    // burn item if tile is not a wall
            this.item = null;
            this.hasItem = false;
        }
        this.type = tileType.EXPLOSION;
        this.is_on_fire = true;
        this.bomb = null;
        this.updateSprite();
    }


    /*
    *   Called after DELAY_EXPLOSION to end explosion
    */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.explosion.stop();
        this.is_on_fire = false;
        this.type = tileType.EMPTY;
        this.updateSprite();
    }


    /*
    *   Set tile sprite according to its type and its state
    */
    public void updateSprite() {
        this.shouldBeRedrawn = true;    // tiles are redrawn only after a sprite change
        switch (this.type) {
            case EMPTY: 
                if(this.hasItem) {                  // empty tiles can have items
                    this.setVisible(true);
                    setSprite( this.item.getSprite() ) ;
                } else if (this.bomb != null) {     //... or bombs
                    this.setVisible(true);
                    setSprite( this.bomb.getSprite() );
                } else {                            //... or can be empty
                    setSprite(1 * SPRITE_SIZE, 0 * SPRITE_SIZE);
                }
                break;
            
            case WALL:
                setSprite(0 * SPRITE_SIZE, 0 * SPRITE_SIZE);
                break;
            
            case HARDWALL:
                setSprite(2 * SPRITE_SIZE, 0 * SPRITE_SIZE);
                break;
            
            case EXPLOSION:
                setSprite(3 * SPRITE_SIZE, 0 * SPRITE_SIZE);
                break;

            default:
                setSprite(1 * SPRITE_SIZE, 0 * SPRITE_SIZE);
                break;
        }
    }






    /*
    *  getters and setters
    */

    public tileType getType(){
        return this.type;
    }

    public boolean hasItem(){
        return this.hasItem;
    }

    public boolean setBomb( Bomb b ) {
        // bomb can only be placed on empty tiles
        if(this.type == tileType.WALL || this.type == tileType.HARDWALL || this.bomb != null) return false;
        this.bomb = b;
        if(this.hasItem){       // bomb replace item
            this.item = null;
            this.hasItem = false;
        }
        this.updateSprite();
        return true;
    }

    public Bomb getBomb( ) {
        return this.bomb;
    }

    public boolean isOnFire() {
        return this.is_on_fire;
    }

    public boolean shouldBeRedrawn() {
        return this.shouldBeRedrawn;
    }
    public void hasBeenDrawn(){
        this.shouldBeRedrawn = false;
    }

    public Rectangle getHitBox() {
        return new Rectangle(this.getX(), this.getY(), SPRITE_SIZE, SPRITE_SIZE);
    }



}