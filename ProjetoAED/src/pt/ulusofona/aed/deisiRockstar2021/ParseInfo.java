package pt.ulusofona.aed.deisiRockstar2021;

public class ParseInfo {
    int numLinhasOk;
    int numLinhasIgnoradas;

    ParseInfo(){}
    public ParseInfo(int numLinhasOk, int numLinhasIgnoradas) {
        this.numLinhasOk = numLinhasOk;
        this.numLinhasIgnoradas = numLinhasIgnoradas;
    }

    @Override
    public String toString() {
        return "OK: "+ numLinhasOk + ", "+ "Ignored: "+ numLinhasIgnoradas;
    }
}