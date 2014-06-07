package Dungeon;


public class GenerationFINAL
{
    private GenerationFINAL()
    {
        
    }
    //use odd numbers to have a distinct center point
    public static final int MAX_LENGTH=101;
    public static final int MAX_WIDTH=MAX_LENGTH;
    public static Tile[][] writeLevel()
    {
        Tile[][] level=new Tile[MAX_LENGTH][MAX_WIDTH];
        int center=MAX_LENGTH/2+1;
        int count=0;
        for(int i=center-6;i<center+3;i++)
        {
            for(int j=center-6;j<center+3;j++)
            {
                if(i==center-6||i==center+2||j==center-6||j==center+2)
                {
                    level[i][j]=new Wall();
                }
                else if(i==49&&j==49)
                {
                    level[i][j]=new Wall();
                }
                else
                {
                    level[i][j]=new Floor();
                }
            }
        }
        for(int i=0;i<MAX_LENGTH;i++)
        {
            for(int j=0;j<MAX_LENGTH;j++)
            {
                if(level[i][j]!=null)
                System.out.print(level[i][j]);
                else
                System.out.print("O");
            }
            System.out.println();
        }
        return null;
    }
}
