package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.example.Tabuleiro.coresTabuleiro;

public class JogoPegaPreaGUI extends JFrame {

    private final JogoPegaPrea jogo; // Representa o estado do jogo.
    private final JButton[][] botoes; // Matriz de botões para exibir o tabuleiro.
    private int[] pecaSelecionada; // Guarda a peça atualmente selecionada.
    private final JLabel jogadorAtualLabel; // Barra para exibir quem deve jogar.

    private final String imagemEstudante1 = "src/main/resources/images/estudante1.jpeg";
    private final String imagemEstudante2 = "src/main/resources/images/estudante2.jpeg";
    private final String imagemEstudante3 = "src/main/resources/images/estudante3.png";
    private final String imagemPrea = "src/main/resources/images/prea.jpeg";

    private final Tabuleiro tabuleiro; // Representa o tabuleiro.

    public JogoPegaPreaGUI() {
        super("Tabuleiro 3x5");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Criação do menu
        JMenuBar menuBar = criarMenu();
        setJMenuBar(menuBar);

        // Inicialização do tabuleiro e jogo
        tabuleiro = new Tabuleiro();
        jogo = new JogoPegaPrea(tabuleiro);

        // Configuração do status do jogador
        jogadorAtualLabel = new JLabel("Jogador Atual: Estudante", SwingConstants.CENTER);
        jogadorAtualLabel.setFont(new Font("Arial", Font.BOLD, 18));
        jogadorAtualLabel.setOpaque(true);
        jogadorAtualLabel.setBackground(Color.BLUE);
        jogadorAtualLabel.setForeground(Color.WHITE);
        add(jogadorAtualLabel, BorderLayout.NORTH);

        // Configuração do painel do tabuleiro
        botoes = new JButton[3][5];
        JPanel panelTabuleiro = new JPanel(new GridLayout(3, 5));
        add(panelTabuleiro, BorderLayout.CENTER);

        inicializarTabuleiroVisual(panelTabuleiro);
        atualizarTabuleiroVisual();

        setVisible(true);
    }

    public void colorirPosicao(int x, int y, Color cor) {
        coresTabuleiro[x][y] = cor; // Atualiza a matriz de cores
    }

    private JMenuBar criarMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuJogo = new JMenu("Jogo");
        JMenuItem reiniciar = new JMenuItem("Reiniciar");
        JMenuItem sair = new JMenuItem("Sair");

        reiniciar.addActionListener(e -> {
            dispose();
            new JogoPegaPreaGUI();
        });

        sair.addActionListener(e -> System.exit(0));

        menuJogo.add(reiniciar);
        menuJogo.add(sair);

        JMenu menuAutoria = new JMenu("Autoria");
        JMenuItem verNomes = new JMenuItem("Ver Nomes");

        verNomes.addActionListener(e -> JOptionPane.showMessageDialog(
                this, "Autores: Ana Carolina Poltronieri e Camilla Borchhardt"));

        menuAutoria.add(verNomes);

        menuBar.add(menuJogo);
        menuBar.add(menuAutoria);

        return menuBar;
    }

    private void inicializarTabuleiroVisual(JPanel panelTabuleiro) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                JButton button = new JButton();
                final int row = i;
                final int col = j;

                // Configuração das peças
                button.setBackground(tabuleiro.getCoresTabuleiro()[i][j]);
                if (tabuleiro.getCoresTabuleiro()[i][j].equals(Color.BLACK)) {
                    button.setEnabled(false);
                } else {
                    button.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (pecaSelecionada == null) {
                                pecaSelecionada = new int[]{row, col};
                                boolean imovel = !mostrarCasasDisponiveis(row, col);
                                System.out.println("Está imovel? " + imovel);
                                System.out.println("mouseClicked row:" + row + "/" + "col:" + col);
                                if (imovel) {
                                    // a peça não pode se mover! Vamos verificar se ainda há peças que podem!                                    
                                    System.out.println("A peça não pode se mover - row:" + row + "/" + "col:" + col);
                                    try {
                                        if (jogo.verificaEmpate()) {
                                            String mensagem = "Empate!";
                                            JOptionPane.showMessageDialog(null, mensagem, "Jogada Inválida", JOptionPane.WARNING_MESSAGE);
                                        }
                                    } catch (JogadaInvalidaException ex) {
                                        Logger.getLogger(JogoPegaPreaGUI.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            } else {

                                int xOrigem = pecaSelecionada[0];
                                int yOrigem = pecaSelecionada[1];

                                System.out.println("Peca selecionada: " + xOrigem + "/" + yOrigem
                                        + "mouseClicked row:" + row + "/" + "col:" + col
                                );

                                try {
                                    // Tenta mover a peça
                                    jogo.moverPeca(xOrigem, yOrigem, row, col);
                                    alternarJogador();
                                } catch (JogadaInvalidaException ex) {
                                    // Apenas registra o erro no log; a mensagem já é exibida no método `moverPeca`
                                    Logger.getLogger(JogoPegaPreaGUI.class.getName()).log(Level.WARNING, "Erro ao mover peça", ex);
                                } finally {
                                    // Sempre limpa a seleção e atualiza a interface
                                    pecaSelecionada = null;
                                    atualizarTabuleiroVisual();
                                }
                            }
                        }
                    });
                }
                botoes[i][j] = button;
                panelTabuleiro.add(button);
            }
        }
    }

    private boolean mostrarCasasDisponiveis(int row, int col) {
        System.out.println("mostrarCasasDisponiveis()");

        // Obtém a peça na posição atual
        Peca peca = tabuleiro.getPeca(row, col);

        // Confere se há uma peça na posição clicada
        if (peca == null) {
            System.out.println("Não há uma peça nesta posição");
            return false;
        }

        System.out.println("Iterando pelas direções possíveis ao redor da peça:");

        // Itera pelas direções possíveis ao redor da peça
        boolean temParaOndeIr = false;
        for (int destRow = -1; destRow <= 1; destRow++) {
            for (int destCol = -1; destCol <= 1; destCol++) {
                int newRow = row + destRow;
                int newCol = col + destCol;

                // Ignora a posição original da peça
                if (newRow == row && newCol == col) {
                    System.out.println("Posição original ignorada: [" + newRow + "][" + newCol + "]");
                    continue;
                }

                // Verifica se a nova posição está dentro dos limites do tabuleiro
                if (tabuleiro.isDentroDosLimites(newRow, newCol)) {
                    // Verifica se a nova posição é ocupada por outra peça
                    if (tabuleiro.getPeca(newRow, newCol) != null) {
                        System.out.println("Posição ocupada por outra peça: [" + newRow + "][" + newCol + "]");
                        continue; // Ignora posições ocupadas
                    }

                    // Verifica se a nova posição é uma casa preta
                    if (tabuleiro.getCorCasa(newRow, newCol).equals(Color.BLACK)) {
                        System.out.println("Posição preta ignorada: [" + newRow + "][" + newCol + "]");
                        continue; // Ignora casas pretas
                    }

                    // Verifica se o movimento é válido para a peça atual
                    if (peca.isMovimentoValido(newRow, newCol)) {
                        // Pinta o botão correspondente como disponível
                        botoes[newRow][newCol].setBackground(Color.GREEN);
                        temParaOndeIr = true;
                    } else {
                        System.out.println("Movimento inválido para a peça em [" + newRow + "][" + newCol + "]");
                    }
                } else {
                    System.out.println("Fora do tabuleiro! [" + newRow + "][" + newCol + "]");
                }
            }

        }
        return temParaOndeIr;
    }

    private void atualizarTabuleiroVisual() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                JButton button = botoes[i][j];
                Peca peca = tabuleiro.getPeca(i, j);

                button.setIcon(null);

                // Configura as peças pretas
                if ((i == 0 && j == 0) || (i == 0 && j == 4) || (i == 2 && j == 0) || (i == 2 && j == 4)) {
                    button.setBackground(Color.BLACK);
                    button.setEnabled(false); // Impede interação
                    continue; // Não altera nada além disso
                }

                // Configura as peças cinzas
                if ((i == 0 && j == 2) || (i == 1 && (j == 1 || j == 3)) || (i == 2 && j == 2)) {
                    button.setBackground(Color.GRAY);
                } else {
                    button.setBackground(Color.WHITE);
                }

                // Atualiza o ícone das peças
                if (peca instanceof Prea) {
                    button.setIcon(carregarImagem(imagemPrea));
                } else if (peca instanceof Estudante) {
                    Estudante estudante = (Estudante) peca;
                    switch (estudante.getId()) {
                        case "Estudante1":
                            button.setIcon(carregarImagem(imagemEstudante1));
                            break;
                        case "Estudante2":
                            button.setIcon(carregarImagem(imagemEstudante2));
                            break;
                        case "Estudante3":
                            button.setIcon(carregarImagem(imagemEstudante3));
                            break;
                    }
                }
            }
        }
    }

    private void alternarJogador() {
        if (jogo.isVezDoEstudante()) {
            jogadorAtualLabel.setText("Jogador Atual: Estudante");
            jogadorAtualLabel.setBackground(Color.BLUE);
        } else {
            jogadorAtualLabel.setText("Jogador Atual: Preá");
            jogadorAtualLabel.setBackground(Color.GREEN);
        }
    }

    private ImageIcon carregarImagem(String caminho) {
        ImageIcon imageIcon = new ImageIcon(caminho);
        Image scaledImage = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

}
