package pt.ulusofona.aed.deisiRockstar2021;

import java.util.ArrayList;
;

public class Song {
    Artista artista;
    TemaMusical temaMusical;

    public Song(TemaMusical temaMusical) {
        this.temaMusical = temaMusical;
    }

    public String toString() {
        double popularidade=temaMusical.popularidade;
        StringBuilder nomesArtistas = new StringBuilder();
        StringBuilder numeroTemas = new StringBuilder("(");
        int count=0;
        boolean primeira = true;

        if (temaMusical.artistas != null) {

            for (int i = 0; i < temaMusical.artistas.length; i++) {
                if (primeira) {
                    nomesArtistas.append(temaMusical.artistas[i].nome);
                    count=Mainfucrions.countNumeroTemas(temaMusical.artistas[i].nome);
                    numeroTemas.append(count);
                    primeira = false;
                } else {
                    nomesArtistas.append(" / ").append(temaMusical.artistas[i].nome);
                    count=Mainfucrions.countNumeroTemas(temaMusical.artistas[i].nome);
                    numeroTemas.append(" / ").append(count);
                }
            }
        }
        numeroTemas.append(")");
        return temaMusical.id + " | " + temaMusical.titulo + " | " + temaMusical.anoLancamento + " | " + (temaMusical.duracaoTema/60000)+":"+((temaMusical.duracaoTema/1000)%60) + " | " + popularidade + " | " + nomesArtistas+ " | " +  numeroTemas;
    }
}

class Artista {
    String id;
    String nome;
    ArrayList<String> tag = new ArrayList<>();


    public Artista(String nome) {
        this.nome = nome;
    }


}

class TemaMusical {
    String id;
    String titulo;
    Artista[] artistas;
    int anoLancamento;
    int duracaoTema;
    boolean letraExplicita;
    int popularidade;
    double grauDancabilidade;
    double grauVivacidade;
    double volumeMedio;

    public TemaMusical(String id, Artista[] artistas) {
        this.id = id;
        this.artistas = artistas;

    }

    public TemaMusical(String id, String titulo, int anoLancamento) {
        this.id = id;
        this.titulo = titulo;
        this.anoLancamento = anoLancamento;
    }

    public TemaMusical(String id, int duracaoTema, boolean letraExplicita, int popularidade, double grauDancabilidade, double grauVivacidade, double volumeMedio) {
        this.id = id;
        this.duracaoTema = duracaoTema;
        this.letraExplicita = letraExplicita;
        this.popularidade = popularidade;
        this.grauDancabilidade = grauDancabilidade;
        this.grauVivacidade = grauVivacidade;
        this.volumeMedio = volumeMedio;
    }


}