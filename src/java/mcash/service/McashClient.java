/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mcash.service;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import mcash.service.objects.MerchantRegistrationRequest;
import mcash.wrapperobjects.RegisterMerchantRequest;
import mcash.wrapperobjects.RegisterMerchantResponse;
import mcash.service.objects.Header;
import nip.tools.AppParams;
import nip.tools.DBConnector;
import nip.tools.NIBBsResponseCodes;
import nip.tools.PGPEncrytionTool;
import nip.tools.T24Link;
import nip.tools.T24TAFJLink;
import nip.wrapperobjects.NameEnquiryResponse;
import org.apache.log4j.Level;

/**
 * REST Web Service
 *
 * @author tadekayero
 */
@Path("McashInterface")
public class McashClient {

    @Context
    private UriInfo context;

    NIBBsResponseCodes respcodes;
    AppParams options;
    PGPEncrytionTool nipssm;
    Connection conn = null;
    private DBConnector db;
    String logfilename = "NIPClientInterface";
    String logTable = "InlaksNIPWrapperLog";
    T24Link t24;
    String apikey = "";
    
    
    
    
    
    /**
     * Creates a new instance of McashClient
     */
    public McashClient() {
        
        try {

          options = new AppParams();

            nipssm = new PGPEncrytionTool(options);
             db = new DBConnector(options.getDBserver(), options.getDBuser(), options.getDBpass(), "NIPLogs");

            t24 = new T24TAFJLink();
       
         } catch (Exception e) {
            options.getServiceLogger(logfilename).LogError(e.getMessage(), e, Level.FATAL);
        }
    }

    /**
     * Retrieves representation of an instance of mcash.service.McashClient
     * @return an instance of java.lang.String
     */
   
    /**
     * Retrieves representation of an instance of mcash.service.McashClient
     * @param authenticationID
     * @param timeStamp
     * @param applicationID
     * @param payload
     * @return an instance of java.lang.String
     */
    @POST
    @Path("RegisterMerchant")
    @Produces(MediaType.APPLICATION_JSON)
    public String RegisterMerchant(@HeaderParam("authenticationID") String authenticationID, @HeaderParam("timeStamp") String timeStamp, @HeaderParam("applicationID") String applicationID, String payload) {
        Gson gson = new Gson();
        String sessionID = "", monthlyTable = "";
        RegisterMerchantResponse response = new RegisterMerchantResponse();

        List<Object> values = new ArrayList<>();
        List<String> headers = new ArrayList<>();

        List<Object> mcashvalues = new ArrayList<>();
        List<String> mcashheaders = new ArrayList<>();

        headers.add("requestPayload");
        values.add(payload);

        Date reqdate = new Date();

        headers.add("requestDate");
        values.add(reqdate);
        
     

        try {

            if (authenticationID == null || timeStamp == null || applicationID == null) {

                respcodes = NIBBsResponseCodes.Invalid_Sender;
                response.setResponseCode(respcodes.getInlaksCode());
                response.setResponseDescription(respcodes.getMessage());
                return gson.toJson(response);
            }
            
            ResultSet rs = db.getData("select * from NIPClients where ApplicationID = '"+applicationID.trim()+"';", conn);
            
            if(rs.next()){
                
              apikey  = rs.getString("APIKey");
              
              String authid = rs.getString("AuthenticationID");
              
              if(!authid.trim().equals(authenticationID.trim())){
                  respcodes = NIBBsResponseCodes.Security_violation;
                response.setResponseCode(respcodes.getInlaksCode());
                response.setResponseDescription(respcodes.getMessage());
                return gson.toJson(response);
              }
                
            }
            else{
                
                  respcodes = NIBBsResponseCodes.Invalid_Sender;
                response.setResponseCode(respcodes.getInlaksCode());
                response.setResponseDescription(respcodes.getMessage());
                return gson.toJson(response);
                
            }
            

            RegisterMerchantRequest request = (RegisterMerchantRequest) gson.fromJson(payload, RegisterMerchantRequest.class);

            headers.add("applicationID");
            values.add(applicationID);

            headers.add("requestID");
            values.add(request.getRequestID());

            String stringtohash = request.getRequestID() + request.getMerchantCode() + request.getAccountNumber();

            String requesthash = request.getHash();

            String hash = options.get_SHA_512_Hash(stringtohash, apikey);

            headers.add("hash");
            values.add(hash);

            if (hash.equals(requesthash)) {

                MerchantRegistrationRequest merchantrequest = new MerchantRegistrationRequest();
                
                Header header = new Header();
                
              //  header.()

               // merchantrequest.
                
//                niprequest.setAccountNumber(request.getAccountNumber());
//                niprequest.setChannelCode(request.getChannelCode());
//                niprequest.setDestinationInstitutionCode(request.getDestinationInstitutionCode());

//                sessionID = options.generateSessionID(request.getInstitutionCode());
//                niprequest.setSessionID(sessionID);

//                nipvalues.add(sessionID);
//                nipheaders.add("SessionID");
//
//                nipvalues.add(request.getAccountNumber());
//                nipheaders.add("AccountNumber");
//
//                nipvalues.add(request.getChannelCode());
//                nipheaders.add("ChannelCode");
//
//                nipvalues.add(request.getDestinationInstitutionCode());
//                nipheaders.add("DestinationInstitutionCode");
//
//                nipvalues.add("OUTWARD");
//                nipheaders.add("TranDirection");
//
//                nipvalues.add("nameenquirysingleitem");
//                nipheaders.add("MethodName");

                String datestr = sessionID.substring(6, 18);

                SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");

                Date date = sdf.parse(datestr);

//                nipvalues.add(date);
//                nipheaders.add("TransactionDate");

                SimpleDateFormat df = new SimpleDateFormat("MMMyyyy");

                monthlyTable = df.format(date) + "NIP_TRANSACTIONS";

                String createquery = options.getCreateNIPTableScript(monthlyTable);

                try {
                    db.Execute(createquery);
                } catch (Exception r) {

                }

               // db.insertData(nipheaders, nipvalues.toArray(), monthlyTable);

                //String niprequeststr = options.ObjectToXML(niprequest);

                //niprequeststr = nipssm.encrypt(niprequeststr);

               // String nipresponse = nip.nameenquirysingleitem(niprequeststr);

              //  nipresponse = nipssm.decrypt(nipresponse);

              //  NESingleResponse nipresponseobject = (NESingleResponse) options.XMLToObject(nipresponse, new NESingleResponse());

             //   respcodes = options.getResponseObject(nipresponseobject.getResponseCode());
               

                response.setResponseCode(respcodes.getInlaksCode());
                response.setResponseDescription(respcodes.getMessage());
            
                response.setHash(request.getHash());

            } else {
                respcodes = NIBBsResponseCodes.Security_violation;
                response.setResponseCode(respcodes.getInlaksCode());
                response.setResponseDescription(respcodes.getMessage());

            }

            headers.add("responseCode");
            values.add(response.getResponseCode());
            headers.add("responseDescription");
            values.add(response.getResponseDescription());
        } catch (Exception e) {
            respcodes = NIBBsResponseCodes.System_malfunction;
            response.setResponseCode(respcodes.getInlaksCode());
            response.setResponseDescription(respcodes.getMessage());
        } finally {
            try {

                headers.add("response");
                values.add(gson.toJson(response));

                db.insertData(headers, values.toArray(), logTable);

                String query = "";

                db.Execute(query);

            } catch (Exception s) {

            }
        }

        return gson.toJson(response);
    }
    
    
}
