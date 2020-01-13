package jpp.addressbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ibjbh {


    public static void main(String[] args) throws FileNotFoundException {
        CAddressBook addressBook = new CAddressBook();
        CAddressBookUtil addressBookUtil = new CAddressBookUtil(addressBook);

       // addressBook.addContact(new CContact(2, Salutation.FRAU, "Milena", "Schweizer", LocalDate.now(), "Hoove 127", 84032, "Landshut", 87138315030L, "milena.schweizer@hotmeel.com"));
//
//        for (Contact con :
//                addressBook.getContacts()) {
//            System.out.println(con.getId());
//        }
//
//        addressBook.removeContact(2);
//        System.out.println(addressBook.getContacts());

        String str = "1;Herr;Muhammed;Döring;07.07.1923;Gleueler Str. 56;61250;Usingen;;\n" +
                "2;Frau;Milena;Schweizer;06.02.1994;Hoove 127;84032;Landshut;87138315030;milena.schweizer@hotmeel.com\n" +
                "3;Herr;Erik;Schäfer;11.02.1973;Oscar-Wilde-Str. 189;24860;Klappholz;46238105692;\n" +
                "4;Frau;Tabea;Carmen;03.09.1953;Erpeler Str. 57;73098;Rechberghausen;716130791825;tabea.carmen@bmx.de\n" +
                "5;Herr;Ian;Jacobs;17.02.1977;Teutonenstr. 126;52072;Aachen;24123269738;ian.jacobs@hotmeel.com";

        PrintStream printStream = new PrintStream("C:\\Users\\Bastian\\Documents\\fehler.txt");

        addressBook.setErrorStream(printStream);
//
//        try {
//            String input = new String(String.valueOf(Files.readAllLines(Paths.get("address_data_3000.csv"))));
//            System.out.println(input);
//            addressBook.importContacts(input);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        String input = "";
        try {
            Path path = Paths.get("address_data_3000.csv");
            BufferedReader rd = Files.newBufferedReader(path, UTF_8);
            String line = rd.readLine();
            while(line != null ){
                input += "\n" + line;
                line = rd.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        addressBook.importContacts(input);

        for (Contact contact: addressBook.getContacts()) {
            System.out.println(contact.getCity());
        }

        System.out.println(addressBook.getContacts());
    }

    }



//    String [] csvContacts = input.split("\n");
//        for ( String contact : csvContacts) {
//        String [] contactInput = contact.split(";");
//        if (contactInput.length <= 10){
//            if (Integer.parseInt(contactInput[0]) > 0){
//                Contact user = new CContact(Integer.parseInt(contactInput[0]), null, null,null,null,null,0, null, (long) 0, null);
//                for (Contact temp : contacts){
//                    if (user.getId() == temp.getId()){
//
//                    }
//                }
//                if (!contactInput[1].isEmpty()) {
//                    if (contactInput[1] == "Frau"){
//                        user.setSalutation(Salutation.FRAU);
//                    }else{
//                        user.setSalutation(Salutation.HERR);
//                    }
//                    if (!contactInput[2].isEmpty()){
//                        user.setFirstName(contactInput[2]);
//                        if (!contactInput[3].isEmpty()){
//                            user.setLastName(contactInput[3]);
//                            Date date = null;
//                            try {
//                                SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
//                                date = sdf.parse(contactInput[4]);
//                                if (!contactInput[4].equals(sdf.format(date))) {
//                                    date = null;
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            if (date != null) {
//                                user.setBirthday(LocalDate.parse(contactInput[4]));
//                                if(!contactInput[5].isEmpty()){
//                                    user.setStreetAddress(contactInput[5]);
//                                    if (Integer.parseInt(contactInput[6]) > 0){
//                                        user.setZipCode(Integer.parseInt(contactInput[6]));
//                                        if (!contactInput[7].isEmpty()){
//                                            user.setCity(contactInput[7]);
//                                            if (!contactInput[8].isEmpty() && Long.parseLong(contactInput[7]) > 0){
//                                                user.setPhone(Long.parseLong(contactInput[8]));
//                                                if (!contactInput[9].isEmpty()){
//                                                    user.setEMail(contactInput[9]);
//                                                    contacts.add(user);
//                                                }else{
//                                                    user.setEMail(null);
//                                                    contacts.add(user);
//                                                }
//
//                                            } else if (contactInput[8].isEmpty()){
//                                                user.setPhone((long) 0);
//                                                if (!contactInput[9].isEmpty()){
//                                                    user.setEMail(contactInput[9]);
//                                                    contacts.add(user);
//                                                }else{
//                                                    user.setEMail(null);
//                                                    contacts.add(user);
//                                                }
//
//                                            } else{
//                                                // Invalid phonenumber
//                                            }
//
//
//                                        } else {
//                                            // Invalid city entry
//                                        }
//
//                                    } else{
//                                        // Invalid zip code
//                                    }
//
//
//                                } else{
//                                    // Invalid streetAddress
//                                }
//
//                            } else {
//                                // Invalid date format
//                            }
//
//
//                        } else{
//                            // invalid last name
//                        }
//
//
//                    }else{
//                        // invalid firstname
//                    }
//
//                } else{
//                    // invalid Salutation
//                }
//
//
//            } else{
//                // invalid id
//            }
//        }else{
//            // invalid entries
//
//        }
//
//
//
//    }
//
//
//}


