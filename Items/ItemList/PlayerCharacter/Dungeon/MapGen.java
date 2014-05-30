package ItemList.PlayerCharacter.Dungeon;

public class MapGen
{
    private int[][] map;
    private int[][] sizeMap;
    private int size;
    public MapGen(int[][] sizes)
    {
        sizeMap=sizes;
        map=new int[sizes.length*11][sizes.length*11];
        size=map.length;
    }

    public void rooms()
    {
        for(int i=0;i<sizeMap.length;i++)
        {
            int roomi=i*11;
            for(int j=0;j<sizeMap.length;j++)
            {
                int roomj=j*11;
                int rSize=sizeMap[i][j];
                for(int y=roomi;y<roomi+rSize;y++)
                {
                    for(int x=roomj;x<roomj+rSize;x++)
                    {
                        map[y][x]=1;
                    }
                }
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
                out+=map[i][j];
            }
            out+="\n";
        }
        return out;
    }
}
