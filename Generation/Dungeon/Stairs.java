package Dungeon;

/**
 * Write a description of class Stairs here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Stairs extends Tile
{
    private int mobs;
    public Stairs(int monsters)
    {
        mobs=monsters;
    }

    public boolean canMove()
    {
        return false;
    }

    public boolean canSee()
    {

        return true;
    }

    public void interact(Key1 k)
    {
        Player p=k.getPlayer();
        if(p.getKills()==mobs)
        {
            System.out.println(true);
            Tile[][] level=GenerationFINAL.writeLevel();
            int center=level.length/2+1;
            Player p1=new Player(level,center,center);
            p1.levelUp();
            p1.setGrid(level);
            k.setPlayer(p1);
            p1.setK(k);
            level[center][center].setEntity(p);
            int numGoblins=(int)(Math.random()*3+p1.getLevel());
            p1.setMobs(numGoblins);
            for(int i=0;i<numGoblins;i++)
            {
            int row=(int)(Math.random()*level.length);
            int col=(int)(Math.random()*level.length);
            if(level[row][col] instanceof Floor &&level[row][col].getEntity()==null)
            {
                level[row][col].setEntity(new Goblin(level,row,col,p1));
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
            GenerationFINAL.printArray(level,p1);
        }
        System.out.println(false);
    }

    public String toString()
    {
        return ">";
    }
}
