# Service SOAP avec Apache CXF - TP12

## Vue d'ensemble
Ce projet implémente un service web SOAP en utilisant **Apache CXF** et **JAX-WS** avec des modèles **JAXB** sérialisables en XML.

### Objectifs du TP
- ✓ Initialiser un projet Maven avec CXF et JAXB
- ✓ Créer des modèles JAXB sérialisables (Person)
- ✓ Définir une interface de service JAX-WS (contrat SOAP)
- ✓ Implémenter la logique métier
- ✓ Publier le service via un serveur embarqué
- ✓ Valider le service avec SoapUI
- ✓ Générer des clients SOAP (wsdl2java)
- ✓ Ajouter la sécurité WS-Security (optionnel)

---

## Structure du Projet

```
soap-cxf-service/
├── pom.xml                           # Configuration Maven avec dépendances CXF
├── README.md                         # Ce fichier
├── WSDL_EXPLANATION.md              # Explication des sections WSDL
├── SOAPUI_GUIDE.md                  # Guide complet pour SoapUI
├── test-soap-service.ps1            # Script de test (curl SOAP)
├── run-server.bat                   # Script pour lancer le serveur (Windows)
├── run-server.ps1                   # Script pour lancer le serveur (PowerShell)
│
├── src/main/java/
│   └── com/acme/cxf/
│       ├── App.java                 # Classe principale (placeholder)
│       ├── Server.java              # ⭐ Serveur SOAP embarqué
│       ├── api/
│       │   └── HelloService.java    # ⭐ Interface JAX-WS (contrat)
│       ├── impl/
│       │   └── HelloServiceImpl.java # ⭐ Implémentation du service
│       ├── model/
│       │   └── Person.java          # ⭐ Modèle JAXB sérialisable
│       └── client/
│           └── SoapClient.java      # Client SOAP pour tester
│
├── src/main/resources/
│   ├── request_sayHello.xml         # Exemple de requête SOAP
│   ├── request_findPerson.xml       # Exemple de requête SOAP
│   ├── response_sayHello.xml        # Exemple de réponse attendue
│   └── response_findPerson.xml      # Exemple de réponse attendue
│
└── src/test/java/
    └── com/acme/cxf/
        └── HelloServiceTest.java    # Tests unitaires JUnit 5
```

---

## Dépendances Principales

| Dépendance | Version | Rôle |
|------------|---------|------|
| `cxf-rt-frontend-jaxws` | 4.0.3 | Framework SOAP/JAX-WS |
| `cxf-rt-transports-http` | 4.0.3 | Transport HTTP |
| `cxf-rt-transports-http-jetty` | 4.0.3 | Serveur Jetty embarqué |
| `jakarta.xml.bind-api` | 4.0.1 | API JAXB (Java 11+) |
| `jaxb-runtime` | 4.0.3 | Runtime JAXB |
| `junit-jupiter` | 5.10.2 | Tests unitaires |

---

## Démarrage Rapide

### 1. Compiler le projet
```bash
cd soap-cxf-service
mvn clean package -DskipTests
```

### 2. Lancer le serveur
```bash
# Option 1 : Avec Maven
mvn exec:java

# Option 2 : Script PowerShell (Windows)
.\run-server.ps1

# Option 3 : Script batch (Windows)
.\run-server.bat
```

Le service sera accessible à :
- **URL de base** : `http://localhost:8080/services/hello`
- **WSDL** : `http://localhost:8080/services/hello?wsdl`

### 3. Tester le service

#### Avec SoapUI (Graphique)
1. Ouvrir SoapUI
2. **File → New SOAP Project**
3. URL du WSDL : `http://localhost:8080/services/hello?wsdl`
4. ✓ Cocher "Create sample requests for all operations"
5. Cliquer les opérations pour tester

Voir : **SOAPUI_GUIDE.md**

#### Avec PowerShell (Ligne de commande)
```bash
.\test-soap-service.ps1
```

#### Avec curl
```bash
curl -X POST http://localhost:8080/services/hello \
  -H "Content-Type: text/xml" \
  -d @src/main/resources/request_sayHello.xml
```

---

## Opérations du Service

### 1. SayHello
Retourne une salutation personnalisée.

**Requête SOAP**
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:api="http://api.cxf.acme.com/">
   <soap:Body>
      <api:sayHello>
         <name>Alice</name>
      </api:sayHello>
   </soap:Body>
</soap:Envelope>
```

**Réponse SOAP**
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <ns2:sayHelloResponse xmlns:ns2="http://api.cxf.acme.com/">
         <greeting>Bonjour, Alice</greeting>
      </ns2:sayHelloResponse>
   </soap:Body>
</soap:Envelope>
```

---

### 2. FindPerson
Retourne un objet Person (sérialisé en XML via JAXB).

**Requête SOAP**
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:api="http://api.cxf.acme.com/">
   <soap:Body>
      <api:findPersonById>
         <id>123</id>
      </api:findPersonById>
   </soap:Body>
</soap:Envelope>
```

**Réponse SOAP**
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <ns2:findPersonByIdResponse xmlns:ns2="http://api.cxf.acme.com/">
         <person>
            <id>123</id>
            <name>Ada Lovelace</name>
            <age>36</age>
         </person>
      </ns2:findPersonByIdResponse>
   </soap:Body>
</soap:Envelope>
```

---

## Annotations Clés

### @WebService
```java
@WebService(targetNamespace = "http://api.cxf.acme.com/")
public interface HelloService { ... }
```
- Marque une interface comme service SOAP
- `targetNamespace` : Identifiant unique du service

### @WebMethod
```java
@WebMethod(operationName = "SayHello")
String sayHello(String name);
```
- Expose une méthode comme opération SOAP
- `operationName` : Nom de l'opération dans le WSDL

### @WebParam & @WebResult
```java
@WebMethod
@WebResult(name = "greeting")
String sayHello(@WebParam(name = "name") String name);
```
- Nomme les paramètres et résultats dans le WSDL

### @XmlRootElement & @XmlElement
```java
@XmlRootElement(name = "Person")
public class Person {
    @XmlElement
    private String id;
}
```
- JAXB : Sérialise la classe en XML
- Nécessite un constructeur vide

---

## Tests Unitaires

Exécuter les tests :
```bash
mvn test
```

Tests disponibles dans `HelloServiceTest.java` :
- ✓ `testSayHello()` : Vérifie le message de salutation
- ✓ `testSayHelloWithNull()` : Teste le comportement avec null
- ✓ `testFindPersonById()` : Teste la récupération de Person
- ✓ `testPersonSerialization()` : Teste la sérialisation JAXB

---

## Génération de Stubs Clients (wsdl2java)

Une fois le service lancé, générer les stubs clients :

```bash
mvn cxf:wsdl2java -Dwsdl=http://localhost:8080/services/hello?wsdl
```

Cela génère les classes dans `target/generated-sources/cxf/` :
- `HelloService.java` : Interface service
- `HelloServiceService.java` : Service factory
- `Person.java` : Modèle métier

---

## Structure du WSDL

Le WSDL généré contient 5 sections principales :

1. **types** : Définitions XSD (Person, String, int)
2. **messages** : Requêtes et réponses SOAP
3. **portType** : Interface logique (opérations)
4. **binding** : Protocole SOAP/HTTP
5. **service** : Endpoint URL réel

Voir : **WSDL_EXPLANATION.md**

---

## Architecture du Service

```
┌─────────────────────────────────────────┐
│         Client SOAP (SoapUI)             │
└────────────────┬────────────────────────┘
                 │
                 │ Requête SOAP HTTP
                 │
┌────────────────▼────────────────────────┐
│      CXF HTTP Transport (Jetty)          │
│   http://localhost:8080/services/hello  │
└────────────────┬────────────────────────┘
                 │
                 │ Désérialisation SOAP
                 │
┌────────────────▼────────────────────────┐
│      HelloServiceImpl (Implémentation)    │
│  ├─ sayHello(String) : String            │
│  └─ findPersonById(String) : Person     │
└────────────────┬────────────────────────┘
                 │
                 │ Sérialisation JAXB
                 │
┌────────────────▼────────────────────────┐
│      Réponse SOAP XML + Métadonnées      │
└─────────────────────────────────────────┘
```

---

## Avancé : Sécurité WS-Security

### Ajouter WS-Security au pom.xml
```xml
<dependency>
    <groupId>org.apache.wss4j</groupId>
    <artifactId>wss4j-ws-security-common</artifactId>
    <version>2.4.3</version>
</dependency>
```

### Configurer dans Server.java
```java
Map<String, Object> inProps = new HashMap<>();
inProps.put(ConfigurationConstants.ACTION, 
            ConfigurationConstants.TIMESTAMP + " " + 
            ConfigurationConstants.SIGNATURE);
inProps.put(ConfigurationConstants.SIG_PROP_FILE, "server-keystore.properties");

WSS4JInInterceptor inInterceptor = new WSS4JInInterceptor(inProps);
factory.getInInterceptors().add(inInterceptor);
```

---

## Troubleshooting

| Problème | Solution |
|----------|----------|
| `Port 8080 déjà utilisé` | `netstat -ano \| find "8080"` puis tuer le process |
| `ClassNotFoundException` | Vérifier que Maven a compilé tous les fichiers |
| `WSDL inaccessible` | S'assurer que le service est démarré et Jetty actif |
| `SOAP Fault` | Vérifier les logs du serveur et la structure de la requête |

---

## Références

- [Apache CXF Documentation](https://cxf.apache.org/)
- [JAX-WS Specification](https://github.com/eclipse-ee4j/jax-ws-api)
- [JAXB Documentation](https://eclipse-ee4j.github.io/jaxb-ri/)
- [SoapUI Official Site](https://www.soapui.org/)
- [SOAP/XML Standards](https://www.w3.org/TR/soap12/)

---

## Auteur
TP12 - Service SOAP avec Apache CXF

## Licence
Éducatif - Libre d'utilisation
