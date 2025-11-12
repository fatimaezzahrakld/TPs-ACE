# Guide SoapUI pour tester le Service SOAP CXF

## Introduction
SoapUI est un outil graphique pour tester, développer et déboguer les web services SOAP et REST. Ce guide montre comment utiliser SoapUI pour tester le service `HelloService`.

---

## 1. Installation de SoapUI

### Téléchargement
- Visiter : https://www.soapui.org/downloads/soapui/
- Télécharger la version **SoapUI 5.7.0** (Community Edition) ou plus récente
- Installer en suivant l'assistant

### Prérequis
- Java 8+ doit être installé
- Port 8080 disponible pour le service SOAP

---

## 2. Lancer le Service

Avant de tester avec SoapUI, le service doit être actif :

```bash
cd soap-cxf-service
mvn exec:java
```

Le service sera disponible à : **http://localhost:8080/services/hello**
Le WSDL sera accessible à : **http://localhost:8080/services/hello?wsdl**

---

## 3. Créer un Projet SOAP dans SoapUI

### Étape 1 : Créer un nouveau projet SOAP
1. Ouvrir SoapUI
2. Menu → **File** → **New SOAP Project**
3. Donner un nom : `HelloService-Test`
4. URL du WSDL : `http://localhost:8080/services/hello?wsdl`
5. ✓ Cocher **Create sample requests for all operations**
6. Cliquer **OK**

### Étape 2 : Résultat
SoapUI génère automatiquement :
- **Interface** : HelloService (portType)
- **Binding** : HelloServiceBinding
- **Service** : HelloService
- **Opérations** :
  - FindPerson
  - SayHello

Chaque opération contient une requête SOAP exemple modifiable.

---

## 4. Structure du Projet dans SoapUI

```
HelloService-Test
├── HelloService (Service)
│   ├── HelloServiceBinding (Binding SOAP)
│   │   ├── FindPerson (Request/Response)
│   │   │   ├── Request 1 (exemple généré)
│   │   │   └── Response
│   │   └── SayHello (Request/Response)
│   │       ├── Request 1 (exemple généré)
│   │       └── Response
│   └── Types (définitions XSD)
│       ├── Person (complexType)
│       ├── id (string)
│       ├── name (string)
│       └── age (int)
└── Coverage (rapports de test)
```

---

## 5. Tester l'Opération SayHello

### Requête SOAP Générée
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:api="http://api.cxf.acme.com/">
   <soap:Body>
      <api:sayHello>
         <name>?</name>
      </api:sayHello>
   </soap:Body>
</soap:Envelope>
```

### Modifier et Exécuter
1. Double-cliquer sur **SayHello → Request 1**
2. Remplacer `<name>?</name>` par `<name>Alice</name>`
3. Cliquer le bouton **Submit** (flèche verte) ou **Ctrl+Enter**

### Réponse Attendue
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <ns2:sayHelloResponse xmlns:ns2="http://api.cxf.acme.com/">
         <greeting>Bonjour, Alice</greeting>
      </ns2:sayHelloResponse>
   </soap:Body>
</soap:Envelope>
```

**Résultat** : ✓ Appel réussi - La salutation est personnalisée avec le nom envoyé.

---

## 6. Tester l'Opération FindPerson

### Requête SOAP Générée
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:api="http://api.cxf.acme.com/">
   <soap:Body>
      <api:findPersonById>
         <id>?</id>
      </api:findPersonById>
   </soap:Body>
</soap:Envelope>
```

### Modifier et Exécuter
1. Double-cliquer sur **FindPerson → Request 1**
2. Remplacer `<id>?</id>` par `<id>123</id>`
3. Cliquer **Submit** ou **Ctrl+Enter**

### Réponse Attendue
```xml
<?xml version="1.0" encoding="UTF-8"?>
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

**Résultat** : ✓ Appel réussi - Objet Person sérialisé en XML via JAXB.

---

## 7. Fonctionnalités Avancées dans SoapUI

### 7.1 Assertions (Vérifications)
Automatiser les validations :

1. **Add Assertion** dans la requête
2. Types d'assertions :
   - **SOAP Response** : Vérifier qu'une réponse SOAP valide a été reçue
   - **Contains** : Vérifier qu'une chaîne est présente
   - **XPath Match** : Vérifier une valeur XPath

Exemple : Vérifier que `<greeting>` contient "Bonjour"
```xpath
//ns2:sayHelloResponse/greeting
```

### 7.2 Test Suites
Grouper plusieurs tests :
1. Créer une **Test Suite** : `MyTestSuite`
2. Créer plusieurs **Test Cases** pour chaque opération
3. Exécuter tous les tests ensemble

### 7.3 Rapport de Couverture
- Automatiquement généré après les tests
- Montre le % d'opérations testées
- Utile pour valider la qualité

### 7.4 Mock Service
Créer un service simulé :
1. Menu → **Tools** → **Create Mock Service**
2. Sélectionner les opérations
3. Définir des réponses simulées
4. Utile pour le développement côté client

---

## 8. Erreurs Courantes et Solutions

| Erreur | Cause | Solution |
|--------|-------|----------|
| `Unable to import WSDL` | Service pas démarré | Lancer `mvn exec:java` |
| `Connection refused` | Port 8080 indisponible | Vérifier `netstat -an \| find "8080"` |
| `SOAP Fault` | Erreur métier | Vérifier les logs du serveur |
| `Type mismatch` | Paramètres incorrects | Vérifier le schéma XSD dans SoapUI |

---

## 9. Exportation et Partage

### Exporter le Projet
- Menu → **File** → **Export Project**
- Format : `*.soapui` (XML compressé)
- Partager avec l'équipe

### Exporter les Rapports
- Après les tests : **Tools** → **Report**
- Générer un PDF/HTML du rapport de test

---

## 10. Intégration Continuous Integration

### Lancer les tests SoapUI en CLI
```bash
testrunner.sh -r html HelloService-Test.xml
```

Utile pour les pipelines CI/CD (Jenkins, GitLab CI, etc.)

---

## Résumé des Validations

✓ Service démaré sur `http://localhost:8080/services/hello`
✓ WSDL accessible et valide
✓ Opération `SayHello` retourne un message personnalisé
✓ Opération `FindPerson` retourne un objet sérialisé
✓ Conformité contractuelle validée par SoapUI

