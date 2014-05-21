package Dungeon;



public class TileTester
{
    public static void main(String[] args)
    {
        Tile[][] map=new Tile[7][7];
        for(int i=0;i<map.length;i++)
        {
            for(int j=0;j<map.length;j++)
            {
                if(i==0||j==0||j==map.length-1||i==map.length-1)
                {
                    map[i][j]=new Wall();
                }
                else
                {
                    map[i][j]=new Floor();
                }
            }
        }
        int row=(int)(Math.random()*4)+1;
        int col=(int)(Math.random()*4)+1;
        map[row][col].setEntity(new Entity(map));
        for(int i=0;i<map.length;i++)
        {
            for(int j=0;j<map.length;j++)
            {
               System.out.print(map[i][j].toString());
            }
            System.out.println();
        }
    }
}
