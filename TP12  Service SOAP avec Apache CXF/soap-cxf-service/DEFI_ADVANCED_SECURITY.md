# Défi (Optionnel) — PasswordDigest + HTTPS + Signature + Chiffrement

## Objectif
Implémenter une sécurité avancée avec WS-Security :
- PasswordDigest (au lieu de PasswordText)
- HTTPS/TLS (Jetty ou Undertow)
- Signature numérique des messages
- Chiffrement des éléments sensibles

---

## 1. PasswordDigest vs PasswordText

### Comparaison

```
PasswordText (TP actuel)              PasswordDigest (Avancé)
├─ Mot de passe en clair              ├─ Hash SHA-1 du mot de passe
├─ Moins sécurisé                     ├─ Plus sécurisé
├─ Simple à tester                    ├─ Validation côté serveur
└─ À n'utiliser qu'en dev/test        └─ Production-ready
```

### Flux PasswordDigest

```
Client:
  username = "student"
  password = "secret123"
  nonce = generateRandomBytes(16)
  timestamp = getCurrentTimestamp()
  
  passwordDigest = SHA1(nonce + timestamp + password)
  
  Envoie : username, nonce, timestamp, passwordDigest
  
Serveur:
  Récupère le password réel de "student" → "secret123"
  Reconstruit : SHA1(nonce + timestamp + "secret123")
  Compare avec le passwordDigest reçu
  ✓ Match → Authentification réussie
  ✗ Mismatch → Authentification échouée
```

### Configuration dans SecureServer.java

```java
Map<String,Object> inProps = new HashMap<>();
inProps.put("action", "UsernameToken");
inProps.put("passwordType", "PasswordDigest");  // ← Changé de PasswordText
inProps.put("passwordCallbackRef", 
    new UTPasswordCallback(Map.of("student", "secret123"))
);
```

---

## 2. Configuration pour Signature + Chiffrement

### 2.1 Configuration dans `SecureServerHttps.java`

```java
Map<String,Object> inProps = new HashMap<>();

// Actions multiples
inProps.put("action", "UsernameToken Signature Encrypt");

// PasswordDigest
inProps.put("passwordType", "PasswordDigest");
inProps.put("passwordCallbackRef", 
    new UTPasswordCallback(Map.of("student", "secret123"))
);

// Signature numérique
inProps.put("signaturePropFile", "server-keystore.properties");

// Chiffrement
inProps.put("encryptionPropFile", "server-keystore.properties");
```

### 2.2 Fichier `server-keystore.properties`

```properties
# Localisation et type du keystore
org.apache.wss4j.crypto.provider=org.apache.wss4j.common.crypto.WSCryptoImpl
org.apache.wss4j.crypto.merlin.keystore.type=jks
org.apache.wss4j.crypto.merlin.keystore.password=keystorepassword
org.apache.wss4j.crypto.merlin.keystore.file=src/main/resources/keystore.jks

# Alias et password de la clé privée
org.apache.wss4j.crypto.merlin.keystore.alias=servicekey
org.apache.wss4j.crypto.merlin.key.password=keypassword
```

### 2.3 Créer le Keystore

```bash
# Générer clé privée et certificat (valide 365 jours)
keytool -genkey -alias servicekey -keyalg RSA -keysize 2048 \
  -keystore keystore.jks -storetype jks \
  -storepass keystorepassword -keypass keypassword \
  -dname "CN=HelloService,O=ACME,C=FR" \
  -validity 365

# Placer dans : src/main/resources/keystore.jks
```

---

## 3. Client avec Signature et Chiffrement

### Configuration `ClientPasswordCallback.java`

```java
package com.acme.cxf.security;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import org.apache.wss4j.common.ext.WSPasswordCallback;

public class ClientPasswordCallback implements CallbackHandler {
    private final String username;
    private final String password;

    public ClientPasswordCallback(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException {
        for (Callback cb : callbacks) {
            if (cb instanceof WSPasswordCallback pc) {
                pc.setPassword(password);
                pc.setIdentifier(username);
            }
        }
    }
}
```

### Configuration Client dans `SecureClientTest.java`

```java
Map<String, Object> outProps = new HashMap<>();
outProps.put("action", "UsernameToken Signature Encrypt");
outProps.put("user", "student");
outProps.put("passwordType", "PasswordDigest");
outProps.put("passwordCallbackRef", 
    new ClientPasswordCallback("student", "secret123")
);
outProps.put("signaturePropFile", "client-keystore.properties");
outProps.put("encryptionPropFile", "server-cert.properties");

WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
client.getOutInterceptors().add(wssOut);
```

---

## 4. Test d'Intégration Automatisé

### `SecureEndpointIntegrationTest.java`

Tests JUnit 5 pour vérifier :

```java
@Test
public void testSecureSayHelloWithValidCredentials() {
    String result = securePort.sayHello("TestUser");
    assertTrue(result.contains("Bonjour"));
}

@Test
public void testSecureFindPersonWithValidCredentials() {
    Person person = securePort.findPersonById("SEC-123");
    assertNotNull(person);
    assertEquals("Ada Lovelace", person.getName());
}
```

### Exécuter les tests

```bash
# Démarrer le service sécurisé en background
mvn exec:java -Dexec.mainClass="com.acme.cxf.SecureServer" &

# Attendre 5 secondes
sleep 5

# Lancer les tests d'intégration
mvn test -Dtest=SecureEndpointIntegrationTest

# Résultats attendus
# ✓ testSecureSayHelloWithValidCredentials
# ✓ testSecureFindPersonWithValidCredentials
# ✓ testSecurePersonSerialization
# ✓ testSecureMultipleRequests
```

---

## 5. Workflow Sécurisé Complet

### Requête SOAP Sécurisée (PasswordDigest + Signature + Chiffrement)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
               xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
               xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd">
    <soap:Header>
        <!-- Security Header -->
        <wsse:Security>
            <!-- UsernameToken avec PasswordDigest -->
            <wsse:UsernameToken>
                <wsse:Username>student</wsse:Username>
                <wsse:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest">
                    WeNfbJlWIVNDued1HQ3J9qXDPhQ=
                </wsse:Password>
                <wsse:Nonce>ToepAS8dSXAxwgqQ7X+DMA==</wsse:Nonce>
                <wsu:Created>2025-11-13T00:30:00Z</wsu:Created>
            </wsse:UsernameToken>
            
            <!-- Signature BinSecurityToken -->
            <wsse:BinarySecurityToken>MIIC...==</wsse:BinarySecurityToken>
            
            <!-- Signature -->
            <ds:Signature xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
                <ds:SignedInfo>
                    <ds:Reference URI="#id-1"/>
                </ds:SignedInfo>
                <ds:SignatureValue>ABC123...==</ds:SignatureValue>
            </ds:Signature>
        </wsse:Security>
    </soap:Header>
    
    <soap:Body wsu:Id="id-1">
        <!-- Body chiffré -->
        <xenc:EncryptedData xmlns:xenc="http://www.w3.org/2001/04/xmlenc#">
            <xenc:CipherData>
                <xenc:CipherValue>XYZ789...==</xenc:CipherValue>
            </xenc:CipherData>
        </xenc:EncryptedData>
    </soap:Body>
</soap:Envelope>
```

---

## 6. HTTPS avec Jetty

### Dépendance pour Jetty HTTPS

```xml
<dependency>
    <groupId>org.apache.cxf</groupId>
    <artifactId>cxf-rt-transports-http-jetty</artifactId>
    <version>${cxf.version}</version>
</dependency>

<dependency>
    <groupId>org.eclipse.jetty</groupId>
    <artifactId>jetty-server</artifactId>
    <version>11.0.17</version>
</dependency>

<dependency>
    <groupId>org.eclipse.jetty</groupId>
    <artifactId>jetty-util</artifactId>
    <version>11.0.17</version>
</dependency>
```

### Configuration HTTPS dans `SecureServerHttps.java`

```java
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class SecureServerHttps {
    public static void main(String[] args) throws Exception {
        // Configuration Jetty HTTPS
        SslContextFactory.Server sslFactory = new SslContextFactory.Server();
        sslFactory.setKeyStorePath("src/main/resources/keystore.jks");
        sslFactory.setKeyStorePassword("keystorepassword");
        sslFactory.setKeyStoreType("jks");

        Server jettyServer = new Server();
        ServerConnector sslConnector = new ServerConnector(jettyServer, sslFactory);
        sslConnector.setPort(8443);
        jettyServer.addConnector(sslConnector);

        // Rest of CXF configuration...
    }
}
```

---

## 7. Checklist Défi

- [ ] `SecureServerHttps.java` créé avec PasswordDigest
- [ ] `server-keystore.properties` configuré
- [ ] `keystore.jks` généré et placé dans `src/main/resources/`
- [ ] `ClientPasswordCallback.java` implémenté
- [ ] `SecureClientTest.java` lancé avec succès
- [ ] `SecureEndpointIntegrationTest.java` tous les tests passent
- [ ] Signature numérique validée (logs CXF)
- [ ] Chiffrement activé (données non lisibles en clair)
- [ ] HTTPS sur port 8443 (optionnel)
- [ ] Rapports de test générés

---

## 8. Commandes de Test

```bash
# Compiler tous les fichiers
mvn clean compile

# Générer le keystore
keytool -genkey -alias servicekey -keyalg RSA -keysize 2048 \
  -keystore src/main/resources/keystore.jks -storetype jks \
  -storepass keystorepassword -keypass keypassword \
  -dname "CN=HelloService,O=ACME,C=FR" -validity 365

# Démarrer le serveur sécurisé
mvn exec:java -Dexec.mainClass="com.acme.cxf.SecureServerHttps" &

# Lancer les tests d'intégration
mvn test -Dtest=SecureEndpointIntegrationTest

# Lancer le client de test
mvn exec:java -Dexec.mainClass="com.acme.cxf.SecureClientTest"

# Voir les logs en debug
mvn -X exec:java -Dexec.mainClass="com.acme.cxf.SecureServer"
```

---

## 9. Résultat Attendu

```
=== Secure Client Test (PasswordDigest) ===

--- Test 1: SayHello avec PasswordDigest ---
✓ Réponse: Bonjour, SecureClient

--- Test 2: FindPerson avec PasswordDigest ---
✓ ID: PRO-999
✓ Nom: Ada Lovelace
✓ Âge: 36

=== Tests Terminés ===
```

Et dans les logs :

```
DEBUG org.apache.wss4j - Processing UsernameToken with PasswordDigest
DEBUG org.apache.wss4j - Signature validation: SUCCESS
DEBUG org.apache.wss4j - Decryption: SUCCESS
```

---

## 10. Production Checklist

- ✓ PasswordDigest activé (jamais PasswordText en prod)
- ✓ HTTPS/TLS activé (port 8443)
- ✓ Signature numérique des messages
- ✓ Chiffrement des éléments sensibles
- ✓ Keystore sécurisé (permissions 600)
- ✓ Certificats auto-signés ou CA-signés
- ✓ Logs audité (WSS4J debug)
- ✓ Tests d'intégration automatisés
- ✓ Rotation des clés planifiée
- ✓ Backup du keystore

