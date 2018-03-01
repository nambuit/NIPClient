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
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import nip.service.objects.NESingleRequest;
import nip.wrapperobjects.NameEnquiryRequest;
import nip.wrapperobjects.NameEnquiryResponse;

/**
 * REST Web Service
 *
 * @author Administrator
 */
@Path("NIPOutwardInterface")
public class NIPInterface {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of NIPInterface
     */
    public NIPInterface() {
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
    public String getXml(@HeaderParam("authenticationID") String authenticationID, @HeaderParam("timeStamp") String timeStamp, @HeaderParam("applicationID") String applicationID, String payload) 
    {
     Gson gson = new Gson();
     
     NameEnquiryResponse response = new NameEnquiryResponse();
       
     try{
         
         NameEnquiryRequest request = (NameEnquiryRequest) gson.fromJson(payload, NameEnquiryRequest.class);
         
         NESingleRequest niprequest = new NESingleRequest();
         
         response.setResponseCode("00");
         response.setKycLevel("2");
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
