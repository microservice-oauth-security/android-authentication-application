# Details

This project creates an Android Authentication Application which has an Authentication screen with Login, Sign-In and Sign-In with Google option.  
Once the user authenticates successfully from the authentication screen, user will be navigated to Home screen, where the user has an option to call a micro-service using the authentication token generated from the authentication screen.

# Points to Note
1. #### Google OAuth Web Client Id
   Change the value of **google_oauth_client_id** in the file *app/src/main/res/values/keys.xml*, with the google web client id from your Google OAuth consent screen project.

2. #### Backend Service Url
   Change the value of urls defined in the file *app/src/main/res/values/url.xml*, with the url of your backend service.  
   You can create your backend service from the below repo.  
   [Authorization Application](https://github.com/microservice-oauth-security/authorization-application)  
   [User Authentication Service](https://github.com/microservice-oauth-security/user-authentication-service)  
   [Demo Microservice](https://github.com/microservice-oauth-security/demo-microservice)

3. #### Clear Text Traffic
   To enable http call without SSL from the android application, please add your backend Hostname or IP address in the file *app/src/main/res/xml/network_security_config.xml* as shown below.  
   `<domain includeSubdomains="true">add your backend Hostname or IP address here</domain>`

# Video Reference
For detailed video explanation of this project please visit my YouTube channel [CodeWithAnish](https://youtu.be/rSA7yMG2eeY)
