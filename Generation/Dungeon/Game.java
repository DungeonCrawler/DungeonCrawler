package Dungeon;



public class Game
{
    public static void main(String[] args)
    {
        Tile[][] level=GenerationFINAL.writeLevel();
        int center=level.length/2+1;
        Player p=new Player(level,center,center);
        level[center][center].setEntity(p);
        Key1 k=new Key1(p,level);
        p.setK(k);
        int numGoblins=(int)(Math.random()*3+p.getLevel());
        p.setMobs(numGoblins);
        for(int i=0;i<numGoblins;i++)
        {
            int row=(int)(Math.random()*level.length);
            int col=(int)(Math.random()*level.length);
            if(level[row][col] instanceof Floor &&level[row][col].getEntity()==null)
            {
                level[row][col].setEntity(new Goblin(level,row,col,p));
            }
            else
            {
                i--;
            }
        }
        while(true)
        {
            int row=(int)(Math.random()*level.length);
            int col=(int)(Math.random()*level.length);
            if(level[row][col] instanceof Floor &&level[row][col].getEntity()==null)
            {
                level[row][col]=new Stairs(numGoblins);
                break;
            }
        }
        GenerationFINAL.printArray(level,p);
    }
}
