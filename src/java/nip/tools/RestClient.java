/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nip.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


/**
 *
 * @author Administrator
 */
public class RestClient {
    
    String endpointurl = "http://196.6.103.58:8080/BVNPlaceHolder";
    String aeskey = "0nfpEr7w2arVDDyi";
    String iv = "S0/MfUffLSFB+TVV";
    String pass ="(4C;Pu:y%%@TB@F,";
            
            



    
    public RestClient(String endpointaddresss){
        
        this.endpointurl = endpointaddresss;
    }
    
       public RestClient(){
         
    }
       
       
public void ReAuthenticateBVN(String orgCode){
        
        try {
            
        URL url = new URL(endpointurl+"/Reset"); 
        
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
        connection.setDoOutput(true); 
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false); 
        connection.setRequestMethod("POST"); 
        connection.setRequestProperty("Accept", "application/json"); 
        
        byte[] encodedBytes = Base64.getEncoder().encode(orgCode.getBytes());
         orgCode =     new String(encodedBytes);
        
        connection.setRequestProperty("OrganisationCode", orgCode);  
        connection.setRequestProperty("Content-Type", "application/json");     
        
        connection.connect();
        
     
          
      
        
     String apikey = connection.getHeaderField("APIKey");
     String ivKey = connection.getHeaderField("IVKey");
     String pass = connection.getHeaderField("Password");
        
       
        
     

//       BufferedReader   br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
//       StringBuilder sb = new StringBuilder();
//       String output;
//          
//          while ((output = br.readLine()) != null) {
//          sb.append(output);
//                 
//        }
        
         

    } catch(Exception e) { 
        throw new RuntimeException(e); 
    } 
    }
    
    public String ProcessBVNRequest(String payload, String methodName){
        
        try {
            
        URL url = new URL(endpointurl+"/"+methodName); 
        
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
        connection.setDoOutput(true); 
        connection.setInstanceFollowRedirects(false); 
        connection.setRequestMethod("POST"); 
        connection.setRequestProperty("Accept", "application/json"); 
        connection.setRequestProperty("OrganisationCode", "NambuitCore");  
        connection.setRequestProperty("Content-Type", "application/json");     
        
         OutputStream os = connection.getOutputStream();
        
        os.write(payload.getBytes("UTF8")); 
        
        os.flush();

            BufferedReader   br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
          StringBuilder sb = new StringBuilder();
          String output;
          
          while ((output = br.readLine()) != null) {
          sb.append(output);
                 
        }
        
          return sb.toString();

    } catch(Exception e) { 
        throw new RuntimeException(e); 
    } 
    }
    
  
     public String get_SHA_512_Hash(String StringToHash, String   salt) throws Exception{
String generatedPassword = null;
    try {
         MessageDigest md = MessageDigest.getInstance("SHA-512");
         md.update(salt.getBytes(StandardCharsets.UTF_8));
         byte[] bytes = md.digest(StringToHash.getBytes(StandardCharsets.UTF_8));
         StringBuilder sb = new StringBuilder();
         for(int i=0; i< bytes.length ;i++){
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
         }
         generatedPassword = sb.toString();
        } 
       catch (NoSuchAlgorithmException e){
       throw (e);
       }
    return generatedPassword;
}
 
     
     public static void main(String [] args){
         
         RestClient client = new RestClient();
         
         client.ReAuthenticateBVN("001011");
     }
    
  
}
