/** Shape.java
  * @author Lencho Burka
  * */
package evolutionApp;

import java.awt.*;
import java.awt.image.*;


/**The tiles that make up the game world.*/
public class Shape
{
  private int x, y, width, height, type;
  
  /**A contructor to create a new tile.
    * 
    * @param x - The x coordiante of the tile.
    * @param y - The y coordinate of the tile.
    * @param type - What object is currently occupying the tile.
    * */
  public Shape(int x, int y, int type)
  {
    this.height = 20;
    this.width = height;
    this.x = x;
    this.y = y;
    this.type = type;
  }
  
  /**Draw the tile*/
  public void draw(Graphics g, BufferedImage[] imgArray)
  {
    g.drawImage(imgArray[type], x, y, width,height,null);
  }
  
}