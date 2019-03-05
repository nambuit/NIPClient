/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bvn.service;

import bvn.service.objects.BVNCredentials;
import bvn.service.objects.BvnRequest;
import bvn.service.objects.BvnSingleResponse;
import bvn.wrapperobjects.BvnSingleSearchRequest;
import bvn.wrapperobjects.BvnSingleSearchResponse;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import nip.tools.AesUtil;
import nip.tools.AppParams;
import nip.tools.DBConnector;
import nip.tools.NIBBsResponseCodes;
import nip.tools.RestClient;
import nip.wrapperobjects.Records;
import nip.wrapperobjects.getFIListRequest;
import nip.wrapperobjects.getFIListResponse;


/**
 * REST Web Service
 *
 * @author tadekayero
 */
@Path("BVNInterface")
public class BVNClient {

    @Context
    private UriInfo context;
    
    AppParams options;
    NIBBsResponseCodes respcodes;
    private DBConnector db;
    String apikey = "";
    RestClient bvnRest;
    String logTable = "InlaksBVNWrapperLog";
   
    /**
     * Creates a new instance of BVNClient
     */
    public BVNClient() {
        
         options = new AppParams();
         
         db = new DBConnector(options.getDBserver(), options.getDBuser(), options.getDBpass(), "BVNLogs");
         
         bvnRest = new RestClient(options.getBvnEndpoint(),options.getBvnOrgcode());
         
         BVNCredentials credentials = bvnRest.ReAuthenticateBVN();
         
         bvnRest = new RestClient(options.getBvnEndpoint(),credentials);
         
    }

    /**
     * Retrieves representation of an instance of bvn.service.BVNClient
     * @param authenticationID
     * @param timeStamp
     * @param applicationID
     * @param payload
     * @return an instance of java.lang.String
     */
    @POST
    @Path("getSingleBVN")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSingleBVN(@HeaderParam("authenticationID") String authenticationID, @HeaderParam("timeStamp") String timeStamp, @HeaderParam("applicationID") String applicationID, String payload) {
        Gson gson = new Gson();
        Connection conn = null;
        BvnSingleSearchResponse response = new BvnSingleSearchResponse();

        List<Object> values = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        headers.add("requestPayload");
        values.add(payload);

        Date reqdate = new Date();

        headers.add("requestDate");
        values.add(reqdate);

        try {

            if (authenticationID == null || timeStamp == null || applicationID == null) {

                respcodes = NIBBsResponseCodes.Invalid_Sender;
                response.setResponsecode(respcodes.getInlaksCode());
                response.setResponsedescription(respcodes.getMessage());
                return gson.toJson(response);
            }
            
                 ResultSet rs = db.getData("select * from NIPClients where ApplicationID = '"+applicationID.trim()+"';", conn);
            
            if(rs.next()){
                
              apikey  = rs.getString("APIKey");
              
              String authid = rs.getString("AuthenticationID");
              
              if(!authid.trim().equals(authenticationID.trim())){
                  respcodes = NIBBsResponseCodes.Security_violation;
                response.setResponsecode(respcodes.getInlaksCode());
                response.setResponsedescription(respcodes.getMessage());
                return gson.toJson(response);
              }
                
            }
            else{
                
                  respcodes = NIBBsResponseCodes.Invalid_Sender;
                response.setResponsecode(respcodes.getInlaksCode());
                response.setResponsedescription(respcodes.getMessage());
                return gson.toJson(response);
                
            }
            
            
            
            

            BvnSingleSearchRequest request = (BvnSingleSearchRequest) gson.fromJson(payload, BvnSingleSearchRequest.class);

            headers.add("applicationID");
            values.add(applicationID);

            headers.add("requestID");
            values.add(request.getRequestID());

            String stringtohash = request.getRequestID()+request.getBVN();

            String requesthash = request.getHash();

            String hash = options.get_SHA_512_Hash(stringtohash, apikey);

            headers.add("hash");
            values.add(hash);

            if (hash.equals(requesthash)) {
      
                 
         
         BvnRequest req = new BvnRequest();
         
         req.setBVN(request.getBVN());
                  
         String reqstr = gson.toJson(req);

       String bvnserviceresponse = bvnRest.ProcessBVNRequest(reqstr,"GetSingleBVN");
       
   
       BvnSingleResponse bvnrespobject = (BvnSingleResponse) gson.fromJson(bvnserviceresponse, BvnSingleResponse.class);
        
       response.setBvn(bvnrespobject.getBVN());
          //continue      

            } else {
                respcodes = NIBBsResponseCodes.Security_violation;
                response.setResponsecode(respcodes.getInlaksCode());
                response.setResponsedescription(respcodes.getMessage());

            }

            headers.add("responseCode");
            values.add(response.getResponsecode());
            headers.add("responseDescription");
            values.add(response.getResponsedescription());
        } catch (Exception e) {
            respcodes = NIBBsResponseCodes.System_malfunction;
            response.setResponsecode(respcodes.getInlaksCode());
            response.setResponsedescription(respcodes.getMessage());
        } finally {
            try {

                headers.add("response");
                values.add(gson.toJson(response));

                db.insertData(headers, values.toArray(), logTable);
            } catch (Exception s) {

            }
        }

        return gson.toJson(response);
    }

    /**
     * PUT method for updating or creating an instance of BVNClient
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
