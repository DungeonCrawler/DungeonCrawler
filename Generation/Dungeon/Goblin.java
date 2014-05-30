package Dungeon;
public class Goblin extends Monster{
    public Goblin(Tile[][] map,int row,int col){
        super(map,row,col);
        setStrength(5+(int)(Math.random()*3));
        setSize("small");
        setDefense(5+(int)(Math.random()*2));
        setHealth(super.getStrength()/2);
    }

    public void attacked(int damageDealt){
        damageDealt-=super.getDefense();
        super.loseHealth(damageDealt);
        if(super.getHealth()<=0){
            System.out.println("D-E-D.... DEAD");
            for(int i=0;i<super.getLoot().length;i++){
                System.out.println(super.getLoot()[i]);
            }
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
