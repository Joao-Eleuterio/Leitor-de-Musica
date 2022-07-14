package pt.ulusofona.aed.deisiRockstar2021;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.HashSet;


public class Main {

    static int[][] estatisticasLinhas = new int[3][2];

    static ArrayList<Song> musicas = new ArrayList<>();

    static ArrayList<Song> artistasSemMusica = new ArrayList<>();

    static ArrayList<Song> detailsSemMusica = new ArrayList<>();

    static LinkedHashMap<String, Song> hashmusicas = new LinkedHashMap<>();


    public static void main(String[] args) {


       String textoIntroduzido;
        long start = System.currentTimeMillis();
        try {
            loadFiles();
        } catch (Exception c) {
            c.printStackTrace();
        }
        long end = System.currentTimeMillis();

        System.out.println(end - start);
        System.out.println(getParseInfo("songs.txt"));
        System.out.println(getParseInfo("song_artists.txt"));
        System.out.println(getParseInfo("song_details.txt"));
        System.out.println("Welcome to DEISI Rockstar!\n");

        Scanner input = new Scanner(System.in);

        textoIntroduzido = input.nextLine();

        while (textoIntroduzido != null && !textoIntroduzido.equals("KTHXBYE")) {


            start = System.currentTimeMillis();

            String resultado = execute(textoIntroduzido);

            end = System.currentTimeMillis();

            System.out.println(resultado);
            System.out.println("(took " + (end - start) + " ms)\n");

            textoIntroduzido = input.nextLine();

        }

    }

    public static void loadFiles() throws IOException {
        //limpeza do array das estatisticas
        for (int[] estatisticasLinha : estatisticasLinhas) {
            Arrays.fill(estatisticasLinha, 0);
        }


        //limpeza do array das musicas
        musicas.clear();
        artistasSemMusica.clear();
        detailsSemMusica.clear();
        hashmusicas.clear();

        long inicio = System.currentTimeMillis();
        String nomeFicheiro = "songs.txt";
        try {
            FileReader ficheiro = new FileReader(nomeFicheiro);
            BufferedReader reader = new BufferedReader(ficheiro);
            lersong(reader);
            reader.close();
            long fim = System.currentTimeMillis();
            System.out.println("songs demorou " + (fim - inicio) + " ms");


            nomeFicheiro = "song_artists.txt";
            inicio = System.currentTimeMillis();
            ficheiro = new FileReader(nomeFicheiro);
            reader = new BufferedReader(ficheiro);
            lerArtistas(reader);
            reader.close();
            fim = System.currentTimeMillis();
            System.out.println("artistas demorou " + (fim - inicio) + " ms");


            nomeFicheiro = "song_details.txt";
            inicio = System.currentTimeMillis();
            ficheiro = new FileReader(nomeFicheiro);
            reader = new BufferedReader(ficheiro);
            lerDetails(reader);
            reader.close();
            fim = System.currentTimeMillis();
            System.out.println("details demorou " + (fim - inicio) + " ms");

            musicas.addAll(hashmusicas.values());
        } catch (FileNotFoundException e) {
            System.out.println("Ficheiro não encontrado");
        } catch (IOException e) {
            System.out.println("Ocorreu um erro durante a leitura.");
        }

    }

    public static void lersong(BufferedReader reader) throws IOException {

        HashSet<String> idsongs = new HashSet<>();
        String linha;
        boolean informacaoInvalidaNosParametros=false;
        while ((linha = reader.readLine()) != null) {

            //partir a linha no caractere separador
            String[] dados = linha.split("@");

            if (dados.length != 3) {
                estatisticasLinhas[0][0]++;
                continue;
            }

            String id = dados[0].trim();
            String nome = dados[1].trim();

            int anoLancamento = 1;
            informacaoInvalidaNosParametros = false;
            try {
                anoLancamento = Integer.parseInt(dados[2].trim());
            } catch (NumberFormatException e) {
                informacaoInvalidaNosParametros = true;
            }

            if (informacaoInvalidaNosParametros) {
                estatisticasLinhas[0][0]++;
                continue;
            }

            if (Integer.parseInt(dados[2].trim()) < 0 && Integer.parseInt(dados[2].trim()) > 2021) {
                estatisticasLinhas[0][0]++;
                continue;
            }


            if (id.equals("") || nome.equals("")) {
                estatisticasLinhas[0][0]++;
                continue;
            }


            if (!idsongs.add(id)) {
                estatisticasLinhas[0][0]++;
                continue;
            }
            int count = 0;
            while (nome.charAt(count)=='\"'){
                count++;
            }
            if (count!=0){
                String[] nomesMusica = dados[1].split("\"");
                if (count==3){

                    nome ="\""+ nomesMusica[count]+"\"";
                }else {
                    nome = nomesMusica[count];
                }
            }


            hashmusicas.put(id, new Song(new TemaMusical(id, nome, anoLancamento)));
            //musicas.add(new Song(new TemaMusical(id,nome,anoLancamento)));
            estatisticasLinhas[0][1]++;
        }

    }
    public static void lerArtistas(BufferedReader reader) throws IOException {
        String linha;
        boolean ingnorada;
        while ((linha = reader.readLine()) != null) {
            ingnorada = false;
            //partir a linha no caractere separador
            String[] dados = linha.split("@");
            if (dados.length != 2) {
                estatisticasLinhas[2][0]++;
                continue;
            }

            Artista[] artistasArray;
            String id = dados[0].trim();
            String[] nomes;
            boolean entrou = false;

            if (hashmusicas.get(id)==null){
                estatisticasLinhas[2][0]++;
                continue;
            }

            nomes = dados[1].trim().split(",");
            artistasArray = new Artista[nomes.length];
            for (int i = 0; i < nomes.length; i++) {
                nomes[i] = nomes[i].trim().split(",")[0];
                if (nomes[i].equals("''")) {
                    entrou = true;
                    break;
                }
                if (nomes[i].split("'").length == 1 || nomes[i].split("'")[1].equals("]\"")) {
                    nomes[i] = nomes[i].split("'")[0];
                } else {
                    nomes[i] = nomes[i].split("'")[1];
                }
                if (nomes[i].equals("\"[")) {
                    entrou = true;
                    break;
                }

                artistasArray[i] = new Artista(nomes[i]);

            }
            if (entrou) {
                estatisticasLinhas[2][0]++;
                continue;
            }

            for (Artista artista : artistasArray) {
                if (artista.nome.equals("")) {
                    ingnorada = true;
                    break;
                }
            }
            if (ingnorada) {
                estatisticasLinhas[2][0]++;
                continue;
            }
            if (id.equals("")) {
                estatisticasLinhas[2][0]++;
                continue;
            }


            if (hashmusicas.get(id) == null) {
                artistasSemMusica.add(new Song(new TemaMusical(id, artistasArray)));
            } else {
                hashmusicas.get(id).temaMusical.artistas = artistasArray;
            }

            estatisticasLinhas[2][1]++;
                /*De seguida seria necessário guardar o objecto Utilizador numa estrutura de dados apropriada
                 (p.e. array, lista, etc).*/
        }
    }
    public static void lerDetails(BufferedReader reader) throws IOException {
        String linha;
        HashSet<String> iddetails = new HashSet<>();
        while ((linha = reader.readLine()) != null) {
            //partir a linha no caractere separador
            String[] dados = linha.split("@");
            if (dados.length != 7) {
                estatisticasLinhas[1][0]++;
                continue;
            }

            String id = dados[0].trim();
            int duracao = 1; // valor default
            int popularidade = 1; // valor default
            double dancabilidade = 1; // valor default
            double vivacidade = 1; // valor default
            double volumeMedio = 1; // valor default
            boolean letraExplicita = false; // valor default
            boolean informacaoInvalidaNosParametros = false;
            try {
                duracao = Integer.parseInt(dados[1].trim());
                popularidade = Integer.parseInt(dados[3].trim());
                dancabilidade = Double.parseDouble(dados[4].trim());
                vivacidade = Double.parseDouble(dados[5].trim());
                volumeMedio = Double.parseDouble(dados[6].trim());
                letraExplicita = Integer.parseInt(dados[2].trim()) == 1;
            } catch (NumberFormatException e) {
                informacaoInvalidaNosParametros = true;
            }


            if (informacaoInvalidaNosParametros) {
                estatisticasLinhas[1][0]++;
                continue;
            }
            if (hashmusicas.get(id)==null){
                estatisticasLinhas[1][0]++;
                continue;
            }

            if (Integer.parseInt(dados[2].trim()) != 1 && Integer.parseInt(dados[2].trim()) != 0) {
                estatisticasLinhas[1][0]++;
                continue;
            }
            if (Integer.parseInt(dados[3].trim()) < 0 || id.equals("")) {
                estatisticasLinhas[1][0]++;
                continue;
            }


            if (!iddetails.add(id)) {
                estatisticasLinhas[1][0]++;
                continue;
            }

            if (hashmusicas.get(id) == null) {

                detailsSemMusica.add(new Song(new TemaMusical(id, duracao, letraExplicita, popularidade, dancabilidade, vivacidade, volumeMedio)));
            } else {
                hashmusicas.get(id).temaMusical.duracaoTema = duracao;
                hashmusicas.get(id).temaMusical.popularidade = popularidade;
                hashmusicas.get(id).temaMusical.volumeMedio = volumeMedio;
                hashmusicas.get(id).temaMusical.grauDancabilidade = dancabilidade;
                hashmusicas.get(id).temaMusical.grauVivacidade = vivacidade;
            }

            estatisticasLinhas[1][1]++;
                /*De seguida seria necessário guardar o objecto Utilizador numa estrutura de dados apropriada
                 (p.e. array, lista, etc).*/
        }
    }

    public static ArrayList<Song> getSongs() {
        return musicas;
    }

    public static ParseInfo getParseInfo(String fileName) {
        ParseInfo estatisticas = new ParseInfo();

        if ((fileName == null || fileName.length() == 0) || (!fileName.equals("songs.txt") && !fileName.equals("song_artists.txt") && !fileName.equals("song_details.txt"))) {
            return null;
        }
        switch (fileName) {
            case "songs.txt":
                estatisticas.numLinhasOk = estatisticasLinhas[0][1];
                estatisticas.numLinhasIgnoradas = estatisticasLinhas[0][0];
                break;
            case "song_details.txt":
                estatisticas.numLinhasOk = estatisticasLinhas[1][1];
                estatisticas.numLinhasIgnoradas = estatisticasLinhas[1][0];
                break;
            default:
                estatisticas.numLinhasOk = estatisticasLinhas[2][1];
                estatisticas.numLinhasIgnoradas = estatisticasLinhas[2][0];
                break;
        }
        return estatisticas;
    }

    public static String execute(String command) {

        String[] comando = command.split(" ");

        switch (comando[0]) {
            case "COUNT_SONGS_YEAR":
                return Mainfucrions.countSongs(Integer.parseInt(comando[1]));/*ler numero e dizer  total de temas musicais que foram lançados no ano X.*/

            case "COUNT_DUPLICATE_SONGS_YEAR":
                return Mainfucrions.countSongsDup(Integer.parseInt(comando[1]));/* ler numero e dizer  total de temas cujo título está repetido, que foram lançados no ano X*/

            case "GET_ARTISTS_FOR_TAG":
                return Mainfucrions.getArtistForTag(comando[1]);/* a lista de artistas associadas a uma certa tag ordenados alfabeticamente.*/

            case "GET_MOST_DANCEABLE":
                return Mainfucrions.getDanceable(Integer.parseInt(comando[1]), Integer.parseInt(comando[2]), Integer.parseInt(comando[3]));/* String contendo várias linhas separadas por \n e respeitando a seguinte sintaxe:“<Nome tema> : <Ano> : <Dançabilidade>”*/

            case "GET_ARTISTS_ONE_SONG":
                return Mainfucrions.getArtistOneSong(Integer.parseInt(comando[1]), Integer.parseInt(comando[2]));

            case "GET_TOP_ARTISTS_WITH_SONGS_BETWEEN":
                return Mainfucrions.getArtists(Integer.parseInt(comando[1]), Integer.parseInt(comando[2]), Integer.parseInt(comando[3]));

            default:
                return execute2(command);
        }
    }
    public static String execute2(String command){

        String [] comando=command.split(" ");

        switch (comando[0]){
            case "MOST_FREQUENT_WORDS_IN_ARTIST_NAME":
                return Mainfucrions.getCaracter(Integer.parseInt(comando[1]), Integer.parseInt(comando[2]), Integer.parseInt(comando[3]));

            case "GET_UNIQUE_TAGS":
                return Mainfucrions.getUniqueTag();

            case "GET_UNIQUE_TAGS_IN_BETWEEN_YEARS":
                return Mainfucrions.getUniqueTagYear(Integer.parseInt(comando[1]), Integer.parseInt(comando[2]));

            case "ADD_TAGS":
                String[] parametros = Mainfucrions.splitArtistasTags(command);
                return Mainfucrions.addTag(parametros[1], parametros[0]);

            case "REMOVE_TAGS":
                String[] parametros2 = Mainfucrions.splitArtistasTags(command);
                return Mainfucrions.removeTag(parametros2[1], parametros2[0]);

            case "CLEANUP":
                return Mainfucrions.cleanUp();

            case "GET_RISING_STARS":
                return Mainfucrions.getRisingStars(Integer.parseInt(comando[1]), Integer.parseInt(comando[2]), comando[3]);

            default:
                return "Illegal command. Try again";
        }
    }

    public static int getTypeOfSecondParameter() {
        return 0;
    }

    public static String getCreativeQuery() {
        return "por defenir";
    }

    public static String getVideoUrl() {
        return null;
    }

}