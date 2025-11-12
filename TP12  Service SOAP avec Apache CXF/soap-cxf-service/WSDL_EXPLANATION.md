# Structure du WSDL généré par CXF

## Sections principales du WSDL

### 1. **types**
Définit les types de données utilisés dans le service.
- Contient les définitions XSD (XML Schema)
- Exemple : la classe `Person` est transformée en schéma XSD

```xml
<wsdl:types>
    <xsd:schema targetNamespace="http://api.cxf.acme.com/">
        <xsd:element name="Person">
            <xsd:complexType>
                <xsd:sequence>
                    <xsd:element name="id" type="xsd:string"/>
                    <xsd:element name="name" type="xsd:string"/>
                    <xsd:element name="age" type="xsd:int"/>
                </xsd:sequence>
            </xsd:complexType>
        </xsd:element>
    </xsd:schema>
</wsdl:types>
```

### 2. **messages**
Définit les requêtes et réponses pour chaque opération.
- Message d'entrée (request)
- Message de sortie (response)

```xml
<wsdl:message name="SayHello">
    <wsdl:part name="parameters" element="tns:sayHello"/>
</wsdl:message>
<wsdl:message name="SayHelloResponse">
    <wsdl:part name="parameters" element="tns:sayHelloResponse"/>
</wsdl:message>
```

### 3. **portType**
Interface logique du service (contrat)
- Définit les opérations disponibles
- Relie les messages d'entrée/sortie

```xml
<wsdl:portType name="HelloService">
    <wsdl:operation name="SayHello">
        <wsdl:input message="tns:SayHello"/>
        <wsdl:output message="tns:SayHelloResponse"/>
    </wsdl:operation>
    <wsdl:operation name="FindPerson">
        <wsdl:input message="tns:FindPerson"/>
        <wsdl:output message="tns:FindPersonResponse"/>
    </wsdl:operation>
</wsdl:portType>
```

### 4. **binding**
Spécifie le protocole de transport (SOAP, HTTP, etc.)
- Protocole : SOAP 1.2 ou 1.1
- Style : Document/Literal ou RPC/Encoded

```xml
<wsdl:binding name="HelloServiceBinding" type="tns:HelloService">
    <soap:binding style="document" transport="http://schemas.xmlsoap.org/soap/http"/>
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
```

### 5. **service**
Définit l'endpoint réel (URL où le service est accessible)

```xml
<wsdl:service name="HelloService">
    <wsdl:documentation>Service SOAP avec Apache CXF</wsdl:documentation>
    <wsdl:port name="HelloServicePort" binding="tns:HelloServiceBinding">
        <soap:address location="http://localhost:8080/services/hello"/>
    </wsdl:port>
</wsdl:service>
```

## Exploitation pratique

### Option 1 : Utiliser SoapUI
1. Télécharger SoapUI depuis https://www.soapui.org/
2. Créer un nouveau projet SOAP
3. Importer le WSDL : `http://localhost:8080/services/hello?wsdl`
4. Tester les opérations directement via l'interface graphique

### Option 2 : Générer les stubs clients avec wsdl2java
Le plugin `cxf-codegen-plugin` dans le pom.xml génère automatiquement les classes clientes :

```bash
mvn clean generate-sources
```

Cela crée les fichiers dans `target/generated-sources/cxf/com/acme/cxf/client/`

Les classes générées incluent :
- **HelloService** : Interface service
- **HelloServiceService** : Service factory
- **Person** : Classe métier sérialisable

### Option 3 : Utiliser curl ou Postman
Créer une requête SOAP manuelle :

```xml
POST http://localhost:8080/services/hello HTTP/1.1
Content-Type: text/xml; charset=UTF-8

<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:api="http://api.cxf.acme.com/">
    <soap:Body>
        <api:sayHello>
            <name>Alice</name>
        </api:sayHello>
    </soap:Body>
</soap:Envelope>
```

## Namespace WSDL
- `targetNamespace="http://api.cxf.acme.com/"` : Identifie uniquement le service
- Utilisé pour éviter les collisions de noms dans les appels SOAP
