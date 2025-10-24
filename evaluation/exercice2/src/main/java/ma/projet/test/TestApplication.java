package ma.projet.test;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestApplication {

    public static void main(String[] args) {
        // Chargement du contexte Spring pour l'application
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

        // Récupération des gestionnaires (services)
        HommeService serviceH = ctx.getBean(HommeService.class);
        FemmeService serviceF = ctx.getBean(FemmeService.class);
        MariageService serviceM = ctx.getBean(MariageService.class);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            // Création des objets Femme (Épouses)
            Femme spouse1 = new Femme("EL BACHIRI", "HIND", "0610101010", "Casablanca", sdf.parse("15/05/1970"));
            Femme spouse2 = new Femme("TALIBI", "MERYEM", "0620202020", "Rabat", sdf.parse("20/08/1975"));
            Femme spouse3 = new Femme("KASSIMI", "HABIBA", "0630303030", "Fes", sdf.parse("10/03/1978"));
            Femme spouse4 = new Femme("SEFRI", "JALILA", "0640404040", "Marrakech", sdf.parse("25/12/1968"));
            Femme spouse5 = new Femme("MOUNIB", "SARAH", "0650505050", "Tanger", sdf.parse("30/07/1980"));
            Femme spouse6 = new Femme("HADDAD", "MALIKA", "0660606060", "Agadir", sdf.parse("18/11/1972"));
            Femme spouse7 = new Femme("MANSOURI", "ZOHRA", "0670707070", "Kenitra", sdf.parse("05/02/1985"));
            Femme spouse8 = new Femme("AZZOUZI", "GHIZLANE", "0680808080", "Meknes", sdf.parse("22/09/1976"));
            Femme spouse9 = new Femme("MEHDI", "HASNAA", "0690909090", "Oujda", sdf.parse("14/04/1982"));
            Femme spouse10 = new Femme("RIZKI", "SANAE", "0601010101", "Tetouan", sdf.parse("08/06/1979"));

            serviceF.create(spouse1);
            serviceF.create(spouse2);
            serviceF.create(spouse3);
            serviceF.create(spouse4);
            serviceF.create(spouse5);
            serviceF.create(spouse6);
            serviceF.create(spouse7);
            serviceF.create(spouse8);
            serviceF.create(spouse9);
            serviceF.create(spouse10);
            System.out.println("-> Dix nouvelles conjointes ont été enregistrées avec succès.");

            // Création des objets Homme (Époux)
            Homme partner1 = new Homme("FASSI", "ADNANE", "0798765432", "Casablanca", sdf.parse("12/01/1965"));
            Homme partner2 = new Homme("CHAFIK", "REDOUANE", "0787654321", "Rabat", sdf.parse("28/03/1970"));
            Homme partner3 = new Homme("MEZIANE", "AMINE", "0776543210", "Fes", sdf.parse("17/07/1968"));
            Homme partner4 = new Homme("GUEZZAR", "YASSINE", "0765432109", "Marrakech", sdf.parse("03/11/1975"));
            Homme partner5 = new Homme("BOUZIDI", "TARIQ", "0754321098", "Tanger", sdf.parse("21/09/1972"));

            serviceH.create(partner1);
            serviceH.create(partner2);
            serviceH.create(partner3);
            serviceH.create(partner4);
            serviceH.create(partner5);
            System.out.println("-> Cinq époux ont été ajoutés à la base de données.");

            // Création des objets Mariage
            Mariage relation1 = new Mariage(sdf.parse("03/09/1989"), sdf.parse("03/09/1990"), 0, partner1, spouse4);
            Mariage relation2 = new Mariage(sdf.parse("03/09/1990"), null, 4, partner1, spouse1);
            Mariage relation3 = new Mariage(sdf.parse("03/09/1995"), null, 2, partner1, spouse2);
            Mariage relation4 = new Mariage(sdf.parse("04/11/2000"), null, 3, partner1, spouse3);

            Mariage relation5 = new Mariage(sdf.parse("10/06/1992"), sdf.parse("15/08/1995"), 1, partner2, spouse5);
            Mariage relation6 = new Mariage(sdf.parse("20/09/1996"), null, 2, partner3, spouse5);

            Mariage relation7 = new Mariage(sdf.parse("15/03/1993"), sdf.parse("10/07/1998"), 0, partner4, spouse6);
            Mariage relation8 = new Mariage(sdf.parse("25/11/2000"), null, 3, partner5, spouse6);

            Mariage relation9 = new Mariage(sdf.parse("12/04/1994"), null, 2, partner2, spouse7);
            Mariage relation10 = new Mariage(sdf.parse("08/08/1997"), null, 1, partner3, spouse8);
            Mariage relation11 = new Mariage(sdf.parse("19/02/1999"), null, 2, partner4, spouse9);
            Mariage relation12 = new Mariage(sdf.parse("30/05/2001"), null, 1, partner5, spouse10);

            serviceM.create(relation1);
            serviceM.create(relation2);
            serviceM.create(relation3);
            serviceM.create(relation4);
            serviceM.create(relation5);
            serviceM.create(relation6);
            serviceM.create(relation7);
            serviceM.create(relation8);
            serviceM.create(relation9);
            serviceM.create(relation10);
            serviceM.create(relation11);
            serviceM.create(relation12);
            System.out.println("-> Les douze relations conjugales ont été enregistrées.");

            // Afficher toutes les femmes
            List<Femme> listeDeFemmes = serviceF.findAll();
            System.out.println("\n--- Catalogue complet des Femmes enregistrées ---");
            for (Femme fItem : listeDeFemmes) {
                System.out.println("-> " + fItem.getPrenom() + " " + fItem.getNom() +
                                            " (Date de naissance : " + sdf.format(fItem.getDateNaissance()) + ")");
            }

            // Trouver la femme la plus âgée
            Femme laPlusVieille = serviceF.findFemmeLaPlusAgee();
            if (laPlusVieille != null) {
                System.out.println("\n** La doyenne du groupe est : " + laPlusVieille.getPrenom() + " " +
                                            laPlusVieille.getNom() + " (Née le : " +
                                            sdf.format(laPlusVieille.getDateNaissance()) + ") **");
            }

            // Trouver les épouses d'un homme entre deux dates
            Date debutIntervalle = sdf.parse("01/01/1990");
            Date finIntervalle = sdf.parse("31/12/1999");
            List<Femme> conjointsParPeriode = serviceH.findEpousesEntreDeuxDates(partner1.getId(), debutIntervalle, finIntervalle);
            System.out.println("\nConjointes de " + partner1.getPrenom() + " " + partner1.getNom() +
                                     " entre " + sdf.format(debutIntervalle) + " et " + sdf.format(finIntervalle) + " :");
            for (Femme fItem : conjointsParPeriode) {
                System.out.println(" \t- " + fItem.getPrenom() + " " + fItem.getNom());
            }

            // Compter les enfants d'une femme entre deux dates
            Date debutPeriodeEnfant = sdf.parse("01/01/1989");
            Date finPeriodeEnfant = sdf.parse("31/12/2005");
            int totalEnfants = serviceF.countEnfantsFemmeEntreDates(spouse1.getId(), debutPeriodeEnfant, finPeriodeEnfant);
            System.out.println("\nBilan des enfants de " + spouse1.getPrenom() + " " + spouse1.getNom() +
                                     " (période : " + sdf.format(debutPeriodeEnfant) + " à " + sdf.format(finPeriodeEnfant) +
                                     ") : " + totalEnfants + " enfants.");

            // Trouver les femmes mariées au moins deux fois
            List<Femme> marieesMultiples = serviceF.findFemmesMarieesAuMoinsDeuxFois();
            System.out.println("\nListe des femmes polygames (au moins 2 mariages) :");
            for (Femme fItem : marieesMultiples) {
                System.out.println(" \t-> " + fItem.getPrenom() + " " + fItem.getNom());
            }

            // Compter les hommes mariés à 4 femmes entre deux dates
            Date dateStartCount = sdf.parse("01/01/1989");
            Date dateEndCount = sdf.parse("31/12/2005");
            int hommesQuatreFemmes = serviceH.countHommesMarieA4FemmesEntreDates(dateStartCount, dateEndCount);
            System.out.println("\nTotal des hommes ayant épousé 4 femmes entre " +
                                     sdf.format(dateStartCount) + " et " + sdf.format(dateEndCount) + " : " + hommesQuatreFemmes);

            // Afficher les mariages en cours et terminés pour un homme
            Homme manDetails = serviceH.findById(partner1.getId());
            List<Mariage> listUnions = serviceH.findMariagesByHomme(partner1.getId());

            System.out.println("\n*** Historique des unions pour " + manDetails.getPrenom() + " " + manDetails.getNom() + " ***");

            System.out.println("\n\t- Unions en cours -");
            int indexActuel = 1;
            for (Mariage mRelation : listUnions) {
                if (mRelation.getDateFin() == null) {
                    System.out.println("\t  " + indexActuel + ". Épouse : " + mRelation.getFemme().getPrenom() +
                                             " " + mRelation.getFemme().getNom() +
                                             " | Depuis le : " + sdf.format(mRelation.getDateDebut()) +
                                             " | Progéniture : " + mRelation.getNbrEnfant());
                    indexActuel++;
                }
            }

            System.out.println("\n\t- Unions terminées (séparation) -");
            int indexTermine = 1;
            for (Mariage mRelation : listUnions) {
                if (mRelation.getDateFin() != null) {
                    System.out.println("\t  " + indexTermine + ". Épouse : " + mRelation.getFemme().getPrenom() +
                                             " " + mRelation.getFemme().getNom() +
                                             " | Début : " + sdf.format(mRelation.getDateDebut()));
                    System.out.println("\t  \t\tFin le : " + sdf.format(mRelation.getDateFin()) +
                                             " | Nombre d'enfants : " + mRelation.getNbrEnfant());
                    indexTermine++;
                }
            }


        } catch (Exception e) {
            System.err.println("!!! Problème survenu lors de l'exécution des scénarios !!!");
            e.printStackTrace();
        }

        // Fermeture du contexte
        ((ClassPathXmlApplicationContext) ctx).close();
    }
}