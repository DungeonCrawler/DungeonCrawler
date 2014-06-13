package Dungeon;
public class InnerWall extends Wall
{
    public InnerWall(int row, int col)
    {
        super(row,col);
    }
    public String toString()
    {
        return "O";
    }
    public boolean canMove()
    {
        return false;
    }
    public boolean canSee()
    {
        return false;
    }
    public int getX()
    {
        return super.getX();
    }
    public int getY()
    {
        return super.getY();
    }
}
