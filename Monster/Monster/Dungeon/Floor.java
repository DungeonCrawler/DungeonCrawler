package Dungeon;

public class Floor extends Tile
{
    public Floor()
    {
        super(1);
    }
    public boolean canMove()
    {
        return true;
    }
    public boolean canSee()
    {
        return true;
    }
    public String toString()
    {
        return " ";
    }
}
