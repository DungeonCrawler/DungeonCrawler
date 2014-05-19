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
    public boolean canWield()
    {
        if(p.getStrength()>=2&&p.getAgility()>=1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}