/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nambuitauth.authinterface;




import javax.naming.InitialContext;
import lombok.Getter;
import lombok.Setter;
;




/**
 *
 * @author Temitope
 */
@Getter @Setter
public class AppParams {
 

  
    private String authkey;
    private String authsecrete;
    private String DBuser;
    private String DBpass;
    private String DBName;
    private String DBserver;
    private int no_of_failed_attempts;
    private Boolean EnforcePin;
    private String TOKEN_NAME;
    private String AppProvisioningmessage;
    
    
    
     public AppParams()
{
    try
    {

        javax.naming.Context ctx = (javax.naming.Context)new InitialContext().lookup("java:comp/env");
 
       
        authsecrete = (String)ctx.lookup("authsecrete");
        authkey = (String)ctx.lookup("authkey");
        DBuser = (String)ctx.lookup("DBuser");
        DBpass = (String)ctx.lookup("DBpass");
        DBName = (String)ctx.lookup("DBName");
        DBserver = (String)ctx.lookup("DBserver");
        no_of_failed_attempts = Integer.parseInt((String)ctx.lookup("no_of_failed_attempts"));
        EnforcePin = (Boolean) ctx.lookup("EnforcePin");
        TOKEN_NAME = (String) ctx.lookup("TOKEN_NAME"); 
        AppProvisioningmessage  = (String) ctx.lookup("AppProvisioningmessage");
        
    }
    catch (Exception e)
    {
      System.out.println(e.getMessage());
    }
    
    
}
 
  
     
}
