package ItemList;
public class DoubleDaggers extends Weapon
{
    public String getName()
    {
        return "Dual Daggers: Str:4, Agi:12";
    }
    public int damage()
    {
        int damage=(int)(Math.random()*3)+6;
        return damage;
    }
     public int reqStr()
    {
        return 4;
    }
    public int reqAgi()
    {
        return 12;
    }
     public int reqInt()
    {
        return 0;
    }
}
