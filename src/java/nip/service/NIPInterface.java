/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nip.service;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import nip.tools.AppParams;
import nip.tools.DBConnector;
import nip.tools.NIBBsResponseCodes;
import nip.tools.PGPEncrytionTool;
import nip.wrapperobjects.NameEnquiryRequest;
import nip.wrapperobjects.NameEnquiryResponse;
import org.apache.log4j.Level;

/**
 * REST Web Service
 *
 * @author Administrator
 */
@Path("NIPOutwardInterface")
public class NIPInterface {

    @Context
    private UriInfo context;
    NIBBsResponseCodes respcodes;
    AppParams options;
    PGPEncrytionTool nipssm;
    DBConnector db;
    String logfilename ="NIPClientInterface";
    String logTable = "InlaksNIPWrapperLog";
    
    
    /**
     * Creates a new instance of NIPInterface
     */
    public NIPInterface() {
        
        try
    {
 
        options = new AppParams();
        
        nipssm = new PGPEncrytionTool(options);
        
       
        
        db = new DBConnector(options.getDBserver(),options.getDBuser(),options.getDBpass(),"NIPLogs");
        
    }
    catch (Exception e)
    {   
        options.getServiceLogger(logfilename).LogError(e.getMessage(), e, Level.FATAL);
    }
            
            
    }

        
    

    /**
     * Retrieves representation of an instance of nip.service.NIPInterface
     * @param authenticationID
     * @param timeStamp
     * @param applicationID
     * @param payload
     * @return an instance of java.lang.String
     */
    @POST
    @Path("NameEnquiry")
    @Produces(MediaType.APPLICATION_JSON)
    public String NameEnquiry(@HeaderParam("authenticationID") String authenticationID, @HeaderParam("timeStamp") String timeStamp, @HeaderParam("applicationID") String applicationID, String payload) 
    {
     Gson gson = new Gson();
     
     NameEnquiryResponse response = new NameEnquiryResponse();
     
       List<Object> values = new ArrayList<>();
        List<String> headers = new ArrayList<>();
      headers.add("requestPayload");
      values.add(payload);
      
      Date reqdate = new Date();
      
      headers.add("requestDate");
      values.add(reqdate);
     
       
     try{
         
         if(authenticationID==null||timeStamp==null||applicationID==null)
         {
            respcodes = NIBBsResponseCodes.Invalid_Sender;
            response.setResponseCode(respcodes.getInlaksCode());
            response.setResponseDescription(respcodes.getMessage());
            return gson.toJson(response);
         }
         
         headers.add("applicationID");
         values.add(applicationID);
         
         
         NameEnquiryRequest request = (NameEnquiryRequest) gson.fromJson(payload, NameEnquiryRequest.class);
         
         String stringtohash = request.getRequestID() + request.getDestinationInstitutionCode() + request.getAccountNumber();
         
      String requesthash = request.getHash();
      
      String hash = options.get_SHA_512_Hash(stringtohash, "inlaks");
      
      headers.add("hash");
      values.add(hash);

     if(hash.equals(requesthash)){
                 
          respcodes = options.getResponseObject("00");
         
         response.setResponseCode(respcodes.getInlaksCode());
         response.setResponseDescription(respcodes.getMessage());
     }
      else{
         respcodes = NIBBsResponseCodes.Security_violation;
         response.setResponseCode(respcodes.getInlaksCode());
         response.setResponseDescription(respcodes.getMessage());
         
     }
     
      headers.add("responseCode");
      values.add(response.getResponseCode());
        headers.add("responseDescription");
      values.add(response.getResponseDescription());
     }
     catch(Exception e)
     {
         
     }
     finally{
         try{
             
             headers.add("response");
             values.add(gson.toJson(response));  
             
          db.insertData(headers, values.toArray(),logTable);
         }
         catch(Exception s){
             
         }
     }
        
        return gson.toJson(response);
    }

    /**
     * PUT method for updating or creating an instance of NIPInterface
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
