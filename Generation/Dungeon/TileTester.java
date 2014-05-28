package Dungeon;
import java.util.Scanner;

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
        Entity e=new Entity(map,row,col);
        map[row][col].setEntity(e);
        for(int i=0;i<map.length;i++)
        {
            for(int j=0;j<map.length;j++)
            {
               System.out.print(map[i][j].toString());
            }
            System.out.println();
        }
        Key1 k=new Key1(e,map);
        /*Scanner s=new Scanner(System.in);
        while(true)
        {
            int next=s.nextInt();
            e.move(next);
            for(int i=0;i<map.length;i++)
            {
                for(int j=0;j<map.length;j++)
                {
                    System.out.print(map[i][j].toString());
                }
                System.out.println();
            }
        }*/
    }
}
