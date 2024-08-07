package org.example;

import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Archivio {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Archivio.class);
    private static List<Catlogo> catalogo;
    static private File file = new File("./persistense/file.txt");
    Logger loger = Logger.getLogger("logger1");
    public Archivio() {
        this.catalogo = new ArrayList<>();
    }

    public void add(Catlogo... c) {
       // Arrays.stream(c).forEach(System.out::println);
        // Set di tutti gli ISBN presenti nel catalogo
        Set<String> isbnSet = catalogo.stream()
                .map(Catlogo::getISBN)
                .collect(Collectors.toSet());
        // Lista dei nuovi cataloghi da aggiungere, controllo se l'ISBN è già presente nel catalogo e lo aggiungo alla lista
        List<Catlogo> nuoviCataloghi = Arrays.stream(c)
                .filter(cat -> !isbnSet.contains(cat.getISBN()))
                .collect(Collectors.toList());
       // loger.info("nuoviCataloghi: " + nuoviCataloghi);
        catalogo.addAll(nuoviCataloghi);
        loger.info("Aggiunti:" + nuoviCataloghi.size());
        System.out.println("\nAggiunti:"+nuoviCataloghi.size());
        nuoviCataloghi.forEach(System.out::println);
    }

    public void remove(Catlogo Catlogo) {
        catalogo.remove(Catlogo);
    }

    public List<Catlogo> getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(List<Catlogo> catalogo) {
        this.catalogo = catalogo;
    }

    //cancella per Isbn
    public void cancellaByIsdn(String isbn) {
        /*catalogo.removeIf(c -> c.getISBN().equals(isbn));*/
        catalogo.removeAll(cercaIsbn(isbn));
        System.out.println("\n isbn" + isbn + " canecllato");
       // catalogo.forEach(System.out::println);
    }

    //ricerca per Isbn
    public List<Catlogo> cercaIsbn(String isbn) {

        List<Catlogo> cercati = catalogo.stream().filter(c -> c.getISBN().equals(isbn)).toList();
        System.out.println("\n isbn cercato:" + isbn);
        //cercati.forEach(System.out::println);
        if (cercati.size() == 0)
            loger.info("isbn" + isbn + " non trovato" );

        return cercati;
    }


    //ricerca per anno di publicazione
    public void cercaAnnoPub(int anno) {
        System.out.println("\n  cercato per anno:" + anno);
        List<Catlogo> t = catalogo.stream().filter(c -> c.getPublicationDate().getYear() == anno).toList();
        t.forEach(System.out::println);
        if (t.size() == 0)
            loger.info("anno" + anno + " non trovato" );
    }

    //ricerca per autore
    public void cercaAutore(String autore) {
        System.out.println("\ni libri dell'autore trovato");
        /*List<Catlogo> aut = catalogo.stream().filter(c -> c instanceof Libro && ((Libro) c).getAutore().toLowerCase().equals(autore.toLowerCase())).toList();*/
        /* aut.forEach(System.out::println);*/
        List<Catlogo> aut = catalogo.stream().filter(c -> c instanceof Libro && ((Libro) c).getAutore().toLowerCase().contains(autore.toLowerCase())).toList();
        aut.forEach(System.out::println);

        /* catalogo.stream().filter(c -> c instanceof Libro).filter(c -> ((Libro) c).getAutore().equals(autore)).forEach(System.out::println);*/
    }

    //salva su disco
    public void save() throws ArchivioException {
        // Concatena tutti gli elementi del catalogo in una stringa separata da #
        String strFile = catalogo.stream()
                .map(Object::toString).collect(Collectors.joining("#"));
        try {
            FileUtils.writeStringToFile(this.file, strFile, Charset.defaultCharset(), false);
            System.out.println("\nFile salvato correttamente.");
            loger.info("File salvato correttamente.");
        } catch (IOException e) {
           // e.printStackTrace();
            throw new ArchivioException("Errore nel salvataggio del file: " + e.getMessage());
        }
    }

    //carica da disco
    public List<Catlogo> carica() throws ArchivioException {
        List<Catlogo> catal = new ArrayList<>();
        try {
            if (!this.file.exists()) {
                throw new NoSuchFileException(this.file.getAbsolutePath());
            }
            System.out.println("\nFile caricato.");
            String str = FileUtils.readFileToString(this.file, Charset.defaultCharset());
            catal = Arrays.stream(str.split("#"))
                    .map(s -> s.split("@", 0))
                    .map(split -> {
                            String n = split[0];
                        if (n.length()==13) { // ISBN 13 caratteri
                            switch (n.charAt(11)) {
                                case '1': // Carica il libro
                                    return new Libro(split[0], split[1], LocalDate.parse(split[2]), Integer.parseInt(split[3]), split[4], split[5]);
                                case '2':
                                    return new Riviste(split[0], split[1], LocalDate.parse(split[2]), Integer.parseInt(split[3]), Periodicita.valueOf(split[4]));
                                default:
                                    throw new IllegalArgumentException("Caso non valido: " + n.charAt(11));
                            }
                        } else {
                            loger.info("Formato del file non valido: mancano elementi dopo lo split");
                            return null; // Ritorna null in caso di formato del file non valido
                        }
                    })
                    .collect(Collectors.toList());
        } catch (NoSuchFileException e) {
            e.printStackTrace();
            throw new ArchivioException("File non trovato: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ArchivioException("Errore durante il caricamento del file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new ArchivioException("Errore durante il caricamento del file: " + e.getMessage());
        }
        finally {
            loger.info("caricamento completato.");
        return catal;
        }
    }

    @Override
    public String toString() {
        return "Archivio{" +
                "catalogo=" + catalogo +
                '}';
    }
}

