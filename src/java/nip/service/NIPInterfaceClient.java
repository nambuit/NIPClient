/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nip.service;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import nibss.nip.core.NIPInterface;
import nibss.nip.core.NIPInterface_Service;
import nip.service.objects.FTSingleCreditRequest;
import nip.service.objects.FTSingleCreditResponse;
import nip.service.objects.NESingleRequest;
import nip.service.objects.NESingleResponse;
import nip.service.objects.Record;
import nip.service.objects.TSQuerySingleRequest;
import nip.tools.AppParams;
import nip.tools.DBConnector;
import nip.tools.NIBBsResponseCodes;
import nip.tools.PGPEncrytionTool;
import nip.wrapperobjects.FundsTransferDCRequest;
import nip.wrapperobjects.FundsTransferDCResponse;
import nip.wrapperobjects.NameEnquiryRequest;
import nip.wrapperobjects.NameEnquiryResponse;
import nip.wrapperobjects.Records;
import nip.wrapperobjects.TransactionStatusQueryRequest;
import nip.wrapperobjects.TransactionStatusQueryResponse;
import nip.wrapperobjects.getFIListRequest;
import nip.wrapperobjects.getFIListResponse;
import org.apache.log4j.Level;

/**
 * REST Web Service
 *
 * @author Administrator
 */
@Path("NIPOutwardInterface")
public class NIPInterfaceClient {

    @Context
    private UriInfo context;
    NIBBsResponseCodes respcodes;
    AppParams options;
    PGPEncrytionTool nipssm;
    DBConnector db;
    String logfilename ="NIPClientInterface";
    String logTable = "InlaksNIPWrapperLog";
    NIPInterface nip;
    
    /**
     * Creates a new instance of NIPInterface
     */
    public NIPInterfaceClient() {
        
        try
    {
 
        options = new AppParams();
        
        nipssm = new PGPEncrytionTool(options);
        
       NIPInterface_Service nipservice = new NIPInterface_Service();
       
       nip = nipservice.getNIPInterfacePort();
        
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
         
        NameEnquiryRequest request = (NameEnquiryRequest) gson.fromJson(payload, NameEnquiryRequest.class);
         
   
         
         
         
         headers.add("applicationID");
         values.add(applicationID);
         
        
         
           headers.add("requestID");
         values.add(request.getRequestID());
         
         String stringtohash = request.getRequestID() + request.getDestinationInstitutionCode() + request.getAccountNumber();
         
      String requesthash = request.getHash();
      
      String hash = options.get_SHA_512_Hash(stringtohash, "inlaks");
      
      headers.add("hash");
      values.add(hash);

     if(hash.equals(requesthash)){
                 
              NESingleRequest niprequest = new NESingleRequest();
             
         niprequest.setAccountNumber(request.getAccountNumber());
         niprequest.setChannelCode(request.getChannelCode());
         niprequest.setDestinationInstitutionCode(request.getDestinationInstitutionCode());
         String sessionID = options.generateSessionID(request.getInstitutionCode());
         niprequest.setSessionID(sessionID);
         
         
         String niprequeststr = options.ObjectToXML(niprequest);
         
         niprequeststr = nipssm.encrypt(niprequeststr);
         
       String nipresponse =  nip.nameenquirysingleitem(niprequeststr);
       
       nipresponse = nipssm.decrypt(nipresponse);
       
       NESingleResponse nipresponseobject = (NESingleResponse) options.XMLToObject(nipresponse, new NESingleResponse());
         
       respcodes = options.getResponseObject(nipresponseobject.getResponseCode());
         
         response.setResponseCode(respcodes.getInlaksCode());
         response.setResponseDescription(respcodes.getMessage());
         response.setNameEnquiryRef(nipresponseobject.getSessionID());
         response.setBankVerificationNo(nipresponseobject.getBankVerificationNumber());
         response.setKycLevel(nipresponseobject.getKYCLevel());
         response.setRequestID(request.getRequestID());
         response.setAccountName(nipresponseobject.getAccountName());
         response.setAccountNumber(nipresponseobject.getAccountNumber());
         
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
          respcodes = NIBBsResponseCodes.System_malfunction;
         response.setResponseCode(respcodes.getInlaksCode());
         response.setResponseDescription(respcodes.getMessage());
     }
     finally{
         try{
             
             headers.add("response");
             values.add(gson.toJson(response));  
             
          db.insertData(headers, values.toArray(),logTable);
         }
         catch(Exception s)
         {
             
         }
     }
        
        return gson.toJson(response);
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
    @Path("FundsTransferDC")
    @Produces(MediaType.APPLICATION_JSON)
    public String FundsTransferDC(@HeaderParam("authenticationID") String authenticationID, @HeaderParam("timeStamp") String timeStamp, @HeaderParam("applicationID") String applicationID, String payload) 
    {
     Gson gson = new Gson();
     
     FundsTransferDCResponse response = new FundsTransferDCResponse();
     
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
         
        FundsTransferDCRequest request = (FundsTransferDCRequest) gson.fromJson(payload, FundsTransferDCRequest.class);
         
   
         
         
         
         headers.add("applicationID");
         values.add(applicationID);
         
        
         
           headers.add("requestID");
         values.add(request.getRequestID());
         
         String stringtohash = request.getRequestID() + request.getDestinationInstitutionCode() + request.getBeneficiaryAccountNumber()+request.getAmount();
         
      String requesthash = request.getHash();
      
      String hash = options.get_SHA_512_Hash(stringtohash, "inlaks");
      
      headers.add("hash");
      values.add(hash);

     if(hash.equals(requesthash)){
                 
         FTSingleCreditRequest niprequest = new FTSingleCreditRequest();
             
         niprequest.setBeneficiaryAccountNumber(request.getBeneficiaryAccountNumber());
         niprequest.setChannelCode(request.getChannelCode());
         niprequest.setDestinationInstitutionCode(request.getDestinationInstitutionCode());
         String sessionID = options.generateSessionID(request.getInstitutionCode());
         niprequest.setSessionID(sessionID);
         niprequest.setBeneficiaryAccountName(request.getBeneficiaryAccountName());
         niprequest.setNameEnquiryRef(request.getNameEnquiryRef());
         niprequest.setBeneficiaryVerificationNumber(request.getBeneficiaryBankVerificationNumber());
         niprequest.setNarration(request.getNarration());//"Inlaks FT Single DC Test ");
         niprequest.setOriginatorKYCLevel(request.getOriginatorKYCLevel());
         niprequest.setPaymentReference(request.getPaymentReference());
         niprequest.setOriginatorAccountName(request.getOriginatorAccountName());
         niprequest.setOriginatorBankVerificationNumber(request.getOriginatorBankVerificationNumber());
         niprequest.setTransactionLocation(request.getTransactionLocation());
         niprequest.setBeneficiaryKYCLevel(request.getBeneficiaryKYCLevel());
         niprequest.setOriginatorAccountNumber(request.getOriginatorAccountNumber());
         
       String niprequeststr = options.ObjectToXML(niprequest);
         
       niprequeststr = nipssm.encrypt(niprequeststr);
         
       String nipresponse =  nip.fundtransfersingleitemDc(niprequeststr);
       
       nipresponse = nipssm.decrypt(nipresponse);
       
       FTSingleCreditResponse nipresponseobject = (FTSingleCreditResponse) options.XMLToObject(nipresponse, new FTSingleCreditResponse());
         
       respcodes = options.getResponseObject(nipresponseobject.getResponseCode());
         
         response.setResponseCode(respcodes.getInlaksCode());
         response.setResponseDescription(respcodes.getMessage());
         response.setNibssSessionID(nipresponseobject.getSessionID());
         response.setInstitutionCode(request.getInstitutionCode());
         response.setDestinationInstitutionCode(nipresponseobject.getDestinationInstitutionCode());
         response.setRequestID(request.getRequestID());
         response.setHash(request.getHash());
         response.setChannelCode(nipresponseobject.getChannelCode());
         response.setBeneficiaryAccountNumber(nipresponse);
         
         
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
          respcodes = NIBBsResponseCodes.System_malfunction;
         response.setResponseCode(respcodes.getInlaksCode());
         response.setResponseDescription(respcodes.getMessage());
     }
     finally{
         try{
             
             headers.add("response");
             values.add(gson.toJson(response));  
             
          db.insertData(headers, values.toArray(),logTable);
         }
         catch(Exception s)
         {
             
         }
     }
        
        return gson.toJson(response);
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
    @Path("TransactionStatusQuery")
    @Produces(MediaType.APPLICATION_JSON)
    public String TransactionStatusQuery(@HeaderParam("authenticationID") String authenticationID, @HeaderParam("timeStamp") String timeStamp, @HeaderParam("applicationID") String applicationID, String payload) 
    {
     Gson gson = new Gson();
     
     TransactionStatusQueryResponse response = new TransactionStatusQueryResponse();
     
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
         
      TransactionStatusQueryRequest request = (TransactionStatusQueryRequest) gson.fromJson(payload, TransactionStatusQueryRequest.class);
         
   
         
         
         
         headers.add("applicationID");
         values.add(applicationID);
         
        
         
           headers.add("requestID");
        values.add(request.getRequestID());
         
        String stringtohash = request.getRequestID() + request.getNibssSessionID();
         
        String requesthash = request.getHash();
      
        String hash = options.get_SHA_512_Hash(stringtohash, "inlaks");
      
        headers.add("hash");
        values.add(hash);

     if(hash.equals(requesthash)){
                 
         TSQuerySingleRequest niprequest = new TSQuerySingleRequest();
             
         niprequest.setSessionID(request.getNibssSessionID());
         niprequest.setChannelCode(request.getChannelCode());
         niprequest.setSourceInstitutionCode(request.getInstitutionCode());
        
         
       String niprequeststr = options.ObjectToXML(niprequest);
         
       niprequeststr = nipssm.encrypt(niprequeststr);
         
       String nipresponse =  nip.txnstatusquerysingleitem(niprequeststr);
       
       nipresponse = nipssm.decrypt(nipresponse);
       
       FTSingleCreditResponse nipresponseobject = (FTSingleCreditResponse) options.XMLToObject(nipresponse, new FTSingleCreditResponse());
         
       respcodes = options.getResponseObject(nipresponseobject.getResponseCode());
         
         response.setResponseCode(respcodes.getInlaksCode());
         response.setResponseDescription(respcodes.getMessage());
         response.setNibssSessionID(nipresponseobject.getSessionID());
         
         
         
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
          respcodes = NIBBsResponseCodes.System_malfunction;
         response.setResponseCode(respcodes.getInlaksCode());
         response.setResponseDescription(respcodes.getMessage());
     }
     finally{
         try{
             
             headers.add("response");
             values.add(gson.toJson(response));  
             
          db.insertData(headers, values.toArray(),logTable);
         }
         catch(Exception s)
         {
             
         }
     }
        
        return gson.toJson(response);
    }

    

  @POST
    @Path("getFIList")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFIList(@HeaderParam("authenticationID") String authenticationID, @HeaderParam("timeStamp") String timeStamp, @HeaderParam("applicationID") String applicationID, String payload) 
    {
     Gson gson = new Gson();
     Connection conn = null;
     getFIListResponse response = new getFIListResponse();
     
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
         
        getFIListRequest request = (getFIListRequest) gson.fromJson(payload, getFIListRequest.class);
         
   
         
         
         
         headers.add("applicationID");
         values.add(applicationID);
         
        
         
           headers.add("requestID");
         values.add(request.getRequestID());
         
         String stringtohash = request.getRequestID();
         
      String requesthash = request.getHash();
      
      String hash = options.get_SHA_512_Hash(stringtohash, "inlaks");
      
      headers.add("hash");
      values.add(hash);

     if(hash.equals(requesthash)){
                 
                   
   
      ResultSet rs = db.getData("Select * from NIP_Institutions", conn);

      response.setChannelCode(request.getChannelCode());
      
     List<Records> records = new LinkedList<>();
      while(rs.next()){
          Records record = new Records();
          
          record.setInstitutionCode(rs.getString("InstitutionCode"));
          record.setCategory(String.valueOf(rs.getInt("Category")));
          record.setInstitutionName(rs.getString("InstitutionName"));
          records.add(record);
      }
      
      response.setNumberOfRecords(String.valueOf(records.size()));
      response.setHash(hash);
      response.setInstitutionCode(request.getInstitutionCode());
      response.setRecord(records.toArray(new Records[records.size()]));
         respcodes = NIBBsResponseCodes.SUCCESS;
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
          respcodes = NIBBsResponseCodes.System_malfunction;
         response.setResponseCode(respcodes.getInlaksCode());
         response.setResponseDescription(respcodes.getMessage());
     }
     finally{
         try{
             
             headers.add("response");
             values.add(gson.toJson(response));  
             
          db.insertData(headers, values.toArray(),logTable);
         }
         catch(Exception s)
         {
             
         }
     }
        
        return gson.toJson(response);
    }

    

    
}
