package com.acme.cxf.client;

import com.acme.cxf.api.HelloService;
import com.acme.cxf.model.Person;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public class SoapClient {
    public static void main(String[] args) {
        // URL du service WSDL
        String serviceUrl = "http://localhost:8080/services/hello";

        // Créer un proxy client
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(HelloService.class);
        factory.setAddress(serviceUrl);
        
        // Créer l'instance du service
        HelloService service = (HelloService) factory.create();

        // Tester l'opération SayHello
        System.out.println("=== Test SayHello ===");
        String greeting = service.sayHello("World");
        System.out.println("Réponse: " + greeting);

        // Tester l'opération FindPerson
        System.out.println("\n=== Test FindPerson ===");
        Person person = service.findPersonById("123");
        if (person != null) {
            System.out.println("ID: " + person.getId());
            System.out.println("Nom: " + person.getName());
            System.out.println("Âge: " + person.getAge());
        }
    }
}
