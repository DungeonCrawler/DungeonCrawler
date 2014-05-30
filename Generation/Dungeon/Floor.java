package Dungeon;

public class Floor extends Tile
{
    public Floor()
    {

    }

    public boolean canMove()
    {
        if(getEntity()==null)
        {
            return true;
        }
        else if(getEntity() instanceof Monster)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean canSee()
    {
        return true;
    }

    public String toString()
    {
        if(getEntity()==null)
        {
            return " ";
        }
        return entityString();
    }
}
