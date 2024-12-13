package org.example;

/**
 * Exceção personalizada para indicar jogadas inválidas no jogo Pega Preá.
 */
public class JogadaInvalidaException extends Exception {
    public JogadaInvalidaException(String mensagem) {
        super(mensagem);
    }
}