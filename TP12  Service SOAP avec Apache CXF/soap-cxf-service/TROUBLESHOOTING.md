# Étape 11 — Dépannage Raisonné

## Objectif
Guide pratique pour diagnostiquer et résoudre les problèmes courants lors du développement de services SOAP avec Apache CXF.

---

## 1. WSDL Introuvable

### Symptôme
```
Error: WSDL not found
Unable to import WSDL from http://localhost:8080/services/hello?wsdl
```

### Causes Possibles

#### 1.1 Service non démarré
**Vérification :**
```powershell
# Vérifier que le serveur écoute sur le port
netstat -ano | findstr "8080"
```

**Solution :**
```bash
cd soap-cxf-service
mvn exec:java  # Ou mvn exec:java -Dexec.mainClass="com.acme.cxf.Server"
```

#### 1.2 URL incorrecte
**Vérification :**
```powershell
# Essayer d'accéder à l'URL
Invoke-WebRequest -Uri "http://localhost:8080/services/hello?wsdl"
```

**Endroits courants :**
- ✓ `http://localhost:8080/services/hello?wsdl` (endpoint standard)
- ✓ `http://localhost:8080/services/hello-secure?wsdl` (endpoint sécurisé)
- ✗ `http://localhost:8080/services/hello` (sans `?wsdl`)
- ✗ `http://localhost:8080/services/` (dossier parent)

**Solution :**
Vérifier dans `Server.java` ou `SecureServer.java` :
```java
factory.setAddress("http://localhost:8080/services/hello");  // ✓ Correct
```

#### 1.3 Firewall/Proxy bloquant
**Vérification :**
```bash
# Test direct depuis le serveur
curl http://localhost:8080/services/hello?wsdl

# Test depuis une machine distante
curl http://<IP-SERVER>:8080/services/hello?wsdl
```

**Solution :**
- Ouvrir le port 8080 dans le firewall
- Tester en localhost d'abord
- Vérifier les logs CXF

---

## 2. Classes jakarta.* Manquantes

### Symptôme
```
Error: package jakarta.xml.ws does not exist
Error: package jakarta.jws does not exist
ClassNotFoundException: jakarta.xml.bind.annotation.XmlElement
```

### Cause
Mismatch entre versions CXF et Java.

```
CXF 3.x  →  javax.xml.ws (Java 8-10)
CXF 4.x  →  jakarta.xml.ws (Java 11+)
```

### Matrice de Compatibilité

| CXF Version | Java Version | Package | Status |
|------------|-------------|---------|--------|
| 3.5.x | 8, 11 | `javax.*` | Legacy |
| 4.0.x | 11, 17+ | `jakarta.*` | ✓ Actuel |
| 4.1.x | 11, 17+ | `jakarta.*` | ✓ Stable |

### Solutions

#### Solution 1 : Vérifier pom.xml

```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>  <!-- ✓ 17+ pour CXF 4.x -->
    <cxf.version>4.0.3</cxf.version>  <!-- ✓ Version 4.x -->
</properties>

<dependency>
    <groupId>org.apache.cxf</groupId>
    <artifactId>cxf-rt-frontend-jaxws</artifactId>
    <version>${cxf.version}</version>
</dependency>

<dependency>
    <groupId>jakarta.xml.bind</groupId>  <!-- ✓ Jakarta pour CXF 4.x -->
    <artifactId>jakarta.xml.bind-api</artifactId>
    <version>4.0.1</version>
</dependency>
```

#### Solution 2 : Recompiler et nettoyer

```bash
# Nettoyer la cache Maven
mvn clean

# Télécharger les dépendances à nouveau
mvn dependency:resolve

# Recompiler
mvn compile
```

#### Solution 3 : Vérifier dans l'IDE

**IntelliJ IDEA :**
- File → Project Structure → Project Settings → SDK
- Vérifier que Java 17+ est sélectionné

**Eclipse :**
- Project → Properties → Java Compiler
- Vérifier Java 11 ou 17

---

## 3. SOAP Fault sans Détail

### Symptôme
```xml
<soap:Fault>
    <faultcode>Server</faultcode>
    <faultstring>Internal Server Error</faultstring>
</soap:Fault>
```

### Diagnostic : Activer les logs DEBUG

#### 3.1 Configuration Logback

Créer `src/main/resources/logback.xml` :

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- CXF Debugging -->
    <logger name="org.apache.cxf" level="DEBUG"/>
    <logger name="org.apache.wss4j" level="DEBUG"/>

    <root level="INFO">
        <appender-ref ref="STDOUT"/>
    </root>
</configuration>
```

#### 3.2 Configuration Log4j2

Créer `src/main/resources/log4j2.properties` :

```properties
# Set to debug or trace if log4j initialization is failing
status = warn

# Name of the configuration
name = SOAPDebug

# Console appender reference
appenders = console

appender.console.type = Console
appender.console.name = STDOUT
appender.console.layout.type = PatternLayout
appender.console.layout.pattern = %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n

# CXF loggers
logger.cxf.name = org.apache.cxf
logger.cxf.level = debug
logger.cxf.additivity = false
logger.cxf.appenderRefs = console
logger.cxf.appenderRef.console.ref = STDOUT

logger.wss4j.name = org.apache.wss4j
logger.wss4j.level = debug

rootLogger.level = info
rootLogger.appenderRefs = console
rootLogger.appenderRef.console.ref = STDOUT
```

#### 3.3 Ajouter la dépendance Logback

```xml
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.4.12</version>
</dependency>
```

### Diagnostic : Inspecter les logs

```bash
mvn exec:java 2>&1 | tee server.log

# Chercher les erreurs
grep -i "error\|exception\|fault" server.log
```

### Diagnostic : SoapUI Raw View

1. **Envoyer la requête dans SoapUI**
2. **Cliquer l'onglet "Raw"**
3. **Inspecter la réponse complète :**

```xml
<!-- Réponse brute avec détails d'erreur -->
<soap:Envelope>
    <soap:Body>
        <soap:Fault>
            <faultcode>Server</faultcode>
            <faultstring>java.lang.NullPointerException: Person not found</faultstring>
            <detail>
                <stackTrace>
                    at com.acme.cxf.impl.HelloServiceImpl.findPersonById(HelloServiceImpl.java:25)
                    ...
                </stackTrace>
            </detail>
        </soap:Fault>
    </soap:Body>
</soap:Envelope>
```

### Causes Communes

| Erreur | Cause | Solution |
|--------|-------|----------|
| `NullPointerException` | Paramètre null non géré | Ajouter `if (param == null)` |
| `ClassNotFoundException` | Classe manquante en classpath | Vérifier pom.xml |
| `Port already in use` | Port occupé | Voir section 4 |
| `Unauthorized` | UsernameToken invalide | Voir section 5 |
| `No such method` | Opération WSDL ≠ implémentation | Vérifier signatures |

---

## 4. Port 8080 Occupé

### Symptôme
```
java.net.BindException: Address already in use: bind
```

### Diagnostic : Trouver le processus

```powershell
# Trouver le PID écoutant sur 8080
netstat -ano | findstr ":8080"

# Exemple output:
# TCP    127.0.0.1:8080    0.0.0.0:0    LISTENING    12345

# Voir le processus
tasklist /FI "PID eq 12345"
```

### Solutions

#### Solution 1 : Tuer le processus existant

```powershell
# Tuer le processus (Windows)
taskkill /PID 12345 /F

# Ou sur Linux/Mac
kill -9 12345
```

#### Solution 2 : Changer le port

Modifier `Server.java` :

```java
public class Server {
  public static void main(String[] args) {
    String address = "http://localhost:9090/services/hello";  // ✓ Port 9090
    JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
    factory.setServiceClass(HelloServiceImpl.class);
    factory.setAddress(address);
    factory.create();
    System.out.println("WSDL: " + address + "?wsdl");
  }
}
```

#### Solution 3 : Utiliser un port aléatoire

```java
public class Server {
  public static void main(String[] args) throws Exception {
    // Chercher un port libre
    ServerSocket socket = new ServerSocket(0);
    int port = socket.getLocalPort();
    socket.close();

    String address = "http://localhost:" + port + "/services/hello";
    // ... rest of code
  }
}
```

---

## 5. UsernameToken Rejeté

### Symptôme
```xml
<soap:Fault>
    <faultcode>SecurityFault</faultcode>
    <faultstring>UsernameToken authentication failed</faultstring>
</soap:Fault>
```

### Cause 1 : Mauvais PasswordType

#### Vérification dans SecureServer.java

```java
Map<String,Object> inProps = new HashMap<>();
inProps.put("passwordType", "PasswordText");  // ✓ OU "PasswordDigest"
```

#### Dans SoapUI

1. **Operations → SayHello Request**
2. **Onglet : Auth**
3. **WS-Security Configuration :**
   - Username: `student`
   - Password: `secret123`
   - **Password Type: Text** (doit correspondre à SecureServer)

### Cause 2 : Utilisateur non enregistré

#### Vérification dans SecureServer.java

```java
inProps.put("passwordCallbackRef", 
  new UTPasswordCallback(
    Map.of(
      "student", "secret123",   // ✓ Ces credentials doivent exister
      "admin", "admin123"
    )
  )
);
```

#### Diagnostic

Activer les logs DEBUG (voir section 3) et chercher :

```
DEBUG org.apache.wss4j - User 'alice' not found in callback map
```

**Solution :**
Vérifier que SoapUI envoie le bon username :

```xml
<!-- Request SOAP brut (Raw) -->
<wsse:UsernameToken>
    <wsse:Username>student</wsse:Username>
    <wsse:Password>secret123</wsse:Password>
</wsse:UsernameToken>
```

### Cause 3 : Mismatch de mot de passe

**Vérification :**
```java
// UTPasswordCallback.java
@Override
public void handle(Callback[] callbacks) throws IOException {
    for (Callback cb : callbacks) {
      if (cb instanceof WSPasswordCallback pc) {
        String pass = users.get(pc.getIdentifier());
        if (pass != null) {
          pc.setPassword(pass);  // ✓ Doit correspondre à SoapUI
        } else {
          // Utilisateur non trouvé
          throw new IOException("User " + pc.getIdentifier() + " not found");
        }
      }
    }
}
```

### Cause 4 : Endpoint sécurisé différent

**Vérification :**
- Server.java publie sur : `http://localhost:8080/services/hello`
- SecureServer.java publie sur : `http://localhost:8080/services/hello-secure`

**SoapUI doit importer :**
```
http://localhost:8080/services/hello-secure?wsdl  (pour le sécurisé)
```

### Table de Diagnostic UsernameToken

| Symptôme | Cause | Check |
|----------|-------|-------|
| `UsernameToken missing` | Pas d'auth en SoapUI | Activer WS-Security |
| `Password mismatch` | Mot de passe incorrect | Vérifier Map users |
| `User not found` | Username n'existe pas | Ajouter user dans Map |
| `Digest failed` | PasswordType incorrect | Aligner Text vs Digest |
| `Timestamp invalid` | Horloges décalées | Synchroniser NTP |

---

## 6. Checklist Complète de Dépannage

### ✓ Avant de lancer
- [ ] Compilation sans erreur : `mvn clean compile`
- [ ] Toutes dépendances téléchargées : `mvn dependency:resolve`
- [ ] Java 17+ installé : `java -version`
- [ ] Port 8080 libre : `netstat -ano | findstr "8080"`

### ✓ Lors du démarrage
- [ ] Service démarre sans exception
- [ ] Message "WSDL: ..." s'affiche
- [ ] Accès localhost:8080/services/hello réussit
- [ ] WSDL téléchargeable : `curl http://localhost:8080/services/hello?wsdl`

### ✓ Dans SoapUI
- [ ] WSDL s'importe sans erreur
- [ ] Opérations visibles (SayHello, FindPerson)
- [ ] Requête exemple générée
- [ ] Raw tab montre XML valide

### ✓ Sécurisé (SecureServer)
- [ ] WS-Security activé dans pom.xml
- [ ] SecureServer.java démarre
- [ ] Endpoint different: `hello-secure`
- [ ] SoapUI: Username + Password configurés

---

## 7. Commandes Utiles

```bash
# Compiler et voir erreurs
mvn clean compile

# Nettoyer complètement
mvn clean

# Telecharger dépendances
mvn dependency:resolve

# Lancer avec logs
mvn exec:java -X  # Extra debug (-X = verbose)

# Lancer avec output en fichier
mvn exec:java > server.log 2>&1

# Tester WSDL via curl
curl -v http://localhost:8080/services/hello?wsdl

# Tester requête SOAP via curl
curl -X POST http://localhost:8080/services/hello \
  -H "Content-Type: text/xml" \
  -d @request.xml
```

---

## 8. Support des Versions

### Vérifier les versions installées

```bash
# Java
java -version
# Sortie : openjdk version "17.0.12"

# Maven
mvn --version
# Sortie : Apache Maven 3.9.11

# CXF dans pom.xml
grep "cxf.version" pom.xml
# Sortie : 4.0.3
```

### Ressources Documentation

- **CXF Docs** : https://cxf.apache.org/docs/index.html
- **Jakarta EE** : https://jakarta.ee/
- **WSS4J** : https://ws.apache.org/wss4j/
- **SoapUI Help** : https://www.soapui.org/docs/

