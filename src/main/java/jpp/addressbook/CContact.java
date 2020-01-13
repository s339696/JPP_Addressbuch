package jpp.addressbook;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class CContact implements Contact {

    private int id;
    private Salutation salutation;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String streetAddress;
    private int zipCode;
    private String city;
    private Optional<Long> phone;
    private Optional<String> eMail;


    public CContact(int id, Salutation salutation, String firstName, String lastName, LocalDate birthday, String streetAddress, int
            zipCode, String city, Optional<Long> phone, Optional<String> eMail) {
        this.id = id;
        this.salutation = salutation;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.streetAddress = streetAddress;
        this.zipCode = zipCode;
        this.city = city;
        this.phone = phone;
        this.eMail = eMail;
    }

    /**
     * @return die ID des Kontakts
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * @return die Anrede
     */
    @Override
    public Salutation getSalutation() {
        return this.salutation;
    }

    /**
     * @return den Vornamen des Kontakts
     */
    @Override
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * @return den Nachnamen des Kontakts
     */
    @Override
    public String getLastName() {
        return this.lastName;
    }

    /**
     * @return das Geburtsdatum des Kontakts
     */
    @Override
    public LocalDate getBirthday() {
        return this.birthday;
    }

    /**
     * @return die Straße und Hausnummer des Kontakts
     */
    @Override
    public String getStreetAddress() {
        return this.streetAddress;
    }

    /**
     * @return die Postleitzahl des Kontakts
     */
    @Override
    public int getZipCode() {
        return this.zipCode;
    }

    /**
     * @return der Wohnort des Kontakts
     */
    @Override
    public String getCity() {
        return this.city;
    }

    /**
     * @return ein Optional der Telefonnummer des Kontakts oder ein leeres Optional, wenn keine Telefonnumer gesetzt ist
     */
    @Override
    public Optional<Long> getPhone() {

            return this.phone;
    }

    /**
     * @return ein Optional der E-Mail-Adresse des Kontakts oder ein leeres Optional, wenn keine E-Mail-Adresse gesetzt ist
     */
    @Override
    public Optional<String> getEMail() {
            return this.eMail;
    }

    /**
     * setzt die Anrede des Kontakts
     *
     * @param salutation
     * @throws NullPointerException wenn die übergebene Anrede null ist
     */
    @Override
    public void setSalutation(Salutation salutation) {
        if (salutation == null) {
            throw new NullPointerException("salutation is null");
        } else {
            this.salutation = salutation;
        }
    }

    /**
     * setzt den Vornamen des Kontakts
     *
     * @param firstName
     * @throws NullPointerException wenn der übergebene Vorname null ist
     */
    @Override
    public void setFirstName(String firstName) {
        if (firstName == null) {
            throw new NullPointerException("firstName is null");
        } else {
            this.firstName = firstName;
        }
    }


    /**
     * setzt den Vornamen des Kontakts
     *
     * @param lastName
     * @throws NullPointerException wenn der übergebene Nachname null ist
     */
    @Override
    public void setLastName(String lastName) {
        if (lastName == null) {
            throw new NullPointerException("lastName is null");
        } else {
            this.lastName = lastName;
        }

    }

    /**
     * setzt das Geburtsdatum des Kontakts
     *
     * @param birthday
     * @throws NullPointerException     wenn das übergebene Geburtsdatum null ist
     * @throws IllegalArgumentException wenn das übergebene Geburtsdatum in der Zukunft liegt
     */
    @Override
    public void setBirthday(LocalDate birthday) {
        if (birthday == null) {
            throw new NullPointerException("birthday is null");
        } else {
            this.birthday = birthday;
        }
    }


    /**
     * setzt Straße und Hausnummer des Kontakts
     *
     * @param streetAddress
     * @throws NullPointerException wenn die übergebene Straße und Hausnummer null ist
     */
    @Override
    public void setStreetAddress(String streetAddress) {
        if (streetAddress == null) {
            throw new NullPointerException("streetAddress is null");
        } else {
            this.streetAddress = streetAddress;
            //setStreetAddress(streetAddress);
        }

    }

    /**
     * setzt die Postleitzahl des Kontakts
     *
     * @param zipCode
     * @throws NullPointerException wenn die übergebene Anrede null ist
     */
    @Override
    public void setZipCode(int zipCode) {
        if (zipCode == 0) {
            throw new NullPointerException("zipCode is null");
        } else {
            this.zipCode = zipCode;
        }

    }

    /**
     * setzt den Wohnort des Kontakts
     *
     * @param city
     * @throws NullPointerException wenn der übergebene Wohnort null ist
     */
    @Override
    public void setCity(String city) {
        if (city == null) {
            throw new NullPointerException("city is null");
        } else {
            this.city = city;
        }
    }

    /**
     * setzt die Telefonnumer des Kontakts
     *
     * @param phone
     */
    @Override
    public void setPhone(Long phone) {
            if (phone.equals(0L)){
                this.phone = Optional.empty();
            } else{
                this.phone = Optional.of(phone);
            }
    }

    /**
     * setzt die E-Mail-Adresse des Kontakts
     *
     * @param mail
     */
    @Override
    public void setEMail(String mail) {
        if (mail == null){
            this.eMail = Optional.empty();
        } else{
            this.eMail = Optional.of(mail);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        Contact other = null;
        if (obj.getClass() == this.getClass())
            other = (Contact) obj;
        else
            return false;
        if (this.getId() == other.getId())
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.id + ", " + this.firstName + ", " + this.lastName;

    }

}
