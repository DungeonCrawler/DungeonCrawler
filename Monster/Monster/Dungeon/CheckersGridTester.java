package Dungeon;


public class CheckersGridTester
{
    public static void main(String[] args)
    {
        CheckersGrid gr=new CheckersGrid(2);
        gr.printMap();
        for(int i=0;i<20;i++)
        {
            System.out.println();
        }
        CheckersGrid gr2=new CheckersGrid(5);
        gr2.printMap();
        for(int i=0;i<20;i++)
        {
            System.out.println();
        }
        CheckersGrid gr3=new CheckersGrid(10);
        gr3.printMap();
        for(int i=0;i<20;i++)
        {
            System.out.println();
        }
    }
}
