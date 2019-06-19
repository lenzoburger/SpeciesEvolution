/** Main.java
  * @author Lencho Burka
  * */
package evolutionApp;

import javax.swing.*;

public class CreateWorld {

    public static void main(String[] args) {
        JFrame shapeFrame = new JFrame();
        shapeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        shapeFrame.getContentPane().add(new ControlPanel());
        shapeFrame.pack();
        shapeFrame.setVisible(true);
        shapeFrame.setTitle("Species Evolution");
    }
}