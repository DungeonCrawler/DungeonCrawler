package ItemList;
public class Longsword extends Weapon
{
    public String getName()
    {
        return "Longsword: Str:7 Agi:5";
    }
    public int damage()
    {
        int damage=(int)(Math.random()*3)+3;
        return damage;
    }
     public int reqStr()
    {
        return 7;
    }
    public int reqAgi()
    {
        return 5;
    }
     public int reqInt()
    {
        return 0;
    }
}