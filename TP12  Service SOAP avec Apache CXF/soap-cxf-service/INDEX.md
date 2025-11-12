# Index Complet du TP12 - Service SOAP avec Apache CXF

## 📚 Étapes du TP

### ✅ Étape 1 - Créer le projet Maven et ajouter les dépendances
**Fichier** : `pom.xml`
- Initialiser le projet Maven
- Ajouter CXF 4.0.3, JAXB, et Jetty
- Configurer Java 17 comme cible

### ✅ Étape 2 - Créer le modèle JAXB (POJO sérialisable)
**Fichier** : `src/main/java/com/acme/cxf/model/Person.java`
- Classe POJO avec annotations JAXB
- `@XmlRootElement(name = "Person")`
- Propriétés : `id`, `name`, `age`
- Constructeur vide requis par JAXB

### ✅ Étape 3 - Définir le contrat JAX-WS (interface de service)
**Fichier** : `src/main/java/com/acme/cxf/api/HelloService.java`
- Interface marquée avec `@WebService`
- Namespace : `http://api.cxf.acme.com/`
- 2 opérations :
  - `SayHello(String) : String`
  - `FindPerson(String) : Person`

### ✅ Étape 4 - Implémenter la logique métier
**Fichier** : `src/main/java/com/acme/cxf/impl/HelloServiceImpl.java`
- Implémente `HelloService`
- `@WebService` avec `endpointInterface`
- Implémentation des 2 opérations

### ✅ Étape 5 - Publier le service avec CXF (serveur embarqué)
**Fichier** : `src/main/java/com/acme/cxf/Server.java`
- Utilise `JaxWsServerFactoryBean`
- Expose le service sur `http://localhost:8080/services/hello`
- WSDL accessible via `?wsdl`

### ✅ Étape 6 - Comprendre et exploiter le WSDL
**Fichier** : `WSDL_EXPLANATION.md`
- Explique les 5 sections du WSDL :
  1. **types** : Schémas XSD
  2. **messages** : Requêtes/Réponses
  3. **portType** : Interface logique
  4. **binding** : Protocole SOAP
  5. **service** : Endpoint
- Outils pour exploiter le WSDL

### ✅ Étape 7 - Tester le service avec SoapUI
**Fichier** : `SOAPUI_GUIDE.md`
- Installation et configuration de SoapUI
- Créer un projet SOAP
- Tester les opérations
- Assertions et Test Suites
- Rapports et Mock Service

---

## 📁 Structure des Fichiers

```
soap-cxf-service/
│
├── 📄 pom.xml                              [Configuration Maven]
├── 📄 README.md                            [Guide principal]
├── 📄 WSDL_EXPLANATION.md                  [Explications WSDL]
├── 📄 SOAPUI_GUIDE.md                      [Guide SoapUI]
├── 📄 test-soap-service.ps1                [Script de test]
├── 📄 run-server.bat                       [Lancer serveur (batch)]
├── 📄 run-server.ps1                       [Lancer serveur (PowerShell)]
│
├── 📂 src/main/java/com/acme/cxf/
│   ├── 📄 App.java                         [Placeholder]
│   ├── 📄 Server.java                      ⭐ Serveur SOAP embarqué
│   ├── 📂 api/
│   │   └── 📄 HelloService.java            ⭐ Interface de service (contrat)
│   ├── 📂 impl/
│   │   └── 📄 HelloServiceImpl.java         ⭐ Implémentation
│   ├── 📂 model/
│   │   └── 📄 Person.java                  ⭐ Modèle JAXB
│   └── 📂 client/
│       └── 📄 SoapClient.java              [Client de test]
│
├── 📂 src/main/resources/
│   ├── 📄 request_sayHello.xml             [Exemple requête]
│   ├── 📄 request_findPerson.xml           [Exemple requête]
│   ├── 📄 response_sayHello.xml            [Exemple réponse]
│   └── 📄 response_findPerson.xml          [Exemple réponse]
│
├── 📂 src/test/java/com/acme/cxf/
│   └── 📄 HelloServiceTest.java            [Tests JUnit 5]
│
└── 📂 target/
    └── 📄 soap-cxf-service-1.0-SNAPSHOT.jar
```

---

## 🚀 Guide de Démarrage Rapide

### 1️⃣ Compiler
```bash
mvn clean package -DskipTests
```

### 2️⃣ Lancer le serveur
```bash
mvn exec:java
# OU
.\run-server.ps1
```

### 3️⃣ Accéder au service
- **URL** : `http://localhost:8080/services/hello`
- **WSDL** : `http://localhost:8080/services/hello?wsdl`

### 4️⃣ Tester
```bash
# Option A : SoapUI (GUI)
# - Importer WSDL → Créer tests → Exécuter

# Option B : PowerShell
.\test-soap-service.ps1

# Option C : curl
curl -X POST http://localhost:8080/services/hello \
  -H "Content-Type: text/xml" \
  -d @src/main/resources/request_sayHello.xml
```

---

## 🔑 Points Clés

### Annotations Importantes
| Annotation | Classe | Rôle |
|-----------|--------|------|
| `@WebService` | HelloService, HelloServiceImpl | Marquer comme service SOAP |
| `@WebMethod` | sayHello(), findPersonById() | Exposer comme opération SOAP |
| `@WebParam` | Paramètres | Nommer les paramètres SOAP |
| `@WebResult` | Retours | Nommer les résultats SOAP |
| `@XmlRootElement` | Person | Racine XML |
| `@XmlElement` | Propriétés | Éléments XML |

### Dépendances Principales
```xml
<!-- SOAP/JAX-WS -->
cxf-rt-frontend-jaxws

<!-- Transport HTTP -->
cxf-rt-transports-http
cxf-rt-transports-http-jetty

<!-- Sérialisation XML -->
jakarta.xml.bind-api
jaxb-runtime

<!-- Tests -->
junit-jupiter
```

### Architecture Globale
```
Requête HTTP SOAP
       ↓
Jetty (port 8080)
       ↓
CXF Interceptors (désérialisation)
       ↓
HelloServiceImpl (logique métier)
       ↓
JAXB (sérialisation XML)
       ↓
Réponse SOAP
```

---

## 📊 Opérations du Service

### Operation 1 : SayHello
```
Input  : String (name)
Output : String (greeting)
Exemple : "Alice" → "Bonjour, Alice"
```

### Operation 2 : FindPerson
```
Input  : String (id)
Output : Person (id, name, age)
Exemple : "123" → Person(id=123, name="Ada Lovelace", age=36)
```

---

## 🧪 Tests Disponibles

### Tests Unitaires
```bash
mvn test
```

Fichier : `HelloServiceTest.java`
- `testSayHello()` ✓
- `testSayHelloWithNull()` ✓
- `testFindPersonById()` ✓
- `testPersonSerialization()` ✓

### Tests Intégration (Manuel)
- Via SoapUI (GUI) - Voir `SOAPUI_GUIDE.md`
- Via PowerShell - `test-soap-service.ps1`
- Via curl - Voir `README.md`

---

## 📖 Documentation Supplémentaire

| Fichier | Contenu |
|---------|---------|
| **README.md** | Guide complet du projet |
| **WSDL_EXPLANATION.md** | Structure et exploitation du WSDL |
| **SOAPUI_GUIDE.md** | Tutoriel SoapUI pas à pas |
| **pom.xml** | Configuration Maven détaillée |

---

## ✨ Fonctionnalités Implémentées

✅ Service SOAP complet avec 2 opérations
✅ Modèle JAXB sérialisable en XML
✅ Serveur embarqué Jetty
✅ WSDL généré automatiquement par CXF
✅ Tests unitaires JUnit 5
✅ Client SOAP exemple
✅ Documentation complète
✅ Scripts de lancement
✅ Exemples de requêtes/réponses SOAP
✅ Guide SoapUI détaillé

---

## 🎯 Résultat Final

| Critère | Statut |
|---------|--------|
| Compilation sans erreur | ✅ OK |
| Service démarre | ✅ OK |
| WSDL accessible | ✅ OK |
| Opérations testables | ✅ OK |
| SoapUI compatible | ✅ OK |
| Tests unitaires | ✅ OK |
| Documentation | ✅ Complète |

---

**Note** : Pour les étapes suivantes (WS-Security, validation XML, etc.), voir les sections avancées dans `README.md`.

