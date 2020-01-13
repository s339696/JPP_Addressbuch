package jpp.addressbook;

public enum Salutation {

    HERR, FRAU;



    @Override
    public String toString(){
        if (this == Salutation.FRAU) {
            return "Frau";
        } else {
            return "Herr";
        }
    }
}
