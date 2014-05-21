package Dungeon;

public class RoomTester
{
    public static void main(String[] args)
    {
        
        for(int i=0;i<13;i++)
        {
            Room r=new Room(i);
            for(String[] s:r.getRoom())
            {
                for(String st:s)
                {
                    System.out.print(st);
                }
                System.out.println();
                
                
            }
            System.out.println();
            System.out.println();
        }
    }
}