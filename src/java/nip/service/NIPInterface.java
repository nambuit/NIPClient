/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nip.service;

import com.google.gson.Gson;
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
       
     try{
         
         
         NameEnquiryRequest request = (NameEnquiryRequest) gson.fromJson(payload, NameEnquiryRequest.class);
         
         String stringtohash = request.getRequestID() + request.getDestinationInstitutionCode() + request.getAccountNumber();
         
      String requesthash = request.getHash();
      
      String hash = options.get_SHA_512_Hash(stringtohash, "inlaks");

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
     
     
     }
     catch(Exception e)
     {
         
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
