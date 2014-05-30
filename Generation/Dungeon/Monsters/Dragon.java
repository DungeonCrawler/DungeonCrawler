package Dungeon.Monsters;
public class Dragon extends Monster{
    public Dragon(){
        super.setStrength(1000+(int)(Math.random()*10)+(int)(Math.random()*100)+(int)(Math.random()*1000));
        super.setSize("superMassive");
        super.setDefense(500+(int)(Math.random()*2));
        super.setHealth(600+(int)(Math.random()*10)+(int)(Math.random()*100));
    }

    public void attacked(int damageDealt){
        damageDealt-=super.getDefense();
        super.loseHealth(damageDealt);
        if(super.getHealth()<=0){
            System.out.println("D-E-D.... DEAD");
        }
        else{
            System.out.println("HAHAHHAHAAHAHAHAHAHAHAHAHAHAHAHHHA LOL");
        }
    }

    public int dealDamage(){
        return super.getStrength()/2+(int)(Math.random()*100)+(int)(Math.random()*10);
    }
}
