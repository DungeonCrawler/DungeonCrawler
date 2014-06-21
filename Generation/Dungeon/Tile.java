package Dungeon;

public abstract class Tile
{
    private Entity occupant;
    public Tile()
    {   
    }
    public abstract boolean canMove();
    public abstract boolean canSee();
    public void interact(Key1 k)
    {
        
    }
    public void setEntity(Entity thing)
    {
        occupant=thing;
    }
    public Entity getEntity()
    {
        return occupant;
    }
    public String entityString()
    {
        return occupant.toString();
    }
}
