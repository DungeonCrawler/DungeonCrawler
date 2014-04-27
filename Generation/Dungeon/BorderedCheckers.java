package Dungeon;


public class BorderedCheckers
{
    private Room[][] map;
    private int size;
    public static final int[] ALL_TYPES={-1,0,1,2,3,4,5,6,7,8,9,10,11,12};
    private int[] typesAvailable;
    public static final int[][] CHECK_OPEN={{0,0,0,0},
                                            {1,1,1,1},
                                            {1,1,1,1},
                                            {1,0,1,0},
                                            {0,1,0,1},
                                            {1,1,1,1},
                                            {1,1,0,1},
                                            {1,1,1,0},
                                            {0,1,1,1},
                                            {1,0,1,1}};//n,e,s,w
    public BorderedCheckers(int newSize)
    {
        size=newSize;
        map=new Room[size][size];
        mapDraw();
        //typesAvailable=checkTypes();
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
}
