package ItemList.PlayerCharacter.Dungeon;

public class GridGen extends Grid //dont have to rewrite printmap
{
    private int size;
    private final int DEFAULT_SIZE=20;//default # of rooms
    public GridGen()
    {
        super(50);
        size=DEFAULT_SIZE;
        
    }
    public GridGen(int newSize)
    {
        super(50);
        size=newSize;
    }
    public void mapDraw()
    {
        Room[][] dun=getGrid();
        dun[24][24]=new Room();
        setMap(dun);
    }

    public void printMap()
    {
        Room[][] dun=getGrid();
        for(int i=0;i<size;i++)
        {
            for(int j=0;j<11;j++)
            {
                for(int row=0;row<size;row++)
                {
                    dun[i][row].printRow(j);
                }
                System.out.println();
            }
            
            
        }
    }
}
