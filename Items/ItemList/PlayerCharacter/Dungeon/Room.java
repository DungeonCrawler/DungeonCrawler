package ItemList.PlayerCharacter.Dungeon;

public class Room
{
    private String[][]room;
    private int[] doorsOpen;//n,e,s,w 0=open 1=closed
    public Room(int r)
    {
        doorsOpen=new int[4];
        roomDraw(r);
    }
    public Room()
    {
        roomDraw(-1);
    }
    public String[][] roomDraw(int type)
    {
        String[][] newRoom=new String[11][11];
        if(type>4)
        {
            newRoom=roomDraw(4);
        }
        for(int i=0;i<11;i++)
        {
            for(int j=0;j<11;j++)
            {
                if(type==-1)
                {
                    newRoom[i][j]=" ";
                }
                if(type==0)
                {
                    if((i==0||i==10||j==0||j==10)&&(i!=5&&j!=5))
                    {
                        //newRoom[i][j]="\u2591";//use this if you can fix spacing
                        newRoom[i][j]="#";
                    }
                    else
                    {
                        newRoom[i][j]=" ";
                    }
                }
                if(type==1)
                {

                    if(i==5||j==5)
                    {
                        newRoom[i][j]=" ";
                    }
                    else if(newRoom[i][j]==null&&i==0||j==0||i==10||j==10)//fills in outer edge
                    {
                        newRoom[i][j]="#";
                    }
                    else if(newRoom[i][j]==null&&i==1||j==1||i==9||j==9)
                    {
                        newRoom[i][j]="#"; 
                    }
                    else
                    {
                        newRoom[i][j]=" "; 
                    }
                }
                if(type==2)
                {
                    if(j!=5)
                    {
                        newRoom[i][j]="#";
                    }
                    else
                    {
                        newRoom[i][j]=" ";
                    }
                }
                if(type==3)
                {
                    if(i!=5)
                    {
                        newRoom[i][j]="#";
                    }
                    else
                    {
                        newRoom[i][j]=" ";
                    }
                }
                if(type==4)
                {
                    if(i!=5&&j!=5)
                    {

                        newRoom[i][j]="#";

                    }
                    else
                    {
                        newRoom[i][j]=" ";
                    }
                }
                if(type==5)
                {
                    if(i>=6)
                    {
                        newRoom[i][j]="#";
                    }
                    
                }
                if(type==6)
                {
                    if(j<=4)
                    {
                        newRoom[i][j]="#";
                    }
                    
                }
                if(type==7)
                {
                    if(i<=4)
                    {
                        newRoom[i][j]="#";
                    }
                   
                }
                if(type==8)
                {
                    if(j>=6)
                    {
                        newRoom[i][j]="#";
                    }
                    
                }
                if(type==9)
                {
                    if(i>=6||j>=6)
                    {
                        newRoom[i][j]="#";
                    }
                }
                if(type==10)
                {
                    if(i>=6||j<=4)
                    {
                        newRoom[i][j]="#";
                    }
                }
                if(type==11)
                {
                    if(i<=4||j<=4)
                    {
                        newRoom[i][j]="#";
                    }
                }
                if(type==12)
                {
                    if(i<=4||j>=6)
                    {
                        newRoom[i][j]="#";
                    }
                }
            }
        }
        room=newRoom;
        return room;
    }

    public void printRow(int row)
    {
        for(String s:room[row])
        {
            System.out.print(s);
        }
    }
    public void closeDoors(int y, int x, int size, Grid g)
    {
        size-=1;
        if(y-1<0)
        {
            doorsOpen[0]=1;
            room[0][5]="#";
        }
        if(x-1<0)
        {
            doorsOpen[3]=1;
            room[5][0]="#";
        }
        if(y+1>size)
        {
            doorsOpen[2]=1;
            room[10][5]="#";
        }
        if(x+1>size)
        {
            doorsOpen[1]=1;
            room[5][10]="#";
        }
        if(x!=0&&g.getGrid()[x-1][y]!=null&&g.getGrid()[x-1][y].getRoom()[10][5].equals("#"))
        {
            doorsOpen[0]=1;
            room[0][5]="#";
        }
        if(y!=size&&g.getGrid()[x][y+1]!=null&&g.getGrid()[x][y+1].getRoom()[5][0].equals("#"))
        {
            doorsOpen[1]=1;
            room[5][10]="#";
        }
        if(x!=size&&g.getGrid()[x+1][y]!=null&&g.getGrid()[x+1][y].getRoom()[0][5].equals("#"))
        {
            doorsOpen[2]=1;
            room[10][5]="#";
        }
        if(y!=0&&g.getGrid()[x][y-1]!=null&&g.getGrid()[x][y-1].getRoom()[5][10].equals("#"))
        {
            doorsOpen[3]=1;
            room[5][0]="#";
        }
    }
    public String[][] getRoom()
    {
        return room;
    }
}