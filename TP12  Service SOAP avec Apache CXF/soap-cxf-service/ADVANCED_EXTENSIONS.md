# Étape 12 — Aller Plus Loin (Extensions "Pro")

## Objectif
Explorer les extensions professionnelles et patterns avancés pour un service SOAP en production.

---

## 1. Intégrer Spring Boot avec CXF

### 1.1 Avantages de Spring Boot + CXF

```
Vanilla CXF (TP actuel)          Spring Boot + CXF
├─ Code déclaratif              ├─ Configuration centralisée
├─ Gestion manuelle des ports   ├─ Ports/Contextes automatiques
├─ Intercepteurs manuels        ├─ Beans Spring pour WS-Security
├─ Tests complexes              ├─ @SpringBootTest facile
└─ Déploiement JAR bricolé      └─ Fat JAR standardisé
```

### 1.2 Ajouter la Dépendance

```xml
<!-- pom.xml -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.0</version>
</parent>

<dependency>
    <groupId>org.apache.cxf</groupId>
    <artifactId>cxf-spring-boot-starter-jaxws</artifactId>
    <version>${cxf.version}</version>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- WS-Security -->
<dependency>
    <groupId>org.apache.cxf</groupId>
    <artifactId>cxf-rt-ws-security</artifactId>
    <version>${cxf.version}</version>
</dependency>
```

### 1.3 Configuration Spring Boot

**Fichier : `src/main/resources/application.yml`**

```yaml
spring:
  application:
    name: soap-cxf-service

cxf:
  path: /ws
  servlet:
    init:
      service-list-path: /

server:
  port: 8080
  servlet:
    context-path: /

# WS-Security Configuration
logging:
  level:
    org.apache.cxf: DEBUG
    org.apache.wss4j: DEBUG
```

### 1.4 Configuration Bean Spring

**Fichier : `src/main/java/com/acme/cxf/config/SoapConfig.java`**

```java
package com.acme.cxf.config;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.acme.cxf.api.HelloService;
import com.acme.cxf.impl.HelloServiceImpl;
import com.acme.cxf.security.UTPasswordCallback;
import javax.xml.ws.Endpoint;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SoapConfig {

    @Bean
    public HelloService helloService() {
        return new HelloServiceImpl();
    }

    @Bean
    public Endpoint helloEndpoint(Bus bus, HelloService helloService) {
        EndpointImpl endpoint = new EndpointImpl(bus, helloService);
        endpoint.publish("/hello");
        return endpoint;
    }

    @Bean
    public Endpoint secureHelloEndpoint(Bus bus, HelloService helloService) {
        // Configuration WS-Security
        Map<String, Object> inProps = new HashMap<>();
        inProps.put("action", "UsernameToken");
        inProps.put("passwordType", "PasswordText");
        inProps.put("passwordCallbackRef", 
            new UTPasswordCallback(Map.of(
                "student", "secret123",
                "admin", "admin123"
            ))
        );

        WSS4JInInterceptor wssIn = new WSS4JInInterceptor(inProps);

        EndpointImpl endpoint = new EndpointImpl(bus, helloService);
        endpoint.getInInterceptors().add(wssIn);
        endpoint.publish("/hello-secure");
        
        return endpoint;
    }
}
```

### 1.5 Application Spring Boot

**Fichier : `src/main/java/com/acme/cxf/Application.java`**

```java
package com.acme.cxf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 1.6 Lancer l'Application

```bash
# Via Maven
mvn spring-boot:run

# Via JAR
mvn clean package
java -jar target/soap-cxf-service-1.0-SNAPSHOT.jar

# Services disponibles
# http://localhost:8080/ws/hello?wsdl
# http://localhost:8080/ws/hello-secure?wsdl
# http://localhost:8080/ws/ (liste des services)
```

### 1.7 Avantages Spring Boot

```
✓ Configuration centralisée (application.yml)
✓ Dépendances auto-configurées
✓ Monitoring intégré (/actuator/health)
✓ Tests @SpringBootTest
✓ Profils (dev/test/prod)
✓ Logging centralisé
✓ Actuator endpoints
```

---

## 2. Déclarer la Sécurité via WS-Policy au Niveau WSDL

### 2.1 Qu'est-ce qu'une WS-Policy ?

Fichier XML décrivant les **garanties de sécurité** attendues.

```
WSDL seul              WSDL + WS-Policy
├─ Décrit opérations  ├─ Décrit opérations
├─ Décrit types       ├─ Décrit types
└─ Implicite : sécurité  └─ Explicite : UsernameToken, Signature, Chiffrement
```

### 2.2 Ajouter WS-Policy au WSDL

**Fichier : `src/main/resources/HelloService-policy.wsdl`**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<wsdl:definitions 
    xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
    xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/"
    xmlns:tns="http://api.cxf.acme.com/"
    xmlns:wsp="http://www.w3.org/ns/ws-policy"
    xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
    targetNamespace="http://api.cxf.acme.com/">

    <!-- Politique de Sécurité UsernameToken -->
    <wsp:Policy wsu:Id="UsernameTokenPolicy">
        <wsp:ExactlyOne>
            <wsp:All>
                <!-- UsernameToken requis -->
                <sp:UsernameToken 
                    xmlns:sp="http://schemas.xmlsoap.org/ws/2005/07/securitypolicy"
                    sp:IncludeToken="http://schemas.xmlsoap.org/ws/2005/07/securitypolicy/IncludeToken/Always">
                    <wsp:Policy>
                        <sp:WssUsernameToken10/>
                    </wsp:Policy>
                </sp:UsernameToken>
                
                <!-- Support du mot de passe en texte clair -->
                <sp:SupportingTokens 
                    xmlns:sp="http://schemas.xmlsoap.org/ws/2005/07/securitypolicy">
                    <sp:UsernameToken sp:IncludeToken="http://schemas.xmlsoap.org/ws/2005/07/securitypolicy/IncludeToken/Always">
                        <wsp:Policy>
                            <sp:WssUsernameToken10/>
                        </wsp:Policy>
                    </sp:UsernameToken>
                </sp:SupportingTokens>
            </wsp:All>
        </wsp:ExactlyOne>
    </wsp:Policy>

    <!-- PortType (Interface) avec Policy -->
    <wsdl:portType name="HelloService">
        <wsdl:operation name="SayHello">
            <wsdl:input message="tns:SayHelloRequest"/>
            <wsdl:output message="tns:SayHelloResponse"/>
        </wsdl:operation>
    </wsdl:portType>

    <!-- Binding avec Policy attachée -->
    <wsdl:binding name="HelloServiceBinding" type="tns:HelloService">
        <wsp:PolicyReference URI="#UsernameTokenPolicy"/>
        <soap:binding transport="http://schemas.xmlsoap.org/soap/http"/>
        
        <wsdl:operation name="SayHello">
            <soap:operation soapAction="http://api.cxf.acme.com/SayHello"/>
            <wsdl:input>
                <soap:body use="literal"/>
            </wsdl:input>
            <wsdl:output>
                <soap:body use="literal"/>
            </wsdl:output>
        </wsdl:operation>
    </wsdl:binding>

    <!-- Service avec endpoint -->
    <wsdl:service name="HelloService">
        <wsdl:port name="HelloServicePort" binding="tns:HelloServiceBinding">
            <soap:address location="http://localhost:8080/ws/hello-secure"/>
        </wsdl:port>
    </wsdl:service>

</wsdl:definitions>
```

### 2.3 Avantages WS-Policy

```
✓ Documentation explicite des garanties
✓ Clients savent quelles sont les exigences de sécurité
✓ Outils (SoapUI, etc.) configurent auto UsernameToken
✓ Conformité aux standards WS-* (WS-SecurityPolicy)
✓ Intégration .NET/Java standardisée
```

---

## 3. Pipeline CI : Tests JUnit + Scénarios SoapUI

### 3.1 Structure

```
GitHub/GitLab
    ↓ (push)
    ↓
CI Pipeline (Jenkins/GitHub Actions)
    ↓
1. Build Maven (mvn clean package)
    ↓
2. Tests JUnit (mvn test)
    ↓
3. Tests Intégration SoapUI (mvn soapui:test)
    ↓
4. Rapports (Coverage, Logs)
    ↓
5. Deploy (si OK)
```

### 3.2 Configuration Maven pour SoapUI

**pom.xml :**

```xml
<plugin>
    <groupId>com.smartbear.soapui</groupId>
    <artifactId>soapui-maven-plugin</artifactId>
    <version>5.7.0</version>
    <configuration>
        <projectFile>soapui-tests/HelloService-Test.xml</projectFile>
        <outputFolder>target/soapui-reports</outputFolder>
        <junitReport>true</junitReport>
    </configuration>
    <executions>
        <execution>
            <phase>integration-test</phase>
            <goals>
                <goal>test</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### 3.3 CI avec GitHub Actions

**Fichier : `.github/workflows/ci.yml`**

```yaml
name: CI Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
    
    - name: Build with Maven
      run: mvn clean package -DskipTests
    
    - name: Run JUnit Tests
      run: mvn test
    
    - name: Start SOAP Service
      run: |
        mvn exec:java -Dexec.mainClass="com.acme.cxf.Server" &
        sleep 10
    
    - name: Run SoapUI Tests
      run: |
        testrunner.sh -r html soapui-tests/HelloService-Test.xml
    
    - name: Upload Reports
      if: always()
      uses: actions/upload-artifact@v3
      with:
        name: test-reports
        path: target/soapui-reports/
    
    - name: Deploy (if tests pass)
      if: success() && github.ref == 'refs/heads/main'
      run: |
        echo "Deploying to production..."
        # docker push ...
```

### 3.4 Lancer les tests localement

```bash
# Tests JUnit
mvn test

# Démarrer le service + tests SoapUI
mvn clean package
mvn exec:java -Dexec.mainClass="com.acme.cxf.Server" &
testrunner.sh -r html soapui-tests/HelloService-Test.xml
```

---

## 4. Approche Contract-First : WSDL Maître

### 4.1 Concept

```
Traditional (Code-First)         Contract-First
├─ Code Java → WSDL généré      ├─ WSDL maître → Code généré
├─ Évolution par refactoring     ├─ Évolution par versioning WSDL
├─ Client/Serveur liés           ├─ Client/Serveur découplés
└─ Flexibilité immédiate         └─ Contrat garanti
```

### 4.2 Créer le WSDL Maître

**Fichier : `src/main/resources/HelloService-master.wsdl`**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<wsdl:definitions 
    xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
    xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/"
    xmlns:tns="http://api.cxf.acme.com/"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    targetNamespace="http://api.cxf.acme.com/">

    <!-- Types (Schéma XSD) -->
    <wsdl:types>
        <xsd:schema targetNamespace="http://api.cxf.acme.com/">
            
            <!-- Type Person -->
            <xsd:complexType name="Person">
                <xsd:sequence>
                    <xsd:element name="id" type="xsd:string"/>
                    <xsd:element name="name" type="xsd:string"/>
                    <xsd:element name="age" type="xsd:int"/>
                </xsd:sequence>
            </xsd:complexType>
            
            <!-- Opération SayHello -->
            <xsd:element name="sayHello">
                <xsd:complexType>
                    <xsd:sequence>
                        <xsd:element name="name" type="xsd:string"/>
                    </xsd:sequence>
                </xsd:complexType>
            </xsd:element>
            
            <xsd:element name="sayHelloResponse">
                <xsd:complexType>
                    <xsd:sequence>
                        <xsd:element name="greeting" type="xsd:string"/>
                    </xsd:sequence>
                </xsd:complexType>
            </xsd:element>
            
            <!-- Opération FindPerson -->
            <xsd:element name="findPersonById">
                <xsd:complexType>
                    <xsd:sequence>
                        <xsd:element name="id" type="xsd:string"/>
                    </xsd:sequence>
                </xsd:complexType>
            </xsd:element>
            
            <xsd:element name="findPersonByIdResponse">
                <xsd:complexType>
                    <xsd:sequence>
                        <xsd:element name="person" type="tns:Person"/>
                    </xsd:sequence>
                </xsd:complexType>
            </xsd:element>
            
        </xsd:schema>
    </wsdl:types>

    <!-- Messages -->
    <wsdl:message name="SayHelloRequest">
        <wsdl:part name="body" element="tns:sayHello"/>
    </wsdl:message>
    
    <wsdl:message name="SayHelloResponse">
        <wsdl:part name="body" element="tns:sayHelloResponse"/>
    </wsdl:message>
    
    <wsdl:message name="FindPersonByIdRequest">
        <wsdl:part name="body" element="tns:findPersonById"/>
    </wsdl:message>
    
    <wsdl:message name="FindPersonByIdResponse">
        <wsdl:part name="body" element="tns:findPersonByIdResponse"/>
    </wsdl:message>

    <!-- PortType (Interface) -->
    <wsdl:portType name="HelloService">
        <wsdl:operation name="SayHello">
            <wsdl:input message="tns:SayHelloRequest"/>
            <wsdl:output message="tns:SayHelloResponse"/>
        </wsdl:operation>
        <wsdl:operation name="FindPerson">
            <wsdl:input message="tns:FindPersonByIdRequest"/>
            <wsdl:output message="tns:FindPersonByIdResponse"/>
        </wsdl:operation>
    </wsdl:portType>

    <!-- Binding -->
    <wsdl:binding name="HelloServiceBinding" type="tns:HelloService">
        <soap:binding style="document" transport="http://schemas.xmlsoap.org/soap/http"/>
        
        <wsdl:operation name="SayHello">
            <soap:operation soapAction="http://api.cxf.acme.com/SayHello"/>
            <wsdl:input><soap:body use="literal"/></wsdl:input>
            <wsdl:output><soap:body use="literal"/></wsdl:output>
        </wsdl:operation>
        
        <wsdl:operation name="FindPerson">
            <soap:operation soapAction="http://api.cxf.acme.com/FindPerson"/>
            <wsdl:input><soap:body use="literal"/></wsdl:input>
            <wsdl:output><soap:body use="literal"/></wsdl:output>
        </wsdl:operation>
    </wsdl:binding>

    <!-- Service -->
    <wsdl:service name="HelloService">
        <wsdl:documentation>Service SOAP Hello - Contract-First</wsdl:documentation>
        <wsdl:port name="HelloServicePort" binding="tns:HelloServiceBinding">
            <soap:address location="http://localhost:8080/ws/hello"/>
        </wsdl:port>
    </wsdl:service>

</wsdl:definitions>
```

### 4.3 Générer le Code depuis le WSDL

```bash
# Générer serveur (ServerImpl stub)
wsdl2java -all -keep -server \
  -p com.acme.cxf.generated \
  src/main/resources/HelloService-master.wsdl

# Générer client
wsdl2java -all -keep -client \
  -p com.acme.cxf.client.generated \
  src/main/resources/HelloService-master.wsdl

# Ou via Maven
mvn org.apache.cxf:cxf-codegen-plugin:wsdl2java \
  -Dwsdl=src/main/resources/HelloService-master.wsdl \
  -Dpackagename=com.acme.cxf.generated
```

### 4.4 Structure Contract-First

```
soap-cxf-service/
├── src/main/resources/
│   └── HelloService-master.wsdl      (WSDL maître)
│
├── target/generated-sources/cxf/
│   ├── com/acme/cxf/generated/
│   │   ├── HelloService.java         (Interface générée)
│   │   ├── HelloServiceImpl_*.java    (Stub serveur)
│   │   └── Person.java               (POJO généré)
│   │
│   └── com/acme/cxf/client/generated/
│       ├── HelloServiceService.java  (Service factory client)
│       └── HelloServiceImplPort.java (Port client)
│
├── src/main/java/
│   └── com/acme/cxf/impl/
│       └── HelloServiceImpl.java      (Implémentation métier)
```

### 4.5 Avantages Contract-First

```
✓ Version unique du contrat
✓ Compatibilité multi-langage (.NET, Node.js, etc.)
✓ Client/Serveur jamais désynchronisés
✓ Évolution contrôlée (versioning WSDL)
✓ Documentation centralisée
✓ Tests basés sur le contrat
```

---

## 5. Checklist Extensions Pro

- [ ] Spring Boot intégré
- [ ] WS-Policy déclaré dans WSDL
- [ ] CI Pipeline fonctionnelle (GitHub Actions)
- [ ] Tests SoapUI automatisés
- [ ] Approche Contract-First avec WSDL maître
- [ ] Génération client/serveur via wsdl2java
- [ ] Logs DEBUG centralisés
- [ ] Rapports de couverture (JaCoCo)

---

## 6. Ressources Avancées

| Sujet | Ressource |
|-------|-----------|
| **Spring Boot + CXF** | https://cxf.apache.org/docs/springboot.html |
| **WS-Policy** | https://www.w3.org/TR/ws-policy/ |
| **Contract-First** | https://www.ibm.com/cloud/learn/contract-first |
| **SoapUI Maven** | https://www.soapui.org/tools/soapui-maven-plugin/ |
| **wsdl2java** | https://cxf.apache.org/docs/wsdl-to-java.html |

---

## Conclusion

Les extensions pro permettent de :
- ✓ Scaler le service en production
- ✓ Garantir la qualité via CI/CD
- ✓ Maintenir un contrat stable
- ✓ Intégrer dans des écosystèmes complexes

