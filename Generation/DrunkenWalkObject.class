public class DrunkenWalkObject
{
    public static void main(String[] args)
    {
        Room[][] map=new Room[50][50];
        String[][]pMap=new String[50][50];
        int xpos=25;
        int ypos=25;
        int size=(int)(Math.random()*10)+25;
        int facing=0;
        map[xpos][ypos]=new Room();
        for(int i=0;i<size;i++)
        {
            int dir=(int)(Math.random()*5);
            if(dir==4)
            {
                facing+=1;
            }
            if(dir==3)
            {
                facing-=1;
            }
            if(facing==-1)
            {
                facing=3;
            }
            if(facing==4)
            {
                facing=0;
            }
            if(facing==0)
            {
                ypos--;
            }
            if(facing==1)
            {
                xpos++;
            }
            if(facing==2)
            {
                ypos++;
            }
            if(facing==3)
            {
                xpos--;
            }
            if(map[xpos][ypos]!=null)
            {
                i--;
            }
            else
            {
                map[xpos][ypos]=new Room();
            }
        }
        for(int i=0;i<map.length;i++)
        {
            for(int j=0;j<map.length;j++)
            {
                if(map[i][j]==null)
                {
                    pMap[i][j]="  ";
                }
                else
                {
                    pMap[i][j]=map[i][j].getSize();
                }
            }
        }
    }
    public static String[][] create()
    {
        return null; //implement
    }
}
