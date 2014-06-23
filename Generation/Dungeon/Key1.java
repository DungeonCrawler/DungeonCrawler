package Dungeon;

import java.awt.event.*;

import javax.swing.*;

public class Key1 extends JFrame implements KeyListener{

    JTextField KeyCodeT;//A Text Field that will display the key code.
    private int count, Right_Key, Left_Key, Down_Key, Up_Key;
    Player e1;
    Tile[][] map;
    String sMap;
    public final static int numpad1 = KeyEvent.VK_NUMPAD1;
    public final static int numpad2 = KeyEvent.VK_NUMPAD2;
    public final static int numpad3 = KeyEvent.VK_NUMPAD3;
    public final static int numpad4 = KeyEvent.VK_NUMPAD4;
    public final static int numpad5 = KeyEvent.VK_NUMPAD5;
    public final static int numpad6 = KeyEvent.VK_NUMPAD6;
    public final static int numpad7 = KeyEvent.VK_NUMPAD7;
    public final static int numpad8 = KeyEvent.VK_NUMPAD8;
    public final static int numpad9 = KeyEvent.VK_NUMPAD9;
    public Key1(Player e, Tile[][] grid){
        e1=e;
        map=grid;
        sMap=GenerationFINAL.returnArray(map,e1);
        KeyCodeT = new JTextField(sMap);
        KeyCodeT.addKeyListener(this);//Listens for key inputs in the text field

        KeyCodeT.setEditable(false);//disallow user input into the Text field.

        add(KeyCodeT);//add the text field to the screen

        setSize(300,300);//set the screen ssize

        setVisible(true);//show the window on screen.    

        count=0;

    }
    public void setPlayer(Player p)
    {
        e1=p;
    }
    public Player getPlayer()
    {
        return e1;
    }
    //Called when the key is pressed down.

    public void keyPressed(KeyEvent e){

        //System.out.println("Key Pressed!!!");
        map=e1.getGrid();
        if(e.getKeyCode()==27) {//check if the Keycode is 27 which is esc
            JOptionPane.showMessageDialog(null,"Good  Bye");//display a good bye messege
            System.exit(0);//exit
        }
        /*if(count<4){
        if(count==0){
        Left_Key=e.getKeyCode();
        count++;
        return;
        }
        if(count==1){
        Right_Key=e.getKeyCode();
        count++;
        return;
        }
        if(count==2){
        Down_Key=e.getKeyCode();
        count++;
        return;
        }
        if(count==3){
        Up_Key=e.getKeyCode();
        count++;
        return;
        }
        }
        else{*/
        if(e.getKeyCode()==numpad4){
            //set direction left
            //System.out.println("LEFT");
            //entity.move(3);

            move(3);
        }
        if(e.getKeyCode()==numpad6){
            //set direction right
            //System.out.println("RIGHT");
            //entity.move(1);

            move(1);
        }
        if(e.getKeyCode()==numpad2){
            //set direction down
            //System.out.println("DOWN");
            //entity.move(2);

            move(2);
        }
        if(e.getKeyCode()==numpad8){
            //set direction up
            //System.out.println("UP");
            //entity.move(0);

            move(0);
        }
        if(e.getKeyCode()==numpad1){
            //set direction left
            
            move(6);
        }
        if(e.getKeyCode()==numpad3){
            //set direction right
            
            move(5);
        }
        if(e.getKeyCode()==numpad7){
            //set direction down
            
            move(7);
        }
        if(e.getKeyCode()==numpad9){
            //set direction up
            
            move(4);
        }

    }

    //Called when the key is released  
    public void keyReleased(KeyEvent e){
        //System.out.println("Key Released!!!");
        KeyCodeT.setText("Key Code:" + e.getKeyCode());//displays the key code in the text box
    }
    //Called when a key is typed
    public void keyTyped(KeyEvent e){
    }

    public void move(int key)
    {
        e1.move(key);
        map=e1.getGrid();
        //for(int count=0;count<10;count++)
        //    System.out.println();
        for(int i=0;i<map.length;i++)
            {
                for(int j=0;j<map.length;j++)
                {
                    System.out.print(map[i][j].toString());
                }
                System.out.println();
            }
        System.out.println("HP:" +e1.getHealth()+" Mobs: "+e1.getKills()+"/"+e1.getMobs());
        
        
    }
    
    //public static void main(String[] args){
    //    Key1 key = new Key1();
    //}

}
