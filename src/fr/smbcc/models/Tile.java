package fr.smbcc.models;

import java.util.Random;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Rectangle;

/**
 * Tile
 */
public class Tile extends Sprite implements ActionListener{

    public static enum tileType { WALL, HARDWALL, EMPTY, EXPLOSION };
    public static int ITEM_CHANCES = 5;  // one in ITEM_CHANCES times
    public static int W = 48;
    public static int H = 48;
    public static int SPRITE_SIZE = 48;
    public static String spriteSheetPath = "C:\\Users\\IN-BR-012\\Documents\\adamingJEE\\code\\workspaces\\01_JAVASE\\SMBCC\\src\\fr\\smbcc\\resources\\tiles.png";
    private tileType type;
    private boolean hasItem;
    private Item item;
    private Bomb bomb;
    private static int DELAY_EXPLOSION = 1000;  // time before explosion
    private Timer explosion;
    private boolean is_on_fire;
    private boolean shouldBeRedrawn;


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
    *  When player pick up item, end him back
    */
    public Item pickItem(){
        this.hasItem = false;
        this.updateSprite();
        return this.item;
    }

    public void removeItem(){
        this.item = null;
    }

    // explode tile
    public void explode() {

        // first check if explosion is caused by another bomb
        if( this.bomb != null ){
            if( !this.bomb.hasExploded() ) {  // if our bomb is still there : deflagration ignite it
                this.bomb.explode();
                return;
            }
        }
        this.explosion = new Timer(DELAY_EXPLOSION, this);
        this.explosion.start();
        if(this.hasItem && this.type == tileType.EMPTY){  // burn item if tile is not a wall
            this.item = null;
            this.hasItem = false;
        }
        this.type = tileType.EXPLOSION;
        this.is_on_fire = true;
        this.bomb = null;
        this.updateSprite();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.explosion.stop();
        this.is_on_fire = false;
        this.type = tileType.EMPTY;
        this.updateSprite();
    }


    public void updateSprite() { // set img corresponding to tile type
        this.shouldBeRedrawn = true;
        switch (this.type) {
            case EMPTY: 
                if(this.hasItem) {  // display item
                    this.setVisible(true);
                    setSprite( this.item.getSprite() ) ;
                } else if (this.bomb != null) {
                    this.setVisible(true);
                    setSprite( this.bomb.getSprite() );
                } else {  // do not display empty tiles
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
        if(this.type == tileType.WALL || this.type == tileType.HARDWALL || this.bomb != null) return false;
        this.bomb = b;
        if(this.hasItem){  // bomb replace item
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