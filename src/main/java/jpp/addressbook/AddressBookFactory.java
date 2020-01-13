package jpp.addressbook;

import java.time.LocalDate;
import java.util.Optional;

public class AddressBookFactory {

    public static Contact createContact(int id, Salutation salutation, String firstName, String lastName, LocalDate birthday, String streetAddress, int
            zipCode, String city, Long phone, String eMail) {
        return  new CContact(id, salutation, firstName, lastName, birthday, streetAddress, zipCode, city, Optional.of(phone),Optional.of(eMail));
    }

    public static AddressBook createAddressBook() {
        return new CAddressBook();
    }

    public static AddressBookUtil createAddressBookUtil(AddressBook addressBook) {
        return new CAddressBookUtil(addressBook);
    }
}
