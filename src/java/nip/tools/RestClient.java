/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nip.tools;

import bvn.service.objects.BVNCredentials;
import bvn.service.objects.BvnRequest;
import bvn.service.objects.BvnSingleResponse;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;


/**
 *
 * @author Administrator
 */
public class RestClient {
    
    String endpointurl = "http://196.6.103.58:8080/bvnr";
    String aeskey = "MWx1vW1mj26hycrX";
    String iv = "iLevK4MTyC+98YsJ";
    String pass ="U\",zJZy<XI:#L)9/";
      AesUtil aes = new AesUtil(128,1); 
    String userid = "001011";
            
            



    
    public RestClient(String endpointaddresss, BVNCredentials credentials){
        
        this.endpointurl = endpointaddresss;
        this.iv = credentials.getIV();
        this.pass = credentials.getPassword();
        this.userid = credentials.getUserid();
        this.aeskey =  credentials.getAesKey();
    }
    
     public RestClient(String endpointaddresss, String Orgcode){
        
      
        this.userid = Orgcode;
       
    }
    
       public RestClient(){
         
    }
       
       
public BVNCredentials ReAuthenticateBVN(){
        
        try {
            
        URL url = new URL(endpointurl+"/Reset"); 
        
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
        connection.setDoOutput(true); 
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false); 
        connection.setRequestMethod("POST"); 
        connection.setRequestProperty("Accept", "application/json"); 
        
        byte[] encodedBytes = Base64.getEncoder().encode(userid.getBytes());
             
        
        connection.setRequestProperty("OrganisationCode", new String(encodedBytes));  
        connection.setRequestProperty("Content-Type", "application/json");     
        
        connection.connect();
        
        HashMap<String,String> headers = this.getHeaders(connection);
        
        BVNCredentials credentials = new BVNCredentials();
        
        credentials.setAesKey(headers.get("AES_KEY"));
        credentials.setIV(headers.get("IVKey"));
        credentials.setPassword(headers.get("Password"));
        credentials.setUserid(userid);
        
        return credentials;
         

    } catch(Exception e) { 
        throw new RuntimeException(e); 
    } 
    }

public HashMap<String,String> getHeaders(HttpURLConnection connection){
    
    HashMap<String,String> headers = new HashMap<>();
    
    for (int i = 0;; i++) {
        
       String headerName = connection.getHeaderFieldKey(i);
      String headerValue = connection.getHeaderField(i);
        
      headers.put(headerName, headerValue) ;


      if (headerName == null && headerValue == null) {
       
        break;
      }
          
          }
    
    return headers;
}
    
    public String ProcessBVNRequest(String payload, String methodName){
        
        try {
            
        URL url = new URL(endpointurl+"/"+methodName); 
        
        String auth = userid+":"+pass;
        
         byte[] encodedBytes =   Base64.getEncoder().encode(auth.getBytes());
         
         auth = new String(encodedBytes);
         
         SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
         
         Date date = new Date();
         
         String formatedDate = sdf.format(date);
         
         //payload = "8cef8a4d9f28463d84cd49dccd2c19d0f13733192177236a8efe8db5fdfb98de";
         
         String sig = userid+formatedDate+pass;
         
         
         sig = this.sha256(sig);
        
        
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
        connection.setDoOutput(true); 
          connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false); 
        connection.setRequestMethod("POST"); 
        connection.setRequestProperty("Accept", "application/json");  
        connection.setRequestProperty("Content-Type", "application/json");     
        connection.setRequestProperty("Authorization", auth);
        connection.setRequestProperty("SIGNATURE", sig);
        connection.setRequestProperty("SIGNATURE_METH", "SHA256");
      // connection.setRequestProperty("Content", payload);
      //  connection.setRequestProperty("OrganisationCode","001011");
        
       payload  = aes.encrypt( aeskey,iv, pass, payload);
        
       // connection.connect();
      OutputStream os = connection.getOutputStream();
       // int recode = connection.getResponseCode();
        os.write(payload.getBytes("UTF8")); 
        
      os.flush();

          BufferedReader   br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
          StringBuilder sb = new StringBuilder();
          String output;
          
          while ((output = br.readLine()) != null) {
          sb.append(output);
                 
        }
        
          String response = sb.toString();
          
          String decryptedresponse = aes.decrypt(aeskey,iv, pass, response);
          
          return decryptedresponse;

    } catch(Exception e) { 
        throw new RuntimeException(e); 
    } 
    }
    
  
    
    
    private  String bytesToHex(byte[] hash) {
    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < hash.length; i++) {
    String hex = Integer.toHexString(0xff & hash[i]);
    if(hex.length() == 1) hexString.append('0');
        hexString.append(hex);
    }
    return hexString.toString();
}
    
    
    
     public String get_SHA_512_Hash(String StringToHash, String   salt) throws Exception{
String generatedPassword = null;
    try {
         MessageDigest md = MessageDigest.getInstance("SHA-512");
        // md.update(salt.getBytes(StandardCharsets.UTF_8));
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
 
     
     public String get_SHA_256_Hash(String rawtxt) throws Exception{
         try{     
         MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
         byte[] hash = digest.digest(rawtxt.getBytes(StandardCharsets.UTF_8));
         
        // byte[] encodedBytes =   Base64.getEncoder().encode(hash);
              // String sha256hex = DigestUtils.sha256Hex(rawtxt); 
         return  new String(hash);
         }
         catch(Exception f){
             throw(f);
         }
         
       
     }
     
 public  String sha256(String base) {
    try{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(base.getBytes("UTF-8"));
       

        return this.bytesToHex(hash);
    } catch(Exception ex){
       throw new RuntimeException(ex);
    }
}
     

     
     public static void main(String [] args){
         try
         {
        RestClient client = new RestClient();
         
        client.ReAuthenticateBVN();
         
         AesUtil aes = new AesUtil(128,1);  
         
         BvnRequest req = new BvnRequest();
         
         req.setBVN("22222222225");
         
         Gson gson =  new Gson();
         
         String reqstr = gson.toJson(req);
         
         String encreq = aes.encrypt( client.aeskey,client.iv, client.pass, reqstr);
         
         String raw = aes.decrypt(client.aeskey,client.iv, client.pass, encreq);
         
         
       String response =   client.ProcessBVNRequest(encreq,"GetSingleBVN");
       
       response = aes.decrypt( client.aeskey,client.iv, client.pass, response);
       
       BvnSingleResponse resp = (BvnSingleResponse) gson.fromJson(response, BvnSingleResponse.class);
       
    String dd =   resp.getEmail();
         }
         catch(Exception d){
             
             System.out.print(d.getMessage());
       
     }
         
     }
    
  
}
