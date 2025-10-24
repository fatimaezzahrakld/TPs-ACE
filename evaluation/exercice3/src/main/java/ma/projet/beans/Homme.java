package ma.projet.beans;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name="homme")
public class Homme extends Personne 
{
    
        @OneToMany(mappedBy="homme",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
        private List<Mariage> mariages;

        
        public Homme() 
        {
        }

        public Homme(String nom,String prenom,String telephone,String adresse,Date dateNaissance) 
        {
              super(nom,prenom,telephone,adresse,dateNaissance);
        }

        
        public List<Mariage> getMariages() 
        {
              return mariages;
        }

        public void setMariages(List<Mariage> mariages) 
        {
              this.mariages=mariages;
        }

        
        @Override
        public String toString() 
        {
              return "Homme{"+
                      "id="+getId()+
                      ", nom='"+getNom()+'\''+
                      ", prenom='"+getPrenom()+'\''+
                      '}';
        }
}