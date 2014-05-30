package ItemList;
public class Greatsword extends Weapon
{
    public String getName()
    {
        return "Greatsword: Str:14 Agi:5";
    }   
    public int damage()
    {
        int damage=(int)(Math.random()*3)+6;
        return damage;
    }
     public int reqStr()
    {
        return 14;
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