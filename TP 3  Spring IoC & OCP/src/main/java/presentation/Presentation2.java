package presentation;

import metier.IMetier;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = {"dao","metier","config"})
public class Presentation2 {
  public static void main(String[] args) {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

    // Choix 1 (profils) : décommenter un profil pour tester
    // ctx.getEnvironment().setActiveProfiles("dev");   // -> DaoImpl2 (150) => 300
    // ctx.getEnvironment().setActiveProfiles("prod");  // -> DaoImpl  (100) => 200
    // ctx.getEnvironment().setActiveProfiles("file");  // -> DaoFile  (180) => 360
    // ctx.getEnvironment().setActiveProfiles("api");   // -> DaoApi   (220) => 440

    // Choix 2 (propriété externe) : laisser les profils vides,
    // PropertyDrivenConfig créera un bean "dao" selon app.properties

    ctx.register(Presentation2.class);
    ctx.refresh();

    IMetier metier = ctx.getBean(IMetier.class);
    System.out.println("Résultat = " + metier.calcul());
    ctx.close();
  }
}