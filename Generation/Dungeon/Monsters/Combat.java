package Dungeon.Monsters;

import Dungeon.Monsters.PlayerCharacter.Player;
public class Combat{

    public Combat(Monster monster, Player player)
    {
        boolean first;
        if(monster.getSpeed()>player.getAgility()){
            first=false;
        }
        else{
            first=true;
        }
        //int player_damage=player.getDamage();
        int monster_damage=monster.dealDamage();
        if(first){
            //monster.attacked(player_damage);
            if(monster.getHealth()>0){
                //player.attacked(monster_damage);
            }
        }
        else{
          //player.attacked(monster_damage);
          if(player.getHP()>0){
              //monster.attacked(player_damage);
            }
            else{
                //player.gameOver();
            }
        }
    }
} 
