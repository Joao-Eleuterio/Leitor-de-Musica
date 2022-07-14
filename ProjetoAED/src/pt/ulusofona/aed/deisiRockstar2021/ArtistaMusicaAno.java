package pt.ulusofona.aed.deisiRockstar2021;

import java.util.ArrayList;

public class ArtistaMusicaAno {
    public Artista artista;
    public ArrayList<Song> musicas;
    int populariadeMedia;

    public ArtistaMusicaAno() {
        musicas = new ArrayList<>();
    }

    public ArtistaMusicaAno(Artista artista1, Song song) {
        musicas = new ArrayList<>();
        artista = artista1;
        musicas.add(song);
    }

    public int getMusicaPopularidade() {
        int soma = 0;
        for(Song song : this.musicas) {
            soma += song.temaMusical.popularidade;
        }
        return soma / this.musicas.size();
    }

    public boolean canContinue(int ano1, int ano2) {
        boolean Continue = false;
        //Começando no ano1 vai até ao ano2 (inclusive) buscar musicas que estajam nesse ano,
        // se em algum momento o artista n tiver criado uma musica entre o ano1 e ano2 metemos a variavel Continue a False
        for(int i = ano1; i <= ano2; i++) {
            Continue = false;
            for(Song song : musicas) {
                if(song.temaMusical.anoLancamento == i) {
                    Continue = true;
                    break;
                }
            }
        }
        return Continue;
    }
}