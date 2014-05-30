 package Dungeon; 
 import Dungeon.*;
public abstract class Monster extends Entity{
    private int health;
    private int defense;
    private int smallAgility,mediumAgility,largeAgility,superMassiveAgility;
    private int strength;
    private String size;
    private String[] loot;
    public Monster(Tile[][] map,int row,int col){
        super(map,row,col);
        health=strength/2;
        strength=2;
        defense=2;
        smallAgility=1;
        size="";
        mediumAgility=5;
        largeAgility=10;
        superMassiveAgility=15;
    }

    public void setHealth(int newHealth){
        health=newHealth;
    }
    
    public void gainHealth(int healthAdded){
        health+=healthAdded;
    }

    public void loseHealth(int healthLost){
        health-=healthLost;
    }

    public int getHealth(){
        return health;
    }

    public void setStrength(int newStrength){
        strength=newStrength;
    }

    public int getStrength(){
        return strength;
    }

    public void setDefense(int newDefense){
        defense=newDefense;
    }

    public int getDefense(){
        return defense;
    }

    public void setSize(String newSize){
        if(newSize.toLowerCase().equals("small")||newSize.toLowerCase().equals("medium")||newSize.toLowerCase().equals("large")){
            size=newSize.toLowerCase();
        }
    }

    public String getSize(){
        return size;
    }

    public int getSpeed(){
        if(size.equals("small")){
            return smallAgility;
        }
        else if(size.equals("medium")){
            return mediumAgility;
        }
        else if(size.equals("large")){
            return largeAgility;
        }
        else{
            return superMassiveAgility;
        }
    }
    
    public void setLoot(String[] list){
        loot=new String[list.length];
        int i=0;
        for(String s:list){
            loot[i]=s;
            i++;
        }
    }
    public String[] getLoot(){
        return loot;
    }
    
    public abstract void attacked(int damageDealt);
    public abstract int dealDamage();
}