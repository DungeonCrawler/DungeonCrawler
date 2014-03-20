public class dungentester
{
   public static void main(String[] args){
		//initial stuff used in making the map
		int x = 80; int y = 25; int dungeon_objects = 0;
 
		//convert a string to a int, if there's more then one arg
		if (args.length >= 1)
			dungeon_objects = Integer.parseInt(args[0]);
		if (args.length >= 2)
			x = Integer.parseInt(args[1]);
 
		if (args.length >= 3)
			y = Integer.parseInt(args[2]);
		//create a new class of "dungen", so we can use all the goodies within it
		dungen generator = new dungen();
 
		//then we create a new dungeon map
		if (generator.createDungeon(x, y, dungeon_objects)){
			//always good to be able to see the results..
			generator.showDungeon();
		}
	}
}
