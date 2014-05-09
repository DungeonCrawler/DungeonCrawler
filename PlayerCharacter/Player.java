import java.util.Scanner;
public class Player
{
    private int hp;
    private int strength;
    private int agility;
    private int smarts;
    public Player()
    {
    }
    public void getStats(String s)
    {
        s=s.toLowerCase();
        strength=(int)(Math.random()*5)+5;
        agility=(int)(Math.random()*5)+3;
        smarts=(int)(Math.random()*5)+2;
        if(s.equals("m"))
        {
            smarts+=5;
            strength-=2;
            System.out.println("Class: Mage");
        }
        if(s.equals("w"))
        {
            strength+=5;
            smarts-=2;
            System.out.println("Class: Warrior");
        }
        if(s.equals("r"))
        {
            agility+=3;
            strength+=2;
            System.out.println("Class: Rogue");
        }
        hp=(strength/2)+2;
        System.out.println("Hitpoints: " + hp);
        System.out.println("Strength: " + strength);
        System.out.println("Agility: " + agility);
        System.out.println("Intelligence: " + smarts);
    }
    public boolean canSee(String[][] see)
    {
        //for(int i=0; i<see.length
        return true;
    }
}