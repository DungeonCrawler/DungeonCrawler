package ItemList.PlayerCharacter;
import java.util.Scanner;
import java.util.ArrayList;
import ItemList.*;
import ItemList.PlayerCharacter.Dungeon.*;
public class Player extends Entity
{
    private int hp;
    private int strength;
    private int agility;
    private int smarts;
    private int defense;
    private String job;
    ArrayList<Item> inventory=new ArrayList<Item>();
    public Player(Tile[][] map, int row, int col, String s)
    {
        super(map, row, col);
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

    public ArrayList<Item> startInventory()
    {
        if(job.equals("m"))
        {
            inventory.add(new InitiateRobe());
            inventory.add(new Staff());
            //inventory.add("Spell Tome:Fireball");
        }
        if(job.equals("w"))
        {
            inventory.add(new Chainmail());
            inventory.add(new Longsword());
        }
        if(job.equals("r"))
        {
            inventory.add(new LeatherArmor());
            inventory.add(new Shortsword());
            //inventory.add("Bow");
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

    public boolean canWield()
    {
        for(int i=0; i<inventory.size(); i++)
        {
            if(inventory.get(i).type()=="weapon")
            {
                if((inventory.get(i).reqStr()<strength)&&(inventory.get(i).reqAgi()<agility))
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            if(inventory.get(i).type()=="spell")
            {
                if(inventory.get(i).reqInt()<smarts)
                {
                    return false;
                }
            }
        }
        return false;
    }
}