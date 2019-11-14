package fr.smbcc.models;

/**
 * Items are player power-up randomly stored in wall tiles
 */
public class Item extends Sprite {

    public static enum Type { SPEED, BOMB_RANGE, BOMB_NB };    // there a 3 types of power-p :
                                                               //     - SPEED : increase player speed
                                                               //     - BOMB_RANGE : increase explosion size
                                                               //     - BOMB_NB : increase max bomb player can place in the same time
    Type type;
    public static int SPRITE_SIZE = 48;
    public static int BOMB_RANGE_UPGRADE = 1;           // bomb range added to player
    public static int SPEED_UPGRADE = 1;                // speed added to player
    public static String spritePath = "items.png";      // 

    public Item(Type t){
        super(0,0,spritePath);
        this.type = t;
        this.sprite_size = SPRITE_SIZE;
        this.updateSprite();
    }


    /*
    *   pick sprite in sprite sheet according to item type
    */
    public void updateSprite(){
        switch (this.type) {
            case SPEED:
                setSprite(2 * SPRITE_SIZE, 0);
                break;

            case BOMB_RANGE:
                setSprite(1 * SPRITE_SIZE, 0);
                break;
        
            case BOMB_NB:
                setSprite(0 * SPRITE_SIZE, 0);
                break;
                
            default:
                break;
        }
    }

    public Type getType(){
        return this.type;
    }


}