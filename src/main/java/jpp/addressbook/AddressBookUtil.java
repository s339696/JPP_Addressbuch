package jpp.addressbook;

import java.time.LocalDate;
import java.time.Year;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public interface AddressBookUtil {

    /**
     * Gibt das durchschnittliche Alter aller Kontakte zu dem übergebenen Referenzdatum zurück.
     * Dabei wird für jeden Kontakt das Alter als natürliche Zahl berechnet, anschließend wird der Durchschnitt (arithmetisches Mittel) gebildet.
     *
     * @param reference
     * @return das durchschnittliche Alter
     * @throws IllegalStateException    wenn kein Kontakt im Adressbuch gespeichert ist
     * @throws IllegalArgumentException wenn das übergebene Datum früher als das Geburtsdatum des jüngsten im Adressbuch enthaltenen Kontakts ist
     */
    double getAverageAgeAt(LocalDate reference);

    /**
     * Gibt den Anteil der Kontakte des Adressbuchs mit der übergebenen Anrede zurück.
     *
     * @param salutation
     * @return der Anteil aller Kontakte mit der übergebenen Anrede
     * @throws IllegalStateException wenn kein Kontakt im Adressbuch gespeichert ist
     */
    double getSalutationShare(Salutation salutation);

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
    Map<String, Double> getMailProviderShare();

    /**
     * @param prefix
     * @return ein Prädikat, das einen Kontakt genau dann akzeptiert, wenn dessen Nachname den übergebenen Präfix hat; true, falls der String leer ist
     * @throws NullPointerException wenn der übergebene Präfix null ist
     */
    Predicate<Contact> lastNamePrefixFilter(String prefix);

    /**
     * @param year
     * @return ein Prädikat, das einen Kontakt genau dann akzeptiert, wenn er das übergebene Geburtsdatum hat
     */
    Predicate<Contact> birthYearFilter(Year year);

    /**
     * @param zipCode
     * @return ein Prädikat, das einen Kontakt genau dann akzeptiert, wenn er die übergebene Postleitzahl hat
     * @throws IllegalArgumentException wenn die übergebene Postleitzahl keine natürliche Zahl (ohne 0) ist
     */
    Predicate<Contact> zipCodeFilter(int zipCode);

    /**
     * @param infix
     * @return ein Prädikat, das einen Kontakt genau dann akzeptiert, wenn dessen E-Mail-Adresse den übergebenen Infix hat
     * @throws NullPointerException wenn der übergebene Infix null ist
     */
    Predicate<Contact> eMailInfixFilter(String infix);

    /**
     * @param filter
     * @return eine Menge aller Kontakte des aktuell gesetzten Adressbuchs, die vom übergebenen Prädikat akzeptiert werden
     */
    Set<Contact> filter(Predicate<Contact> filter);

    /**
     * @param filters
     * @return eine Menge aller Kontakte des aktuell gesetzten Adressbuchs, die von allen übergebenen Prädikaten akzeptiert werden; wird eine leere Menge
     * übergeben, sollen alle Kontakte akzeptiert werden
     */
    Set<Contact> filterAnd(Set<Predicate<Contact>> filters);

    /**
     * @param filters
     * @return eine Menge aller Kontakte des aktuell gesetzten Adressbuchs, die von mindestens einem der übergebenen Prädikate akzeptiert werden; wird eine
     * leere Menge übergeben, sollen alle Kontakte akzeptiert werden
     */
    Set<Contact> filterOr(Set<Predicate<Contact>> filters);
}