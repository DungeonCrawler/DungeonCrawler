package ItemList;
import java.util.ArrayList;
import ItemList.PlayerCharacter.Player;
public abstract class Item
{
    Player p;
    public Item(Player pl)
    {
        p=pl;
    }
    public String toString()
    {
        return getName();
    }
    public abstract String getName();
}