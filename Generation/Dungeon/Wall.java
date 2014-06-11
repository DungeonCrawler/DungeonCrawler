package Dungeon;

public class Wall extends Tile
{
    private int row;
    private int col;
    public Wall(int x,int y)
    {
        row=y;
        col=x;
    }
    public boolean canMove()
    {
        return false;
    }
    public boolean canSee()
    {
        return false;
    }
    public String toString()
    {
        return "#";
    }
    public int getX()
    {
        return col;
    }
    public int getY()
    {
        return row;
    }
}
