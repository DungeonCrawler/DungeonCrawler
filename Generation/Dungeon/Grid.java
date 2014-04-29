package Dungeon;

public class Grid
{
    private Room[][] map;
    private int size;
    public Grid(int newSize)
    {
        size=newSize;
        map=new Room[size][size];
        mapDraw();
    }

    public void mapDraw()
    {
        for(int i=0;i<size;i++)
        {
            for(int j=0;j<size;j++)
            {
                int rand=(int)(Math.random()*13);
                map[i][j]=new Room(rand);
            }
        }
    }

    public void printMap()
    {

        for(int i=0;i<size;i++)
        {
            for(int j=0;j<11;j++)
            {
                for(int row=0;row<size;row++)
                {
                    map[i][row].printRow(j);
                }
                System.out.println();
            }

        }
    }

    public Room[][] getGrid()
    {
        return map;
    }
    public int getSize()
    {
        return size;
    }
    public void setMap(Room[][] newMap)
    {
        map=newMap;
    }
}
