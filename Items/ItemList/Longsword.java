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
    public boolean canWield()
    {
        if(p.getStrength()>=7&&p.getAgility()>=5)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}