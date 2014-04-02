public class RoomDraw
{
    public static String[][] roomDraw(int type)
    {
        String[][] room=new String[11][11];
        if(type>4)
        {
            room=roomDraw(4);
        }
        for(int i=0;i<11;i++)
        {
            for(int j=0;j<11;j++)
            {
                if(type==0)
                {
                    if(i==0||i==10||j==0||j==10)
                    {
                        room[i][j]="0";
                    }
                    else
                    {
                        room[i][j]=" ";
                    }
                }
                if(type==1)
                {
                    
                    if((i!=0&&i!=10&&j!=0&&j!=10)&&(i==1||i==9||j==1||j==9)&&(i!=5&&j!=5))
                    {
                        room[i][j]="0";
                    }
                    else
                    {
                        room[i][j]=" ";
                    }
                    if(((i==0||i==10)&&(j==4))||((i==0||i==10)&&(j==6)))
                    {
                       room[i][j]="0"; 
                    }
                    if(((j==0||j==10)&&(i==4))||((j==0||j==10)&&(i==6)))
                    {
                       room[i][j]="0"; 
                    }
                }
                if(type==2)
                {
                    if(j==4||j==6)
                    {
                        room[i][j]="0";
                    }
                    else
                    {
                        room[i][j]=" ";
                    }
                }
                if(type==3)
                {
                    if(i==4||i==6)
                    {
                        room[i][j]="0";
                    }
                    else
                    {
                        room[i][j]=" ";
                    }
                }
                if(type==4)
                {
                    if(i==4||i==6||j==4||j==6)
                    {
                        if(i==5||j==5)
                        {
                            room[i][j]=" ";
                        }
                        else
                        {
                            room[i][j]="0";
                        }
                    }
                    else
                    {
                        room[i][j]=" ";
                    }
                }
                if(type==5)
                {
                    if(i==6)
                    {
                        room[i][j]="0";
                    }
                    if(i>6)
                    {
                        room[i][j]=" ";
                    }
                }
                if(type==6)
                {
                    if(j==4)
                    {
                        room[i][j]="0";
                    }
                    if(j<4)
                    {
                        room[i][j]=" ";
                    }
                }
                if(type==7)
                {
                    if(i==4)
                    {
                        room[i][j]="0";
                    }
                    if(i<4)
                    {
                        room[i][j]=" ";
                    }
                }
                if(type==8)
                {
                    if(j==6)
                    {
                        room[i][j]="0";
                    }
                    if(j>6)
                    {
                        room[i][j]=" ";
                    }
                }
            }
        }
        return room;
    }
}
