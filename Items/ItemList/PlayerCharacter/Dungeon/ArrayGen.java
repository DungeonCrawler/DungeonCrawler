package ItemList.PlayerCharacter.Dungeon;

public class ArrayGen
{
    private int[][] map;
    private int size;
    public ArrayGen(int size)
    {
        map = new int[size][size];
        this.size=size;
    }
    public void makeFloor()
    {
        for(int i=0;i<size;i++)
        {
            for(int j=0;j<size;j++)
            {
                map[i][j]=(int)(Math.random()*100);
            }
        }
    }
    
    public String toString()
    {
        String out="";
        for(int i=0;i<size;i++)
        {
            for(int j=0;j<size;j++)
            {
                out+=map[i][j]+" ";
                if(map[i][j]<10)
                {
                    out+=" ";
                }
            }
            out+="\n";
        }
        return out;
    }
    
    public void reduceFloor()
    {
        for(int i=0;i<size;i++)
        {
            for(int j=0;j<size;j++)
            {
                if(map[i][j]<=50)
                {
                    map[i][j]=0;
                }
                if(map[i][j]>50&&map[i][j]<=60)
                {
                    map[i][j]=5;
                }
                if(map[i][j]>60&&map[i][j]<=70)
                {
                    map[i][j]=7;
                }
                if(map[i][j]>70&&map[i][j]<=80)
                {
                    map[i][j]=9;
                }
                if(map[i][j]>80&&map[i][j]<99)
                {
                    map[i][j]=11;
                }
                if(map[i][j]==99)
                {
                    map[i][j]=21;
                }
            }
        }
    }
    
    public int[][] getMap()
    {
        return map;
    }
}
