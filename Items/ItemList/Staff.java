package ItemList;
public class Staff extends Weapon
{
    public String getName()
    {
        return "Staff: Str:2, Agi:1";
    }
    public int damage()
    {
        int damage=(int)(Math.random()*2)+1;
        return damage;
    }
    public int reqStr()
    {
        return 2;
    }
    public int reqAgi()
    {
        return 1;
    }
     public int reqInt()
    {
        return 0;
    }
}