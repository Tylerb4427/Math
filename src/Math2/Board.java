package Math2;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Math.*;
import java.awt.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class Board extends JPanel {

    private final int B_WIDTH = 800;
    private final int B_HEIGHT = 800;
    private final int INITIAL_DELAY = 250;
    private final int PERIOD_INTERVAL = 1;
    private Timer timer;
    private int x = 1, y,scale=1;

    ArrayList<Double> wave = new ArrayList<>();
    ArrayList<Double> polar = new ArrayList<>();
    ArrayList<Double> ft = new ArrayList<>();
    ArrayList<Double> polar2 = new ArrayList<>();
    ArrayList<Double> gt = new ArrayList<>();
    public Board() {
        initBoard();
    }

    private void loadImage() {
    }

    private void initBoard() {
        for (int i = 0; i < 1000000; i++) {
            ft.add(0.0);
        }
        for (int i = 0; i < 1000000; i++) {
            gt.add(0.0);
        }

        for (int i = 0; i < 1000000; i++) {
            wave.add(((70 * Math.cos((Math.PI / 180.0) * i / 2))));
            wave.set(i, wave.get(i) + 90 * Math.cos((Math.PI / 180.0) * i / (3)));
            wave.set(i, wave.get(i) + 20 * Math.cos((Math.PI / 180.0) * i / (7)));
            wave.set(i, wave.get(i) + 40 * Math.cos((Math.PI / 180.0) * i / (1)));
            wave.set(i, wave.get(i) + 30 * Math.cos((Math.PI / 180.0) * i / (12)));            
            //wave.set(i, wave.get(i) + 50 * Math.cos((Math.PI / 180.0) * i / (16)));
        }
        JTextField textField = new JTextField();

        textField.addKeyListener(new MKeyListener());

        JFrame jframe = new JFrame();

        jframe.add(textField);

        jframe.setSize(400, 350);
        reset();
        jframe.setVisible(true);
        setBackground(Color.white);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

        loadImage();

        timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(),
                INITIAL_DELAY, PERIOD_INTERVAL);

    }

    private void reset() {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawStuff(g);
    }

    private void drawStuff(Graphics g) {
        int f = 0;
        double d = 0;
        polar = new ArrayList<>();
        polar2 = new ArrayList<>();
        for (int i = 0; i < 7200; i++) {
            d = wave.get((int) (i * (x / 100.0)));
            f = (int) d;
            drawPolar(f, i, g);
            polar.add((f * Math.cos(i * 2.0 * Math.PI / 360)));
            polar2.add((f * Math.sin(i * 2.0 * Math.PI / 360)));

            //g.fillOval(i, f + 400, 5, 5);
        }
        double avg = 0;
        double avg2 = 0;
        for (double i : polar) {
            avg += i;
        }
        for (double i : polar2) {
            avg2 += i;
        }
        avg /= polar.size();
        
        avg2 /= polar2.size();
        
        ft.set((int)Math.floor(x),avg);
        gt.set((int)Math.floor(x),avg2);
        for(int i=0;i<B_WIDTH*scale;i++){
            g.fillRect(((int)Math.floor(5.0*ft.get(i)))+400,((int)Math.floor(5.0*gt.get(i))+400),5,5);
        }

        g.setColor(Color.blue);
        g.fillRect(x,B_HEIGHT-((int)Math.floor(5.0*ft.get(x))+100),5,5);
        Font font = new Font("Serif", Font.PLAIN, 12);
        g.setFont(font);
        g.drawString("" + (x / 100.0), 100, 100);
        g.drawString("" + (avg), 100, 110);
        g.fillOval(400, 5 * (int) Math.floor(avg2)+400, 10, 10);
        g.fillOval(5 * (int) Math.floor(avg) + 400, 400, 10, 10);
        for(int i=0;i<10*scale;i++)
            g.drawRect(i*100/scale,600,1,200);
    }

    private void drawPolar(double r, double theta, Graphics g) {
        int xPos = (int) (r * Math.cos(theta * 2.0 * Math.PI / 360));
        int yPos = (int) (r * Math.sin(theta * 2.0 * Math.PI / 360));
        g.fillOval(xPos + 400, yPos + 400, 2, 2);
    }

    private class ScheduleTask extends TimerTask {

        @Override
        public void run() {

            repaint();
        }
    }

    private class MKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent event) {
            int key = event.getKeyCode();

            System.out.println("Key codes: " + key);
            if (key == 32) {//space
                System.out.println(x);
            } else if (key == 38) {//up
                x++;
            } else if (key == 40) {//down
                x--;
            } else if (key == 37) {//left
                x -= 5;
            } else if (key == 39) {//right
                x += 5;
            } else if (key == 68) {//left
                if(scale-2>0)
                scale -= 2;
            } else if (key == 65) {//right

                scale += 2;
            }
        }
    }

}
