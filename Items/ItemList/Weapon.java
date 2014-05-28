package ItemList;
public abstract class Weapon extends Item
{
    public abstract int damage();
    public abstract int reqStr();
    public abstract int reqAgi();
    public String type()
    {
        return "weapon";
    }
}