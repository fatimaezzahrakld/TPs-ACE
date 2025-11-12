package com.acme.cxf;

import com.acme.cxf.api.HelloService;
import com.acme.cxf.model.Person;
import jakarta.xml.ws.Service;
import javax.xml.namespace.QName;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SecureEndpointIntegrationTest {

    private static HelloService securePort;

    @BeforeAll
    public static void setUp() throws Exception {
        // Configuration WS-Security pour le client (PasswordDigest)
        Map<String, Object> outProps = new HashMap<>();
        outProps.put("action", "UsernameToken");
        outProps.put("user", "student");
        outProps.put("passwordType", "PasswordDigest");
        outProps.put("passwordCallbackRef", 
            new com.acme.cxf.security.ClientPasswordCallback("student", "secret123")
        );

        // Créer le client pour l'endpoint sécurisé
        try {
            URL wsdl = new URL("http://localhost:8080/services/hello-secure?wsdl");
            QName qname = new QName("http://api.cxf.acme.com/", "HelloService");
            Service svc = Service.create(wsdl, qname);
            securePort = svc.getPort(HelloService.class);

            // Ajouter l'intercepteur WS-Security
            Client client = ClientProxy.getClient(securePort);
            client.getOutInterceptors().add(new WSS4JOutInterceptor(outProps));
        } catch (Exception e) {
            System.err.println("Erreur lors de la configuration du client sécurisé: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testSecureSayHelloWithValidCredentials() {
        assertNotNull(securePort, "Service client ne doit pas être null");
        
        String result = securePort.sayHello("TestUser");
        
        assertNotNull(result);
        assertTrue(result.contains("Bonjour"));
        assertTrue(result.contains("TestUser"));
    }

    @Test
    public void testSecureFindPersonWithValidCredentials() {
        assertNotNull(securePort, "Service client ne doit pas être null");
        
        Person person = securePort.findPersonById("SEC-123");
        
        assertNotNull(person);
        assertNotNull(person.getId());
        assertNotNull(person.getName());
        assertTrue(person.getAge() > 0);
    }

    @Test
    public void testSecureSayHelloResponse() {
        String response = securePort.sayHello("Integration");
        assertEquals("Bonjour, Integration", response);
    }

    @Test
    public void testSecurePersonSerialization() {
        Person person = securePort.findPersonById("TEST-001");
        
        assertNotNull(person.getId());
        assertEquals("Ada Lovelace", person.getName());
        assertEquals(36, person.getAge());
    }

    @Test
    public void testSecureMultipleRequests() {
        // Tester plusieurs appels sans reconnexion
        for (int i = 0; i < 3; i++) {
            String result = securePort.sayHello("User" + i);
            assertNotNull(result);
            assertTrue(result.contains("Bonjour"));
        }
    }
}
