package ItemList;
import java.util.ArrayList;
public abstract class Item
{
    public String toString()
    {
        return getName();
    }
    public abstract String getName();
}