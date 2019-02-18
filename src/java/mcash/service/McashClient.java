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
import mcash.service.objects.Account;
import mcash.service.objects.FinancialInstitutionCode;
import mcash.service.objects.MerchantRegistrationRequest;
import mcash.wrapperobjects.RegisterMerchantRequest;
import mcash.wrapperobjects.RegisterMerchantResponse;
import mcash.service.objects.Header;
import mcash.service.objects.Merchant;
import mcash.service.objects.Param;
import mcash.service.objects.PaymentDetailRequest;
import mcash.service.objects.PaymentDetailResponse;
import mcash.service.objects.PhysicalAddress;
import mcash.wrapperobjects.RegisterPaymentDetailRequest;
import mcash.wrapperobjects.RegisterPaymentDetailResponse;
import mcash.wrapperobjects.param;
import nip.tools.AppParams;
import nip.tools.DBConnector;
import nip.tools.NIBBsResponseCodes;
import nip.tools.PGPEncrytionTool;
import nip.tools.T24Link;
import nip.tools.T24TAFJLink;
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
                
                Merchant merchant = new Merchant();
                
                Account account = new Account();
                
                PhysicalAddress address = new PhysicalAddress();
                
                Merchant [] merchants = new Merchant [1];
                
                header.setInstitutionCode(request.getInstitutionCode());
                mcashheaders.add("InstitutionCode");
                mcashvalues.add(request.getInstitutionCode());
                
                header.setTotalCount("1");
                
                merchantrequest.setHeader(header);
                
                  //setting merchant values
                  merchant.setContactName(request.getContactName());
                  mcashheaders.add("ContactName");
                  mcashvalues.add(request.getContactName());
               
                  merchant.setEmailAddress(request.getEmailAddress());
                  mcashheaders.add("EmailAddress");
                  mcashvalues.add(request.getEmailAddress());
                  
            
                  merchant.setGroupName(request.getGroupName());
                  mcashheaders.add("GroupName");
                  mcashvalues.add(request.getGroupName());
                  
                  merchant.setGPSLocation(request.getGpsLocation());
                  mcashheaders.add("GPSLocation");
                  mcashvalues.add(request.getGpsLocation());
                  
                  merchant.setGroupCode(request.getGroupCode());
                  mcashheaders.add("GroupCode");
                  mcashvalues.add(request.getGroupCode());
                  
                  merchant.setMerchantName(request.getMerchantName());
                  mcashheaders.add("MerchantName");
                  mcashvalues.add(request.getMerchantName());
                  
                  merchant.setMerchantCode(request.getMerchantCode());
                  mcashheaders.add("MerchantCode");
                  mcashvalues.add(request.getMerchantCode());
                  
                  merchant.setPhoneNumber(request.getPhoneNumber());
                  mcashheaders.add("PhoneNumber");
                  mcashvalues.add(request.getPhoneNumber());
                  
                  merchant.setRequestID(request.getRequestID());
                  mcashheaders.add("RequestID");
                  mcashvalues.add(request.getRequestID());
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                 
                  //setting account values
                  account.setAccountName(request.getAccountName());
                  mcashheaders.add("AccountName");
                  mcashvalues.add(request.getAccountName());
                  
                  account.setBankVerificationNumber(request.getBvn());
                  mcashheaders.add("Bvn");
                  mcashvalues.add(request.getBvn());
                  
                  account.setDeferredSettlement(request.getDefferedSettlement());
                  mcashheaders.add("DeferredSettlement");
                  mcashvalues.add(request.getDefferedSettlement());
                  
                  account.setKyc(request.getKyc());
                  mcashheaders.add("Kyc");
                  mcashvalues.add(request.getKyc());
                  
                  account.setAccountNumber(request.getAccountNumber());
                  mcashheaders.add("AccountNumber");
                  mcashvalues.add(request.getAccountNumber());
                  
                  account.setMaximumTransactionAmount(request.getMaximumTransactionAmount());
                  mcashheaders.add("MaximumTransactionAmount");
                  mcashvalues.add(request.getMaximumTransactionAmount());
                  
                  
                  
                  
                  
                  
                  
                  
                  //setting physical address
                  address.setLGA(request.getLGA());
                  mcashheaders.add("LGA");
                  mcashvalues.add(request.getLGA());
                  
                  address.setState(request.getState());
                  mcashheaders.add("State");
                  mcashvalues.add(request.getState());
                  
                  address.setStreet(request.getStreet());
                  mcashheaders.add("Street");
                  mcashvalues.add(request.getStreet());
                  
                  
                  
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
                   mcashheaders.add("MethodName");
                   mcashvalues.add("RegisterMerchant");
                
                
                  //setting merchant sub classes
                    merchant.setAccount(account);
                    merchant.setPhysicalAddress(address);
                  
     
                        
                merchants[0] = merchant;
                
                
                merchantrequest.setMerchant(merchants);


                String datestr = sessionID.substring(6, 18);

                SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");

                Date date = sdf.parse(datestr);

//                nipvalues.add(date);
//                nipheaders.add("TransactionDate");

                SimpleDateFormat df = new SimpleDateFormat("MMMyyyy");

                monthlyTable = df.format(date) + "mCASH_TRANSACTIONS";

                String createquery = options.getCreateMcashTableScript(monthlyTable);

                try {
                    db.Execute(createquery);
                } catch (Exception r) {

                }

                db.insertData(mcashheaders, mcashvalues.toArray(), monthlyTable);
                
               
                
               String nibsmerchantrequest = options.ObjectToXML(merchantrequest);
                
                nibsmerchantrequest = nipssm.encrypt(nibsmerchantrequest);
                
                String nibsmerchantresposne = nibsmerchantrequest;
                
                
                
                nibsmerchantresposne = nipssm.decrypt(nibsmerchantresposne);
                
                RegisterMerchantResponse nibsresposneobject = (RegisterMerchantResponse) options.XMLToObject(nibsmerchantresposne, new RegisterMerchantResponse());
                
                
                response.setMerchantCode(nibsresposneobject.getMerchantCode()); 

         
               

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
    
    
    
    
     /**
     * Retrieves representation of an instance of mcash.service.McashClient
     * @param authenticationID
     * @param timeStamp
     * @param applicationID
     * @param payload
     * @return an instance of java.lang.String
     */
    @POST
    @Path("executePaymentDetails")
    @Produces(MediaType.APPLICATION_JSON)
    public String executePaymentDetails(@HeaderParam("authenticationID") String authenticationID, @HeaderParam("timeStamp") String timeStamp, @HeaderParam("applicationID") String applicationID, String payload) {
        Gson gson = new Gson();
        String sessionID = "", monthlyTable = "";
        RegisterPaymentDetailResponse response = new RegisterPaymentDetailResponse();

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
                response.setResponsecode(respcodes.getInlaksCode());
                response.setResponseDescription(respcodes.getMessage());
                return gson.toJson(response);
            }
            
            ResultSet rs = db.getData("select * from NIPClients where ApplicationID = '"+applicationID.trim()+"';", conn);
            
            if(rs.next()){
                
              apikey  = rs.getString("APIKey");
              
              String authid = rs.getString("AuthenticationID");
              
              if(!authid.trim().equals(authenticationID.trim())){
                  respcodes = NIBBsResponseCodes.Security_violation;
                response.setResponsecode(respcodes.getInlaksCode());
                response.setResponseDescription(respcodes.getMessage());
                return gson.toJson(response);
              }
                
            }
            else{
                
                  respcodes = NIBBsResponseCodes.Invalid_Sender;
                response.setResponsecode(respcodes.getInlaksCode());
                response.setResponseDescription(respcodes.getMessage());
                return gson.toJson(response);
                
            }
            

            RegisterPaymentDetailRequest request = (RegisterPaymentDetailRequest) gson.fromJson(payload, RegisterPaymentDetailRequest.class);

            headers.add("applicationID");
            values.add(applicationID);

            headers.add("requestID");
            values.add(request.getRequestID());

            String stringtohash = request.getRequestID() + request.getMerchantcode() + request.getAccountnumber();

            String requesthash = request.getHash();

            String hash = options.get_SHA_512_Hash(stringtohash, apikey);

            headers.add("hash");
            values.add(hash);

            if (hash.equals(requesthash)) {

                PaymentDetailRequest payrequest = new PaymentDetailRequest();
                
                FinancialInstitutionCode ficode = new FinancialInstitutionCode();
                
             
                
                  //setting FinancialInstitutionCode values
                  ficode.setAccountNumber(request.getAccountnumber());
                  mcashheaders.add("Accountnumber");
                  mcashvalues.add(request.getAccountnumber());
               
                  ficode.setFinancialInstitutionCode(request.getFinancialInstitutionCode());
                  mcashheaders.add("FinancialInstitutionCode");
                  mcashvalues.add(request.getFinancialInstitutionCode());
                  
                  
                  //setting payment request values
                  payrequest.setAmount(request.getAmount());
                  mcashheaders.add("Amount");
                  mcashvalues.add(request.getAmount());
                  
                  mcashheaders.add("MethodName");
                  mcashvalues.add("executePaymentDetails");
                   
                   
                  
                  
                  
                  
                  
                  //setting payment details request subclasses
                  payrequest.setFinancialInstitutionCode(ficode);
                  
                  
                  
            
      
 

                String datestr = sessionID.substring(6, 18);

                SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");

                Date date = sdf.parse(datestr);

//                nipvalues.add(date);
//                nipheaders.add("TransactionDate");

                SimpleDateFormat df = new SimpleDateFormat("MMMyyyy");

                monthlyTable = df.format(date) + "mCASH_TRANSACTIONS";

                String createquery = options.getCreateMcashTableScript(monthlyTable);

                try {
                    db.Execute(createquery);
                } catch (Exception r) {

                }

                db.insertData(mcashheaders, mcashvalues.toArray(), monthlyTable);
                
               
                
               String nibsspaymentdetailrequest = options.ObjectToXML(payrequest);
                
                nibsspaymentdetailrequest = nipssm.encrypt(nibsspaymentdetailrequest);
                
                String nibsspaymentdetailresponse = nibsspaymentdetailrequest;
                
            
                
                nibsspaymentdetailresponse = nipssm.decrypt(nibsspaymentdetailresponse);
                
                PaymentDetailResponse nibssresponseobject = (PaymentDetailResponse) options.XMLToObject(nibsspaymentdetailresponse, new PaymentDetailResponse());
                
                Param [] params = nibssresponseobject.getParams().getParam();
                param [] wparams = new param[params.length];
                
                for(int i=0; i<params.length;i++){
                     
                    param wparam = new param();
                     
                     wparam.setName(params[i].getName());
                     wparam.setName(params[i].getDescription());
                     
                     wparams[i] = wparam;
                }
                
               
                
                response.setMerchantCode(nibssresponseobject.getMerchantCode());
                 response.setMerchantname(nibssresponseobject.getMerchantName());
                 response.setPayerBVN(nibssresponseobject.getMerchantCode());
                 response.setRequestID(nibssresponseobject.getMerchantCode());
                 response.setAmount(nibssresponseobject.getMerchantCode());
                 response.setFee(nibssresponseobject.getMerchantCode());
                 response.setHash(nibssresponseobject.getMerchantCode());
                 response.setSessionID(nibssresponseobject.getMerchantCode());
                 response.setParams(wparams);
                 response.setFinancialInstitutionCode(nibssresponseobject.getFinancialInstitutionCode().getFinancialInstitutionCode());
                 response.setSecondFactorAuthCode(nibssresponseobject.getFinancialInstitutionCode().getSecondFactorAuthCode());
                 response.setAccountNumber(nibssresponseobject.getFinancialInstitutionCode().getAccountNumber());
                 response.setFISpecificInformation(nibssresponseobject.getFinancialInstitutionCode().getFISpecificInformation());
                 response.setName(nibssresponseobject.getFinancialInstitutionCode().getName());
                 
                 
                 

         
     
                response.setResponsecode(respcodes.getInlaksCode());
                response.setResponseDescription(respcodes.getMessage());
            
                response.setHash(request.getHash());

            } else {
                respcodes = NIBBsResponseCodes.Security_violation;
                response.setResponsecode(respcodes.getInlaksCode());
                response.setResponseDescription(respcodes.getMessage());

            }

            headers.add("responseCode");
            values.add(response.getResponsecode());
            headers.add("responseDescription");
            values.add(response.getResponseDescription());
        } catch (Exception e) {
            respcodes = NIBBsResponseCodes.System_malfunction;
            response.setResponsecode(respcodes.getInlaksCode());
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
