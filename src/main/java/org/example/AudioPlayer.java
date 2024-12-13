package org.example;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {
    private Clip clip;

    // Método para carregar o áudio
    public void carregarMusica(String caminhoArquivo) {
        try {
            File arquivoAudio = new File("C:\\faculdade\\pega-prea\\src\\main\\resources\\video-game-boss-fiight-259885.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(arquivoAudio);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Método para iniciar a música
    public void tocarMusica() {
        if (clip != null) {
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Música em loop
        }
    }

    // Método para parar a música
    public void pararMusica() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
