package Dungeon;

public class Room
{
    private String[][]room;
    public Room(int r)
    {
        roomDraw(r);
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

    public String[][] getRoom()
    {
        return room;
    }
}