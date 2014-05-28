package ItemList;
public abstract class Armor extends Item
{
    public abstract int defense();
    public abstract int fireResist();
    public abstract int coldResist();
    public abstract int necroResist();
    public abstract int holyResist();
    public String type()
    {
        return "armor";
    }
}