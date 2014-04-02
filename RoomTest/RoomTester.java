public class RoomTester
{
    public static void main(String[] args)
    {
        for(int i=0;i<9;i++)
        {
            for(String[] s:RoomDraw.roomDraw(i))
            {
                for(String st:s)
                {
                    System.out.print(st);
                }
                System.out.println();
            }
        }
    }
}
