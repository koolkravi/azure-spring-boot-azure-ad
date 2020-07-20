#  Microsoft identity platform and OAuth 2.0 authorization code flow

- This sample demonstrates a Java web application calling a Microsoft Graph that is secured using Azure Active Directory.
-- The Java web application uses the Microsoft Authentication Library for Java (MSAL4J) to obtain an:
--- Id Token from Azure Active Directory (Azure AD) to sign in an user
--- Access token that is used as a bearer token when calling the Microsoft Graph to get basic information of the signed-in user.

## Step 1 : Clone or download this repository
```
git clone https://github.com/Azure-Samples/ms-identity-java-webapp.git
msal-java-webapp-sample
```
## Step 2: Register the app (java-webapp)
```
name : java-webapp
```

## Step 3: Configure the sample to use your Azure AD tenant
```
#application.properties

#keytool -genkeypair -alias testCert -keyalg RSA -storetype PKCS12 -keystore keystore.p12 -storepass password
Put the generated keystore file in the "resources" folder.
```

## Step 4: Run the application
```
mvn clean package spring-boot:run

home page URL is https://localhost:8443

Once deployed, go to https://localhost:8443/msal4jsample
```

# Issue : This Spring boot application is not running with SSL certificate but other sample springboot application is running ok.
```
2020-07-20 19:17:23.899 ERROR 9120 --- [           main] org.apache.tomcat.util.net.SSLUtilBase   : Failed to load keystore type [PKCS12 ] with path [file:/D:/sample/ms-identity-java-webapp-master/msal-java-webapp-sample/target/classes/keystore.p12%20%20] due to [PKCS12  not found]

java.security.KeyStoreException: PKCS12  not found
	at java.security.KeyStore.getInstance(KeyStore.java:851) ~[na:1.8.0_202]
	at org.apache.tomcat.util.net.SSLUtilBase.getStore(SSLUtilBase.java:177) [tomcat-embed-core-9.0.17.jar:9.0.17]
	at org.apache.tomcat.util.net.SSLHostConfigCertificate.getCertificateKeystore(SSLHostConfigCertificate.java:206) [tomcat-embed-core-9.0.17.jar:9.0.17]
	at org.apache.tomcat.util.net.SSLUtilBase.getKeyManagers(SSLUtilBase.java:272) [tomcat-embed-core-9.0.17.jar:9.0.17]
	at org.apache.tomcat.util.net.SSLUtilBase.createSSLContext(SSLUtilBase.java:239) [tomcat-embed-core-9.0.17.jar:9.0.17]

Caused by: java.security.NoSuchAlgorithmException: PKCS12  KeyStore not available
	at sun.security.jca.GetInstance.getInstance(GetInstance.java:159) ~[na:1.8.0_202]


Caused by: java.io.IOException: Failed to load keystore type [PKCS12 ] with path [file:/D:/sample/ms-identity-java-webapp-master/msal-java-webapp-sample/target/classes/keystore.p12%20%20] due to [PKCS12  not found]
	at org.apache.tomcat.util.net.SSLUtilBase.getStore(SSLUtilBase.java:221) ~[tomcat-embed-core-9.0.17.jar:9.0.17]
	at org.apache.tomcat.util.net.SSLHostConfigCertificate.getCertificateKeystore(SSLHostConfigCertificate.java:206) ~[tomcat-embed-core-9.0.17.jar:9.0.17]
	at org.apache.tomcat.util.net.SSLUtilBase.getKeyManagers(SSLUtilBase.java:272) ~[tomcat-embed-core-9.0.17.jar:9.0.17]
``` 


# References : 
## 1.Microsoft identity platform and OAuth 2.0 authorization code flow
```
https://docs.microsoft.com/en-us/azure/active-directory/develop/v2-oauth2-auth-code-flow
```

## 2. sample - msal-java-webapp-sample
```
https://docs.microsoft.com/en-in/azure/active-directory/develop/quickstart-v2-java-webapp
#https://github.com/Azure-Samples/ms-identity-java-webapp/tree/master/msal-java-webapp-sample
#https://github.com/Azure-Samples/ms-identity-java-webapp
#https://docs.microsoft.com/en-us/azure/active-directory/develop/sample-v2-code
```

## 3. v2-oauth2-auth-code-flow
### https://tools.ietf.org/html/rfc6749#section-4.1
### 4.1.  Authorization Code Grant
```

   The authorization code grant type is used to obtain both access
   tokens and refresh tokens and is optimized for confidential clients.
   Since this is a redirection-based flow, the client must be capable of
   interacting with the resource owner's user-agent (typically a web
   browser) and capable of receiving incoming requests (via redirection)
   from the authorization server.

     +----------+
     | Resource |
     |   Owner  |
     |          |
     +----------+
          ^
          |
         (B)
     +----|-----+          Client Identifier      +---------------+
     |         -+----(A)-- & Redirection URI ---->|               |
     |  User-   |                                 | Authorization |
     |  Agent  -+----(B)-- User authenticates --->|     Server    |
     |          |                                 |               |
     |         -+----(C)-- Authorization Code ---<|               |
     +-|----|---+                                 +---------------+
       |    |                                         ^      v
      (A)  (C)                                        |      |
       |    |                                         |      |
       ^    v                                         |      |
     +---------+                                      |      |
     |         |>---(D)-- Authorization Code ---------'      |
     |  Client |          & Redirection URI                  |
     |         |                                             |
     |         |<---(E)----- Access Token -------------------'
     +---------+       (w/ Optional Refresh Token)

   Note: The lines illustrating steps (A), (B), and (C) are broken into
   two parts as they pass through the user-agent.

                     Figure 3: Authorization Code Flow
```

### 4. Certificates creation 
```
Create cert
https://www.thomasvitale.com/https-spring-boot-ssl-certificate/
keytool -genkeypair -alias testCert -keyalg RSA -keysize 2048 -keystore keystore.jks -validity 3650 -storepass password
keytool -genkeypair -alias testCert -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 3650 -storepass password

verify
keytool -list -v -keystore keystore.jks
keytool -list -v -storetype pkcs12 -keystore keystore.p12

Convert a JKS keystore into PKCS12
keytool -importkeystore -srckeystore keystore.jks -destkeystore keystore.p12 -deststoretype pkcs12

Use an existing SSL certificate
keytool -import -alias testCert -file myCertificate.crt -keystore keystore.p12 -storepass password
```

###  5. add certificates in Spring Boot application 
```
sample ssl in sproing boot
https://mkyong.com/spring-boot/spring-boot-ssl-https-examples/
git clone https://github.com/mkyong/spring-boot
cd spring-boot-ssl
$ mvn clean package
$ java -jar target/spring-boot-web.jar
access https://localhost:8443

# Read: https://www.baeldung.com/spring-boot-https-self-signed-certificate
```