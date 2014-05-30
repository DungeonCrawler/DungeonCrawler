package ItemList;
import java.util.ArrayList;
public abstract class Item
{
    public String toString()
    {
        return getName();
    }
    public abstract String getName();
    public abstract String type();
    public abstract int reqStr();
    public abstract int reqAgi();
    public abstract int reqInt();
}