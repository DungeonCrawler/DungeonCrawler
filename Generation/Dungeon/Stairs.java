package Dungeon;

/**
 * Write a description of class Stairs here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Stairs extends Tile
{
    private int mobs;
    public Stairs(int monsters)
    {
        mobs=monsters;
    }

    public boolean canMove()
    {
        return false;
    }

    public boolean canSee()
    {

        return true;
    }

    public void interact(Player p, Key1 k)
    {
        if(p.getKills()==mobs)
        {
            Tile[][] level=GenerationFINAL.writeLevel();
            int center=level.length/2+1;
            Player p1=new Player(level,center,center);
            p1.levelUp();
            p1.setGrid(level);
            k.setPlayer(p1);
            level[center][center].setEntity(p);
            
        }

    }

    public String toString()
    {
        return ">";
    }
}
