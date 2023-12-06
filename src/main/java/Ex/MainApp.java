package Ex;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MainApp {

    public static void scriere(List<Angajat> lista) {
        try {
            ObjectMapper mapper=new ObjectMapper();

            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            File file=new File("src/main/resources/angajati.json");
            mapper.writeValue(file,lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<Angajat> citire() {
        try {
            File file=new File("src/main/resources/angajati.json");
            ObjectMapper mapper=new ObjectMapper();

            mapper.registerModule(new JavaTimeModule());

            List<Angajat> angajati = mapper
                    .readValue(file, new TypeReference<List<Angajat>>(){});
            return angajati;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {

        List<Angajat> lista_angajati=new ArrayList<>();
        int opt;

        Scanner my_scanner=new Scanner(System.in);

        while(true)
        {
            System.out.println("0.Iesire");
            System.out.println("1.Functia citire+scriere");
            System.out.println("2.Afișarea listei de angajați folosind referințe la metode");
            System.out.println("3.Afișarea angajaților care au salariul peste 2500 RON");
            System.out.println("4.Crearea unei liste cu angajații din luna aprilie, a anului trecut");
            System.out.println("5.Afișarea angajaților care nu au funcție de conducere");
            System.out.println("6.Extragerea din lista de angajați a unei liste de String-uri care conține numele angajaților scrise cu majuscule");
            System.out.println("7.Afișarea salariilor mai mici de 3000 de RON");
            System.out.println("8.Afișarea datelor primului angajat al firmei");
            System.out.println("9.Afișarea de statistici referitoare la salariul angajaților");
            System.out.println("10.Afișarea unor mesaje care indică dacă printre angajați există cel puțin un “Ion”");
            System.out.println("11.Afișarea numărului de persoane care s-au angajat în vara anului precedent");
            System.out.println();
            System.out.print("Dati optiunea dvs: ");
            opt=my_scanner.nextInt();
            my_scanner.nextLine();

            switch(opt)
            {
                case 0:
                    System.exit(0);

                case 1:
                {
                    System.out.println();
                    lista_angajati=citire();
                    for(Angajat a: lista_angajati)
                        System.out.println(a.toString());
                    System.out.println();
                    break;
                }

                case 2:
                {
                    System.out.println();
                    lista_angajati.forEach(System.out::println);
                    System.out.println();
                    break;
                }

                case 3:
                {
                    System.out.println("\nAngajatii cu salar peste 2500 RON sunt: ");
                    lista_angajati
                            .stream()
                            .filter((a)->a.getSalariul()>2500)
                            .forEach(System.out::println);
                    System.out.println();
                    break;
                }

                case 4:
                {
                    int an_curent=LocalDate.now().getYear();
                    System.out.println();
                    List<Angajat>lista_noua=lista_angajati
                            .stream()
                            .filter(angajat -> angajat.getData_angajarii().getMonthValue()==04&&
                                    angajat.getData_angajarii().getYear()==an_curent-1&&
                                    (angajat.getPostul().contains("sef"))||(angajat.getPostul().contains("director")))
                            .collect(Collectors.toList());
                    lista_noua.forEach(System.out::println);
                    System.out.println();
                    break;
                }

                case 5:
                {
                    lista_angajati
                            .stream()
                            .filter(angajat -> !angajat.getPostul().contains("sef")&& !angajat.getPostul().contains("director"))
                            .sorted((a1,a2)->Float.compare(a2.getSalariul(), a1.getSalariul())).
                            forEach(System.out::println);
                    System.out.println();
                    break;
                }

                case 6: {
                    System.out.println();
                    List<String> lista_majuscule = lista_angajati
                            .stream()
                            .map(a -> a.getNume().toUpperCase())
                            .collect(Collectors.toList());
                    lista_majuscule.forEach(System.out::println);
                    System.out.println();
                    break;
                }

                case 7:
                {
                    System.out.println("\nAngajatii cu salar sub 3000 RON sunt: ");
                    lista_angajati
                            .stream()
                            .map(Angajat::getSalariul)
                            .filter(salariul->salariul<3000)
                            .forEach(System.out::println);
                    System.out.println();
                    break;
                }

                case 8:
                {
                    System.out.println("\nPrimul angajat este:");
                    lista_angajati
                            .stream()
                            .min(Comparator.comparing(Angajat::getData_angajarii))
                            .ifPresent(System.out::println);
                    System.out.println();
                    break;
                }

                case 9:
                {
                    System.out.println("Statistici: ");
                    DoubleSummaryStatistics statistici=lista_angajati
                            .stream()
                            .collect(Collectors.summarizingDouble(Angajat::getSalariul));
                    System.out.println("Salariul mediu: "+statistici.getAverage());
                    System.out.println("Salariul maxim: "+statistici.getMax());
                    System.out.println("Salariul minim: "+statistici.getMin());
                    System.out.println();
                    break;
                }

                case 10:
                {
                    System.out.println();
                    Optional<Angajat> numeIon=lista_angajati
                            .stream()
                            .filter(angajat -> angajat.getNume().equalsIgnoreCase("Ion"))
                            .findAny();

                    numeIon.ifPresentOrElse(angajat -> System.out.println("Firma are cel putin un Ion angajat") ,
                            ()-> System.out.println("Firma nu are nici un Ion angajat"));
                    System.out.println();
                    break;
                }

                case 11:
                {
                    System.out.println();
                    int anCurent = LocalDate.now().getYear();
                    float nr_angajati=lista_angajati
                            .stream()
                            .filter(angajat -> angajat.getData_angajarii().getMonthValue()>=6&&
                                    angajat.getData_angajarii().getMonthValue()<=8&&
                                    angajat.getData_angajarii().getYear()==anCurent-1)
                            .count();
                    System.out.println(nr_angajati);
                    System.out.println();
                    break;
                }
            }
            System.out.println();
        }
    }
}
