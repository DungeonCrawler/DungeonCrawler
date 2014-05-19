public class MyGen
{
    private final int tileWall=0;
    private final int tileFloor=1;
    private final int tileDoor=2;
    private final int tileUpStairs=3;
    private final int tileDownStairs=4;
    private final int DEFAULT_SIZE=50;
    private final int DEFAULT_ROOMS=15;
    private final int MIN_ROOM_SIZE=7;//5x5 and 2 walls
    private final int MAX_ROOM_SIZE=23;//21x21 and 2 walls
    private int[][] map;
    private int numRooms;
    public MyGen()
    {
        map=new int[DEFAULT_SIZE][DEFAULT_SIZE];
        numRooms=DEFAULT_ROOMS;
        generate();
    }

    public MyGen(int size)
    {
        map=new int[size][size];
        numRooms=DEFAULT_ROOMS;
        generate();
    }

    public MyGen(int size,int amtRooms)
    {
        map=new int[size][size];
        numRooms=amtRooms;
        generate();
    }

    public int getCell(int x, int y)
    {
        return map[x][y];
    }

    public void setCell(int x, int y, int type)
    {
        map[x][y]=type;
    }

    public void generate()
    {
        int size=map.length;
        int num=numRooms;
        for(int i=0;i<size;i++)
        {
            for(int j=0;j<size;j++)
            {
                map[i][j]=tileWall;
            }
        }
        makeRoom(size/2,size/2);
        int count=1;
        while(count<num)
        {
            int x=(int)Math.random()*map.length;
            int y=(int)Math.random()*map.length;
            while(getCell(y,x)==tileFloor)
            {
                int yOrX=(int)(Math.random()*2);
                if(yOrX==1)
                {
                    x++;
                }
                else
                {
                    y++;
                }
                if(y>=map.length||x>=map.length)
                {
                    break;
                }
            }    
            if(makeRoom(x,y))
            {
                count++;
            }

        }
    }

    public void printMap()
    {
        for(int i=0;i<map.length;i++)
        {
            for(int j=0; j<map.length;j++)
            {
                switch (map[i][j])  //look how cool i am using case switch!
                {
                    case(tileWall):
                    System.out.print("#");
                    break;
                    case(tileFloor):
                    System.out.print(" ");
                    break;
                    case(tileDoor):
                    System.out.print("+");
                    break;
                    case(tileUpStairs):
                    System.out.print("<");
                    break;
                    case(tileDownStairs):
                    System.out.print(">");
                    break;
                }
            }
            System.out.println();
        }
    }

    public boolean makeRoom(int x,int y)
    {
        int dir=(int)(Math.random()*4);
        int size=(int)(Math.random()*7)+8;
        switch(dir)
        {
            case 0://n
            for(int i=y;i>y-size;i--)
            {
                if(i<0||i>map.length)
                {
                    return false;
                }
                for(int j=(x-size/2);j<x+(size+1)/2;j++)
                {
                    if(j<0||j>map.length)
                    {
                        return false;
                    }
                    if(getCell(i,j)!=tileWall)
                    {
                        return false;
                    }
                }
            }
            for(int i=y;i>y-size;i--)
            {
                for(int j=(x-size/2);j<x+(size+1)/2;j++)
                {
                    if (j == (x-size/2)) setCell(j, i, tileWall);
					else if (xtemp == (x+(xlen-1)/2)) setCell(xtemp, ytemp, tileWall);
					else if (ytemp == y) setCell(xtemp, ytemp, tileWall);
					else if (ytemp == (y-ylen+1)) setCell(xtemp, ytemp, tileWall);
					else setCell(j, i, tileFloor);
                }
            }
            case 1://e
            for(int i=y-size/2;i<y+(size+1)/2;i++)
            {
                if(i<0||i>map.length)
                {
                    return false;
                }
                for(int j=x;j<x+size;j++)
                {
                    if(j<0||j>map.length)
                    {
                        return false;
                    }
                    if(getCell(i,j)!=tileWall)
                    {
                        return false;
                    }
                }
            }
            for(int i=y-size/2;i<y+(size+1)/2;i++)
            {
                for(int j=x;j<x+size;j++)
                {

                }
            }
            case 2://s
            for(int i=y;i>y+size;i++)
            {
                if(i<0||i>map.length)
                {
                    return false;
                }
                for(int j=(x-size/2);j<x+(size+1)/2;j++)
                {
                    if(j<0||j>map.length)
                    {
                        return false;
                    }
                    if(getCell(i,j)!=tileWall)
                    {
                        return false;
                    }
                }
            }
            for(int i=y;i>y+size;i++)
            {
                for(int j=(x-size/2);j<x+(size+1)/2;j++)
                {

                }
            }
            case 3://w
            for(int i=y-size/2;i>y-size;i++)
            {
                if(i<0||i>map.length)
                {
                    return false;
                }
                for(int j=(x-size);j<x;j++)
                {
                    if(j<0||j>map.length)
                    {
                        return false;
                    }
                    if(getCell(i,j)!=tileWall)
                    {
                        return false;
                    }
                }
            }
            for(int i=y-size/2;i>y-size;i++)
            {
                for(int j=(x-size);j<x;j++)
                {

                }
            }
        }

    }
}
