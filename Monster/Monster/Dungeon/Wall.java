package Dungeon;


public class Wall extends Tile
{
    public Wall()
    {
        super(0);
    }

    public  boolean canMove()
    {
        return false;
    }

    public  boolean canSee()
    {
        return false;
    }
    public String toString()
    {
        return "#";
    }
}
