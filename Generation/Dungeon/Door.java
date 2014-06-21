package Dungeon;

/**
 * Write a description of class Door here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Door extends Tile
{
    private boolean isOpen;
    public Door()
    {
        isOpen=false;
    }

    public boolean canMove()
    {
        if(isOpen)
        {
            return true;
        }
        return false;
    }

    public boolean canSee()
    {
        if(isOpen)
        {
            return true;
        }
        return false;
    }

    public void interact(Key1 k)
    {
        isOpen=true;
    }

    public String toString()
    {
        if(getEntity()!=null)
        {
            return getEntity().toString();
        }
        if(!isOpen)
        {
            return "+";
        }
        return "-";
    }
}
