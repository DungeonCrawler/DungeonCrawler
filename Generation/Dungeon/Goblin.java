package Dungeon;
public class Goblin extends Monster{
    private Player player;
    public Goblin(Tile[][] map,int row,int col,Player p){
        super(map,row,col);
        setStrength((int)(Math.random()*3)+2);
        setSize("small");
        setDefense(3+(int)(Math.random()*2));
        setHealth(super.getStrength()/2);
        player=p;
    }

    public void attacked(int damageDealt){
        damageDealt-=super.getDefense();
        super.loseHealth(damageDealt);
        if(super.getHealth()<=0){
            System.out.println("D-E-D.... DEAD");
            /*for(int i=0;i<super.getLoot().length;i++){
                System.out.println(super.getLoot()[i]);
            }*/
            this.getGrid()[getX()][getY()].setEntity(null);
            player.kill();
        }
        else{
            System.out.println("Squeeeeeeeeaaaak!!!!!!!!!");
        }
    }
    public String toString()
    {
        return "g";
    }
    public int dealDamage(){
        return super.getStrength()/2+(int)(Math.random()*3);
    }
}
