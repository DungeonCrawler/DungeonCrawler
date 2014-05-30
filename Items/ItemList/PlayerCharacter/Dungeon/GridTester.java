package ItemList.PlayerCharacter.Dungeon;

public class GridTester
{
    public static void main(String[] args)
    {
        Grid gr=new Grid(2);
        gr.printMap();
        for(int i=0;i<20;i++)
        {
            System.out.println();
        }
        Grid gr2=new Grid(5);
        gr2.printMap();
        for(int i=0;i<20;i++)
        {
            System.out.println();
        }
        Grid gr3=new Grid(10);
        gr3.printMap();
        for(int i=0;i<20;i++)
        {
            System.out.println();
        }
    }
}
