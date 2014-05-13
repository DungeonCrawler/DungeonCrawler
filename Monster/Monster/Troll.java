public class Troll extends Monster{
    public Troll(){
        super.setHealth(50+(int)(Math.random()*10));
        super.setStrength(15+(int)(Math.random()*5));
        super.setDefense(10+(int)(Math.random()*10));
        super.setSize("large");
    }

    public void attacked(int damageDealt){
        damageDealt-=super.getDefense();
        super.loseHealth(damageDealt);
        if(super.getHealth()<=0){
            System.out.println("D-E-D.... DEAD");
        }
        else{
            System.out.println("RUAAAAAAAAAAAGGGGGGHHHHH!!!!!!!!!!!! ):<");
        }
    }
    
    public int dealDamage(){
        return super.getStrength()/2+(int)(Math.random()*10);
    }
}
