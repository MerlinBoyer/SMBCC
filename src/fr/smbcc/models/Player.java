package fr.smbcc.models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.Rectangle;

import javax.swing.Timer;

public class Player extends Sprite implements ActionListener {

    // graphic var
    protected Timer timerAnim;                          // called to update animation on movement
    protected static final int DELAY_ANIMATION = 100;   // delay (ms) between movement animations
    protected static final int NB_SPRITE_ANIM = 3;      // nb sprites available to animate 
    protected int spriteOffsetX = 0;                    // sprite set offset on the spritesheet
    protected int spriteOffsetY = 0;
    protected int dx;                                   // to move x,y coor
    protected int dy;
    protected final int X_SPEED = 3;                    // px to add on x or y axis on movement
    protected final int Y_SPEED = 3;
    protected static final int SPRITE_SIZE = 48;        // 1 sprite size (in px)
    protected int current_sprite_animation;             // current animation sprite displayed
    protected int current_sprite_movement;              // current movement sprite displayed

    // game var
    protected int hitbox_offset_x, hitbox_offset_y, hitbox_w, hitbox_h;
    protected int bomb_range;
    protected int bonus_speed;
    protected int max_bomb;
    protected boolean is_dead;
    protected ArrayList<Bomb> bombs = new ArrayList<Bomb>();

    public Player(int x, int y, String spriteSheetPath, int offx, int offy) {
        super(x, y, spriteSheetPath);       // put sprite on board at coor x,y
        this.spriteOffsetX = offx;          
        this.spriteOffsetY = offy;

        initGraphics();

        initPlayer();

        setSprite(offx * SPRITE_SIZE, offy * SPRITE_SIZE); // offx & offy allow to select sprites set in spritesheet

        initAnimation();  // add timer on animation

    }


    /*
    *  initialization
    */

    public void initGraphics() {
        this.sprite_size = SPRITE_SIZE;     
        this.current_sprite_animation = 0;  // save current sprite displadqdyded
        this.current_sprite_movement = 0;
    }

    public void initPlayer(){
        this.is_dead = false;
        this.bomb_range = 1;
        this.bonus_speed = 0;
        this.max_bomb = 2;
    }




    /*
     * update position / appearance
     * called by board in main loop
     */
    public void update(Map map) {

        this.move(map);

        this.updateMovement();

        this.updateSprite();
    }

    // specific timer for animation update
    public void initAnimation() {
        timerAnim = new Timer(DELAY_ANIMATION, this);
        timerAnim.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.updateAnimation();
    }



    /*
    *   Player actions
    */

    // change sprite coor
    public void move(Map map) {

        int col = this.getXPos() / Tile.W;
        int row = this.getYPos() / Tile.H;
        
        x += dx;
        y += dy;

        // first check if player is out of map
        if( this.x < 0 || this.x + this.getWidth() >= map.getWidth() 
            || y < 0 || y + this.getHeight() > map.getHeight() ){
                x -= dx;
                y -= dy;
                return;
        }

        // walk on current tile
        this.walkOnTile( map.getTile( row,col ) );

        // check player neighboorhood (collisions)
        if( !this.checkNextTiles(map, row, col) ) {
            x -= dx;
            y -= dy;
        }
    }

    public void walkOnTile(Tile t) {
        if (t==null) return;
        switch(t.getType()){

            case EXPLOSION:
                this.is_dead = true;
                return;

            case EMPTY:
                if(t.hasItem()) {
                    this.addItem( t.pickItem() );
                    t.removeItem();
                    return;
                }
            
            default:
                return; 
        }
    }

    public boolean checkNextTiles(Map map, int row, int col) {
        if(dx > 0) {
            if( !this.canWalkOnTile( map.getTile(row, col + 1) ) ) return false;
        }
        if(dx < 0) {
            if( !this.canWalkOnTile( map.getTile(row, col - 1) ) ) return false;
        }
        if(dy > 0) {
            if( !this.canWalkOnTile( map.getTile(row + 1, col) ) ) return false;
        }
        if(dy < 0) {
            if( !this.canWalkOnTile( map.getTile(row - 1, col) ) ) return false;
        }
        return true;
    }

    public boolean canWalkOnTile(Tile t) {
        if (t==null) return true;

        if( !this.getHitBox().intersects( t.getHitBox() )) return true;   // if next tile is too far

        switch(t.getType()){

            case WALL:
                return false;

            case HARDWALL:
                return false;
            
            case EMPTY:
                if(t.getBomb() == null) {
                    return true;
                } else {
                    return false;
                }

            default:
                return true; 
        }
    }

    public void addItem(Item item) {
        switch (item.getType()) {
            case BOMB_RANGE:
                System.out.println("BOMB RANGE ++");
                this.bomb_range += Item.BOMB_RANGE_UPGRADE;
                break;
        
            case SPEED:
                System.out.println("SPEED UP");
                this.bonus_speed += Item.SPEED_UPGRADE;
                break;

            case BOMB_NB:
                System.out.println("MAX BOMB UP");
                this.max_bomb ++;
                break;

            default:
                break;
        }
    }

    public void placeBomb(){
        if ( this.getNbBombs() < this.max_bomb ) {
            this.bombs.add( new Bomb( this.getXPos(), this.getYPos(), this.getBombRange() ));
            System.out.println("bomb placed");
        }
    }





    /*
    *   Updates visual
    */

    // change animation current sprite
    public void updateAnimation() {  // update sprite to animate
        if ( this.dx != 0 || this.dy != 0) {  // animate only when moving
            // System.out.println("UPDATE ANIMATION");
            this.current_sprite_animation = this.current_sprite_animation < NB_SPRITE_ANIM -1 ? this.current_sprite_animation+1 : 0;
        }
    }

    // change movement sprite (up, down, right, left)
    public void updateMovement() {  // change sprite direction

        if (this.dy > 0 ) {  // up
            // System.out.println("go up");
            this.current_sprite_movement = 0;
        } else if ( this.dy < 0 ) {  // down
            // System.out.println("go down");
            this.current_sprite_movement = 3;
        } else if ( this.dx > 0 ) {  // right
            // System.out.println("go right");
            this.current_sprite_movement = 2;
        } else if ( this.dx < 0 ) {  // left
            // System.out.println("go left");
            this.current_sprite_movement = 1;
        }
    }


    // change buffered img sprite
    public void updateSprite() {
        setSprite(this.spriteOffsetX * SPRITE_SIZE + this.current_sprite_animation * SPRITE_SIZE, 
                this.spriteOffsetY * SPRITE_SIZE + this.current_sprite_movement * SPRITE_SIZE);
    }




    /*
    *  getters & setters 
    */
    
    public int getWidth() {
        return w;
    }
    
    public int getHeight() {
        return h;
    }

    public int getNbBombs() {
        return this.bombs.size();
    }

    public ArrayList<Bomb> getBombs() {
        return this.bombs;
    }

    public int getBombRange() {
        return this.bomb_range;
    }

    // baryceneter of player
    public int getXPos(){
        return this.x + SPRITE_SIZE / 2;
    }

    public int getYPos(){
        return this.y + SPRITE_SIZE / 2;
    }

    public boolean isDead(){
        return this.is_dead;
    }

    public void setHitbox(int x, int y, int w, int h) {
        hitbox_offset_x = x;
        hitbox_offset_y = y;
        hitbox_w = w;
        hitbox_h = h;
    }
    
    public Rectangle getHitBox() {
        return new Rectangle(this.getX() + hitbox_offset_x, 
                            this.getY()  + hitbox_offset_y,
                            this.hitbox_w,
                            this.hitbox_h);
    }
    

    
}