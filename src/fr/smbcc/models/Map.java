package fr.smbcc.models;

import java.util.ArrayList;
import java.util.Random;

import fr.smbcc.models.Tile.tileType;

/**
 * Map class is in charge of the field. it is composed by an array of tiles and have a method 
 * to propagate esplosion on near tiles.
 * There are different methods ( generateFieldXX() ) to generate a map.
 */
public class Map {

    private static int nb_tiles_x = 16;     // shoud be even nb
    private static int nb_tiles_y = 16;     // shoud be even nb
    private int width;
    private int height;
    private ArrayList<ArrayList<Tile>> tiles = new ArrayList<ArrayList<Tile>>();

    public Map(){
        this.setHeight( Tile.H * nb_tiles_y );  // computes map width in pixels
        this.setWidth( Tile.W * nb_tiles_x );   // computes map height in pixels

        Random r = new Random();                // used to choose a generation algorythm
        int map_type = r.nextInt(2);
        if(map_type == 0) {
            this.generateField1();              // create tiles according to specific pattern
        } else if ( map_type == 1 ) {
            this.generateField2();              // create tiles according to another specific pattern
        }
        
    }





    /*****************
    *  Generate tiles 
    ******************/

    /*
    *  first way to generate a map
    */
    private void generateField1(){
        this.tiles = new ArrayList<ArrayList<Tile>>();
        tiles.add( this.generateEmptyRow(nb_tiles_x, 0) );  // first row is empty

        for (int i = 1; i < nb_tiles_y - 2; i++) {
            if( i%2 == 1 ) {
                tiles.add( this.generateRow(nb_tiles_x, i * Tile.H, 0) );
            } else {
                tiles.add( this.generateWallRow(nb_tiles_x, i * Tile.H) );
            }
        }
        tiles.add( this.generateEmptyRow(nb_tiles_x, (nb_tiles_y - 2)* Tile.H));  // cat row is empty
        tiles.add( this.generateFullRow(nb_tiles_x, (nb_tiles_y - 1)* Tile.H));   // last row is hard walled here
    }


    /*
    *  2nd way to generate a map  : 
    */
    private void generateField2(){
        this.tiles = new ArrayList<ArrayList<Tile>>();
        tiles.add( this.generatePlayerRow(nb_tiles_x, 0) );  // first row is empty

        for (int i = 1; i < nb_tiles_y - 2; i++) {
            if( i%2 == 1 && i > 2 && i < nb_tiles_y - 4) {
                tiles.add( this.generateRow(nb_tiles_x, i * Tile.H, 0) );
            } else {
                tiles.add( this.generateWallRow(nb_tiles_x, i * Tile.H) );
            }
        }
        tiles.add( this.generatePlayerRow(nb_tiles_x, (nb_tiles_y - 2)* Tile.H));  // cat row is empty
        tiles.add( this.generateFullRow(nb_tiles_x, (nb_tiles_y - 1)* Tile.H));    // last row is hard walled here
    }


    /*
    *  generate a row filled with EMPTY tiles only
    */
    private ArrayList< Tile > generateEmptyRow(int nb_tiles, int y_start) {
        ArrayList< Tile > arr = new ArrayList<>();
        for (int i = 0; i < nb_tiles_x; i++) {
            arr.add(new Tile(i * Tile.W, y_start, Tile.tileType.EMPTY ));
        }
        return arr;
    }


    /*
    *  generate a row with WALL tiles int the middle and first and last 3 tiles EMPTY (to place a player on it)
    */
    private ArrayList< Tile > generatePlayerRow(int nb_tiles, int y_start) {
        ArrayList< Tile > arr = new ArrayList<>();
        for (int i = 0; i < nb_tiles_x; i++) {
            if(i<3 || i>nb_tiles_x - 4) {
                arr.add(new Tile(i * Tile.W, y_start, Tile.tileType.EMPTY ));
            } else {
                arr.add(new Tile(i * Tile.W, y_start, Tile.tileType.WALL ));
            }
        }
        return arr;
    }


    /*
    *  generate a row alterating HARDWALL and WALL tiles
    */
    private ArrayList< Tile > generateWallRow(int nb_tiles, int y_start) {
        ArrayList< Tile > arr = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < nb_tiles_x; i++) {
            int is_wall = r.nextInt(6);
            if( is_wall == 0) {
                arr.add(new Tile(i * Tile.W, y_start, Tile.tileType.EMPTY ));
            } else {
                arr.add(new Tile(i * Tile.W, y_start, Tile.tileType.WALL ));
            }
        }
        return arr;
    }

    /*
    *  generate a row composed by HARDWALL tiles only
    */
    private ArrayList< Tile > generateFullRow(int nb_tiles, int y_start) {
        ArrayList< Tile > arr = new ArrayList<>();
        for (int i = 0; i < nb_tiles_x; i++) {
            arr.add(new Tile(i * Tile.W, y_start, Tile.tileType.HARDWALL ));
        }
        return arr;
    }

    /*
    *  same as generateWallRow but with a parity parameter
    */
    private ArrayList< Tile > generateRow(int nb_tiles, int y_start, int parity) {
        ArrayList< Tile > arr = new ArrayList<>();
        for (int i = 0; i < nb_tiles_x; i++) {
            Tile.tileType t = (i%2 == parity) ? Tile.tileType.HARDWALL : Tile.tileType.WALL;
            arr.add(new Tile(i * Tile.W, y_start, t ));
        }
        return arr;
    }



    /*
    *  handle game events
    */

    /*
    *   link a bomb & its tile
    */
    public boolean associateBombeAndTile(Bomb b) {
        System.out.println("associate bomb & tile");
        // get corresponding tile according to bomb coors
        int col = b.getX() / Tile.W;
        int row = b.getY() / Tile.H;

        Tile t = this.tiles.get( row ).get( col );
        boolean bombplaced = t.setBomb(b);      // if return false : bomb cannot be placed on this tile

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

        // propagation on right side
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