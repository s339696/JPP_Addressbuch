package jpp.addressbook;

import java.io.PrintStream;
import java.util.Optional;
import java.util.Set;

public interface AddressBook {

    /**
     * Legt den Fehlerausgabestrom fest.
     * Auf diese Fehlerausgabe sollen Fehlermeldungen u. a. bei der Methode importContacts(String) geschrieben werden.
     * Wurde noch kein Fehlerausgabe festgelegt, soll auf System.err geschrieben werden.
     *
     * @param err der Ausgabestrom
     */
    void setErrorStream(PrintStream err);

    /**
     * importiert alle Kontakte
     *
     * @param input String in CSV-Format
     */
    void importContacts(String input);

    /**
     * Fügt den übergebenen Kontakt hinzu.
     * Existiert bereits ein gleicher Kontakt, wird dieser überschrieben.
     *
     * @param contact der Kontakt
     * @throws NullPointerException wenn der übergebene Kontakt null ist
     */
    void addContact(Contact contact);

    /**
     * @param id
     * @return ein Optional des Kontakts mit der übergebenen ID, sofern er im Addressbuch enthalten ist oder ein leeres Optional, wenn er nicht enthalten ist
     * @throws IllegalArgumentException wenn die id kleiner oder gleich 0 ist
     */
    Optional<Contact> getContact(int id);

    /**
     * Gibt alle Kontakte des Addressbuchs zurück.
     *
     * @return eine Menge aller enthaltenen Kontakte oder eine leere Menge, wenn keine Kontakte enthalten sind
     */
    Set<Contact> getContacts();

    /**
     * entfernt den Kontakt mit der übergebenen ID
     *
     * @param id
     * @throws IllegalArgumentException wenn der Kontakt nicht im Adressbuch enthalten istd
     */
    void removeContact(int id);

    /**
     * entfernt den übergebenen Kontakt;
     * genauer: entfernt den Kontakt, der gleich dem übergebenen Kontakt ist
     *
     * @param contact
     * @throws IllegalArgumentException wenn der Kontakt nicht im Adressbuch enthalten ist
     */
    void removeContact(Contact contact);

    /**
     * @return gibt die kleinste gültige ID zurück, die nicht von einem Kontakt im Addressbuch verwendet wird
     */
    int getSmallestUnusedId();

    /**
     * Exportiert alle Kontakte des Adressbuchs als Zeichenkette, die als Inhalt einer validen CSV-Datei gespeichert werden kann.
     * Dabei sollen die Kontakte aufsteigend nach ID sortiert sein.
     * Ist das Adressbuch leer, soll ein leerer String zurückgegeben werden.
     * Verwenden Sie als Zeilen-Trennzeichen \n.
     *
     * @return alle Kontakte als String in CSV-Format mit aufsteigenden IDs
     */
    String exportAllContacts();

    /**
     * Exportiert alle Kontakte mit den übergebenen IDs als Zeichenkette, die als Inhalt einer validen CSV-Datei gespeichert werden kann.
     * Dabei sollen die Kontakte aufsteigend nach ID sortiert sein.
     * Ist übergebene Menge leer, soll ein leerer String zurückgegeben werden.
     * Verwenden Sie als Zeilen-Trennzeichen \n.
     *
     * @param ids
     * @return Kontakte mit übergebenen IDs als String in CSV-Format mit aufsteigenden IDs
     * @throws IllegalArgumentException wenn für mindestens eine der übergebenen IDs kein Kontakt im Adressbuch existiert
     */
    String exportMultipleContacts(Set<Integer> ids);

    /**
     *
     * @return Kontakt mit übergebener ID als String in CSV-Format
     * @throws IllegalArgumentException wenn für die übergebene ID kein Kontakt im Adressbuch existiert
     */
    String exportSingleContact(int id);
}
