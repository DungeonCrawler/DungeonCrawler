package ItemList.PlayerCharacter;
public class Move
{    
    public boolean makeMove(/*Tile[][] map, int dir*/String[][] map)
    {
        for(int i=0;i<map[0].length;i++)
        {
            for(int j=0;j<map.length;j++)
            {
                if(dir==8)
                {
                    if(map[x][y-1].canMove())
                    {
                        
                    }
                }
            }
        }
        return true;
    }
}