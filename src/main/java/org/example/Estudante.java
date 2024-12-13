package org.example;

import java.awt.Color;

/**
 * Representa uma peça do tipo Estudante no jogo. Cada estudante possui um
 * identificador único e uma imagem correspondente.
 */
public class Estudante extends Peca {

    private final String id; // Identificador único para o estudante.

    /**
     * Construtor para criar um Estudante com um identificador.
     *
     * @param x Linha inicial do Estudante no tabuleiro.
     * @param y Coluna inicial do Estudante no tabuleiro.
     * @param id Identificador único do Estudante.
     */
    public Estudante(int x, int y, String id) {
        super(x, y);
        this.id = id;
    }

    /**
     * Retorna o identificador do Estudante.
     *
     * @return O identificador único do Estudante.
     */
    public String getId() {
        return id;
    }

    @Override
    public boolean mover(int novoX, int novoY) {
        if (isMovimentoValido(novoX, novoY)) {
            setPosicao(novoX, novoY); // Atualiza a posição
            return true; // Movimento válido
        } else {
            return false;
        }
    }

    @Override
    public boolean isMovimentoValido(int novoX, int novoY) {
        int deltaX = Math.abs(novoX - x);
        int deltaY = Math.abs(novoY - y);
        System.out.println("isMovimentoValido?"+deltaX+","+deltaY);

        // Não pode pular mais de uma casa
        if (deltaX > 1 || deltaY > 1) { 
            System.out.println("Não pode pular mais de uma casa!!");
            return false;
        } 

        // Se está em uma posição cinza, bloqueia movimentos diagonais
        if (Tabuleiro.coresTabuleiro[x][y] == Color.GRAY && deltaX == 1 && deltaY == 1) {
            return false; // Movimento diagonal é inválido em posições cinzas
        }

        if (novoY < y) {
            System.out.println("O estudante não pode andar para trás");
            return false; // O estudante não pode andar para trás
        }
        return true;
    }

}
