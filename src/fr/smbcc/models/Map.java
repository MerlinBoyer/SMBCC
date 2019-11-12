package fr.smbcc.models;

import java.util.ArrayList;

import fr.smbcc.models.Tile.tileType;

/**
 * Map
 */
public class Map {

    private static int nb_tiles_x = 16;  // shoud be even nb
    private static int nb_tiles_y = 16;  // shoud be even nb
    private int width;
    private int height;
    private ArrayList<ArrayList<Tile>> tiles = new ArrayList<ArrayList<Tile>>();

    public Map(){
        this.setHeight( Tile.H * nb_tiles_y );  // computes map width in pixels
        this.setWidth( Tile.W * nb_tiles_x );   // computes map height in pixels

        tiles.add( this.generateEmptyRow(nb_tiles_x, 0) );  // first row is empty

        for (int i = 1; i < nb_tiles_y - 2; i+=2) {        // map core is a grid of WALL / HARDWALL / EMPTY
            tiles.add( this.generateRow(nb_tiles_x, i * Tile.H, 0) );
            tiles.add( this.generateEmptyRow(nb_tiles_x, (i + 1) * Tile.H) );
        }

        tiles.add( this.generateFullRow(nb_tiles_x, (nb_tiles_y - 1)* Tile.H));  // last row is empty
    }





    /*
    *  Generate tiles 
    */
    private ArrayList< Tile > generateEmptyRow(int nb_tiles, int y_start) {
        ArrayList< Tile > arr = new ArrayList<>();
        for (int i = 0; i < nb_tiles_x; i++) {
            arr.add(new Tile(i * Tile.W, y_start, Tile.tileType.EMPTY ));
        }
        return arr;
    }

    private ArrayList< Tile > generateFullRow(int nb_tiles, int y_start) {
        ArrayList< Tile > arr = new ArrayList<>();
        for (int i = 0; i < nb_tiles_x; i++) {
            arr.add(new Tile(i * Tile.W, y_start, Tile.tileType.HARDWALL ));
        }
        return arr;
    }

    private ArrayList< Tile > generateRow(int nb_tiles, int y_start, int parity) {
        ArrayList< Tile > arr = new ArrayList<>();
        for (int i = 0; i < nb_tiles_x; i++) {
            Tile.tileType t = (i%2 == parity) ? Tile.tileType.WALL : Tile.tileType.HARDWALL;
            arr.add(new Tile(i * Tile.W, y_start, t ));
        }
        return arr;
    }



    /*
    *  handle game events
    */

    // link a bomb & its tile
    public boolean associateBombeAndTile(Bomb b) {
        System.out.println("associate bomb & tile");
        // get corresponding tile according to bomb coors
        int col = b.getX() / Tile.W;
        int row = b.getY() / Tile.H;

        Tile t = this.tiles.get( row ).get( col );
        boolean bombplaced = t.setBomb(b);

        if( !bombplaced ) return false;

        b.setTile(t);
        b.startTimer();

        return true;   // bomb placed & armed

    }

    public void generateExplosion(Bomb b) {
        int range = b.getRange();
        Tile central_tile = b.getTile();
        int col = central_tile.getX() / Tile.W;
        int row = central_tile.getY() / Tile.H;
        central_tile.explode();


        // // propagation on right side
        for (int i = col + 1; i < col + range + 1 && i < nb_tiles_x ; i++) {
            if ( this.tiles.get( row ).get( i ).getType() == tileType.HARDWALL ) break; 
            this.tiles.get( row ).get( i ).explode();
        }
        // propagation on left side
        for (int i = col - 1; i > col - range - 1 && i >= 0 ; i--) {
            if ( this.tiles.get( row ).get( i ).getType() == tileType.HARDWALL ) break;
            this.tiles.get( row ).get( i ).explode();
        }
        // propagation on top side
        for (int j = row + 1; j < row + range + 1 && j < nb_tiles_y ; j++) {
            if ( this.tiles.get( j ).get( col ).getType() == tileType.HARDWALL ) break;
            this.tiles.get( j ).get( col ).explode();
        }
        // propagation on bottom side
        for (int j = row - 1; j > row - range - 1 && j >= 0 ; j--) {
            if ( this.tiles.get( j ).get( col ).getType() == tileType.HARDWALL ) break;
            this.tiles.get( j ).get( col ).explode();
        }
    }















    /*
    *  getters and setters
    */
    public void setWidth(int w) {
        this.width = w;
    }

    public void setHeight(int h) {
        this.height = h;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void print(){
        for (int i = 0; i < this.tiles.size(); i++) {
            ArrayList<Tile> ui = this.tiles.get( i );
            for (int j = 0; j < ui.size(); j++) {
                System.out.print(ui.get(j).getType() + " ");
            }
            System.out.println();
        }
    }

    public static int getNbTilesX(){
        return nb_tiles_x;
    }

    public static int getNbTilesY(){
        return nb_tiles_y;
    }

    public ArrayList<ArrayList<Tile>> getTiles(){
        return this.tiles;
    }

    // get specific tile with row/col coordinates
    public Tile getTile(int row, int col) {
        if(row >= 0 && row < this.tiles.size()){
            if(col >= 0 && col < this.tiles.get(row).size() ) {
                return this.tiles.get( row ).get( col );
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}