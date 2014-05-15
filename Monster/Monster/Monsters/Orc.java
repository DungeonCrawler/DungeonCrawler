package Monsters;
public class Orc extends Monster{
    public Orc(){
        super.setHealth(20+(int)(Math.random()*10));
        super.setStrength(10+(int)(Math.random()*5));
        super.setDefense(5+(int)(Math.random()*5));
        super.setSize("medium");
    }

    public void attacked(int damageDealt){
        damageDealt-=super.getDefense();
        super.loseHealth(damageDealt);
        if(super.getHealth()<=0){
            System.out.println("D-E-D.... DEAD");
        }
        else{
            System.out.println("Urghhh!!");
        }
    }
    
    public int dealDamage(){
        return super.getStrength()/2+(int)(Math.random()*5);
    }
}