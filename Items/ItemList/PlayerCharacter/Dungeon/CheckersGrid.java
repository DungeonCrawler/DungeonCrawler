package ItemList.PlayerCharacter.Dungeon;

public class CheckersGrid extends Grid
{
    private Room[][] map;
    private int size;
    public CheckersGrid(int newSize)
    {
        super(newSize);
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
                if(i%2==0)
                {
                    if(j%2==0)
                    {
                        int rand=(int)(Math.random()*13);
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
                        int rand=(int)(Math.random()*13);
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
