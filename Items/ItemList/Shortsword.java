package ItemList;
public class Shortsword extends Weapon
{
    public String getName()
    {
        return "Shortsword: Str:4, Agi:4";
    }
    public int damage()
    {
        int damage=(int)(Math.random()*3)+2;
        return damage;
    }
    public int reqStr()
    {
        return 4;
    }
    public int reqAgi()
    {
        return 4;
    }
    public int reqInt()
    {
        return 0;
    }
}
