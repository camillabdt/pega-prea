package org.example;

import java.awt.Color;

/**
 * Representa o tabuleiro do jogo como uma matriz 3x5. Gerencia as peças,
 * movimentações e validações de posição.
 */
public class Tabuleiro {

    private final Peca[][] tabuleiro;
    public static Color[][] coresTabuleiro;

    public Color[][] getCoresTabuleiro() {
        return coresTabuleiro;
    }

    public Peca[][] getTabuleiro() {
        return tabuleiro;
    }

    /**
     * Construtor do tabuleiro. Inicializa a matriz de peças.
     */
    public Tabuleiro() {
        tabuleiro = new Peca[3][5];
        coresTabuleiro = new Color[][]{
            new Color[]{Color.BLACK, Color.WHITE, Color.GRAY, Color.WHITE, Color.BLACK},
            new Color[]{Color.WHITE, Color.GRAY, Color.WHITE, Color.GRAY, Color.WHITE},
            new Color[]{Color.BLACK, Color.WHITE, Color.GRAY, Color.WHITE, Color.BLACK}
        };

    }

    public Color getCorCasa(int row, int col) {
    return coresTabuleiro[row][col]; // Retorna a cor da casa
}

    /**
     * Obtém a peça na posição especificada.
     *
     * @param x Linha do tabuleiro.
     * @param y Coluna do tabuleiro.
     * @return A peça na posição (x, y) ou null se não houver peça.
     */
    public Peca getPeca(int x, int y) {
        if (x >= 0 && x < 3 && y >= 0 && y < 5) { // Verifica os limites do tabuleiro
            return tabuleiro[x][y];
        }
        return null;
    }

    /**
     * Adiciona uma peça no tabuleiro na posição especificada.
     *
     * @param peca A peça a ser posicionada.
     */
    public void colocarPeca(Peca peca) {
        if (peca != null) {
            tabuleiro[peca.getX()][peca.getY()] = peca;
        }
    }

    public boolean isPosicaoOcupada(int x, int y) {
        return getPeca(x, y) != null; // True se a posição estiver ocupada
    }

    /**
     * Move uma peça de uma posição inicial para uma posição de destino.
     *
     * @param xOrigem Linha de origem.
     * @param yOrigem Coluna de origem.
     * @param xDestino Linha de destino.
     * @param yDestino Coluna de destino.
     */
    public boolean moverPeca(int xOrigem, int yOrigem, int xDestino, int yDestino) {
        if (!isPosicaoOcupada(xDestino, yDestino)) {
            Peca peca = getPeca(xOrigem, yOrigem);
            peca.mover(xDestino, yDestino);

//        if (peca != null && peca.m(xDestino, yDestino, this)) {
            tabuleiro[xDestino][yDestino] = peca;
            tabuleiro[xOrigem][yOrigem] = null;
//            peca.setPosicao(xDestino, yDestino);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Verifica se uma posição está dentro dos limites do tabuleiro.
     *
     * @param x Linha da posição.
     * @param y Coluna da posição.
     * @return true se a posição estiver dentro dos limites; false caso
     * contrário.
     */
    public boolean isDentroDosLimites(int x, int y) {
        return x >= 0 && x < 3 && y >= 0 && y < 5; // Ajuste para o tamanho do seu tabuleiro
    }

    /**
     * Verifica se a Preá está cercada por peças ou limites do tabuleiro.
     *
     * @param prea A peça representando a Preá.
     * @return true se a Preá estiver cercada; false caso contrário.
     */
    public boolean estaCercado(Prea prea) {
        int x = prea.getX();
        int y = prea.getY();

        // Verifica todas as posições ao redor da Preá
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) { // Ignora a posição atual
                    int novaLinha = x + i;
                    int novaColuna = y + j;
                    if (isValidMove(novaLinha, novaColuna)) {
                        return false; // Se houver um movimento válido, não está cercada
                    }
                }
            }
        }
        return true; // Está cercada
    }

    /**
     * Verifica se uma posição está ocupada por um estudante.
     *
     * @param x Linha da posição.
     * @param y Coluna da posição.
     * @return true se a posição estiver ocupada por um estudante; false caso
     * contrário.
     */
    public boolean isOcupadoPorEstudante(int x, int y) {
        Peca peca = getPeca(x, y);
        return peca instanceof Estudante;
    }

    /**
     * Verifica se uma posição está ocupada.
     *
     * @param x Linha da posição.
     * @param y Coluna da posição.
     * @return true se houver uma peça na posição, false caso contrário.
     */
    public boolean isOcupado(int x, int y) {
        return getPeca(x, y) != null;
    }

    public boolean isAllowed(int x, int y) {
        return coresTabuleiro[x][y] != Color.BLACK;
    }
    
  public void resetarCoresTabuleiro() {
    for (int i = 0; i < coresTabuleiro.length; i++) {
        for (int j = 0; j < coresTabuleiro[0].length; j++) {
            coresTabuleiro[i][j] = Color.WHITE; // Reseta para a cor padrão
        }
    }
}


    
    public void colorirPosicao(int x, int y, Color cor) {
    coresTabuleiro[x][y] = cor; // Atualiza a matriz de cores
}


    /**
     * Verifica se a posição especificada está dentro dos limites do tabuleiro e
     * está vazia.
     *
     * @param x Linha da posição.
     * @param y Coluna da posição.
     * @return true se a posição estiver dentro dos limites e estiver vazia.
     */
    public boolean isValidMove(int x, int y) {
        try {
            Peca p = tabuleiro[x][y];
            if (!isOcupado(x, y) && isAllowed(x, y)) {
                return true;
            }
        } catch (ArrayIndexOutOfBoundsException i) {
            return false;
        }
        return false;
    }

}
