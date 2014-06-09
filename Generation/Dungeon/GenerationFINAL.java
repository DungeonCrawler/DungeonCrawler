package Dungeon;

public class GenerationFINAL
{
    private GenerationFINAL()
    {

    }
    //use odd numbers to have a distinct center point
    public static final int MAX_LENGTH=101;
    public static final int MAX_WIDTH=MAX_LENGTH;
    public static final int NUMBER_OF_ROOMS=1;
    public static Tile[][] writeLevel()
    {
        Tile[][] level=new Tile[MAX_LENGTH][MAX_WIDTH];
        int center=MAX_LENGTH/2+1;

        for(int i=center-6;i<center+3;i++)
        {
            for(int j=center-6;j<center+3;j++)
            {
                if(i==center-6||i==center+2||j==center-6||j==center+2)
                {
                    level[i][j]=new Wall();
                }
                else
                {
                    level[i][j]=new Floor();
                }
            }
        }
        int rooms=0;
        while(rooms<NUMBER_OF_ROOMS)
        {
            rooms+=addRoom(level);
        }
        printArray(level);
        return level;
    }

    public static void printArray(Tile[][] level)
    {
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
    }

    public static int addRoom(Tile[][] level)
    {
        int row=(int)(Math.random()*(MAX_LENGTH-14))+7;
        int col=(int)(Math.random()*(MAX_LENGTH-14))+7;
        if(level[row][col] instanceof Wall)
        {
            int rand=(int)(Math.random()*4)+2;
            int size=(2*rand)+1;
            
            if(level[row-1][col] instanceof Wall &&level[row+1][col] instanceof Wall)//room horizontal
            {
                if(level[row][col-1] instanceof Floor)//make room right
                {
                    for(int i=row-size/2;i<row+size/2;i++)
                    {
                        for(int j=col;j<col+size;j++)
                        {
                            if(level[i][j]!=null)
                            {
                                return 0;
                            }
                        }
                    }
                    //room clear for build
                    for(int i=row-size/2;i<row+size/2;i++)
                    {
                        for(int j=col;j<col+size;j++)
                        {
                            if(i==row-size/2||i==row+size/2||j==col||j==col+size)
                            {
                                level[i][j]=new Wall();
                            }
                            else
                            {
                                level[i][j]=new Floor();
                            }
                            return 1;
                        }
                    }
                }
                if(level[row][col+1] instanceof Floor)//make room left
                {
                    for(int i=row-size/2;i<row+size/2;i++)
                    {
                        for(int j=col-size;j<col;j++)
                        {
                            if(level[i][j]!=null)
                            {
                                return 0;
                            }
                        }
                    }
                    //clear for build
                    for(int i=row-size/2;i<row+size/2;i++)
                    {
                        for(int j=col-size;j<col;j++)
                        {
                            if(i==row-size/2||i==row+size/2||j==col||j==col-size)
                            {
                                level[i][j]=new Wall();
                            }
                            else
                            {
                                level[i][j]=new Floor();
                            }
                            return 1;
                        }
                    }
                }
            }
            if(level[row][col-1] instanceof Wall &&level[row][col+1] instanceof Wall)//room vertical
            {
                if(level[row-1][col] instanceof Floor)//make room down
                {
                    for(int i=row;i<row+size;i++)
                    {
                        for(int j=col-size/2;j<col+size/2;j++)
                        {
                            if(level[i][j]!=null)
                            {
                                return 0;
                            }
                        }
                    }
                    //build-on!
                    for(int i=row;i<row+size;i++)
                    {
                        for(int j=col-size/2;j<col+size/2;j++)
                        {
                            if(i==col-size/2||i==col+size/2||j==row||j==row+size)
                            {
                                level[i][j]=new Wall();
                            }
                            else
                            {
                                level[i][j]=new Floor();
                            }
                            return 1;
                        }
                    }
                }
                if(level[row+1][col] instanceof Floor)//make room up
                {
                    for(int i=row-size;i<row;i++)
                    {
                        for(int j=col-size/2;j<col+size/2;j++)
                        {
                            if(level[i][j]!=null)
                            {
                                return 0;
                            }
                        }
                    }
                    //ready to build
                    for(int i=row-size;i<row;i++)
                    {
                        for(int j=col-size/2;j<col+size/2;j++)
                        {
                            if(i==col-size/2||i==col+size/2||j==row||j==row-size)
                            {
                                level[i][j]=new Wall();
                            }
                            else
                            {
                                level[i][j]=new Floor();
                            }
                            return 1;
                        }
                    }
                }
            }
        }
        level[row][col]=new Door();
        return 0;
    }
}
