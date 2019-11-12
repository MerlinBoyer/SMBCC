package fr.smbcc.services;

/**
 * Application
 */
import java.awt.EventQueue;
import javax.swing.JFrame;

import fr.smbcc.models.Board;
import fr.smbcc.models.Map;
import fr.smbcc.models.Tile;

public class Application extends JFrame {

    public Application() {
        
        initUI();
    }
    
    private void initUI() {

        add(new Board());

        setTitle("Super Smash Bomber Corgi vs Cat");
        setSize( Map.getNbTilesX() * Tile.W, Map.getNbTilesY() * Tile.H);
        
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
        	Application ex = new Application();
            ex.setVisible(true);
        });
    }
}