package fr.smbcc.models;

/**
 * Item
 */
public class Item extends Sprite {

    public static enum Type { SPEED, BOMB_RANGE, BOMB_NB };
    public static int SPRITE_SIZE = 48;
    public static int BOMB_RANGE_UPGRADE = 1;
    public static int SPEED_UPGRADE = 1;
    public static String spritePath = "C:\\Users\\IN-BR-012\\Documents\\adamingJEE\\code\\workspaces\\01_JAVASE\\SMBCC\\src\\fr\\smbcc\\resources\\items.png";
    Type type;

    public Item(Type t){
        super(0,0,spritePath);
        this.type = t;
        this.sprite_size = SPRITE_SIZE;
        this.updateSprite();
    }

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