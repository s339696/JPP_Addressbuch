package jpp.addressbook;


import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CAddressBookUtil implements AddressBookUtil {


    private AddressBook addressBook;

    public CAddressBookUtil(AddressBook addressBook){
        this.addressBook = addressBook;
    }
    /**
     * Gibt das durchschnittliche Alter aller Kontakte zu dem übergebenen Referenzdatum zurück.
     * Dabei wird für jeden Kontakt das Alter als natürliche Zahl berechnet, anschließend wird der Durchschnitt (arithmetisches Mittel) gebildet.
     *
     * @param reference
     * @return das durchschnittliche Alter
     * @throws IllegalStateException    wenn kein Kontakt im Adressbuch gespeichert ist
     * @throws IllegalArgumentException wenn das übergebene Datum früher als das Geburtsdatum des jüngsten im Adressbuch enthaltenen Kontakts ist
     */
    @Override
    public double getAverageAgeAt(LocalDate reference) {
        if (addressBook.getContacts().isEmpty()){
            throw new IllegalStateException("There is no contact in addressBook");
        }
        double age = 0;
        LocalDate minAge = null;
        int count = 0;
        for ( Contact contact : addressBook.getContacts()) {
                count++;
                if (count == 1){
                    minAge = contact.getBirthday();
                } else{
                    if (contact.getBirthday().isAfter(minAge)){
                        minAge = contact.getBirthday();
                    }
                }

                age += Period.between(contact.getBirthday(), reference).getYears();

        }
        if (minAge.isAfter(reference)){
            throw new IllegalArgumentException("thats impossible");
        } else{
            return age / addressBook.getContacts().size();
        }

    }

    /**
     * Gibt den Anteil der Kontakte des Adressbuchs mit der übergebenen Anrede zurück.
     *
     * @param salutation
     * @return der Anteil aller Kontakte mit der übergebenen Anrede
     * @throws IllegalStateException wenn kein Kontakt im Adressbuch gespeichert ist
     */
    @Override
    public double getSalutationShare(Salutation salutation) {
        if (addressBook.getContacts().isEmpty()){
            throw new IllegalStateException("addressBook is empty");
        } else{

        double count =0;
        for (Contact contact : addressBook.getContacts()){
            if (salutation.equals(contact.getSalutation())){
                count++;
            }
        }
        double stake = count/ addressBook.getContacts().size();
        return stake;
        }
    }

    /**
     * Gibt eine Map zurück, die die Anteile der E-Mail-Provider der Kontakte repräsentiert. Als Provider wird der String verstanden, der bei einer
     * E-Mail-Adresse nach dem @ steht, also z. B. "pudlmail.com".
     * <p>
     * Beinhaltet ein Adressbuch also 5 Kontakte, wobei 3 Kontakte eine E-Mail-Adresse bei pudlmail.com und 2 Kontakte bei hotmeel.com haben, sollen die
     * Schlüssel-Wert-Paare der zurückgegebenen Map wie folgt sein:
     * <p>
     * Schlüssel: Wert
     * pudlmail.com: 0.6
     * hotmeel.com: 0.4
     * <p>
     * Kontakte, die keine E-Mail-Adresse haben, sollen so behandelt werden, als wäre der Provider ein leerer String, der ebenfalls in der
     * zurückgegebenen Map enthalten sein soll. Daher ist die Summe aller Werte der Map stets gleich 1.
     *
     * @return eine Map mit den Providern als Schlüssel und den jeweiligen Anteilen als Werten
     * @throws IllegalStateException wenn kein Kontakt im Adressbuch gespeichert ist
     */
    @Override
    public Map<String, Double> getMailProviderShare() {
        if (addressBook.getContacts().isEmpty()){
            throw new IllegalStateException("address Book is empty");
        }
        int empty = 0;
        Map<String, Integer> providerCount = new HashMap<>();
        for (Contact contact: addressBook.getContacts()) {
            if (!contact.getEMail().isPresent()){
                empty++;
                providerCount.put("", empty);

            }else{
                String[] provider = contact.getEMail().get().split("@");
                int foundValue = 0;
                String foundKey = null;
                for (Map.Entry<String, Integer> pairEntry : providerCount.entrySet()){
                    if (pairEntry.getKey().equals(provider[1])){
                        foundKey = pairEntry.getKey();
                        foundValue = pairEntry.getValue();
                        break;
                    }
                }
                if (foundValue == 0){
                    providerCount.put(provider[1], 1);
                } else{
                    providerCount.put(foundKey, foundValue + 1);
                }
            }
        }
        System.out.println(providerCount);
        Map<String, Double> providerValue = new HashMap<>();
        double value = 0;
        for (Map.Entry<String, Integer> pairEntry : providerCount.entrySet()){
            value = (double) pairEntry.getValue()/ (double) addressBook.getContacts().size();
            System.out.println(value);
            providerValue.put(pairEntry.getKey(), value);
        }

        return providerValue;
    }

    /**
     * @param prefix
     * @return ein Prädikat, das einen Kontakt genau dann akzeptiert, wenn dessen Nachname den übergebenen Präfix hat; true, falls der String leer ist
     * @throws NullPointerException wenn der übergebene Präfix null ist
     */
    @Override
    public Predicate<Contact> lastNamePrefixFilter(String prefix) {
        if (prefix == null){
            throw new NullPointerException("prefix is  empty");
        }

        return p -> p.getLastName().startsWith(prefix);
    }

    /**
     * @param year
     * @return ein Prädikat, das einen Kontakt genau dann akzeptiert, wenn er das übergebene Geburtsdatum hat
     */
    @Override
    public Predicate<Contact> birthYearFilter(Year year) {
        return p -> p.getBirthday().getYear() == year.getValue();
    }

    /**
     * @param zipCode
     * @return ein Prädikat, das einen Kontakt genau dann akzeptiert, wenn er die übergebene Postleitzahl hat
     * @throws IllegalArgumentException wenn die übergebene Postleitzahl keine natürliche Zahl (ohne 0) ist
     */
    @Override
    public Predicate<Contact> zipCodeFilter(int zipCode) {
        if (zipCode < 1){
            throw new IllegalArgumentException("zip Code is not natural");
        }
        return p -> p.getZipCode() == zipCode;
    }

    /**
     * @param infix
     * @return ein Prädikat, das einen Kontakt genau dann akzeptiert, wenn dessen E-Mail-Adresse den übergebenen Infix hat
     * @throws NullPointerException wenn der übergebene Infix null ist
     */
    @Override
    public Predicate<Contact> eMailInfixFilter(String infix) {
        if (infix == null){
            throw new NullPointerException("infix is null");
        }
        Predicate<Contact> predicate = p -> {

            if (p.getEMail().isPresent()){
            return p.getEMail().get().contains(infix);
        } else{
                return infix.isEmpty();
            }
        };
    return predicate;
    }

    /**
     * @param filter
     * @return eine Menge aller Kontakte des aktuell gesetzten Adressbuchs, die vom übergebenen Prädikat akzeptiert werden
     */
    @Override
    public Set<Contact> filter(Predicate<Contact> filter) {
        Set<Contact> contacts = new HashSet<>();
        for (Contact contact : addressBook.getContacts()) {
            if (filter.test(contact)){
                contacts.add(contact);
            }
        }

        return contacts;
    }

    /**
     * @param filters
     * @return eine Menge aller Kontakte des aktuell gesetzten Adressbuchs, die von allen übergebenen Prädikaten akzeptiert werden; wird eine leere Menge
     * übergeben, sollen alle Kontakte akzeptiert werden
     */
    @Override
    public Set<Contact> filterAnd(Set<Predicate<Contact>> filters) {
        if (filters.isEmpty()){
            return addressBook.getContacts();
        }
        Predicate<Contact> first = p -> true;
        Set<Contact> contacts = new HashSet<>();
        for ( Predicate<Contact> contactPredicate: filters) {
            first = first.and(contactPredicate);
        }
        for (Contact contact : addressBook.getContacts()) {
            if (first.test(contact)){
                contacts.add(contact);
            }
        }

        return contacts;
    }

    /**
     * @param filters
     * @return eine Menge aller Kontakte des aktuell gesetzten Adressbuchs, die von mindestens einem der übergebenen Prädikate akzeptiert werden; wird eine
     * leere Menge übergeben, sollen alle Kontakte akzeptiert werden
     */
    @Override
    public Set<Contact> filterOr(Set<Predicate<Contact>> filters) {
        if (filters.isEmpty()){
            return addressBook.getContacts();
       }
//        Predicate<Contact> first = p -> false;
//        Set<Contact> contacts = new HashSet<>();
//        for ( Predicate<Contact> contactPredicate: filters) {
//            first = first.or(contactPredicate);
//        }
//        for (Contact contact : addressBook.getContacts()) {
//            if (first.test(contact)){
//                contacts.add(contact);
//            }
//        }

        return  addressBook.getContacts().stream()
                .filter(filters.stream().reduce(x-> false, Predicate :: or))
                .collect(Collectors.toSet());




    }
}
