package ItemList.PlayerCharacter;
import java.util.Scanner;
import java.util.ArrayList;
public class Player
{
    private int hp;
    private int strength;
    private int agility;
    private int smarts;
    private int defense;
    private String job;
    public Player(String s)
    {
        job=s.toLowerCase();
    }
    public void getStats()
    {
        strength=(int)(Math.random()*5)+5;
        agility=(int)(Math.random()*5)+3;
        smarts=(int)(Math.random()*5)+2;
        if(job.equals("m"))
        {
            smarts+=5;
            strength-=2;
            defense=4;
            System.out.println("Class: Mage");
        }
        if(job.equals("w"))
        {
            strength+=5;
            smarts-=2;
            defense=6;
            System.out.println("Class: Warrior");
        }
        if(job.equals("r"))
        {
            agility+=3;
            strength+=2;
            defense=5;
            System.out.println("Class: Rogue");
        }
        hp=(strength/2)+2;
        System.out.println("Hitpoints: " + hp);
        System.out.println("Strength: " + strength);
        System.out.println("Agility: " + agility);
        System.out.println("Intelligence: " + smarts);
    }
    public ArrayList<String> startInventory()
    {
        ArrayList<String> inventory=new ArrayList<String>();
        if(job.equals("m"))
        {
            inventory.add("Initiate Robe");
            inventory.add("Staff");
            inventory.add("Spell Tome:Fireball");
        }
        if(job.equals("w"))
        {
            inventory.add("Chain Mail");
            inventory.add("Longsword");
        }
        if(job.equals("r"))
        {
            inventory.add("Leather Armor");
            inventory.add("Shortsword");
            inventory.add("Bow");
            //add ammo
        }
        return inventory;
    }
    public String getJob()
    {
        return job;
    }
    public int getHP()
    {
        return hp;    
    }
    public int getStrength()
    {
        return strength;
    }
    public int getAgility()
    {
        return agility;
    }
    public int getSmarts()
    {
        return smarts;
    }
}