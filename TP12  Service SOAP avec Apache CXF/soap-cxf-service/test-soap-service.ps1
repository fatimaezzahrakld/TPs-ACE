# Test SOAP Service avec PowerShell et curl
# Ce script teste les opérations du service SOAP CXF

$serviceUrl = "http://localhost:8080/services/hello"
$headers = @{
    "Content-Type" = "text/xml; charset=UTF-8"
    "SOAPAction"   = ""
}

Write-Host "=== Test du Service SOAP CXF ===" -ForegroundColor Green
Write-Host "URL du service: $serviceUrl" -ForegroundColor Cyan
Write-Host ""

# Test 1: SayHello
Write-Host "--- Test 1: Operation SayHello ---" -ForegroundColor Yellow
$soapRequest = @"
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:api="http://api.cxf.acme.com/">
   <soap:Body>
      <api:sayHello>
         <name>Bob</name>
      </api:sayHello>
   </soap:Body>
</soap:Envelope>
"@

try {
    $response = Invoke-WebRequest -Uri $serviceUrl `
                                  -Method Post `
                                  -Headers $headers `
                                  -Body $soapRequest `
                                  -ErrorAction SilentlyContinue
    
    Write-Host "Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Response:" -ForegroundColor Green
    Write-Host $response.Content
    Write-Host ""
} catch {
    Write-Host "Erreur: $_" -ForegroundColor Red
    Write-Host ""
}

# Test 2: FindPerson
Write-Host "--- Test 2: Operation FindPerson ---" -ForegroundColor Yellow
$soapRequest = @"
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:api="http://api.cxf.acme.com/">
   <soap:Body>
      <api:findPersonById>
         <id>42</id>
      </api:findPersonById>
   </soap:Body>
</soap:Envelope>
"@

try {
    $response = Invoke-WebRequest -Uri $serviceUrl `
                                  -Method Post `
                                  -Headers $headers `
                                  -Body $soapRequest `
                                  -ErrorAction SilentlyContinue
    
    Write-Host "Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Response:" -ForegroundColor Green
    Write-Host $response.Content
    Write-Host ""
} catch {
    Write-Host "Erreur: $_" -ForegroundColor Red
    Write-Host ""
}

# Test 3: Récupérer le WSDL
Write-Host "--- Test 3: Récupérer le WSDL ---" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$serviceUrl`?wsdl" -ErrorAction SilentlyContinue
    Write-Host "Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "WSDL reçu (premiers 500 caractères):" -ForegroundColor Green
    Write-Host $response.Content.Substring(0, [Math]::Min(500, $response.Content.Length))
    Write-Host ""
} catch {
    Write-Host "Erreur: $_" -ForegroundColor Red
}

Write-Host "=== Tests Terminés ===" -ForegroundColor Green
