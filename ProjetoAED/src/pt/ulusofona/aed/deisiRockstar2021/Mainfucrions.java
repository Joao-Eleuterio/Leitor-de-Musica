package pt.ulusofona.aed.deisiRockstar2021;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Mainfucrions {

    public static String[] splitArtistasTags(String command) {
        String[] resultado = new String[2];
        String[] comandoEArtistaMaisTags = command.split(";"); // tirando a posição 0 todas as posições sao uma TAG
        String[] comandoEArtista = comandoEArtistaMaisTags[0].split(" "); // comandoEArtista[0]=query comandoEArtista[1]=nome do artista

        for (int i = 1; i < comandoEArtista.length - 1; i++) {
            comandoEArtista[1] += " " + comandoEArtista[i + 1];
        }
        resultado[0] = comandoEArtista[1];

        for (int i = 1; i < comandoEArtistaMaisTags.length - 1; i++) {
            comandoEArtistaMaisTags[1] += ";" + comandoEArtistaMaisTags[i + 1];
        }
        resultado[1] = comandoEArtistaMaisTags[1];

        return resultado;
    }

    public static String countSongs(int year) {
        int count = 0;

        for (Song musica : Main.musicas) {
            if (musica.temaMusical.anoLancamento == year) {
                count++;
            }
        }
        return "" + count;
    }

    public static String countSongsDup(int year) {

        Main.musicas.sort(Comparator.comparing((Song song) -> song.temaMusical.titulo));
        int ano;
        String titulo;
        boolean jaRepetido = false;
        int duplicado = 0;
        for (int i = 0; i < Main.musicas.size() - 1; i++) {

            ano = Main.musicas.get(i).temaMusical.anoLancamento;
            titulo = Main.musicas.get(i).temaMusical.titulo;
            if (ano == year) {
                if (!jaRepetido && titulo.equals(Main.musicas.get(i + 1).temaMusical.titulo)) {
                    duplicado++;
                    jaRepetido = true;
                }
                if (!titulo.equals(Main.musicas.get(i + 1).temaMusical.titulo)) {
                    jaRepetido = false;
                }
            }

        }

        return "" + duplicado;
    }

    public static String getDanceable(int anoMin, int anoMax, int result) {


        Main.musicas.sort(Comparator.comparingDouble((Song song) -> song.temaMusical.grauDancabilidade).reversed());

        StringBuilder text = new StringBuilder();
        int musicasprintln = 0;
        int idDasMusicas = 0;
        int ano;

        while (musicasprintln < result && idDasMusicas != Main.musicas.size()) {
            ano = Main.musicas.get(idDasMusicas).temaMusical.anoLancamento;
            if (ano >= anoMin && ano <= anoMax) {
                text.append(Main.musicas.get(idDasMusicas).temaMusical.titulo).append(" : ").append(ano).append(" : ").append(Main.musicas.get(idDasMusicas).temaMusical.grauDancabilidade).append("\n");
                musicasprintln++;
            }
            idDasMusicas++;

        }


        /* resultado String separadas por \n pela sintaxe Nome tema +":"+ ano + ":" + dancabilidade */
        return text.toString();
    }

    public static String getArtistOneSong(int inicio, int fim) {

        ArrayList<String> repetidos = new ArrayList<>();
        ArrayList<String> chaves = new ArrayList<>();
        StringBuilder texto = new StringBuilder();
        int ano;
        HashMap<String, String> informacao = new HashMap<>();

        if (inicio >= fim) {
            return "Invalid period";
        }
        for (Song musica : Main.musicas) {
            ano = musica.temaMusical.anoLancamento;
            if (ano >= inicio && ano <= fim) {
                for (int f = 0; musica.temaMusical.artistas != null && f < musica.temaMusical.artistas.length; f++) {
                    if (informacao.get(musica.temaMusical.artistas[f].nome) == null) {
                        informacao.put(musica.temaMusical.artistas[f].nome, musica.temaMusical.titulo + " | " + musica.temaMusical.anoLancamento);
                        chaves.add(musica.temaMusical.artistas[f].nome);
                    } else {
                        repetidos.add(musica.temaMusical.artistas[f].nome);
                    }
                }
            }
        }

        for (String repetido : repetidos) {
            informacao.remove(repetido);
        }

        for (String chave : chaves) {
            if (informacao.get(chave) != null) {
                texto.append(chave).append(" | ").append(informacao.get(chave));
            }
        }

        return texto.toString();
    }

    public static String getArtists(int result, int temaMin, int temaMax) {
        StringBuilder texto = new StringBuilder();
        boolean entrou;
        ArrayList<OcorrenciasDeUmaString> nomesMaisNumetoTemas = new ArrayList<>();
        for (Song musica : Main.musicas) {
            if (musica.temaMusical.artistas != null) {
                for (int f = 0; f < musica.temaMusical.artistas.length; f++) { // juntar todos os artistas

                    if (nomesMaisNumetoTemas.size() == 0) {
                        nomesMaisNumetoTemas.add(new OcorrenciasDeUmaString(musica.temaMusical.artistas[f].nome, 1));
                    } else {
                        entrou = false;
                        for (OcorrenciasDeUmaString nomesMaisNumetoTema : nomesMaisNumetoTemas) {

                            if (musica.temaMusical.artistas[f].nome.equals(nomesMaisNumetoTema.nome)) {
                                nomesMaisNumetoTema.numeroDeTemas++;
                                entrou = true;
                                break;
                            }
                        }
                        if (!entrou) {
                            nomesMaisNumetoTemas.add(new OcorrenciasDeUmaString(musica.temaMusical.artistas[f].nome, 1));

                        }
                    }
                }
            }
        }

        int countResultado = 0;


        nomesMaisNumetoTemas.sort(Comparator.comparingInt((OcorrenciasDeUmaString temas) -> temas.numeroDeTemas).reversed());

        for (int i = 0; i < nomesMaisNumetoTemas.size() && countResultado != result; i++) {
            int temas = nomesMaisNumetoTemas.get(i).numeroDeTemas;
            if (temas >= temaMin && temas <= temaMax) {
                texto.append(nomesMaisNumetoTemas.get(i).nome).append(" ").append(nomesMaisNumetoTemas.get(i).numeroDeTemas).append("\n");
                countResultado++;
            }
        }
        if (countResultado == 0) {
            return "No results";
        }


        return texto.toString();
    }//complexa E TIMEOUT

    public static String cleanUp() {

        String id;
        boolean temMusicaA, temMusicaD;
        int countmusicas = 0, countartistas = 0;


        for (int i = 0; i < Main.musicas.size(); i++) {

            if (Main.musicas.get(i).temaMusical.artistas == null || Main.musicas.get(i).temaMusical.duracaoTema == 0.0) {
                countmusicas++;
                Main.musicas.remove(i);
                i--;
            }
        }

        countartistas = Main.artistasSemMusica.size();
        Main.artistasSemMusica.clear();
        return "Musicas removidas: " + countmusicas + ";Artistas removidos: " + countartistas;

    }

    public static String getUniqueTag() {

        ArrayList<OcorrenciasDeUmaString> tags = new ArrayList<>();
        StringBuilder texto = new StringBuilder();
        boolean entrou;

        for (Song musica : Main.musicas) {
            for (int f = 0; musica.temaMusical.artistas != null && f < musica.temaMusical.artistas.length; f++) {
                for (int j = 0; j < musica.temaMusical.artistas[f].tag.size(); j++) {
                    if (tags.size() == 0) {
                        tags.add(new OcorrenciasDeUmaString(musica.temaMusical.artistas[f].tag.get(j), 1));
                    } else {
                        entrou = false;
                        for (OcorrenciasDeUmaString tag : tags) {

                            if (musica.temaMusical.artistas[f].nome.equals(tag.nome)) {
                                tag.numeroDeTemas++;
                                entrou = true;
                                break;
                            }
                        }
                        if (!entrou) {
                            tags.add(new OcorrenciasDeUmaString(musica.temaMusical.artistas[f].nome, 1));
                        }
                    }

                }
            }
        }

        int countResultado = 0;


        tags.sort(Comparator.comparingInt((OcorrenciasDeUmaString temas) -> temas.numeroDeTemas));

        for (OcorrenciasDeUmaString tag : tags) {
            texto.append(tag.nome).append(" ").append(tag.numeroDeTemas).append("\n");
            countResultado++;

        }
        if (countResultado == 0) {
            return "No results";
        }

        return texto.toString();
    }

    public static String addTag(String tag, String artista) {

        tag = tag.toUpperCase();
        String[] tags = tag.split(";");
        int[] index = getartistsposition(artista);
        if (index[0] < 0) {
            return "Inexistent artist";
        }
        StringBuilder texto = new StringBuilder(artista + " | ");

        if (Main.musicas.get(index[0]).temaMusical.artistas[index[1]].nome.equals(artista)) {
            if (Main.musicas.get(index[0]).temaMusical.artistas[index[1]].tag.size() == 0) {
                Main.musicas.get(index[0]).temaMusical.artistas[index[1]].tag.add(tags[0]);
            }
            for (String s : tags) {
                if (!Main.musicas.get(index[0]).temaMusical.artistas[index[1]].tag.contains(s)) {
                    Main.musicas.get(index[0]).temaMusical.artistas[index[1]].tag.add(s);
                }
            }
        }

        texto.append(Main.musicas.get(index[0]).temaMusical.artistas[index[1]].tag.get(0));

        for (int i = 1; i < Main.musicas.get(index[0]).temaMusical.artistas[index[1]].tag.size(); i++) {

            texto.append(",").append(Main.musicas.get(index[0]).temaMusical.artistas[index[1]].tag.get(i));
        }

        return texto.toString();

    }

    public static int[] getartistsposition(String artista) {

        int[] posicao = {-1, -1};


        for (int i = 0; i < Main.musicas.size(); i++) {
            for (int j = 0; Main.musicas.get(i).temaMusical.artistas != null && j < Main.musicas.get(i).temaMusical.artistas.length; j++) {
                if (Main.musicas.get(i).temaMusical.artistas[j].nome.equals(artista)) {
                    posicao[0] = i;
                    posicao[1] = j;
                }
            }
        }
        return posicao;
    }

    public static String removeTag(String tag, String artista) {

        tag = tag.toUpperCase();

        String[] tags = tag.split(";");
        int[] index = getartistsposition(artista);
        if (index[0] < 0) {
            return "Inexistent artist";
        }
        StringBuilder texto = new StringBuilder(artista + " | ");

        if (Main.musicas.get(index[0]).temaMusical.artistas[index[1]].nome.equals(artista)) {
            if (Main.musicas.get(index[0]).temaMusical.artistas[index[1]].tag.size() == 0) {
                Main.musicas.get(index[0]).temaMusical.artistas[index[1]].tag.add(tags[0]);
            }
            for (String s : tags) {
                Main.musicas.get(index[0]).temaMusical.artistas[index[1]].tag.remove(s);
            }

        }
        if (Main.musicas.get(index[0]).temaMusical.artistas[index[1]].tag.size() == 0) {
            texto.append("No tags");
            return texto.toString();
        }

        texto.append(Main.musicas.get(index[0]).temaMusical.artistas[index[1]].tag.get(0));

        for (int i = 1; i < Main.musicas.get(index[0]).temaMusical.artistas[index[1]].tag.size(); i++) {

            texto.append(";").append(Main.musicas.get(index[0]).temaMusical.artistas[index[1]].tag.get(i));
        }
        return texto.toString();
    }

    public static String getArtistForTag(String tag) {

        tag = tag.toUpperCase();
        StringBuilder texto = new StringBuilder();
        boolean first = true;

        for (Song musica : Main.musicas) {
            for (int f = 0; musica.temaMusical.artistas != null && f < musica.temaMusical.artistas.length; f++) {
                if (musica.temaMusical.artistas[f].tag.contains(tag)) {
                    if (first) {
                        texto.append(musica.temaMusical.artistas[f].nome);
                        first = false;
                    } else {
                        texto.append(";").append(musica.temaMusical.artistas[f].nome);
                    }
                }
            }
        }
        if (texto.length() == 0) {
            return "No results";
        }

        return texto.toString();
    }

    public static String getUniqueTagYear(int inicio, int fim) {
        StringBuilder texto = new StringBuilder();
        int ano;
        boolean entrou;
        ArrayList<OcorrenciasDeUmaString> estatisticas = new ArrayList<>();
        for (Song musica : Main.musicas) {
            ano = musica.temaMusical.anoLancamento;
            if (ano >= inicio && ano <= fim) {
                for (int j = 0; musica.temaMusical.artistas != null && j < musica.temaMusical.artistas.length; j++) {
                    for (int k = 0; k < musica.temaMusical.artistas[j].tag.size(); k++) {
                        if (estatisticas.size() == 0) {
                            estatisticas.add(new OcorrenciasDeUmaString(musica.temaMusical.artistas[j].tag.get(k), 1));
                        } else {
                            entrou = false;
                            for (OcorrenciasDeUmaString estatistica : estatisticas) {

                                if (musica.temaMusical.artistas[j].tag.get(k).equals(estatistica.nome)) {
                                    estatistica.numeroDeTemas++;
                                    entrou = true;
                                    break;
                                }
                            }
                            if (!entrou) {
                                estatisticas.add(new OcorrenciasDeUmaString(musica.temaMusical.artistas[j].tag.get(k), 1));
                            }
                        }
                    }
                }
            }
        }

        for (OcorrenciasDeUmaString estatistica : estatisticas) {
            texto.append(estatistica.nome).append(" ").append(estatistica.numeroDeTemas).append("\n");
        }

        return texto.toString();
    } //complexa

    public static String getCaracter(int result, int temaMin, int tamanhoMin) {

        String nomesArtistas;
        StringBuilder texto = new StringBuilder();
        String nome;
        boolean entrou;
        String[] subNomesDoArtista;
        ArrayList<OcorrenciasDeUmaString> estatisticas = new ArrayList<>();

        for (Song musica : Main.musicas) {

            for (int f = 0; musica.temaMusical.artistas != null && f < musica.temaMusical.artistas.length; f++) {
                nomesArtistas = musica.temaMusical.artistas[f].nome;
                subNomesDoArtista = nomesArtistas.split(" ");
                for (String s : subNomesDoArtista) {
                    nome = s;
                    if (nome.length() > tamanhoMin) {
                        if (estatisticas.size() == 0) {
                            estatisticas.add(new OcorrenciasDeUmaString(nome, 1));
                        } else {
                            entrou = false;
                            for (OcorrenciasDeUmaString estatistica : estatisticas) {
                                for (int k = 0; k < nomesArtistas.length(); k++) {
                                    if (nome.equals(estatistica.nome)) {
                                        estatistica.numeroDeTemas++;
                                        entrou = true;
                                        break;
                                    }
                                }
                            }
                            if (!entrou) {
                                estatisticas.add(new OcorrenciasDeUmaString(nome, 1));
                            }
                        }

                    }
                }
            }
        }
        estatisticas.sort(Comparator.comparingInt((OcorrenciasDeUmaString numero) -> numero.numeroDeTemas));
        int countResultados = 0;
        for (OcorrenciasDeUmaString estatistica : estatisticas) {
            if (estatistica.numeroDeTemas >= temaMin && countResultados < result) {
                texto.append(estatistica.nome).append(" ").append(estatistica.numeroDeTemas).append("\n");
                countResultados++;
            }
        }

        return texto.toString();
    } //complexa

    public static String getRisingStars(int ano1, int ano2, String ordenacao) {
        ArrayList<ArtistaMusicaAno> _artistasMusica = new ArrayList<>();
        ArrayList<ArtistaMusicaAno> _canContinue = new ArrayList<>();
        ArrayList<Artista> _artistas = new ArrayList<>();

        // Percorre musicas e ve se estao entre ano1 e ano2
        for (Song musica : Main.musicas) {
            if (musica.temaMusical.anoLancamento >= ano1 && musica.temaMusical.anoLancamento <= ano2) {
                if (musica.temaMusical.artistas != null && musica.temaMusical.artistas.length > 0) { //ve se tem artistas (pd n ter)
                    for (Artista artista : musica.temaMusical.artistas) {
                        addArtistaMusica(_artistasMusica, artista, musica); //adiciona no arrayLista _artistasMusica caso esteja entre o ano1 e ano2 e possua pelo menos 1 artista guardamos a musica pelo artista
                    }
                }
            }
        }
        //Temos todos as musicas entre ano1 e ano2 mas precisamos musicas que
        // estejam em TODOS OS ANOS ou seja precisamos de buscar artistas que tenham
        // criado pelo menos uma musica para adicionar ao array _canContinue;
        for (ArtistaMusicaAno _artistaMusicaAno : _artistasMusica) {
            if (_artistaMusicaAno.canContinue(ano1, ano2)) {
                _canContinue.add(_artistaMusicaAno);
            }
        }
        //caso tenha artistas / musicas entre o ano1 e ano2 continua caso contrario return "No Result"
        if (_canContinue.size() == 0) {
            return "No Result";
        } else {
            for (ArtistaMusicaAno _artistasMusicas : _canContinue) {
                _artistasMusicas.populariadeMedia = _artistasMusicas.getMusicaPopularidade();
            }
        }

        if(ordenacao.equals("ASD")){
            _canContinue.sort(Comparator.comparingInt((ArtistaMusicaAno ola)->ola.populariadeMedia));}
        else{
            _canContinue.sort(Comparator.comparingInt((ArtistaMusicaAno ola)->ola.populariadeMedia).reversed());
        }
        StringBuilder Final = new StringBuilder();
        for (int i = 0; i < _canContinue.size() && i < 15; i++){
            Final.append(_canContinue.get(i).artista.nome).append(" <=> ").append(_canContinue.get(i).populariadeMedia).append("\n");
        }

        return Final.toString();
    }

    public static ArtistaMusicaAno addArtistaMusica(ArrayList<ArtistaMusicaAno> _artistaMusicaAno, Artista _artista, Song _song) {
        for (ArtistaMusicaAno artistaMusicaAno : _artistaMusicaAno) {
            if (artistaMusicaAno.artista.nome.equals(_artista.nome)) {
                artistaMusicaAno.musicas.add(_song);
                return null;
            }
        }
        _artistaMusicaAno.add(new ArtistaMusicaAno(_artista, _song));
        return null;
    }

    public static int partition(ArrayList<ArtistaMusicaAno> arrayList, int left, int right, String ordenacao) {
        ArtistaMusicaAno pivot = arrayList.get(right);
        int leftIdx = left;
        int rightIdx = right - 1; // exclude the pivot
        if (ordenacao.equals("ASD")) {
            while (leftIdx <= rightIdx) {
                // se o left fôr maior que o pivot e o right fôr maior que o pivot, então troca
                if (arrayList.get(leftIdx).populariadeMedia > pivot.populariadeMedia && arrayList.get(rightIdx).populariadeMedia < pivot.populariadeMedia) {
                    ArtistaMusicaAno temp = arrayList.get(leftIdx);
                    arrayList.set(leftIdx, arrayList.get(rightIdx));
                    arrayList.set(rightIdx, temp);
                }
                // se o left fôr menor que o pivot, avança
                if (arrayList.get(leftIdx).populariadeMedia <= pivot.populariadeMedia) {
                    leftIdx++;
                }
                // se o right fôr maior que o pivot, recua
                if (arrayList.get(rightIdx).populariadeMedia >= pivot.populariadeMedia) {
                    rightIdx--;
                }
            }
        } else {
            while (leftIdx <= rightIdx) {
                // se o left fôr maior que o pivot e o right fôr maior que o pivot, então troca
                if (arrayList.get(leftIdx).populariadeMedia < pivot.populariadeMedia && arrayList.get(rightIdx).populariadeMedia > pivot.populariadeMedia) {
                    ArtistaMusicaAno temp = arrayList.get(leftIdx);
                    arrayList.set(leftIdx, arrayList.get(rightIdx));
                    arrayList.set(rightIdx, temp);
                }
                // se o left fôr menor que o pivot, avança
                if (arrayList.get(leftIdx).populariadeMedia >= pivot.populariadeMedia) {
                    leftIdx++;
                }
                // se o right fôr maior que o pivot, recua
                if (arrayList.get(rightIdx).populariadeMedia <= pivot.populariadeMedia) {
                    rightIdx--;
                }
            }
        }
        // troca pivot com left
        arrayList.set(right, arrayList.get(leftIdx));
        arrayList.set(leftIdx, pivot);
        return leftIdx;
    }//complexa

    public static ArrayList<ArtistaMusicaAno> ordenar(ArrayList<ArtistaMusicaAno> arrayList, int left, int right, String ordenacao) {
        if (left < right) {
            int posicaoPivot = partition(arrayList, left, right - 1, ordenacao);

            arrayList = ordenar(arrayList, left, posicaoPivot, ordenacao);
            arrayList = ordenar(arrayList, posicaoPivot + 1, right, ordenacao);
        }
        return arrayList;
    }

    public static int countNumeroTemas(String artista) {
        int count = 0;
        for (Song musica : Main.musicas) {
            for (int f = 0; musica.temaMusical.artistas != null && f < musica.temaMusical.artistas.length; f++) {
                if (musica.temaMusical.artistas[f].nome.equals(artista)) {
                    count++;
                }
            }
        }
        return count;
    }

}
