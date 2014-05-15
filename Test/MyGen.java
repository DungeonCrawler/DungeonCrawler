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
    
    public void makeRoom(int x,int y)
    {
        int dir=(int)(Math.random()*4);
        switch(dir)
        {
            case 0:
            case 1:
            case 2:
            case 3:
        }
        
    }
}
