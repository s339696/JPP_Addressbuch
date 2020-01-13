package jpp.addressbook;


import java.time.LocalDate;
import java.util.Optional;

/**
 * Represents a single contact.
 *
 * @author ifland
 */
public interface Contact {

    /**
     *
     * @return die ID des Kontakts
     */
    int getId();

    /**
     *
     * @return die Anrede
     */
    Salutation getSalutation();

    /**
     *
     * @return den Vornamen des Kontakts
     */
    String getFirstName();

    /**
     *
     * @return den Nachnamen des Kontakts
     */
    String getLastName();

    /**
     *
     * @return das Geburtsdatum des Kontakts
     */
    LocalDate getBirthday();

    /**
     *
     * @return die Straße und Hausnummer des Kontakts
     */
    String getStreetAddress();

    /**
     *
     * @return die Postleitzahl des Kontakts
     */
    int getZipCode();

    /**
     *
     * @return der Wohnort des Kontakts
     */
    String getCity();

    /**
     *
     * @return ein Optional der Telefonnummer des Kontakts oder ein leeres Optional, wenn keine Telefonnumer gesetzt ist
     */
    Optional<Long> getPhone();

    /**
     *
     * @return ein Optional der E-Mail-Adresse des Kontakts oder ein leeres Optional, wenn keine E-Mail-Adresse gesetzt ist
     */
    Optional<String> getEMail();

    /**
     * setzt die Anrede des Kontakts
     *
     * @param salutation
     * @throws NullPointerException wenn die übergebene Anrede null ist
     */
    void setSalutation(Salutation salutation);

    /**
     * setzt den Vornamen des Kontakts
     *
     * @param firstName
     * @throws NullPointerException wenn der übergebene Vorname null ist
     */
    void setFirstName(String firstName);

    /**
     * setzt den Vornamen des Kontakts
     *
     * @param lastName
     * @throws NullPointerException wenn der übergebene Nachname null ist
     */
    void setLastName(String lastName);

    /**
     * setzt das Geburtsdatum des Kontakts
     *
     * @param birthday
     * @throws NullPointerException wenn das übergebene Geburtsdatum null ist
     * @throws IllegalArgumentException wenn das übergebene Geburtsdatum in der Zukunft liegt
     */
    void setBirthday(LocalDate birthday);

    /**
     * setzt Straße und Hausnummer des Kontakts
     *
     * @param streetAddress
     * @throws NullPointerException wenn die übergebene Straße und Hausnummer null ist
     */
    void setStreetAddress(String streetAddress);

    /**
     * setzt die Postleitzahl des Kontakts
     *
     * @param zipCode
     */
    void setZipCode(int zipCode);

    /**
     * setzt den Wohnort des Kontakts
     *
     * @param city
     * @throws NullPointerException wenn der übergebene Wohnort null ist
     */
    void setCity(String city);

    /**
     * setzt die Telefonnumer des Kontakts
     *
     * @param phone
     */
    void setPhone(Long phone);

    /**
     * setzt die E-Mail-Adresse des Kontakts
     *
     * @param mail
     */
    void setEMail(String mail);
}
