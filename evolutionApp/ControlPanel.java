/** ControlPanel.java
  * @author Lencho Burka
  * */
package evolutionApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

/** This is a horrific class that will be replaced by a nicer non-JPanel UI at a later date, or maybe it already has and
  * this is just here for legacy reasons, who knows, I'll probably for to update this comment when I do replace this class.
  */
public class ControlPanel extends JPanel
{
  private static final long serialVersionUID = 1L;
  private static int shapeCount;
  private static ArrayList<Shape> drawObjects = new ArrayList();
  private JButton init;
  private JButton step;
  private JButton stepMult;
  private JButton reinit;
  private JButton gen;
  private JButton debug;
  private JLabel countLabel;
  private JTextField displayCount;
  private JLabel enLabel;
  private JTextField displayEn;
  private static DrawingPanel drawPanel;
  private JPanel controlPanel;
  
  private javax.swing.Timer timer;
  private JButton start;
  private JButton stop;
  private int squareSize;
  
  private static BufferedImage[] imgArray;
  
  public ControlPanel()
  {
    getImages();
    squareSize = 40;
    init = new JButton("Initialize");
    step = new JButton("Step Forward");
    stepMult = new JButton("5 Steps Forward");
    reinit = new JButton("Repopulate");
    debug = new JButton("Debug");
    gen = new JButton("15 Generations");
    countLabel = new JLabel("Creatures:");
    displayCount = new JTextField(3);
    enLabel = new JLabel("Energy:");
    displayEn = new JTextField(3);
    start = new JButton("Run");
    stop = new JButton("Pause");
    
    ButtonListener listener = new ButtonListener();
    init.addActionListener (listener);
    step.addActionListener (listener);
    gen.addActionListener (listener);
    stepMult.addActionListener (listener);
    reinit.addActionListener (listener);
    debug.addActionListener (listener);
    start.addActionListener (listener);
    stop.addActionListener (listener);
    
    controlPanel = new JPanel();
    //controlPanel.setPreferredSize( new Dimension(120,400) );
    
    timer = new javax.swing.Timer(50, listener);
    
    drawPanel = new DrawingPanel();
    GridLayout gridLayout = new GridLayout(12,1);
    gridLayout.setHgap(10);
    gridLayout.setVgap(5);
    controlPanel.setLayout(gridLayout);

    controlPanel.add(init);
    controlPanel.add(reinit);
    controlPanel.add(step);
    controlPanel.add(stepMult);
    controlPanel.add(gen);
    controlPanel.add(debug);
    controlPanel.add(countLabel);
    controlPanel.add(displayCount);
    controlPanel.add(enLabel);
    controlPanel.add(displayEn);
    controlPanel.add(start);
    controlPanel.add(stop);
    add(controlPanel);
    
    add(drawPanel);
  }

  private void getImages(){        
    imgArray = new BufferedImage[4];
    try {
        imgArray[0] = ImageIO.read(new File("../assets/Creature.png"));
        imgArray[1] = ImageIO.read(new File("../assets/Monster.png"));
        imgArray[2] = ImageIO.read(new File("../assets/Strawberry.png"));
        imgArray[3] = ImageIO.read(new File("../assets/Mushroom.png"));
    } catch (IOException e) {
        System.out.println(e.toString());
    }
  }

  private class ButtonListener implements ActionListener
  {
    public void actionPerformed(ActionEvent aE)
    {
      if (aE.getSource() == timer){
        drawPanel.repaint();
        drawObjects.clear();
        Evolution.timeStep();
        
        shapeCount = 0;
        for(int x = 0; x < Evolution.grid_size; x++){
          for(int y = 0; y < Evolution.grid_size; y++){
            if(Evolution.strawb_array[x][y] > 0){
              drawObjects.add(new Shape(x*squareSize,y*squareSize,2)); 
            }
            if(Evolution.mushroom_array[x][y] > 0){
              drawObjects.add(new Shape(x*squareSize,y*squareSize,3));
            }
            if(Evolution.creature_array[x][y] > 0){
              drawObjects.add(new Shape(x*squareSize,y*squareSize,0)); 
              shapeCount++;
            }
            if(Evolution.monster_array[x][y] > 0){
              drawObjects.add(new Shape(x*squareSize,y*squareSize,1));
            }
          }
        }
        if(shapeCount < 13){
          Evolution.reinitialize();
        }
      } else {
        if (aE.getSource() == init)
        {
          drawObjects.clear();
          Evolution.initialize();
          shapeCount = 0;
          for(int x = 0; x < Evolution.grid_size; x++){
            for(int y = 0; y < Evolution.grid_size; y++){
              if(Evolution.strawb_array[x][y] > 0){
                drawObjects.add(new Shape(x*squareSize,y*squareSize,2));
              }
              if(Evolution.mushroom_array[x][y] > 0){
                drawObjects.add(new Shape(x*squareSize,y*squareSize,3));  
              }
              if(Evolution.creature_array[x][y] > 0){
                drawObjects.add(new Shape(x*squareSize,y*squareSize,0));
                shapeCount++;
              }
              if(Evolution.monster_array[x][y] > 0){
                drawObjects.add(new Shape(x*squareSize,y*squareSize,1)); 
              }
            }
          }
        }
        if (aE.getSource() == step)
        {
          drawObjects.clear();
          Evolution.timeStep();
          shapeCount = 0;
          for(int x = 0; x < Evolution.grid_size; x++){
            for(int y = 0; y < Evolution.grid_size; y++){
              if(Evolution.strawb_array[x][y] > 0){
                drawObjects.add(new Shape(x*squareSize,y*squareSize,2)); 
              }
              if(Evolution.mushroom_array[x][y] > 0){
                drawObjects.add(new Shape(x*squareSize,y*squareSize,3));
              }
              if(Evolution.creature_array[x][y] > 0){
                drawObjects.add(new Shape(x*squareSize,y*squareSize,0)); 
                shapeCount++;
              }
              if(Evolution.monster_array[x][y] > 0){
                drawObjects.add(new Shape(x*squareSize,y*squareSize,1));
              }
            }
          }
        }
        if (aE.getSource() == stepMult)
        {
          for(int i = 0; i < 5; i++){
            drawObjects.clear();
            Evolution.timeStep();
            drawPanel.repaint();
          }
          
          shapeCount = 0;
          for(int x = 0; x < Evolution.grid_size; x++){
            for(int y = 0; y < Evolution.grid_size; y++){
              if(Evolution.strawb_array[x][y] > 0){
                drawObjects.add(new Shape(x*squareSize,y*squareSize,2)); 
              }
              if(Evolution.mushroom_array[x][y] > 0){
                drawObjects.add(new Shape(x*squareSize,y*squareSize,3));
              }
              if(Evolution.creature_array[x][y] > 0){
                drawObjects.add(new Shape(x*squareSize,y*squareSize,0)); 
                shapeCount++;
              }
              if(Evolution.monster_array[x][y] > 0){
                drawObjects.add(new Shape(x*squareSize,y*squareSize,1));
              }
            }
          }
        }
        if (aE.getSource() == reinit)
        {
          drawObjects.clear();
          Evolution.reinitialize();
          shapeCount = 0;
          for(int x = 0; x < Evolution.grid_size; x++){
            for(int y = 0; y < Evolution.grid_size; y++){
              if(Evolution.strawb_array[x][y] > 0){
                drawObjects.add(new Shape(x*squareSize,y*squareSize,2)); 
              }
              if(Evolution.mushroom_array[x][y] > 0){
                drawObjects.add(new Shape(x*squareSize,y*squareSize,3)); 
              }
              if(Evolution.creature_array[x][y] > 0){
                drawObjects.add(new Shape(x*squareSize,y*squareSize,0));
                shapeCount++;
              }
              if(Evolution.monster_array[x][y] > 0){
                drawObjects.add(new Shape(x*squareSize,y*squareSize,1));
              }
            }
          }
        }
        if (aE.getSource() == gen)
        {
          drawObjects.clear();
          for(int j = 0; j < 15; j++){
            for(int i = 0; i < 25; i++){
              drawObjects.clear();
              Evolution.timeStep();
              drawPanel.repaint();
            }
            Evolution.reinitialize();
          }
          drawPanel.repaint();
          shapeCount = 0;
          for(int x = 0; x < Evolution.grid_size; x++){
            for(int y = 0; y < Evolution.grid_size; y++){
              if(Evolution.strawb_array[x][y] > 0){
                drawObjects.add(new Shape(x*squareSize,y*squareSize,2)); 
              }
              if(Evolution.mushroom_array[x][y] > 0){
                drawObjects.add(new Shape(x*squareSize,y*squareSize,3)); 
              }
              if(Evolution.creature_array[x][y] > 0){
                drawObjects.add(new Shape(x*squareSize,y*squareSize,0));
                shapeCount++;
              }
              if(Evolution.monster_array[x][y] > 0){
                drawObjects.add(new Shape(x*squareSize,y*squareSize,1));
              }
            }
          }
        }
        if (aE.getSource() == debug)
        {
          for(int n = 0; n < Evolution.creature_count; n++){
            System.out.println(Evolution.creatures[n].toString());
          }
          System.out.println();
        }
        
        if (aE.getSource() == start)
        {
          timer.start();
        }
        if (aE.getSource() == stop)
        {
          timer.stop();
        }
      }
      
      displayCount.setText(Integer.toString(shapeCount));  
      int totalEn = 0;
      for(Creature c : Evolution.creatures){
        totalEn += c.energy_level;
      }
      displayEn.setText(Integer.toString(totalEn/Evolution.creature_count));  
      drawPanel.repaint();
    }
  }
  
  private class DrawingPanel extends JPanel
  {
    private static final long serialVersionUID = 1L;

    public DrawingPanel()
    {
      setPreferredSize( new Dimension(Evolution.grid_size*squareSize,Evolution.grid_size*squareSize) );
      setBackground(new Color(80, 170, 56)); 
    }
    
    public void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      for (Shape s : drawObjects)
      {
        s.draw(g, imgArray);
      }
    }
  }
  
}