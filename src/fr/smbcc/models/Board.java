package fr.smbcc.models;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

/*
*   This class control game logic. It initiate the game and run infinite loop.
*   game is composed by a Map(Tile Array) & 2 players : Corgi and Cat
*/

public class Board extends JPanel implements ActionListener {

    private Timer timer;                // infinite loop timer
    private final int DELAY = 10;       // game is refreshed every DELAY ms
    private Corgi corgi;                // player 1
    private Cat   cat;                  // player 2
    private Map   map;                  // generate & manage tiles

    private int corgi_x = 48 * 15 - 1, corgi_y = 0;     // Corgi start coors
    private int cat_x= 0, cat_y = 48 * 14;              // Cat start coors
    private boolean game_over;                          // game state
    private String game_over_msg;
    

    public Board() {

        initBoard();
    }

    private void initBoard() {

        this.game_over = false;             // game is running
        addKeyListener(new TAdapter());     // add keyboard listener (used by Corgi & Cat)
        setBackground(Color.GREEN);         // set a default background
	    setFocusable(true);                 // add focus on pannel
        
        map   = new Map();                      // generate the map
        corgi = new Corgi(corgi_x, corgi_y);    // create a corgi
        cat   = new Cat(cat_x, cat_y);          // create a cat

        timer = new Timer(DELAY, this);     // start main loop 
        timer.start();

    }





    /*********************
    *  graphic stuff
    *********************/

    /*
    *   draw pannel
    */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!game_over) {    // if game is still running display the game

            doDrawing(g);

        } else {             // if game is not running, display the game, game over screen and stop main loop
            System.out.println(this.game_over_msg);
            doDrawing(g);
            drawGameOver(g);
            timer.stop();
        }
        
        Toolkit.getDefaultToolkit().sync();
    }

    /*
    *  draw a game over message
    */
    private void drawGameOver(Graphics g) {
        String msg = "Game Over : " + this.game_over_msg;
        Font small = new Font("Helvetica", Font.BOLD, 50);
        FontMetrics fm = getFontMetrics(small);

        
        int xx = (map.getWidth() - fm.stringWidth(msg)) / 2;
        int yy = map.getHeight() / 2;
        g.setColor(Color.black);
        g.fillRect(xx - 5,yy - 45 ,fm.stringWidth(msg) + 5, 60);

        g.setColor(Color.red);
        g.setFont(small);
        g.drawString(msg, xx, yy);
    }

    
    /*
    *  update game events & draw entities
    */
    private void doDrawing(Graphics g) {
        
        
        Graphics2D g2d = (Graphics2D) g;

        updateBombs(corgi, map);
        updateBombs(cat, map);

        drawMap( g2d );

        drawPlayers( g2d );

        checkGameOver();
    }

    /*
    *  paint each tile
    */
    private void drawMap( Graphics2D g2d ){
        ArrayList<ArrayList<Tile>> map = this.map.getTiles();
        for (ArrayList<Tile> row : map) {
            for(Tile t : row){
                this.drawTile(g2d, t);
            }
        }
    }


    /*
    *  paint a specific tile
    */
    private void drawTile(Graphics2D g2d, Tile t){
        
        g2d.drawImage(t.getSprite(), t.getX(), 
            t.getY(), this);

        if(t.shouldBeRedrawn()){
            repaint(t.getX(), t.getY(), t.getSpriteWidth(), t.getSpriteHeight());
            t.hasBeenDrawn();
        }
    }

    /*
    *   paint players sprite at its coor.
    */
    private void drawPlayers(Graphics2D g2d) {

        g2d.drawImage(corgi.getSprite(), corgi.getX(), 
            corgi.getY(), this);

        g2d.drawImage(cat.getSprite(), cat.getX(), 
            cat.getY(), this);
    }


    /*
    *   manage bombs : 
    *       - new bombs need to be associated with a tile
    *       - old bomb can have exploded
    */
    private void updateBombs(Player p, Map map) {

        ArrayList<Bomb> bombs = p.getBombs();
        for(int i = 0; i<bombs.size(); i++) {
            Bomb b = bombs.get( i );

            if(b.getTile() == null) {       // if bomb has no tile, it is a new bomb -> give her a tile
                boolean bomb_placed = map.associateBombeAndTile(b);
                if( !bomb_placed ) {        // illegal bomb placement, delete it
                    System.out.println("illegal bomb");
                    bombs.remove( i );
                    b = null;
                } else {
                    repaint(b.getX(), b.getY(), b.getSpriteWidth(), b.getSpriteHeight());
                }

            } else if( b.hasExploded()){    // check if bomb is about exploding 
                map.generateExplosion(b);
                bombs.remove( b );
                System.out.println("delete bomb");
                b = null;
                repaint();                  // repaint ALL pannel because we don't know which tiles exploded
                                            // todo : get tiles from map.generateExplosion and only redraw this ones
            }
        }
    }

    /*
    *   check if a player is dead
    */
    public void checkGameOver(){
        if (corgi.isDead()) {
            this.game_over_msg = " Cat has won :/";
            this.game_over = true;
        } else if ( cat.isDead() ) {
            this.game_over_msg = " Corgi has won <3";
            this.game_over = true;
        }
    }
    



    /*******************/
    /*    Main loop    */
    /*******************/

    /*
    *   on timer interrruption
    */
    @Override
    public void actionPerformed(ActionEvent e) {
        
        step();
    }

    /*
    *   update players & redraw
    */
    private void step() {
        
        corgi.update(this.map);
        cat.update(this.map);

        repaint(corgi.getX()-1, corgi.getY()-1, 
                corgi.getWidth()+2, corgi.getHeight()+10);

        repaint(cat.getX()-1, cat.getY()-1, 
            cat.getWidth()+3, cat.getHeight()+10);
    }










   /*********************
    *   I/O manager
    *********************/
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            corgi.keyReleased(e);
            cat.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            corgi.keyPressed(e);
            cat.keyPressed(e);
        }
    }




}