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
    public boolean canWield()
    {
        if(p.getStrength()>=4&&p.getAgility()>=4)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
