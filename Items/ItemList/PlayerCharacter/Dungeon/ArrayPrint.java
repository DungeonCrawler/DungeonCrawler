package ItemList.PlayerCharacter.Dungeon;

import java.util.Scanner;
public class ArrayPrint
{
    public static void main(String[] args)
    {    
        Scanner input=new Scanner(System.in);
        System.out.print("What size do you want? ");
        int size = input.nextInt();
        ArrayGen a1=new ArrayGen(size);
        a1.makeFloor();
        System.out.println(a1);
        a1.reduceFloor();
        System.out.println(a1);
        
        MapGen map=new MapGen(a1.getMap());
        map.rooms();
        System.out.println(map);
    }
}
