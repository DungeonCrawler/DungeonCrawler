public class Titan extends Monster{
    public Titan(){
        super.setHealth(500+(int)(Math.random()*10)+(int)(Math.random()*10));
        super.setStrength(150+(int)(Math.random()*100)+(int)(Math.random()*10));
        super.setDefense(200+(int)(Math.random()*100)+(int)(Math.random()*10));
        super.setSize("superMassive");
    }

    public void attacked(int damageDealt){
        damageDealt-=super.getDefense();
        super.loseHealth(damageDealt);
        if(super.getHealth()<=0){
            System.out.println("D-E-D.... DEAD");
        }
        else{
            System.out.println("MWAHAHAHAHAHAHAHAAAA PUNY SCUM");
        }
    }

    public int dealDamage(){
        return super.getStrength()/2+(int)(Math.random()*5);
    }
}
