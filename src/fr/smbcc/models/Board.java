package fr.smbcc.models;

import java.awt.Color;
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

public class Board extends JPanel implements ActionListener {

    private Timer timer;
    private Corgi corgi;
    private Cat   cat;
    private Map   map;
    private int corgi_x = 48 * 15, corgi_y = 0;
    private int cat_x= 0, cat_y = 48 * 14;
    private final int DELAY = 10;
    

    public Board() {

        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.GREEN);
	    setFocusable(true);
        
        map   = new Map();
        // map.print();
        corgi = new Corgi(corgi_x, corgi_y);
        cat   = new Cat(cat_x, cat_y);

        timer = new Timer(DELAY, this);  // main loop 
        timer.start();

    }





    /*
    *  graphic stuff
    */

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
        
        Toolkit.getDefaultToolkit().sync();
    }
    
    private void doDrawing(Graphics g) {
        
        
        Graphics2D g2d = (Graphics2D) g;

        updateBombs(corgi, map);
        updateBombs(cat, map);

        drawMap( g2d );

        drawPlayers( g2d );

        checkGameOver();
    }

    private void drawMap( Graphics2D g2d ){
        ArrayList<ArrayList<Tile>> map = this.map.getTiles();
        for (ArrayList<Tile> row : map) {
            for(Tile t : row){
                this.drawTile(g2d, t);
            }
        }
    }

    private void drawTile(Graphics2D g2d, Tile t){
        
        g2d.drawImage(t.getSprite(), t.getX(), 
            t.getY(), this);

        if(t.shouldBeRedrawn()){
            repaint(t.getX(), t.getY(), t.getSpriteWidth(), t.getSpriteHeight());
            t.hasBeenDrawn();
        }
    }

    private void drawPlayers(Graphics2D g2d) {

        g2d.drawImage(corgi.getSprite(), corgi.getX(), 
            corgi.getY(), this);

        g2d.drawImage(cat.getSprite(), cat.getX(), 
            cat.getY(), this);
    }

    private void updateBombs(Player p, Map map) {

        // System.out.println("update bombs");

        ArrayList<Bomb> bombs = p.getBombs();
        for(int i = 0; i<bombs.size(); i++) {
            Bomb b = bombs.get( i );
            if(b.getTile() == null) {   // if bomb has no tile, it is a new bomb -> give her a tile
                boolean bomb_placed = map.associateBombeAndTile(b);
                if( !bomb_placed ) {   // illegal bomb placement, delete it
                    System.out.println("illegal bomb");
                    bombs.remove( i );
                    b = null;
                } else {
                    repaint(b.getX(), b.getY(), b.getSpriteWidth(), b.getSpriteHeight());
                }
            } else if( b.hasExploded()){  // check if bomb is about exploding 
                map.generateExplosion(b);
                bombs.remove( b );
                System.out.println("delete bomb");
                b = null;
                repaint();
            }
        }
    }

    public void checkGameOver(){
        if (corgi.isDead()) {
            System.out.println(" Cat has won :/");
            timer.stop();
        } else if ( cat.isDead() ) {
            System.out.println(" Corgi has won <3");
            timer.stop();
        }
    }
    



    /*******************/
    /*    Main loop    */
    /*******************/

    @Override  // acions called
    public void actionPerformed(ActionEvent e) {
        
        step();
    }

    private void step() {
        
        corgi.update(this.map);
        cat.update(this.map);

        repaint(corgi.getX()-1, corgi.getY()-1, 
                corgi.getWidth()+2, corgi.getHeight()+10);

        repaint(cat.getX()-1, cat.getY()-1, 
            cat.getWidth()+3, cat.getHeight()+10);
    }










    /*
    *    I/O manager
    */
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





    /*
    *   Access Map size
    */
}