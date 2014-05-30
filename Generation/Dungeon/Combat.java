package Dungeon;

import Dungeon.Monsters.PlayerCharacter.*;
public class Combat{

    public static void combat(Monster monster, Player player)
    {
        boolean first=false;
        /*if(monster.getSpeed()>player.getAgility()){
            first=false;
        }
        else{
            first=true;
        }*/
        int player_damage=player.getDamage();
        int monster_damage=monster.dealDamage();
        if(first){
            monster.attacked(player_damage);
            if(monster.getHealth()>0){
                player.takeDamage(monster_damage);
            }
        }
        else{
          player.takeDamage(monster_damage);
          if(player.getHealth()>0){
              monster.attacked(player_damage);
            }
            else{
                player.gameOver();
            }
        }
    }
} 
