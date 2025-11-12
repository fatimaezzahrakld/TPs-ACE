$projectPath = "c:\Users\ezzah\Desktop\TPs-ACE\TP12  Service SOAP avec Apache CXF\soap-cxf-service"
cd $projectPath

Write-Host "Building classpath..."

# Generate dependency classpath
mvn dependency:build-classpath -q -Dmdep.outputFile=target/classpath.txt 2>&1 | Out-Null

$jarFile = "$projectPath\target\soap-cxf-service-1.0-SNAPSHOT.jar"
$cpFile = "$projectPath\target\classpath.txt"

if (Test-Path $cpFile) {
    $deps = Get-Content $cpFile -Raw
    $fullClassPath = "$jarFile;$deps"
    Write-Host "Classpath built. Starting server..."
} else {
    Write-Host "Warning: classpath file not found, using jar only"
    $fullClassPath = $jarFile
}

# Launch server
Write-Host "Starting SOAP CXF Server on http://localhost:8080/services/hello"
Write-Host "WSDL: http://localhost:8080/services/hello?wsdl"
Write-Host ""
java -cp $fullClassPath com.acme.cxf.Server
