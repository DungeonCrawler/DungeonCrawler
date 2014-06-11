package Dungeon;


/**
 * Write a description of class Stairs here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Stairs extends Tile
{
    
    public Stairs()
    {
        
    }

    public boolean canMove()
    {
        return false;
    }

    public boolean canSee()
    {
        
        return true;
    }

    public void interact()
    {
        //GenerationFINAL.writeLevel(); FIX THIS!!!!!!!
        TileTester.makeFloor(new Tile[7][7]);
    }

    public String toString()
    {
        
       
        return ">";
    }
}
