package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IntroGUI extends JPanel {
    private int x = 0; // Posição horizontal inicial
    private int y = 100; // Posição vertical
    private int step = 5; // Velocidade de movimento
    private Image image; // Imagem para a animação
    private JFrame frame; // Referência ao JFrame
    private final AudioPlayer audioPlayer; // Player de música

    public IntroGUI(JFrame frame) {
        this.frame = frame;
        this.audioPlayer = new AudioPlayer(); // Inicializa o player de áudio

        // Carregar a imagem
        image = new ImageIcon("C:/faculdade/pega-prea/src/main/resources/images/capa.jpeg").getImage();

        // Reproduzir a música de introdução
        iniciarMusica();

        // Inicializa a interface gráfica
        inicializarInterface();

        // Timer para movimentar a imagem
        Timer timer = new Timer(80, e -> {
            x += step;

            // Verifica as bordas para mudar a direção
            if (x >= getWidth() - image.getWidth(null) || x <= 0) {
                step = -step;
            }

            // Atualiza o desenho
            repaint();
        });
        timer.start();

        // Timer para fechar o JFrame após 5 segundos
        Timer closeTimer = new Timer(5000, e -> {
            pararMusica(); // Para a música quando a introdução terminar
            frame.dispose(); // Fecha o JFrame de introdução
            abrirJogo(); // Abre o jogo principal
        });
        closeTimer.setRepeats(false); // Executa apenas uma vez
        closeTimer.start();
    }

    private void inicializarInterface() {
        setLayout(new BorderLayout());

        JButton startButton = new JButton("Iniciar Jogo");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarJogo();
            }
        });
        add(startButton, BorderLayout.SOUTH);
    }

    private void iniciarMusica() {
        try {
            audioPlayer.carregarMusica("C:\\faculdade\\pega-prea\\src\\main\\resources\\video-game-boss-fiight-259885.wav");
            audioPlayer.tocarMusica();
        } catch (Exception e) {
            System.err.println("Erro ao reproduzir música: " + e.getMessage());
        }
    }

    private void pararMusica() {
        audioPlayer.pararMusica();
    }

    private void iniciarJogo() {
        pararMusica(); // Para a música antes de iniciar o jogo
        frame.dispose(); // Fecha o frame de introdução
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Fundo
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Desenha a imagem na posição atual
        g.drawImage(image, x, y, null);
    }

    private void abrirJogo() {
        SwingUtilities.invokeLater(JogoPegaPreaGUI::new); // Abre o JFrame do jogo
    }
}
