package org.example;

import java.awt.Color;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.example.Estudante;
import org.example.JogadaInvalidaException;
import org.example.Peca;
import org.example.Prea;

/**
 * Gerencia o estado e a lógica principal do jogo. Controla os turnos,
 * movimentações e verificações de vitória.
 */
public class JogoPegaPrea {

    private static final Logger logger = Logger.getLogger(JogoPegaPrea.class.getName());
    private final Tabuleiro tabuleiro; // Representa o tabuleiro do jogo.
    private boolean vezDoEstudante; // Indica se é o turno dos estudantes.
    private int estudanteSelecionado; // Controla o estudante que está ativo.
    private final Estudante[] estudantes; // Lista de estudantes no jogo.
    private final Prea prea; // A peça Preá.

    /**
     * Inicializa o estado inicial do jogo com peças posicionadas no tabuleiro.
     *
     * @param tabuleiro O tabuleiro onde o jogo ocorre.
     */
    public JogoPegaPrea(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        vezDoEstudante = true;
        estudanteSelecionado = 0;

        // Criação das peças do jogo
        prea = new Prea(1, 4);
        boolean debug = false;
        if (!debug) {
            estudantes = new Estudante[]{
                new Estudante(1, 0, "Estudante1"),
                new Estudante(0, 1, "Estudante2"),
                new Estudante(2, 1, "Estudante3")};
        } else { // posição empatada
            estudantes = new Estudante[]{
                new Estudante(1, 2, "Estudante1"),
                new Estudante(0, 3, "Estudante2"),
                new Estudante(2, 3, "Estudante3")
            };
        }

        // Posiciona as peças no tabuleiro
        tabuleiro.colocarPeca(prea);
        for (Estudante estudante : estudantes) {
            tabuleiro.colocarPeca(estudante);
        }

        logger.info("Jogo iniciado: Estudantes jogam primeiro.");
    }

    /**
     * Move uma peça de uma posição inicial para uma posição de destino.
     *
     * @param xOrigem Linha de origem.
     * @param yOrigem Coluna de origem.
     * @param xDestino Linha de destino.
     * @param yDestino Coluna de destino.
     */
    public void moverPeca(int xOrigem, int yOrigem, int xDestino, int yDestino) throws JogadaInvalidaException {
        Peca peca = tabuleiro.getPeca(xOrigem, yOrigem);

        if (peca == null) {
            String mensagem = "Nenhuma peça na posição selecionada: (" + xOrigem + ", " + yOrigem + ")";
            logger.warning(mensagem);
            JOptionPane.showMessageDialog(null, mensagem, "Jogada Inválida: nenhuma peça na posição", JOptionPane.WARNING_MESSAGE);
            throw new JogadaInvalidaException(mensagem);
        }

        // Verifica turno e tipo da peça
        if (vezDoEstudante && peca instanceof Estudante) {
            if (!peca.isMovimentoValido(xDestino, yDestino)) {
                String mensagem = "Movimento inválido para o Estudante.";
                logger.warning(mensagem);
                JOptionPane.showMessageDialog(null, mensagem, "Jogada Inválida", JOptionPane.WARNING_MESSAGE);
                throw new JogadaInvalidaException(mensagem);
            }
            boolean moveu = tabuleiro.moverPeca(xOrigem, yOrigem, xDestino, yDestino);
            if (moveu) {
                vezDoEstudante = false; // Alterna turno após movimento válido
                estudanteSelecionado++; // Avança para o próximo Estudante
                if (estudanteSelecionado >= estudantes.length) {
                    estudanteSelecionado = 0; // Reseta para o primeiro Estudante
                }
            } else {
                String mensagem = "Você não pode passar por cima do amigo, ta maluco?";
                exibeMensagemErro(mensagem);
            }
        } else if (!vezDoEstudante && peca instanceof Prea) {
            if (!peca.mover(xDestino, yDestino)) {
                String mensagem = "Movimento inválido para a Preá.";
                logger.warning(mensagem);
                JOptionPane.showMessageDialog(null, mensagem, "Jogada Inválida", JOptionPane.WARNING_MESSAGE);
                throw new JogadaInvalidaException(mensagem);
            }

            boolean moveu = tabuleiro.moverPeca(xOrigem, yOrigem, xDestino, yDestino);
            if (moveu) {
                vezDoEstudante = true; // Alterna turno após movimento válido
            } else {
                String mensagem = "Você não pode passar por cima do amigo, ta maluco?";
                exibeMensagemErro(mensagem);
            }
        } else {
            String mensagem = "Movimento inválido: Não é o turno dessa peça.";
            exibeMensagemErro(mensagem);
        }

        verificarVitoria();
    }

    /**
     * Verifica as condições de vitória. Declara vitória se a Preá estiver
     * cercada ou alcançar a posição de destino.
     */
    private void verificarVitoria() throws JogadaInvalidaException {
        // Verifica se os Estudantes venceram
        if (verificarVitoriaEstudante()) {
            JOptionPane.showMessageDialog(null, "Fim de jogo: Estudantes venceram!", "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
            throw new JogadaInvalidaException("Fim de jogo: Estudantes venceram!");
        } // Verifica se a Preá alcançou o objetivo
        else if (prea.getY() == 0) {
            JOptionPane.showMessageDialog(null, "Fim de jogo: Preá venceu!", "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
            throw new JogadaInvalidaException("Fim de jogo: Preá venceu!");
        }
    }

    /**
     * Indica se é o turno dos estudantes.
     *
     * @return true se for o turno dos estudantes, false caso contrário.
     */
    public boolean isVezDoEstudante() {
        return vezDoEstudante;
    }

    /**
     * Verifica se os estudantes venceram o jogo.
     *
     * @return true se os estudantes venceram (a Preá não pode mais se mover),
     * false caso contrário.
     */
    public boolean verificarVitoriaEstudante() {
        // Obtém a posição atual da Preá
        int[] posicaoPrea = getPosicaoDaPreaNoTabuleiro(tabuleiro);
        int x = posicaoPrea[0];
        int y = posicaoPrea[1];

        // Verifica todas as posições ao redor da Preá
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Ignora a posição da própria Preá
                if (i == 0 && j == 0) {
                    continue;
                }

                int novaLinha = x + i;
                int novaColuna = y + j;

                // Verifica se a posição ao redor está dentro dos limites do tabuleiro
                if (tabuleiro.isDentroDosLimites(novaLinha, novaColuna)) {
                    // Se a posição está livre ou não é ocupada por um Estudante, a Preá ainda pode se mover
                    if (!tabuleiro.isOcupadoPorEstudante(novaLinha, novaColuna)
                            && tabuleiro.isValidMove(novaLinha, novaColuna)) {
                        return false; // A Preá ainda pode se mover, os estudantes não venceram
                    }
                }
            }
        }

        // Se todas as posições ao redor da Preá estão ocupadas ou inválidas, os estudantes venceram
        return true;
    }

    /**
     * Captura a posição (coordenadas) da preá no tabuleiro
     *
     * @param tabuleiro tabuleiro do jogo
     *
     * @return coordenadas da preá
     */
    public int[] getPosicaoDaPreaNoTabuleiro(Tabuleiro tabuleiro) {
        int[] coordenadas = new int[]{0, 0};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                if (tabuleiro.getPeca(i, j) instanceof Prea) {
                    coordenadas = new int[]{i, j};
                }
            }
        }
        return coordenadas;
    }

    public void aoSelecionarPeca(int x, int y) {
        // Obtém a peça selecionada
        Peca peca = tabuleiro.getPeca(x, y);

        if (peca != null) {
            // Verifica e pinta os movimentos válidos
            verificarEPintarMovimentos(peca);
        }
    }

    public void verificarEPintarMovimentos(Peca peca) {
        int x = peca.getX();
        int y = peca.getY();

        // Deslocamentos para todas as direções possíveis
        int[][] deslocamentos = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}, // Cima, Baixo, Esquerda, Direita
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // Diagonais
        };

        // Reseta as cores do tabuleiro
        tabuleiro.resetarCoresTabuleiro();

        // Itera por todas as direções possíveis
        for (int[] deslocamento : deslocamentos) {
            int novoX = x + deslocamento[0];
            int novoY = y + deslocamento[1];

            // Verifica se o movimento é válido e dentro dos limites do tabuleiro
            if (tabuleiro.isDentroDosLimites(novoX, novoY) && peca.isMovimentoValido(novoX, novoY)) {
                // Pinta as casas válidas no tabuleiro
                tabuleiro.colorirPosicao(novoX, novoY, Color.GREEN);
            }
        }
    }

    private void exibeMensagemErro(String mensagem) throws JogadaInvalidaException {
        logger.warning(mensagem);
        JOptionPane.showMessageDialog(null, mensagem, "Jogada Inválida", JOptionPane.WARNING_MESSAGE);
        throw new JogadaInvalidaException(mensagem);
    }

    public boolean verificaEmpate() throws JogadaInvalidaException {
        int estudantesBloqueados = 0;
        // O jogo só pode empatar na vez dos Estudantes
        if (vezDoEstudante) {
            System.out.println("Checando empate!");
            // Itera sobre todos os Estudantes no tabuleiro
            for (Estudante estudante : estudantes) {
                if (!temParaOndeIr(estudante)) {
                    System.out.println("O estudante está bloqueado (" + estudante.getX() + "," + estudante.getY() + ")");
                    estudantesBloqueados += 1;
                }
            }
            System.out.println("verificaEmpate() = estudantesBloqueados: "+estudantesBloqueados);
            if (estudantesBloqueados == 3) {
                // Se nenhum movimento for válido para todos os Estudantes
                System.out.println("Todos os Estudantes estão bloqueados.");
                return true; // Jogo empatado
            } else {
                return false;
            }
        } else {
            // Prea deve ter ganho!
            System.out.println("Prea ganhou?!");
            verificarVitoria();
        }

        // Não verifica empate na vez da Preá
        return false;
    }

    public boolean temParaOndeIr(Peca peca) {
        boolean temParaOndeIr = false;
        for (int destRow = -1; destRow <= 1; destRow++) {
            for (int destCol = -1; destCol <= 1; destCol++) {
                int newRow = peca.getY() + destRow;
                int newCol = peca.getX() + destCol;

                // Ignora a posição original da peça
                if (newRow == peca.getY() && newCol == peca.getX()) {
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
}
