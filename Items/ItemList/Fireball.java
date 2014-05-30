package ItemList;

public class Fireball extends Spell
{
    public String getName()
    {
        return "Fireball: Int:5";
    }
    public int damage()
    {
        int damage=(int)(Math.random()*3)+2;
        return damage;
    }
    public int reqStr()
    {
        return 0;
    }
    public int reqAgi()
    {
        return 0;
    }
    public int reqInt()
    {
        return 5;
    }
    public String element()
    {
        return "fire";
    }
}