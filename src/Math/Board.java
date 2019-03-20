package Math;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    private int x = 1, y, scale = 1;
    private boolean drawWave = false;

    ArrayList<Double> wave = new ArrayList<>();
    ArrayList<Double> polar = new ArrayList<>();
    ArrayList<Double> ft = new ArrayList<>();
    ArrayList<Integer> removed = new ArrayList<>();

    public Board() {
        initBoard();
    }

    private void loadImage() {
    }

    private void initBoard() {
        for (int i = 0; i < 10000000; i++) {
            ft.add(0.0);
        }

        for (int i = 0; i < 10000000; i++) {
            wave.add(((70 * Math.cos((Math.PI / 180.0) * i / 1))));
            wave.set(i, wave.get(i) + 90 * Math.cos((Math.PI / 180.0) * i / (3)));
            wave.set(i, wave.get(i) + 20 * Math.cos((Math.PI / 180.0) * i / (7)));
            wave.set(i, wave.get(i) + 40 * Math.cos((Math.PI / 180.0) * i / (5)));
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

        if(drawWave)
        for (int i = 0; i < B_WIDTH; i++) {
            g.fillRect(i, 400-(int) Math.round(wave.get(i*10)), 5, 5);
        }
        int f = 0;
        double d = 0;
        polar = new ArrayList<>();
        for (int i = 0; i < 7200; i++) {
            d = wave.get((int) (i * (x / 100.0)));
            f = (int) d;
            if(!drawWave)
            drawPolar(f, i, g);
            polar.add((f * Math.cos(i * 2.0 * Math.PI / 360)));

            //g.fillOval(i, f + 400, 5, 5);
        }
        double avg = 0;
        for (double i : polar) {
            avg += i;
        }
        avg /= polar.size();

        ft.set((int) Math.floor(x), avg);
        for (int i = 0; i < B_WIDTH * scale; i++) {
            g.fillRect(i / scale, B_HEIGHT - ((int) Math.floor(5.0 * ft.get(i)) + 100), 5, (int) Math.floor(5.0 * ft.get(i) + 10));
        }

        g.setColor(Color.RED);
        g.fillRect(x, B_HEIGHT - ((int) Math.floor(5.0 * ft.get(x)) + 100), 1, 100);
                g.setColor(Color.blue);
        Font font = new Font("Serif", Font.PLAIN, 12);
        g.setFont(font);
        g.drawString("" + (x / 100.0), 100, 100);
        g.drawString("" + (avg), 100, 110);
        g.fillOval(5 * (int) Math.floor(avg) + 400, 400, 10, 10);

        for (int i = 0; i < 10 * scale; i++) {
            g.drawRect(i * 100 / scale, 600, 1, 200);
        }
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
                if (scale - 2 > 0) {
                    scale -= 2;
                }
            } else if (key == 65) {//right

                scale += 2;
            } else if (key == 8) {//right

                drawWave=!drawWave;
            } else if (key == 10) {//return
                double max = 0;
                int maxi = 0;
                for (int i = 0; i < ft.size(); i++) {
                    if (ft.get(i) > max) {
                        max = ft.get(i);
                        maxi = i;
                    }
                }
                boolean contained = false;
                for (int i : removed) {
                    if (i == Math.round(maxi / 100)) {
                        contained = true;
                    }
                }
                if (!contained) {
                    for (int i = 0; i < 1000000; i++) {
                        removed.add((int) Math.round(maxi / 100.0));
                        wave.set(i, wave.get(i) - Math.floor(max * 2) * Math.cos((Math.PI / 180.0) * i / Math.round(maxi / 100.0)));
                    }
                }

            }
        }
    }
}
