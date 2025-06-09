@echo off
cd /d "%~dp0"

set PORT=9000
start java -jar SpringCloudConfigServer\target\SpringCloudConfigServer-1.0.0.jar

set PROFILE=eurekaservice1
start java -jar EurekaDiscoveryService\target\EurekaDiscoveryService-1.0.0.jar

set PROFILE=eurekaservice2
start java -jar EurekaDiscoveryService\target\EurekaDiscoveryService-1.0.0.jar

set PROFILE=eurekaservice3
start java -jar EurekaDiscoveryService\target\EurekaDiscoveryService-1.0.0.jar

set PORT=8010
start java -jar SpringCloudAPIGateway\target\SpringCloudAPIGateway-1.0.0.jar

set PORT=1001
start java -jar UserService\target\UserService-1.0.0.jar

set PORT=2001
start java -jar ProductService\target\ProductService-1.0.0.jar

set PORT=3001
start java -jar OrderService\target\OrderService-1.0.0.jar

pause
