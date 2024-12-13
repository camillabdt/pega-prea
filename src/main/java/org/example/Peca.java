package org.example;

/**
 * Representa uma peça no tabuleiro.
 * Classes concretas como Prea e Estudante devem herdar esta classe.
 */
public abstract class Peca {
    protected int x, y;
    protected Tabuleiro Tabuleiro;

    /**
     * Construtor da peça.
     *
     * @param x Linha inicial da peça.
     * @param y Coluna inicial da peça.
     */
    public Peca(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Obtém a posição X (linha) da peça.
     *
     * @return A linha da peça no tabuleiro.
     */
    public int getX() {
        return x;
    }

    /**
     * Obtém a posição Y (coluna) da peça.
     *
     * @return A coluna da peça no tabuleiro.
     */
    public int getY() {
        return y;
    }

    /**
     * Define a nova posição da peça no tabuleiro.
     *
     * @param x Nova linha da peça.
     * @param y Nova coluna da peça.
     */
    public void setPosicao(int x, int y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Move a peça para uma nova posição.
     *
     * @param novoX Nova linha.
     * @param novoY Nova coluna.
     * @return true se o movimento for válido; false caso contrário.
     */
    public abstract boolean mover(int novoX, int novoY);
    
    public abstract boolean isMovimentoValido(int novoX, int novoY);
}
