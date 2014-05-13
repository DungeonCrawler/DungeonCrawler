public class Zombie extends Monster{
    public Zombie(){
        super.setStrength(10+(int)(Math.random()*5));
        super.setDefense(5+(int)(Math.random()*5));
        super.setSize("medium");
        super.setHealth(super.getStrength()/2);
    }

    public void attacked(int damageDealt){
        damageDealt-=super.getDefense();
        super.loseHealth(damageDealt);
        if(super.getHealth()<=0){
            System.out.println("D-E-D.... DEAD");
        }
        else{
            System.out.println("EEHHHHHHHhhhhhhhhh T-T");
        }
    }

    public int dealDamage(){
        return super.getStrength()/2+(int)(Math.random()*5);
    }
}
