package com.acme.cxf;

import com.acme.cxf.api.HelloService;
import com.acme.cxf.model.Person;
import jakarta.xml.ws.Service;
import javax.xml.namespace.QName;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SecureClientTest {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Secure Client Test (PasswordDigest) ===");
        
        // Configuration WS-Security pour le client
        Map<String, Object> outProps = new HashMap<>();
        outProps.put("action", "UsernameToken");
        outProps.put("user", "student");
        outProps.put("passwordType", "PasswordDigest");
        outProps.put("passwordCallbackRef", 
            new com.acme.cxf.security.ClientPasswordCallback("student", "secret123")
        );

        // Créer le client
        URL wsdl = new URL("http://localhost:8080/services/hello-secure?wsdl");
        QName qname = new QName("http://api.cxf.acme.com/", "HelloService");
        Service svc = Service.create(wsdl, qname);
        HelloService port = svc.getPort(HelloService.class);

        // Ajouter l'intercepteur de sécurité
        Client client = ClientProxy.getClient(port);
        client.getOutInterceptors().add(new WSS4JOutInterceptor(outProps));

        // Tester SayHello
        System.out.println("\n--- Test 1: SayHello avec PasswordDigest ---");
        try {
            String response = port.sayHello("SecureClient");
            System.out.println("✓ Réponse: " + response);
        } catch (Exception e) {
            System.err.println("✗ Erreur: " + e.getMessage());
        }

        // Tester FindPerson
        System.out.println("\n--- Test 2: FindPerson avec PasswordDigest ---");
        try {
            Person person = port.findPersonById("PRO-999");
            System.out.println("✓ ID: " + person.getId());
            System.out.println("✓ Nom: " + person.getName());
            System.out.println("✓ Âge: " + person.getAge());
        } catch (Exception e) {
            System.err.println("✗ Erreur: " + e.getMessage());
        }

        System.out.println("\n=== Tests Terminés ===");
    }
}
