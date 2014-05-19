package Dungeon;

public class Floor extends Tile
{
    public Floor()
    {
        super(1);
    }
    public void canMove()
    {
        return true;
    }
    public void canSee()
    {
        return true;
    }
    public String toString()
    {
        return " ";
    }
}
