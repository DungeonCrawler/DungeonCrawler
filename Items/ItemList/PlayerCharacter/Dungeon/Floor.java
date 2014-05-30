package ItemList.PlayerCharacter.Dungeon;

public class Floor extends Tile
{
    public Floor()
    {
        
    }
    public boolean canMove()
    {
        return true;
    }
    public boolean canSee()
    {
        return true;
    }
    public String toString()
    {
        if(getEntity()==null)
        {
            return " ";
        }
        return entityString();
    }
}
