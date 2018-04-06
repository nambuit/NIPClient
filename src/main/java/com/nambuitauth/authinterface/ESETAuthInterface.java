/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nambuitauth.authinterface;


import com.nambuitauth.eset2fatorcore.Authenticator;
import com.nambuitauth.eset2fatorcore.DBConnector;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Temitope
 */
@Path("ESETAuthInterface")
public class ESETAuthInterface {


 

  private   Authenticator auth; 
  private AppParams options;
    /**
     * Creates a new instance of ESETAuthInterface
     */
   
    public ESETAuthInterface() {
    try{   
        
        options = new AppParams();
        
        
         auth  = new Authenticator();
        auth.setKey(options.getAuthkey());
        auth.setSecret(options.getAuthsecrete());
        auth.setEnforcePin(options.getEnforcePin());
        auth.setMax_no_of_failed_attemps(options.getNo_of_failed_attempts());
        auth.setTokenName(options.getTOKEN_NAME());
        auth.setDb(new DBConnector(options.getDBserver(),options.getDBuser(),options.getDBpass(),options.getDBName()));
        auth.setAppProvisioningmessage(options.getAppProvisioningmessage());
        auth.setAuthenticator(auth.get2FactorAuthenticator());
    }
    catch(Exception d){
        System.out.print(d.getMessage());
    }
      
    }

    /**
     * Retrieves representation of an instance of com.nambuitauth.authinterface.ESETAuthInterface
     * @param username
     * @param mobile
     * @return an instance of java.lang.String
     */
    @GET
    @Path("CreateUser")
    @Produces(MediaType.TEXT_PLAIN)
    public String CreateUser(@QueryParam("id")String username, @QueryParam("mobile")String mobile) {
       
        try{
          
            auth.CreateUser(username, mobile);
            return "User Created Successfully";
            
        }
        catch(Exception d){
            return d.getMessage();
        }
       
       // auth.CreateUser(key, secret);
       
    }


    @GET
    @Path("PreAuthenticateUser")
    @Consumes(MediaType.TEXT_PLAIN)
    public String PreAuthenticateUser(@QueryParam("id")String username) {
         try{
        
            auth.PreAuthenticateUser(username);
            return "User PreAuthenticated Successfully";
            
        }
        catch(Exception d){
            return d.getMessage();
        }
        
    }
    
    @GET
    @Path("Authenticate")
    @Consumes(MediaType.APPLICATION_XML)
    public String Authenticate(@QueryParam("id")String username, @QueryParam("otp")String otp) {
          try{
              
       
            return String.valueOf(auth.AuthenticateOTP(username, otp));
            
        }
        catch(Exception d){
            return d.getMessage();
        }
    }
    
    
    
            
                
    @GET
    @Path("ProvisionUserForMobileApp")
    @Consumes(MediaType.APPLICATION_XML)
    public String ProvisionUserForMobileApp(@QueryParam("id")String username) {
          try{
     
            auth.ProvisionUserForMobileApp(username);
            return "User Successfully Provisioned for Mobile App";
            
        }
        catch(Exception d){
            return d.getMessage();
        }
    }
    
    @GET
    @Path("DeProvisionUser")
    @Consumes(MediaType.APPLICATION_XML)
    public String DeProvisionUser(@QueryParam("id")String username) {
          try{
         
            auth.DeprovisionUser(username);
            return "User Successfully Deprovisioned for OTP";
            
        }
        catch(Exception d){
            return d.getMessage();
        }
    }
    
        @GET
    @Path("IsUserLocked")
    @Consumes(MediaType.APPLICATION_XML)
    public String IsUserLocked(@QueryParam("id")String username) {
          try{
      
            return String.valueOf(auth.IsUserLocked(username));        
        }
        catch(Exception d){
            return d.getMessage();
        }
    }
    
}
