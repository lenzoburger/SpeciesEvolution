/** Evolution.java
  * @author Lencho Burka
  * */
package evolutionApp;

import java.util.*;

public class Evolution{
  
  public static final int hood_size = 3; //Distance a creature can sense objects in.
  public static final int grid_size = 25;  //Size of the world. 1/5th of tiles will contain food, 1/10th mushrooms.
  public static final int creature_count = 75; //Number of creatures in the simulation, monster cound will be half this.
  public static final int mushroom_count = grid_size;
  public static final int strawberry_count = grid_size * 8;
  public static final int selection_const = 6;  //Size of the subset of creatures to be considered when creating children.
  public static final int mutation_chance = 6;  //Chance of a mutation occuring after crossover is one in mutation_chance.
  
  public static int[][] strawb_array = new int[grid_size][grid_size];
  public static int[][] mushroom_array = new int[grid_size][grid_size];
  public static int[][] creature_array = new int[grid_size][grid_size];
  public static int[][] monster_array = new int[grid_size][grid_size];
  static Creature[] creatures = new Creature[creature_count];
  static Monster[] monsters = new Monster[creature_count/2];
  
  public static int current_step = 0;
  
  /**Populate the world with food, mushrooms, monsters and creatures*/
  public static void initialize(){
    Random r = new Random();
    for(int x = 0; x < grid_size; x++){
      for(int y = 0; y < grid_size; y++){
        strawb_array[x][y] = 0;
        mushroom_array[x][y] = 0;
        creature_array[x][y] = 0;
        monster_array[x][y] = 0;
      }
    }
    
    for(int i = 0; i < creature_count; i++){
      int x = r.nextInt(grid_size);
      int y = r.nextInt(grid_size);
      creatures[i] = new Creature(x,y);
      creature_array[x][y] += 1;
    }
    
    for(int i = 0; i < (creature_count/2); i++){
      int x = r.nextInt(grid_size);
      int y = r.nextInt(grid_size);
      monsters[i] = new Monster(x,y);
      monster_array[x][y] += 1;
    }
    for(int i = 0; i < strawberry_count; i++){
      int x = r.nextInt(grid_size);
      int y = r.nextInt(grid_size);
      if(mushroom_array[x][y] == 0){
        strawb_array[x][y] += 1;
      }
    }
    for(int i = 0; i < mushroom_count; i++){
      int x = r.nextInt(grid_size);
      int y = r.nextInt(grid_size);
      if(strawb_array[x][y] == 0){
        mushroom_array[x][y] += 1;
      }
    }
  }
  
  /**Repopulate the world with food, mushrooms, monsters and creatures. Generate the new population of creatures through evolution processes*/
  public static void reinitialize(){
    Random r = new Random();
    for(int x = 0; x < grid_size; x++){
      for(int y = 0; y < grid_size; y++){
        strawb_array[x][y] = 0;
        mushroom_array[x][y] = 0;
        creature_array[x][y] = 0;
        monster_array[x][y] = 0;
      }
    }
    
    Arrays.sort(creatures);
    Creature temp1 = creatures[0];
    Creature temp2 = null;
    if(creature_count % 2 == 0){
      temp2 = creatures[1];
    }

    for(int i = 1; i < creature_count; i++){
      Creature[] temp = new Creature[selection_const];
      for(int j = 0; j < selection_const; j++){
        temp[j] = creatures[r.nextInt(creature_count)];
      }
      Arrays.sort(temp);
      Creature[] newC = crossover(temp[0], temp[1]);
      if(r.nextBoolean()){
        creatures[i] = newC[0];
      } else {
        creatures[i] = newC[1];
      }
    }
    
    creatures[0] = new Creature(r.nextInt(grid_size),r.nextInt(grid_size),temp1.chromosome);
    if(temp2 != null) creatures[1] = new Creature(r.nextInt(grid_size),r.nextInt(grid_size),temp2.chromosome);
    
    for(int i = 0; i < creature_count; i++){
      if(r.nextInt(mutation_chance) == 0) creatures[i].mutate();
      creature_array[creatures[i].x][creatures[i].y] += 1;
    }
    
    for(int i = 0; i < (creature_count/2); i++){
      int x = r.nextInt(grid_size);
      int y = r.nextInt(grid_size);
      monsters[i] = new Monster(x,y);
      monster_array[x][y] += 1;
    }
    
     for(int i = 0; i < strawberry_count; i++){
      int x = r.nextInt(grid_size);
      int y = r.nextInt(grid_size);
      if(mushroom_array[x][y] == 0){
        strawb_array[x][y] += 1;
      }
    }
    for(int i = 0; i < mushroom_count; i++){
      int x = r.nextInt(grid_size);
      int y = r.nextInt(grid_size);
      if(strawb_array[x][y] == 0){
        mushroom_array[x][y] += 1;
      }
    }
  }
  
  /**Move forward one timestep, each creature performs an action, monsters perform an action every second timestep.*/
  public static void timeStep(){
    for(Creature c : creatures){
      if(c.energy_level > 0){
        c.do_action(c.select_action());
      }
    }
    
    if(current_step%2 == 0){
      for(Monster m : monsters){
        m.move();
      }
    }
    
    current_step++;
  }
  
  /**Combine two creatures to produce an array containing two possible offspring
    * 
    * @param a - The first parent creature.
    * @param b - The second parent creature.
    * @return children - An array holding two creatures, both possible offspring of the parent.
    */
  public static Creature[] crossover(Creature a, Creature b){
    Creature[] children = new Creature[2];
    float[] chromosome1 = new float[13];
    float[] chromosome2 = new float[13];
    
    Random r = new Random();
    
    for(int i = 0; i < 13; i++){
      if(i%2 != 0){
        chromosome1[i] = a.chromosome[i];
        chromosome2[i] = b.chromosome[i];
      } else {
        chromosome1[i] = b.chromosome[i];
        chromosome2[i] = a.chromosome[i];
      }
    }
    children[0] = new Creature(r.nextInt(grid_size), r.nextInt(grid_size), chromosome1);
    children[1] = new Creature(r.nextInt(grid_size), r.nextInt(grid_size), chromosome2);
    
    return children;
  }
  
}