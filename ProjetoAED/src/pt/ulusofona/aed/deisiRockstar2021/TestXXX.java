package pt.ulusofona.aed.deisiRockstar2021;

import java.util.ArrayList;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;



public class TestXXX {
    @BeforeClass
    public static void beforeClass()throws Exception{
        Main.loadFiles();
    }

    @Test
    public void teste1(){
        ArrayList<Song> musicas;
        musicas=Main.getSongs();

        if (!musicas.isEmpty()){
            assertEquals("0nxvFG50rGXkiGQqOO2MHr | Be Alright | 2012", musicas.get(6).toString());
        }
    }

    @Test
    public void teste2(){

        ArrayList<Song> musicas;
        musicas=Main.getSongs();

        if (!musicas.isEmpty()){
            assertEquals("17ZnveSDBpG9QtL7zLJNPy | Only For You | 2012", musicas.get(0).toString());
        }
    }
}