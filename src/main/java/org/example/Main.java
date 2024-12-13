package org.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Criar JFrame para a introdução
        JFrame introFrame = new JFrame("Introdução");
        IntroGUI introGUI = new IntroGUI(introFrame);

        introFrame.add(introGUI);
        introFrame.setSize(800, 600);
        introFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        introFrame.setVisible(true);

    }
}