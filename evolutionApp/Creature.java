/** Creature.java
  * @author Lencho Burka
  * */
package evolutionApp;

import java.util.*;

class Creature implements Comparable<Creature>{
  int x;
  int y;
  int energy_level;
  float[] chromosome = new float[13];
  
  /**Create a new creature with a random chromosome.
    * @param x - The x coordinate of the creature.
    * @param y - The y coordinate of the creature.
    */
  public Creature(int x, int y){
    this.x = x;
    this.y = y;
    energy_level = 30;
    
    Random r = new Random();
    chromosome[0] = r.nextInt(2);
    chromosome[1] = r.nextInt(2);
    chromosome[6] = r.nextInt(5);
    
    for(int i = 2; i < 6; i++){
      chromosome[i] = r.nextInt(4);
    }
    for(int i = 7; i < 13; i++){
      chromosome[i] = (float)(1/18.0);
    }
    for(int i = 0; i < 13; i++){
      chromosome[r.nextInt(6)+7] += (1/18.0);
    }
  }
  
  /**Create a new creature with a given chromosome.
    * @param x - The x coordinate of the creature.
    * @param y - The y coordinate of the creature.
    * @param chromosome[] - The chromosome of the creature.
    */
  public Creature(int x, int y, float chromosome[]){
    this.x = x;
    this.y = y;
    energy_level = 20;
    this.chromosome = chromosome;
  }
  
  /**Move based on a direction parameter, and an option parameter.
    * @param heading - The direction of the current object of interest.
    * @param option - Whether the creature wants to move towards or away from the object of interest, or ignore it.
    */
  void move(int heading, float option){
    if(option == 0) return;
    
    Evolution.creature_array[x][y] -= 1;
    
    if(option == 3){
      Random r = new Random();
      heading = r.nextInt(4) + 1;
    }
    
    if(option == 2){
      if((heading == 3) && (y != (Evolution.grid_size - 1))){
        y += 1;
      } else if ((heading == 24) && (x != (Evolution.grid_size - 1))){
        x += 1;
      } else if ((heading == 1) && (y != 0)){
        y -= 1;
      } else if ((heading == 2) && (x != 0)){
        x -= 1;
      }
    } else {
      if((heading == 1) && (y != (Evolution.grid_size - 1))){
        y += 1;
        //System.out.println("Moved +" + y);
      } else if ((heading == 2) && (x != (Evolution.grid_size - 1))){
        x += 1;
        //System.out.println("Moved +" + x);
      } else if ((heading == 3) && (y != 0)){
        y -= 1;
        //System.out.println("Moved +" + y);
      } else if ((heading == 4) && (x != 0)){
        x -= 1;
        //System.out.println("Moved +" + x);
      }
    }
    Evolution.creature_array[x][y] += 1;
    if((Evolution.creature_array[x][y] > 0) && (Evolution.monster_array[x][y] > 0)){
      kill();
      //System.out.println("Creature killed by Monster");
    }
  }
  
  /**Check current tile for a strawberry.*/
  Boolean strawb_present(){
    if(Evolution.strawb_array[x][y] != 0){
      return true;
    } else {
      return false;
    }
  }
  
  /**Check current tile for a mushroom.*/
  Boolean mushroom_present(){
    if(Evolution.mushroom_array[x][y] != 0){
      return true;
    } else {
      return false;
    }
  }
  
  /**Eat whatever is on the current tile.*/
  void eat(){
    Random r = new Random();
    
    if(strawb_present()){
      Evolution.strawb_array[x][y]--;
      energy_level += 5;
      Evolution.strawb_array[r.nextInt(Evolution.grid_size)][r.nextInt(Evolution.grid_size)]++;
    } else if(mushroom_present()){
      Evolution.mushroom_array[x][y]--;
      kill();
      //System.out.println("Creature killed by mushroom");
      Evolution.mushroom_array[r.nextInt(Evolution.grid_size)][r.nextInt(Evolution.grid_size)]++;
    }
  }
  
  /**Set the energy level of the creature 0, and remove it from the grid*/
  void kill(){
    energy_level = 0;
    Evolution.creature_array[x][y]--;
  }
  
  /**Find the nearest object in the given array
    * @return heading - An enumeration of the direction to towards the nearest object.
    */
  int nearest(int[][] searchArray){
    int smallestX = 0;
    int smallestY = 0;
    for(int sX = (x - Evolution.hood_size); sX <= (x + Evolution.hood_size); sX++){
      for(int sY = (y - Evolution.hood_size); sY <= (y + Evolution.hood_size); sY++){
        if((sY < Evolution.grid_size) && (sX < Evolution.grid_size) && (sY > -1) && (sX > -1)){
          if(searchArray[sX][sY] != 0){
            if(((Math.abs(sY-y) + Math.abs(sX-x)) < ((Math.abs(smallestY) + Math.abs(smallestX)))) || (smallestY == 0 && smallestX == 0)){
              smallestY = sY-y;
              smallestX = sX-x;
            }
          }
        }
      }
    }
    
    if(smallestY != 0){
      if(smallestY < 0){
        return 3;
      }
      return 1;
    }
    
    if(smallestX != 0){
      if(smallestX < 0){
        return 4;
      }
      return 2;
    }
    
    return 0;
  }
  
  /**Return an action to perform based on the creatures senses and the weights in the chromosome.
    * @return index - The index of the action to perform within the chromosome array.
    */
  int select_action(){
    ArrayList<Integer> actions_list = new ArrayList<Integer>();
    float biggest = 0;
    int index = 0;
    if (mushroom_present() != false) {
      if(chromosome[0] != 0){
        actions_list.add(7);
      }
    }
    if(strawb_present() != false){
      if(chromosome[1] != 0){
        actions_list.add(8);
      }
    }
    if(nearest(Evolution.mushroom_array) != 0){
      if(chromosome[2] != 0){
        actions_list.add(9);
      }
    }
    if(nearest(Evolution.strawb_array) != 0){
      if(chromosome[3] != 0){
        actions_list.add(10);
      }
    }
    if(nearest(Evolution.creature_array) != 0){
      if(chromosome[4] != 0){
        actions_list.add(11);
      }
    }
    if(nearest(Evolution.monster_array) != 0){
      if(chromosome[5] != 0){
        actions_list.add(12);
      }
    }
    
    if(actions_list.size() == 0){
      return 6;
    } else {
      for(int i : actions_list){
        if(chromosome[i] > biggest){
          biggest = chromosome[i];
          index = i;
        }
      }
    }
    return index - 7;
  }
  
  /**Do a given action and reduce the creatures energy level
    * @param i - Controls which action in the switch statement is performed.
    */
  void do_action(int i){
    if((Evolution.creature_array[x][y] > 0) && (Evolution.monster_array[x][y] > 0)){
      kill();
      return;
    }
    
    energy_level--;
    if(energy_level <= 0){
      kill();
      return;
    }
    
    switch(i){
      case 0:
        eat();
        //System.out.println("Eating Mushroom");
        break;
      case 1:
        eat();
        //System.out.println("Eating Strawberry");
        break;
      case 2:
        move(nearest(Evolution.mushroom_array), chromosome[2]);
        //System.out.println("Moving towards Mushroom");
        break;
      case 3:
        move(nearest(Evolution.strawb_array), chromosome[3]);
        //System.out.println("Moving towards Strawberry");
        break;
      case 4:
        move(nearest(Evolution.creature_array), chromosome[4]);
        //System.out.println("Moving towards Creature");
        break;
      case 5:
        move(nearest(Evolution.monster_array), chromosome[5]);
        //System.out.println("Moving towards Monster");
        break;
      case 6:
        move(1, 3);
        //System.out.println("Moving randomly");
        break;
      default:
        break;
    }
    return;
  }
  
  /**Change one action and one weight in the chromosome at random*/
  public void mutate(){
    Random r = new Random();
    int i = r.nextInt(7);
    int g = 0;
    switch(i){
      case 0:
        if(chromosome[0] == 1) chromosome[0] = 0;
        if(chromosome[0] == 0) chromosome[0] = 1;
        break;
      case 1:
        if(chromosome[1] == 1) chromosome[1] = 0;
        if(chromosome[1] == 0) chromosome[1] = 1;
        break;
      case 2:
        g = r.nextInt(4);
        while(g == chromosome[2]) g = r.nextInt(4);
        chromosome[2] = g;
        break;
      case 3:
        g = r.nextInt(4);
        while(g == chromosome[3]) g = r.nextInt(4);
        chromosome[3] = g;
        break;
      case 4:
        g = r.nextInt(4);
        while(g == chromosome[4]) g = r.nextInt(4);
        chromosome[4] = g;
        break;
      case 5:
        g = r.nextInt(4);
        while(g == chromosome[5]) g = r.nextInt(4);
        chromosome[5] = g;
        break;
      case 6:
        g = r.nextInt(5);
        while(g == chromosome[6]) g = r.nextInt(4);
        chromosome[6] = g;
        break;
      default:
        break;
    }
    i = r.nextInt(7) + 7;
    switch(i){
      case 7:
        if(r.nextBoolean()){
           chromosome[7]-= 0.5;
        } else {
           chromosome[7] += 0.5;
        }
      break;
      case 8:
        if(r.nextBoolean()){
           chromosome[8]-= 0.5;
        } else {
           chromosome[8] += 0.5;
        }
      break;
      case 9:
        if(r.nextBoolean()){
           chromosome[9]-= 0.5;
        } else {
           chromosome[9] += 0.5;
        }
      case 10:
        if(r.nextBoolean()){
           chromosome[10]-= 0.5;
        } else {
           chromosome[10] += 0.5;
        }
      case 11:
        if(r.nextBoolean()){
           chromosome[11]-= 0.5;
        } else {
           chromosome[11] += 0.5;
        }
      case 12:
        if(r.nextBoolean()){
           chromosome[12]-= 0.5;
        } else {
           chromosome[12] += 0.5;
        }
      default:
        break;
    }
    return;
  }
  
  /**Comparator based on energy levels to sort the Creatures array when needed*/
  public int compareTo(Creature other) {
    //return Integer.compare(other.energy_level, this.energy_level);
    if(other.energy_level > this.energy_level){
      return 1;
    } else if (other.energy_level < this.energy_level){
      return -1;
    } else {
      return 0;
    }
  }
  
  /**toString method for debugging*/
  @Override public String toString(){
    String out = "X: " + x + ", Y: " + y + ", Energy: " + energy_level + ", Chromosome: |";
    for(int i = 0; i < 13; i++){
      out += chromosome[i]+"|";
      if(i == 6){
        out += "    |";
      }
    }
    return out;
  }
  
}