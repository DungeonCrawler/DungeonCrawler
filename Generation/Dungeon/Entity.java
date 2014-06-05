package Dungeon;



public class Entity
{
    private Tile[][] grid;
    private int myX;
    private int myY;
    public Entity(Tile[][] map, int row, int col)
    {
        grid=map;
        myX=row;
        myY=col;
    }
    public String toString()
    {
        return "?";
    }
    public int getX()
    {
        return myX;
    }
    public int getY()
    {
        return myY;
    }
    public void setX(int num)
    {
        myX=num;
    }
    public void setY(int num)
    {
        myY=num;
    }
    public void move(int num)
    {
        if(num==0)//n
        {
            if(canMove(0))
            {
                grid[myX][myY].setEntity(null);
                grid[myX-1][myY].setEntity(this);
                myX--;
            }
        }
        if(num==1)//e
        {
            if(canMove(1))
            {
                grid[myX][myY].setEntity(null);
                grid[myX][myY+1].setEntity(this);
                myY++;
            }
        }
        if(num==2)//s
        {
           if(canMove(2))
           {
               grid[myX][myY].setEntity(null);
               grid[myX+1][myY].setEntity(this);
               myX++;
           }
        }
        if(num==3)//w
        {
            if(canMove(3))
            {
                grid[myX][myY].setEntity(null);
                grid[myX][myY-1].setEntity(this);
                myY--;
            }
        }
    }
    public boolean canMove(int dir)
    {
        if(dir==0)
        {
            if(myX!=0&&grid[myX-1][myY].canMove())
            {
                return true;
            }
        }
        if(dir==1)
        {
            if(myY!=grid.length-1&&grid[myX][myY+1].canMove())
            {
                return true;
            }
        }
        if(dir==2)
        {
            if(myX!=grid.length-1&&grid[myX+1][myY].canMove())
            {
                return true;
            }
        }
        if(dir==3)
        {
            if(myY!=0&&grid[myX][myY-1].canMove())
            {
                return true;
            }
        }
        return false;
    }
    public Tile[][] getGrid()
    {
        return grid;
    }
    public int getHealth()
    {
        return 0;
    }
}
