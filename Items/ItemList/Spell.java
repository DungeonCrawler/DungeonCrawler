package ItemList;
public abstract class Spell extends Item
{
    public abstract String element();
    public abstract int damage();
    public String type()
    {
        return "spell";
    }
}