package Monsters.PlayerCharacter;

import java.awt.event.*;

import javax.swing.*;

public class Key1 extends JFrame implements KeyListener{

    JTextField KeyCodeT = new JTextField("Key Code:");//A Text Field that will display the key code.

    public Key1(){

        KeyCodeT.addKeyListener(this);//Listens for key inputs in the text field

        KeyCodeT.setEditable(false);//disallow user input into the Text field.

        add(KeyCodeT);//add the text field to the screen

        setSize(300,300);//set the screen ssize

        setVisible(true);//show the window on screen.    

    }

    //Called when the key is pressed down.

    public void keyPressed(KeyEvent e){

        //System.out.println("Key Pressed!!!");


        if(e.getKeyCode()==27) {//check if the Keycode is 27 which is esc

            JOptionPane.showMessageDialog(null,"Good  Bye");//display a good bye messege

            System.exit(0);//exit

        }

        if(e.getKeyCode()==37){
            //set direction left
            System.out.println("LEFT");
            //entity.move(3);
        }
        if(e.getKeyCode()==39){
            //set direction right
            System.out.println("RIGHT");
            //entity.move(1);
        }
        if(e.getKeyCode()==40){
            //set direction down
            System.out.println("DOWN");
            //entity.move(2);
        }
        if(e.getKeyCode()==38){
            //set direction up
            System.out.println("UP");
            //entity.move(0);
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
    public static void main(String[] args){
        Key1 key = new Key1();
    }
     
}
