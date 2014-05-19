package Dungeon;

public abstract class Tile
{
    private int type;
    public Tile(int type)
    {
        this.type=type;
    }
    public abstract boolean canMove();
    public abstract boolean canSee();
}
