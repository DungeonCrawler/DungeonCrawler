package Dungeon;

public class BorderedCheckers extends Grid
{
    private Room[][] map;
    private int size;
    public static final int[] ALL_TYPES={-1,0,1,2,3,4,5,6,7,8,9,10,11,12};
    private int[] typesAvailable;
    public BorderedCheckers(int newSize)
    {
        super(newSize);
        size=newSize;
        map=new Room[size][size];
        mapDraw();
        fillHoles();
    }

    public void mapDraw()
    {
        for(int i=0;i<size;i++)
        {
            for(int j=0;j<size;j++)
            {
                if(i%2==0)
                {
                    if(j%2==0)
                    {
                        int rand=(int)(Math.random()*9);
                        map[i][j]=new Room(rand);
                        if(i==0||j==0||i==size-1||j==size-1)
                        {
                            map[i][j].closeDoors(i,j,size, this);
                        }
                    }
                    else
                    {
                        map[i][j]=new Room();
                    }
                }
                else
                {
                    if(j%2==1)
                    {
                        int rand=(int)(Math.random()*9);
                        map[i][j]=new Room(rand);
                        if(i==0||j==0||i==size-1||j==size-1)
                        {
                            map[i][j].closeDoors(i,j,size, this);
                        }
                    }
                    else
                    {
                        map[i][j]=new Room();
                    }
                }
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

    public void fillHoles()
    {
        for(int i=0;i<size;i++)
        {
            for(int j=0;j<size;j++)
            {
                if(i%2==0)
                {
                    if(j%2==1)
                    {
                        int rand=(int)(Math.random()*9);
                        map[i][j]=new Room(rand);
                        map[i][j].closeDoors(i,j,size, this);
                    }
                    
                }
                else
                {
                    if(j%2==0)
                    {
                        int rand=(int)(Math.random()*9);
                        map[i][j]=new Room(rand);
                    }
                    
                }
            }
        }
    }
}
