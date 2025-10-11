package metier;

import dao.IDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("metier")
public class MetierImpl implements IMetier {
  @Autowired
  private IDao dao;                 // pas de @Qualifier ici
  @Override
  public double calcul() { return dao.getValue() * 2; }
public void setDao(IDao dao2) {
	// TODO Auto-generated method stub
	
}
}