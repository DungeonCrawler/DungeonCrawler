package Monsters;
public class GiantRat extends Monster{
    public GiantRat(){
        String[] loot = new String[3];
        loot[0]="5gp";
        loot[1]="dagger";
        loot[2]="AIDS";
        super.setStrength(1+(int)(Math.random()*3));
        super.setSize("small");
        super.setDefense(1+(int)(Math.random()*2));
        super.setHealth(super.getStrength()/2);
        super.setLoot(loot);
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

    public int dealDamage(){
        return super.getStrength()/2+(int)(Math.random()*3);
    }
}
