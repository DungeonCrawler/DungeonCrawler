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
    public boolean canWield()
    {
        if(p.getStrength()>=5&&p.getAgility()>=3)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}