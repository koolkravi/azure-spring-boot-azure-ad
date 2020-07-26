# Secure a Java web app using the Spring Boot Starter for Azure Active Directory

# Step 1: Create an app using Spring Initializr
```
https://start.spring.io/

Dependencies :
Spring Web, Azure Active Directory and Spring Security.
```


# Step 2: Create Azure Active Directory instance

## 1st application registration 

### 2.1 
```
All Services ->Identity -> Azure Active Directory
App registrations-> click Register an application.

Tenant ID 				 : <<>>
Application registration : mydemospringbootapp
Application (client)  ID : 8967958f-d3d8-w-a4cd-09ee2224db

```

### 2.2
```
Certificates & secrets ->   New client secret.

client secret 			 :  jDB30RI.EB1mEMwlo_~F0-7r6z-x22LxIQ8
```

### 2.3
```
API permissions - > Microsoft Graph 
Tick Access the directory as the signed-in user and Sign in and read user profile
Grant Permissions... and Yes
```

### 2.4
```
Authentication -> Add a platform -> click Web applications
Redirect URI= http://localhost:8080/login/oauth2/code/azure
click Configure.
```

### 2.5
```
Click Manifest
oauth2AllowImplicitFlow = true

click Save
```

## 2nd application registration  (follow from step 2.1. to 2.4)
```
Default Directory (ravijuly2020rediffmail.onmicrosoft.com)

Application registration : mydemospringbootapp
Application (client) ID  : 6e691934-251a-w-a23f-bfed718f869d
secret 					 : HdCX._h_4P4yQo0U3lXXq07Yb~i3jfW_j2
```


# Step 3: Add a user account to your directory, and add that account to a group
```
#Overview page of your Active Directory -> click Users, and then click New user
User name 	= 	test-user
Name 		=	test user

Click Create
 
#Groups, then Create a new group
click No members selected -> select  test-user ->Create 
group 		= 	users

#Users panel ->  Reset password
copy the password
```

# Step 4: Configure and compile your app

add in pom.xml
```
<dependency>
   <groupId>org.springframework.security</groupId>
   <artifactId>spring-security-oauth2-client</artifactId>
</dependency>
<dependency>
   <groupId>org.springframework.security</groupId>
   <artifactId>spring-security-oauth2-jose</artifactId>
</dependency>
```

application.properties
```
# Specifies your Active Directory ID:
azure.activedirectory.tenant-id=22222222-2222-2222-2222-222222222222

# Specifies your App Registration's Application ID:
spring.security.oauth2.client.registration.azure.client-id=11111111-1111-1111-1111-1111111111111111

# Specifies your App Registration's secret key:
spring.security.oauth2.client.registration.azure.client-secret=AbCdEfGhIjKlMnOpQrStUvWxYz==

# Specifies the list of Active Directory groups to use for authorization:
azure.activedirectory.active-directory-groups=Users
```

```
HelloController.java

package com.example.demo2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController
{
    @Autowired
    @PreAuthorize("hasRole('users')")
    @RequestMapping("/")
    public String helloWorld()
    {
        return "Hello World!";
    }
}
```

```
WebSecurityConfig.java

package com.example.demo2.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService;


    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .oauth2Login()
            .userInfoEndpoint()
            .oidcUserService(oidcUserService);
    }
}

```

# Step 4: Test 
```
mvn clean package spring-boot:run
1st application : http://localhost:8090
2nd application : http://localhost:8091

```

# Summary  
Two spring boot application work as single sign on. Both application have their own Client ID  and Clinet secret configured in application propety file
User has to to login only once.

```
user 	: test-user@ravijuly2020rediffmail.onmicrosoft.com
password: July152020@123
Tenant ID 				 : rrrr-rrrr-rrrr-rrrr-rrrr

1st application
Application registration : mydemospringbootapp
Application (client) ID  : 55555-251a-55-a23f-bfed718f869d
client secret 			 : HdCX.gdddddd~i3jfW_j2

2st application
Application registration : mydemospringbootapp
Application (client)  ID : ddd-gggg-4195-a4cd-dddddd
client secret 			 : dddd-K-9FVEZF.PZ~ggggg
```

# Issues
1. 
```
Login with OAuth 2.0
[authorization_request_not_found]
fix :  app registration -> authorization -> Check  ID tokens
```

# References:

## 1. Secure a Java web app using the Spring Boot Starter for Azure Active Directory
```
https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-boot-starter-java-app-with-azure-active-directory
```
## 2. For a full list of values that are available in your application.properties file, see the Azure Active Directory Spring Boot Sample on GitHub.
```
https://github.com/Azure/azure-sdk-for-java/tree/master/sdk/spring/azure-spring-boot-samples/azure-spring-boot-sample-active-directory-backend
```

## 3. Secure a Java web app using the Spring Boot Starter for Azure Active Directory B2C
```
https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-boot-starter-java-app-with-azure-active-directory-b2c-oidc

https://youtu.be/87chWYrUDXM
```

## 4. How to use the Spring Boot Starter for Azure Key Vault
```
https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-boot-starter-java-app-with-azure-key-vault 

az login
az account list
az account set -s <<subscription GUID>>
```
# Create a new Azure Key Vault
```
az group create --name koolkravi-rg --location eastus

az keyvault create --resource-group koolkravi-rg \
    --name koolkravikeyvault \
    --enabled-for-deployment true \
    --enabled-for-disk-encryption true \
    --enabled-for-template-deployment true \
    --sku standard --query properties.vaultUri \
    --location eastus
out:
https://koolkravikeyvault.vault.azure.net/
	
# Store a secret in your new key vault; for example
az keyvault secret set --vault-name "koolkravikeyvault" \
    --name "client-secret" \
    --value "_32jT6535-K-9FVEZF.PZ~jvKT77TOF3Ty"
	

# add in application propery file
azure.keyvault.enabled=true
azure.keyvault.uri=Your-Keyvault-uri
azure.keyvault.client-id=Your-Client-ID
azure.keyvault.tenant-id=Your-Tenant-ID
```

## 5.  Tutorial: Use Key Vault references in a Java Spring app
```
https://docs.microsoft.com/en-us/azure/azure-app-configuration/use-key-vault-references-spring-boot
```
## 6. Read application properties from Azure
```
https://www.shardik.com/blog/2020/04/09/azure-properties/
https://kreuzwerker.de/post/keyvault-with-spring
```

