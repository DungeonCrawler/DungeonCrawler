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
    public boolean canWield()
    {
        if(p.getStrength()>=14&&p.getAgility()>=5)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}