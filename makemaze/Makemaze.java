/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.PrintWriter;
import java.io.File;
import java.awt.*;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.HashSet;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author mohit
 */
public class Makemaze extends JApplet {
    
    private static final int JFXPANEL_WIDTH_INT = 480;
    private static final int JFXPANEL_HEIGHT_INT = 550;
    private static JFXPanel fxContainer;
    private Button ar[][];
    HashSet<String> printMe;
    static PrintWriter pr;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        
        if(args.length == 0)
            pr = new PrintWriter(System.out);
        else 
            pr = new PrintWriter(new File(args[0]));
        
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception e) {
                }
                
                JFrame frame = new JFrame("JavaFX 2 in Swing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                JApplet applet = new Makemaze();
                applet.init();
                
                frame.setContentPane(applet.getContentPane());
                
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                
                applet.start();
            }
        });
    }
    


    
    @Override
    public void init() {
        fxContainer = new JFXPanel();
        ar = new Button[16][16];
        printMe = new HashSet<>();
        fxContainer.setPreferredSize(new Dimension(JFXPANEL_WIDTH_INT, JFXPANEL_HEIGHT_INT));
        add(fxContainer, FlowLayout.LEFT);
        // create JavaFX scene
        Platform.runLater(new Runnable() {
            
            @Override
            public void run() {
                createScene();
            }
        });
    }
    
    boolean isCenter(int x, int y){
        
        if(x == 7 && y == 7)
            return true;
        if(x == 7 && y == 8)
            return true;        
        if(x == 8 && y == 7)
            return true;        
        if(x == 8 && y == 8)
            return true;
        
        return false;
    }
    
    private void createScene() {
        GridPane root = new GridPane();

        Button ok = new Button();
        ok.setText("Print");
        ok.setPrefWidth(100);
        ok.setPrefHeight(40);        
        ok.setMinWidth(100);
        ok.setMinHeight(40);        
        ok.setMaxWidth(100);
        ok.setMaxHeight(40);
        ok.setTranslateX(190);
        ok.setTranslateY(500);
        ok.setOnAction(new EventHandler<ActionEvent>(){
        @Override
                public void handle(ActionEvent event) {

                    pr.write("dimension 16 16\n");
                    for(String s : printMe)
                        pr.write(s);
                    pr.close();
                    System.exit(0);
                }
            });
        root.getChildren().add(ok);
        
        for(int i = 0 ; i < 16 ; i++){
            for(int j = 0 ; j < 16 ; j++){
                ar[i][j] = new Button();
                ar[i][j].setPrefWidth(25);
                ar[i][j].setPrefHeight(25);
                ar[i][j].setMinWidth(25);
                ar[i][j].setMinHeight(25);
                ar[i][j].setMaxWidth(25);
                ar[i][j].setMaxHeight(25);
                ar[i][j].setText("obstacle " + j + " " + i +"\n");
                ar[i][j].setStyle("-fx-background-color: #ffffff; ");
                ar[i][j].setTranslateX(i*30);
                ar[i][j].setTranslateY(j*30);
                if(isCenter(i, j))
                    ar[i][j].setStyle("-fx-background-color: #ff0000; ");
              
                ar[i][j].setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        
                        Button b = (Button)event.getSource();
                        
                        if(!b.getStyle().equals("-fx-background-color: #ff0000; ")){
                        
                            if(!b.getStyle().equals("-fx-background-color: #000000; ")){
                                b.setStyle("-fx-background-color: #000000; ");
                                printMe.add(b.getText());
                            }
                            else{
                                b.setStyle("-fx-background-color: #ffffff; ");
                                printMe.remove(b.getText());
                            }
                        }
                   
                    }
                });
                root.getChildren().add(ar[i][j]);
            }
        }

        
        fxContainer.setScene(new Scene(root));
    }

    
}
