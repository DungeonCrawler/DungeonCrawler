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
    public boolean canWield()
    {
        if(p.getStrength()>=4&&p.getAgility()>=12)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}