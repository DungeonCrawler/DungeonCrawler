package Dungeon;

public class Player extends Entity
{
    private int health;
    private int damage;
    public Player(Tile[][] grid, int row, int col)
    {
        super(grid, row, col);
        damage=(int)(Math.random()*5)+5;
        health=10+(int)(Math.random()*5);
    }
    public String toString()
    {
        return "@";
    }
    public int getDamage()
    {
        return damage;
    }
    public int getHealth()
    {
        return health; 
    }
    public void takeDamage(int dmg)
    {
        health-=dmg;
    }
    public void gameOver()
    {
        System.out.print("You lose.");
        System.exit(0);
    }
    public void move(int dir)
    {
        Tile[][] gr=getGrid();
        if(dir==0)//n
        {
            if(canMove(0))
            {
                gr[getX()][getY()].setEntity(null);
                gr[getX()-1][getY()].setEntity(this);
                setX(getX()-1);
            }
            else if(gr[getX()-1][getY()].getEntity() instanceof Monster)
            {
                Monster m=(Monster)gr[getX()-1][getY()].getEntity();
                Combat.combat(m,this);
            }
            else
            {
                gr[getX()-1][getY()].interact();
            }
        }
        if(dir==1)//e
        {
            if(canMove(1))
            {
                getGrid()[getX()][getY()].setEntity(null);
                getGrid()[getX()][getY()+1].setEntity(this);
                setY(getY()+1);
            }
            else if(gr[getX()][getY()+1].getEntity() instanceof Monster)
            {
                Monster m=(Monster)gr[getX()][getY()+1].getEntity();
                Combat.combat(m,this);
            }
            else
            {
                gr[getX()][getY()+1].interact();
            }
        }
        if(dir==2)//s
        {
           if(canMove(2))
           {
               getGrid()[getX()][getY()].setEntity(null);
               getGrid()[getX()+1][getY()].setEntity(this);
               setX(getX()+1);
           }
           else if(gr[getX()+1][getY()].getEntity() instanceof Monster)
            {
                Monster m=(Monster)gr[getX()+1][getY()].getEntity();
                Combat.combat(m,this);
            }
            else
            {
                gr[getX()+1][getY()].interact();
            }
        }
        if(dir==3)//w
        {
            if(canMove(3))
            {
                getGrid()[getX()][getY()].setEntity(null);
                getGrid()[getX()][getY()-1].setEntity(this);
                setY(getY()-1);
            }
            else if(gr[getX()][getY()-1].getEntity() instanceof Monster)
            {
                Monster m=(Monster)gr[getX()][getY()-1].getEntity();
                Combat.combat(m,this);
            }
            else
            {
                gr[getX()][getY()-1].interact();
            }
        }
        if(dir==4)//ne
        {
            if(canMove(4))
            {
                getGrid()[getX()][getY()].setEntity(null);
                getGrid()[getX()-1][getY()+1].setEntity(this);
                setY(getY()+1);
                setX(getX()-1);
            }
            else if(gr[getX()-1][getY()+1].getEntity() instanceof Monster)
            {
                Monster m=(Monster)gr[getX()-1][getY()+1].getEntity();
                Combat.combat(m,this);
            }
            else
            {
                gr[getX()-1][getY()+1].interact();
            }
        }
        if(dir==5)//se
        {
            if(canMove(5))
            {
                getGrid()[getX()][getY()].setEntity(null);
                getGrid()[getX()+1][getY()+1].setEntity(this);
                setY(getY()+1);
                setX(getX()+1);
            }
            else if(gr[getX()+1][getY()+1].getEntity() instanceof Monster)
            {
                Monster m=(Monster)gr[getX()+1][getY()+1].getEntity();
                Combat.combat(m,this);
            }
            else
            {
                gr[getX()+1][getY()+1].interact();
            }
        }
        if(dir==6)//sw
        {
            if(canMove(6))
            {
                getGrid()[getX()][getY()].setEntity(null);
                getGrid()[getX()+1][getY()-1].setEntity(this);
                setY(getY()-1);
                setX(getX()+1);
            }
            else if(gr[getX()+1][getY()-1].getEntity() instanceof Monster)
            {
                Monster m=(Monster)gr[getX()+1][getY()-1].getEntity();
                Combat.combat(m,this);
            }
            else
            {
                gr[getX()+1][getY()-1].interact();
            }
        }
        if(dir==7)//nw
        {
            if(canMove(7))
            {
                getGrid()[getX()][getY()].setEntity(null);
                getGrid()[getX()-1][getY()-1].setEntity(this);
                setY(getY()-1);
                setX(getX()-1);
            }
            else if(gr[getX()-1][getY()-1].getEntity() instanceof Monster)
            {
                Monster m=(Monster)gr[getX()-1][getY()-1].getEntity();
                Combat.combat(m,this);
            }
            else
            {
                gr[getX()-1][getY()-1].interact();
            }
        }
    }
}
