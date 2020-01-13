package jpp.addressbook;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CAddressBook implements AddressBook {

    private Set<Contact> contacts = new HashSet<>();
    private PrintStream errorStream = System.err;

    /**
     * Legt den Fehlerausgabestrom fest.
     * Auf diese Fehlerausgabe sollen Fehlermeldungen u. a. bei der Methode importContacts(String) geschrieben werden.
     * Wurde noch kein Fehlerausgabe festgelegt, soll auf System.err geschrieben werden.
     *
     * @param err der Ausgabestrom
     */
    @Override
    public void setErrorStream(PrintStream err) {
        errorStream = err;
    }


    // tet if email is valid
    public boolean validEmail(String str){
        if (str.matches("\\S+@\\S+\\.\\S+")){
            return true;
        }
        return false;
    }

    // test if string is empty
    public boolean isNotEmpty(String str){
        if (str.isEmpty()){
            return false;
        } else {
            return true;
        }

    }


    // test if integer is natrual

    public boolean isNatrual(String str){
        int nr = Integer.parseInt(str);
        if (nr > 0){
            return true;
        } else {
            return false;
        }
    }

    // test if string isNumeric
    public boolean isNumeric(String str){
        for ( char c : str.toCharArray()) {
            if (!Character.isDigit(c)){
                return false;
            }
        }
        return true;
    }

    // test if date is valid
    public boolean isThisDateValid(String dateToValidate, String dateFormat){
        if(dateToValidate == null){
            return false;
        }

        if (!dateToValidate.matches("[0-9][0-9].[0-1][0-9].[0-9][0-9][0-9][0-9]")){
            return false;
        }


        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);

        try {

            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);


        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

            return true;
    }


    /**
     * importiert alle Kontakte
     *
     * @param input String in CSV-Format
     */


    @Override
    public void importContacts(String input) {
        if (input == null){
            throw new NullPointerException("input is empty");
        }
        String[] csvContacts = input.split("\n|\r\n|\r ");
        String[] realContacts = Arrays.copyOfRange(csvContacts, 1, csvContacts.length);
        List<Integer> usedId = new ArrayList<>();
        int index = 0;
        for (String contact : csvContacts) {
            Salutation salutation;
            String firstName;
            String lastName;
            LocalDate birthday;
            String streetAddress;
            int zipCode;
            String city;
            Optional<Long> phone;
            Optional <String> eMail;
            index++;
            String[] contactInput = contact.split(";", -1);
            if(contactInput.length != 10){
                errorStream.println("import error: line " + index + " must contain exactly 10 columns");
                continue;
            }

            //überprüft id
            if (contactInput[0].isEmpty()){
                errorStream.println("import error: invalid value for id empty in line " + index);
                continue;
            }
            if (Integer.parseInt(contactInput[0]) < 1){
                errorStream.println("import error: invalid value for id" + contactInput[0] + "in line "+ index);
                continue;
            }
            int id = Integer.parseInt(contactInput[0]);
            if (usedId.contains(id)){
                errorStream.println("import error: duplicated id" + id + "in line "+ index);
                continue;
            }

            //überprüft salutation
            if (contactInput[1].isEmpty()){
                errorStream.println("import error: invalid value for salutation empty in line "+ index);
                continue;
            }
            if (!contactInput[1].equals("Herr") && !contactInput[1].equals("Frau")){
                errorStream.println("import error: invalid value for salutation " + contactInput[1] + "in line " + index);
                continue;
            }
            if (contactInput[1].equals("Frau")){
                salutation = Salutation.FRAU;
            } else{
                salutation = Salutation.HERR;
            }

            //überprüft first name
            if (contactInput[2].isEmpty()){
                errorStream.println("import error: invalid value for first name empty in line "+ index);
                continue;
            }
            firstName = contactInput[2];

            //überprüft last name
            if (contactInput[3].length() < 1){
                errorStream.println("import error: invalid value for last name empty in line "+ index);
                continue;
            }
            lastName = contactInput[3];

            //überprüft birthdate
            if (contactInput[4].length() < 1) {
                errorStream.println("import error: invalid value for birthday empty in line "+ index);
                continue;
            }
            if (isThisDateValid(contactInput[4], "dd.MM.yyyy")){
                birthday = LocalDate.parse(contactInput[4], DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } else{
                errorStream.println("import error: invalid value for birthday" + contactInput[4] + "in line " + index);
                continue;
            }

            //überprüft street address
            if (contactInput[5].length() < 1){
                errorStream.println("import error: invalid value for street address empty in line "+ index);
                continue;
            }
            streetAddress = contactInput[5];

            // überprüft zipCode
            if (contactInput[6].length() < 1){
                errorStream.println("import error: invalid value for zip code empty in line "+ index);
                continue;
            }
            if (!isNumeric(contactInput[6]) || Integer.parseInt(contactInput[6]) < 1){
                errorStream.println("import error: invalid value for zip code " + contactInput[6] + "in line "+ index);
                continue;
            }
            zipCode = Integer.parseInt(contactInput[6]);

            //überprüft city
            if (contactInput[7].length() < 1){
                errorStream.println("import error: invalid value for city empty in line "+ index);
                continue;
            }
            city = contactInput[7];

            //überprüft phone
            if (contactInput[8].length() == 0){
                phone = Optional.empty();
            }else{
                if (!isNumeric(contactInput[8]) || Long.parseLong(contactInput[8]) < 1){
                errorStream.println("import error: invalid value for phone number " + contactInput[8] + "in line "+ index);
                continue;
                }
            phone = Optional.of(Long.parseLong(contactInput[8]));
            }

            //überprüft email
            if (contactInput[9].isEmpty()){
                eMail = Optional.empty();
            } else {
                if (validEmail(contactInput[9])){

                eMail = Optional.of(contactInput[9]);

                } else {
                    errorStream.println("import error: invalid value for email" + contactInput[9] + "in line " + index);
                    continue;
                }
            }

            Contact contact1 = new CContact(id, salutation, firstName, lastName, birthday, streetAddress, zipCode, city, phone, eMail);
            usedId.add(contact1.getId());
            addContact(contact1);
        }

    }

    /**
     * Fügt den übergebenen Kontakt hinzu.
     * Existiert bereits ein gleicher Kontakt, wird dieser überschrieben.
     *
     * @param contact der Kontakt
     * @throws NullPointerException wenn der übergebene Kontakt null ist
     */
    @Override
    public void addContact(Contact contact) {
        Contact one = null;
        if (contact != null){
            for(Contact temp : contacts){
                if (contact.getId() == temp.getId()){
                    one = temp;
                }
            }
         if (one != null){
             contacts.remove(one);
             contacts.add(contact);
         } else {
             contacts.add(contact);
         }
        } else {
            throw new NullPointerException("contact is null");
        }
    }

    /**
     * @param id
     * @return ein Optional des Kontakts mit der übergebenen ID, sofern er im Addressbuch enthalten ist oder ein leeres Optional, wenn er nicht enthalten ist
     * @throws IllegalArgumentException wenn die id kleiner oder gleich 0 ist
     */
    @Override
    public Optional<Contact> getContact(int id) {
        int count = 0;
        Contact name = null;
        if(id > 0){
            for (Contact temp : contacts){
                if (temp.getId() == id){
                    count++;
                    name = temp;
                }
            }
        }else{
            throw new IllegalArgumentException("id smaller or same as null");
        }
        if(count>0){
            return Optional.of(name);
        }else{
            return Optional.empty();
        }

    }

    /**
     * Gibt alle Kontakte des Addressbuchs zurück.
     *
     * @return eine Menge aller enthaltenen Kontakte oder eine leere Menge, wenn keine Kontakte enthalten sind
     */
    @Override
    public Set<Contact> getContacts() {
        return contacts;
    }

    /**
     * entfernt den Kontakt mit der übergebenen ID
     *
     * @param id
     * @throws IllegalArgumentException wenn der Kontakt nicht im Adressbuch enthalten istd
     */
    @Override
    public void removeContact(int id) {
        Contact one = null;
        for ( Contact temp: contacts){
            if (temp.getId() == id){
                one = temp;
                System.out.println(one);
            }
        }
        System.out.println(one);
        if (one != null){
            contacts.remove(one);
        } else {
            throw new IllegalArgumentException("contact is not in the book");
        }
    }

    /**
     * entfernt den übergebenen Kontakt;
     * genauer: entfernt den Kontakt, der gleich dem übergebenen Kontakt ist
     *
     * @param contact
     * @throws IllegalArgumentException wenn der Kontakt nicht im Adressbuch enthalten ist
     */
    @Override
    public void removeContact(Contact contact) {
        Contact one = null;
        for ( Contact temp: contacts){
            if (temp.equals(contact)){
                one = temp;
                System.out.println(one);
            }
        }
        System.out.println(one);
        if (one != null){
            contacts.remove(one);
        } else {
            throw new IllegalArgumentException("contact is not in the book");
        }
    }

    /**
     * @return gibt die kleinste gültige ID zurück, die nicht von einem Kontakt im Addressbuch verwendet wird
     */
    @Override
    public int getSmallestUnusedId() {
        ArrayList<Integer> ids = new ArrayList<>();
        for (Contact temp : contacts) {
            int id = temp.getId();
            ids.add(id);
        }
        Integer[] aIds = ids.toArray(new Integer[ids.size()]);
        if (ids.size() <1){
            return 1;
        }
        int n  = aIds.length;
        for (int i = 0; i < n; i++) {
            while (aIds[i] != i + 1) {
                if (aIds[i] <= 0 || aIds[i] >= n)
                    break;

                if(aIds[i]==aIds[aIds[i]-1])
                    break;

                int temp = aIds[i];
                aIds[i] = aIds[temp - 1];
                aIds[temp - 1] = temp;
            }
        }

        for (int i = 0; i < n; i++){
            if (aIds[i] != i + 1){
                return i + 1;
            }
        }

        return n + 1;
    }




    /**
     * Exportiert alle Kontakte des Adressbuchs als Zeichenkette, die als Inhalt einer validen CSV-Datei gespeichert werden kann.
     * Dabei sollen die Kontakte aufsteigend nach ID sortiert sein.
     * Ist das Adressbuch leer, soll ein leerer String zurückgegeben werden.
     * Verwenden Sie als Zeilen-Trennzeichen \n.
     *
     * @return alle Kontakte als String in CSV-Format mit aufsteigenden IDs
     */
    @Override
    public String exportAllContacts() {
        List<Integer> list = new ArrayList<>();
        for ( Contact contact : contacts) {
            list.add(contact.getId());
        }
        Collections.sort(list);
        Set<Integer> ids = new TreeSet<>(list);
        String output = "";
        output += exportMultipleContacts(ids);
        return output;
    }

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
    @Override
    public String exportMultipleContacts(Set<Integer> ids) {
        List<Integer> list = new ArrayList(ids);
        Collections.sort(list);
        String output = "";
        int count = 0;
        for (Integer id : list ){
            for (Contact contact : contacts) {
                if (contact.getId() == id){
                    output += exportSingleContact(id);
                    count++;
                    if(count < list.size()){
                        output += "\n";
                    }
                }
            }
        }
        if (count != list.size()){
            throw new IllegalArgumentException("id is not existing");
        } else{

        return output;
        }
    }

    /**
     * @param id
     * @return Kontakt mit übergebener ID als String in CSV-Format
     * @throws IllegalArgumentException wenn für die übergebene ID kein Kontakt im Adressbuch existiert
     */
    @Override
    public String exportSingleContact(int id) {
        Contact one = null;
        for (Contact temp : contacts){
            if (temp.getId() == id){
                one = temp;
            }
        }
        if (one == null){
            throw new IllegalArgumentException("There is no contact with this ID");
        } else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String text = one.getBirthday().format(formatter);
            String output = "";
            output = Integer.toString(one.getId()) + ";" + one.getSalutation().toString() + ";" + one.getFirstName() + ";" + one.getLastName() + ";" + text + ";" + one.getStreetAddress() + ";" + Integer.toString(one.getZipCode()) + ";" + one.getCity() + ";";
            if (one.getPhone().isPresent() == true){
                output += one.getPhone().get() + ";";
            } else{
                output += ";";
            }
            if (one.getEMail().isPresent() == true){
                output +=  one.getEMail().get();
            }
            return output;
        }

    }
}
