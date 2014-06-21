package Dungeon;
import java.util.ArrayList;
public class GenerationFINAL
{
    private GenerationFINAL()
    {

    }
    //use odd numbers to have a distinct center point
    public static final int MAX_LENGTH=73;
    public static final int MAX_WIDTH=MAX_LENGTH;
    public static final int NUMBER_OF_ROOMS=10;
    private static ArrayList<Wall> walls;
    public static Tile[][] writeLevel()
    {
        Tile[][] level=new Tile[MAX_LENGTH][MAX_WIDTH];
        int center=MAX_LENGTH/2+1;
        walls=new ArrayList<Wall>();
        
        for(int i=center-6;i<center+3;i++)
        {
            for(int j=center-6;j<center+3;j++)
            {
                if(i==center-6||i==center+2||j==center-6||j==center+2)
                {
                    Wall w=new Wall(i,j);
                    walls.add(w);
                    level[i][j]=w;
                }
                else
                {
                    level[i][j]=new Floor();
                }
            }
        }
        int rooms=1;
        while(rooms<NUMBER_OF_ROOMS)
        {
          rooms+=addRoom(level);
        }
        rooms+=addRoom(level);
        return level;
    }

    public static String returnArray(Tile[][] level, Player p)
    {
        String s="";
        for(int i=0;i<MAX_LENGTH;i++)
        {
            for(int j=0;j<MAX_LENGTH;j++)
            {
                if(level[i][j]!=null)
                    s+=level[i][j].toString();
                else
                {
                    level[i][j]=new InnerWall(i,j);
                    s+=level[i][j].toString();
                }
            }
            s+="\n";
        }
        return s;
    } 
    public static void printArray(Tile[][] level, Player p)
    {
        for(int i=0;i<MAX_LENGTH;i++)
        {
            for(int j=0;j<MAX_LENGTH;j++)
            {
                if(level[i][j]!=null)
                    System.out.print(level[i][j]);
                else
                {
                    level[i][j]=new InnerWall(i,j);
                    System.out.print(level[i][j]);
                }
            }
            System.out.println();
        }
    }
    public static int addRoom(Tile[][] level)
    {
        int seed=(int)(Math.random()*walls.size());
        //System.out.println("It ran the method");
        int col=walls.get(seed).getX();
        int row=walls.get(seed).getY();
        int size=(int)(Math.random()*4)+3;//distance out from center
        
        //System.out.println("case 1");
        
        if(level[row-1][col] instanceof Wall &&level[row+1][col] instanceof Wall)//room horizontal
        {
            //System.out.println("case 1.5");
            if(level[row][col-1] instanceof Floor)//make room right
            {
                for(int i=row-size;i<row+size;i++)
                {
                    for(int j=col;j<col+(size*2)+1;j++)
                    {
                        //System.out.println("case 2r");
                        if(level[i][j]==null||level[i][j] instanceof Wall)
                        {
                            break;
                        }
                        else
                        {
                            //System.out.println("case Break");
                            return 0;
                        }
                    }
                }
                //room clear for build
                for(int i=row-size;i<row+size;i++)
                {
                    for(int j=col;j<col+(size*2)+1;j++)
                    {
                        //System.out.println("case 3");
                        if(i==row-size||i==row+size-1||j==col||j==col+(2*size))
                        {
                            if(level[i][j]==null)
                            {
                                Wall w=new Wall(i,j);
                                walls.add(w);
                                level[i][j]=w;
                            }
                        }
                        else
                        {
                            level[i][j]=new Floor();
                        }
                        
                    }
                }
                level[row][col]=new Door();
                return 1;
            }
            if(level[row][col+1] instanceof Floor)//make room left
            {
                for(int i=row-size;i<row+size;i++)
                {
                    for(int j=col-(2*size);j<=col;j++)
                    {
                        //System.out.println("case 2l");
                        if(level[i][j]==null||level[i][j] instanceof Wall)
                        {
                            break;
                        }
                        else
                        {
                            //System.out.println("case Break");
                            return 0;
                        }
                    }
                }
                //clear for build
                for(int i=row-size;i<row+size;i++)
                {
                    for(int j=col-(2*size);j<=col;j++)
                    {
                        //System.out.println("case 3");
                        if(i==row-size||i==row+size-1||j==col||j==col-(2*size))
                        {
                            if(level[i][j]==null)
                            {
                                Wall w=new Wall(i,j);
                                walls.add(w);
                                level[i][j]=w;
                            }
                        }
                        else
                        {
                            level[i][j]=new Floor();
                        }
                        
                    }
                }
                level[row][col]=new Door();
                return 1;
            }
        }
        else if(level[row][col-1] instanceof Wall &&level[row][col+1] instanceof Wall)//room vertical
        {
            //System.out.println("case 1.5");
            if(level[row-1][col] instanceof Floor)//make room down
            {
                for(int i=row;i<row+(2*size)+1;i++)
                {
                    //System.out.println("case 2d");
                    for(int j=col-size;j<col+size;j++)
                    {
                        if(level[i][j]==null||level[i][j] instanceof Wall)
                        {
                            break;
                        }
                        else
                        {
                            //System.out.println("case Break");
                            return 0;
                        }
                    }
                }
                //build-on!
                for(int i=row;i<row+(2*size);i++)
                {
                    //System.out.println("case 3");
                    for(int j=col-size;j<col+size;j++)
                    {
                        if(j==col-size||j==col+size-1||i==row||i==row+(2*size)-1)
                        {
                            if(level[i][j]==null)
                            {
                                Wall w=new Wall(i,j);
                                walls.add(w);
                                level[i][j]=w;
                            }
                        }
                        else
                        {
                            level[i][j]=new Floor();
                        }
                        
                    }
                }
                level[row][col]=new Door();
                return 1;
            }
            if(level[row+1][col] instanceof Floor)//make room up
            {
                for(int i=row-(2*size);i<=row;i++)
                {
                    for(int j=col-size;j<col+size;j++)
                    {
                        //System.out.println("case 2u");
                        if(level[i][j]==null||level[i][j] instanceof Wall)
                        {
                            break;
                        }
                        else
                        {
                            //System.out.println("case Break");
                            return 0;
                        }
                    }
                }
                //ready to build
                for(int i=row-(2*size);i<=row;i++)
                {
                    for(int j=col-size;j<col+size;j++)
                    {
                        //System.out.println("case 3");
                        if(j==col-size||j==col+size-1||i==row||i==row-(size*2))
                        {
                            if(level[i][j]==null)
                            {
                                Wall w=new Wall(i,j);
                                walls.add(w);
                                level[i][j]=w;
                            }
                        }
                        else
                        {
                            level[i][j]=new Floor();
                        }
                        
                    }
                }
                level[row][col]=new Door();
                return 1;
            }
        }

        return 0;
    }
    
    
}
