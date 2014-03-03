public class DrunkenWalk
{
    public static void main(String[] args)
    {
        int[][] map=new int[50][50];
        int xpos=25;
        int ypos=25;
        int size=(int)(Math.random()*10)+5;
        int facing=0;
        map[xpos][ypos]=(int)(Math.random()*9)+1;
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
            map[xpos][ypos]=(int)(Math.random()*9)+1;
        }
        
        for(int i=0;i<map.length;i++)
        {
            for(int j=0;j<map.length;j++)
            {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }
}
