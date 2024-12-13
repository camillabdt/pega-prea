package org.example;

import java.awt.Color;

public class Prea extends Peca {

    public Prea(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean mover(int novoX, int novoY) {
        if (isMovimentoValido(novoX, novoY)) { // Se está em uma posição cinza, bloqueia movimentos diagonais
            setPosicao(novoX, novoY); // Atualiza a posição
            System.out.println("Moveu peça para " + novoX + "," + novoY);
            return true; // Movimento válido
        } else {
            System.out.println("Movimento inválido!");
            return false; // Movimento diagonal é inválido em posições cinzas
        }
    }

    @Override
    public boolean isMovimentoValido(int novoX, int novoY) {
        int deltaX = Math.abs(novoX - x);
        int deltaY = Math.abs(novoY - y);

          // Não pode pular mais de uma casa
        if (deltaX > 1 || deltaY > 1) { 
            System.out.println("Não pode pular mais de uma casa!!");
            return false;
        } 

        // Se está em uma posição cinza, bloqueia movimentos diagonais
        if (Tabuleiro.coresTabuleiro[x][y] == Color.GRAY && deltaX == 1 && deltaY == 1) {
            return false; // Movimento diagonal é inválido em posições cinzas
        }
        return true;
    }

}
