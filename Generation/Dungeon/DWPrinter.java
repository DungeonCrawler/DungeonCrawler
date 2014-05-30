package Dungeon;

public class DWPrinter
{
   public static void printer(String[][] map)
   {
       for(String[] s:map)
       {
           for(String st:s)
           {
               System.out.print(st);
           }
           System.out.println();
       }
   }
}
