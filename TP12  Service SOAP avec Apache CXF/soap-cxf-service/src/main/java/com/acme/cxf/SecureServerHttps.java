package com.acme.cxf;

import com.acme.cxf.impl.HelloServiceImpl;
import com.acme.cxf.security.UTPasswordCallback;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import java.util.HashMap;
import java.util.Map;

public class SecureServerHttps {
  public static void main(String[] args) {
    Map<String,Object> inProps = new HashMap<>();
    inProps.put("action", "UsernameToken Signature Encrypt");
    inProps.put("passwordType", "PasswordDigest");
    inProps.put("passwordCallbackRef", 
        new UTPasswordCallback(Map.of(
            "student", "secret123",
            "admin", "admin123"
        ))
    );
    
    // Signature
    inProps.put("signaturePropFile", "server-keystore.properties");
    
    // Chiffrement
    inProps.put("encryptionPropFile", "server-keystore.properties");

    WSS4JInInterceptor wssIn = new WSS4JInInterceptor(inProps);

    JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
    factory.setServiceClass(HelloServiceImpl.class);
    factory.setAddress("http://localhost:8443/services/hello-secure-pro");
    Server server = factory.create();
    server.getEndpoint().getInInterceptors().add(wssIn);

    System.out.println("===================================");
    System.out.println("Secure Pro Service (PasswordDigest)");
    System.out.println("WSDL: http://localhost:8443/services/hello-secure-pro?wsdl");
    System.out.println("Signature: ENABLED");
    System.out.println("Encryption: ENABLED");
    System.out.println("===================================");
  }
}
