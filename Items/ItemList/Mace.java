package ItemList;
public class Mace extends Weapon
{
    public String getName()
    {
        return "Mace: Str:9 Agi:3";
    }
    public int damage()
    {
        int damage=(int)(Math.random()*3)+4;
        return damage;
    }
    public int reqStr()
    {
        return 9;
    }
    public int reqAgi()
    {
        return 3;
    }
     public int reqInt()
    {
        return 0;
    }
}
