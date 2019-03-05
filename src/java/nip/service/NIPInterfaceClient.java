/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nip.service;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import nibss.nip.core.NIPInterface;
import nibss.nip.core.NIPInterface_Service;
//import nibss.nip.core.NIPTSQInterface;
//import nibss.nip.core.NIPTSQInterface_Service;
import nip.service.objects.FTSingleCreditRequest;
import nip.service.objects.FTSingleCreditResponse;
import nip.service.objects.NESingleRequest;
import nip.service.objects.NESingleResponse;
import nip.service.objects.TSQuerySingleRequest;
import nip.service.objects.TSQuerySingleResponse;
import nip.tools.AppParams;
import nip.tools.DBConnector;
import nip.tools.InstitutionDetails;
import nip.tools.NIBBsResponseCodes;
import nip.tools.PGPEncrytionTool;
import nip.tools.T24Link;
import nip.tools.T24TAFJLink;
import nip.tools.ofsParam;
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
    Connection conn = null;
    private DBConnector db;
    String logfilename = "NIPClientInterface";
    String logTable = "InlaksNIPWrapperLog";
    NIPInterface nip;
//    NIPTSQInterface niptsq;
    T24Link t24;
    Thread watcherthread = new Thread();
    String apikey = "";
    /**
     * Creates a new instance of NIPInterface
     */
    public NIPInterfaceClient() {

        try {

            options = new AppParams();

            nipssm = new PGPEncrytionTool(options);

            NIPInterface_Service nipservice = new NIPInterface_Service();

            nip = nipservice.getNIPInterfacePort();
            
//             NIPTSQInterface_Service niptsqservice = new NIPTSQInterface_Service();

//            niptsq = niptsqservice.getNIPTQSInterfacePort();

            db = new DBConnector(options.getDBserver(), options.getDBuser(), options.getDBpass(), "NIPLogs");

            t24 = new T24TAFJLink();

            if (watcherthread.getState() == Thread.State.NEW) {

                watcherthread = new Thread(() -> {
                    try {
                        Timer timer = new Timer();
                        timer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                options.MonitorPending(db, nipssm, t24);
                            }
                        }, 60 * 1000, 60 * 1000);
                    } catch (Exception v) {

                    }
                });

                watcherthread.setName("ExecutePendingCredits");
                watcherthread.start();
            }

        } catch (Exception e) {
            options.getServiceLogger(logfilename).LogError(e.getMessage(), e, Level.FATAL);
        }

    }

    /**
     * Retrieves representation of an instance of nip.service.NIPInterface
     *
     * @param authenticationID
     * @param timeStamp
     * @param applicationID
     * @param payload
     * @return an instance of java.lang.String
     */
    @POST
    @Path("NameEnquiry")
    @Produces(MediaType.APPLICATION_JSON)
    public String NameEnquiry(@HeaderParam("authenticationID") String authenticationID, @HeaderParam("timeStamp") String timeStamp, @HeaderParam("applicationID") String applicationID, String payload) {
        Gson gson = new Gson();
        String sessionID = "", monthlyTable = "";
        NameEnquiryResponse response = new NameEnquiryResponse();

        List<Object> values = new ArrayList<>();
        List<String> headers = new ArrayList<>();

        List<Object> nipvalues = new ArrayList<>();
        List<String> nipheaders = new ArrayList<>();

        headers.add("requestPayload");
        values.add(payload);

        Date reqdate = new Date();

        headers.add("requestDate");
        values.add(reqdate);
        
        String acctname = "";
        String bvn="";
        String kyc="";

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
            

            NameEnquiryRequest request = (NameEnquiryRequest) gson.fromJson(payload, NameEnquiryRequest.class);

            headers.add("applicationID");
            values.add(applicationID);

            headers.add("requestID");
            values.add(request.getRequestID());

            String stringtohash = request.getRequestID() + request.getDestinationInstitutionCode() + request.getAccountNumber();

            String requesthash = request.getHash();

            String hash = options.get_SHA_512_Hash(stringtohash, apikey);

            headers.add("hash");
            values.add(hash);

            if (hash.equals(requesthash)) {

                NESingleRequest niprequest = new NESingleRequest();

                niprequest.setAccountNumber(request.getAccountNumber());
                niprequest.setChannelCode(request.getChannelCode());
                niprequest.setDestinationInstitutionCode(request.getDestinationInstitutionCode());

                sessionID = options.generateSessionID(request.getInstitutionCode());
                niprequest.setSessionID(sessionID);

                nipvalues.add(sessionID);
                nipheaders.add("SessionID");

                nipvalues.add(request.getAccountNumber());
                nipheaders.add("AccountNumber");

                nipvalues.add(request.getChannelCode());
                nipheaders.add("ChannelCode");

                nipvalues.add(request.getDestinationInstitutionCode());
                nipheaders.add("DestinationInstitutionCode");

                nipvalues.add("OUTWARD");
                nipheaders.add("TranDirection");

                nipvalues.add("nameenquirysingleitem");
                nipheaders.add("MethodName");

                String datestr = sessionID.substring(6, 18);

                SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");

                Date date = sdf.parse(datestr);

                nipvalues.add(date);
                nipheaders.add("TransactionDate");

                SimpleDateFormat df = new SimpleDateFormat("MMMyyyy");

                monthlyTable = df.format(date) + "NIP_TRANSACTIONS";

                String createquery = options.getCreateNIPTableScript(monthlyTable);

                try {
                    db.Execute(createquery);
                } catch (Exception r) {

                }

                db.insertData(nipheaders, nipvalues.toArray(), monthlyTable);

                String niprequeststr = options.ObjectToXML(niprequest);

                niprequeststr = nipssm.encrypt(niprequeststr);

                String nipresponse = nip.nameenquirysingleitem(niprequeststr);

                nipresponse = nipssm.decrypt(nipresponse);

                NESingleResponse nipresponseobject = (NESingleResponse) options.XMLToObject(nipresponse, new NESingleResponse());

                respcodes = options.getResponseObject(nipresponseobject.getResponseCode());
                
                bvn = nipresponseobject.getBankVerificationNumber();
                acctname = nipresponseobject.getAccountName();
                kyc = nipresponseobject.getKYCLevel();

                response.setResponseCode(respcodes.getInlaksCode());
                response.setResponseDescription(respcodes.getMessage());
                response.setNameEnquiryRef(nipresponseobject.getSessionID());
                response.setBankVerificationNo(nipresponseobject.getBankVerificationNumber());
                response.setKycLevel(nipresponseobject.getKYCLevel());
                response.setRequestID(request.getRequestID());
                response.setAccountName(nipresponseobject.getAccountName());
                response.setAccountNumber(nipresponseobject.getAccountNumber());
                response.setDestinationInstitutionCode(nipresponseobject.getDestinationInstitutionCode());
                response.setChannelCode(nipresponseobject.getChannelCode());
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

                String query = "Update " + monthlyTable + " set ResponseCode='" + respcodes.getCode() + "', AccountName='"+acctname+"', KYCLevel='"+kyc+"', BankVerificationNumber='"+bvn+"', StatusMessage='" + respcodes.getMessage() + "' where SessionID='" + sessionID + "' and MethodName='nameenquirysingleitem'";

                db.Execute(query);

            } catch (Exception s) {

            }
        }

        return gson.toJson(response);
    }

    /**
     * Retrieves representation of an instance of nip.service.NIPInterface
     *
     * @param authenticationID
     * @param timeStamp
     * @param applicationID
     * @param payload
     * @return an instance of java.lang.String
     */
    @POST
    @Path("FundsTransferDC")
    @Produces(MediaType.APPLICATION_JSON)
    public String FundsTransferDC(@HeaderParam("authenticationID") String authenticationID, @HeaderParam("timeStamp") String timeStamp, @HeaderParam("applicationID") String applicationID, String payload) {
        Gson gson = new Gson();

        FundsTransferDCResponse response = new FundsTransferDCResponse();

        // 
        FTSingleCreditResponse nipresponseobject = new FTSingleCreditResponse();
        String sessionID = "";

        List<Object> values = new ArrayList<>();
        List<String> headers = new ArrayList<>();

        List<Object> nipvalues = new ArrayList<>();
        List<String> nipheaders = new ArrayList<>();

        headers.add("requestPayload");
        values.add(payload);

        Date reqdate = new Date();

        headers.add("requestDate");
        values.add(reqdate);
        String monthlyTable = "";

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
            
            

            FundsTransferDCRequest request = (FundsTransferDCRequest) gson.fromJson(payload, FundsTransferDCRequest.class);

            headers.add("applicationID");
            values.add(applicationID);

            headers.add("requestID");
            values.add(request.getRequestID());

            String stringtohash = request.getRequestID() + request.getDestinationInstitutionCode() + request.getBeneficiaryAccountNumber() + request.getAmount();

            String requesthash = request.getHash();

            String hash = options.get_SHA_512_Hash(stringtohash, apikey);

            headers.add("hash");
            values.add(hash);

            if (hash.equals(requesthash)) {

                FTSingleCreditRequest niprequest = new FTSingleCreditRequest();

                niprequest.setBeneficiaryAccountNumber(request.getBeneficiaryAccountNumber());
                niprequest.setChannelCode(request.getChannelCode());
                niprequest.setDestinationInstitutionCode(request.getDestinationInstitutionCode());
                sessionID = options.generateSessionID(request.getInstitutionCode());
                niprequest.setSessionID(sessionID);
                niprequest.setBeneficiaryAccountName(request.getBeneficiaryAccountName());
                niprequest.setNameEnquiryRef(request.getNameEnquiryRef());
                niprequest.setBeneficiaryBankVerificationNumber(request.getBeneficiaryBankVerificationNumber());
                niprequest.setNarration(request.getNarration());//"Inlaks FT Single DC Test ");
                niprequest.setOriginatorKYCLevel(request.getOriginatorKYCLevel());
                niprequest.setPaymentReference(request.getPaymentReference());
                niprequest.setOriginatorAccountName(request.getOriginatorAccountName());
                niprequest.setOriginatorBankVerificationNumber(request.getOriginatorBankVerificationNumber());
                niprequest.setTransactionLocation(request.getTransactionLocation());
                niprequest.setBeneficiaryKYCLevel(request.getBeneficiaryKYCLevel());
                niprequest.setOriginatorAccountNumber(request.getOriginatorAccountNumber());
                niprequest.setAmount(request.getAmount());

                nipvalues.add(Double.parseDouble(request.getAmount()));
                nipheaders.add("Amount");

                nipvalues.add(request.getBeneficiaryAccountName());
                nipheaders.add("BeneficiaryAccountName");

                nipvalues.add(sessionID);
                nipheaders.add("SessionID");

                nipvalues.add(request.getBeneficiaryAccountNumber());
                nipheaders.add("BeneficiaryAccountNumber");

                nipvalues.add(request.getBeneficiaryBankVerificationNumber());
                nipheaders.add("BeneficiaryBankVerificationNumber");

                nipvalues.add(request.getChannelCode());
                nipheaders.add("ChannelCode");

                nipvalues.add(request.getDestinationInstitutionCode());
                nipheaders.add("DestinationInstitutionCode");

                nipvalues.add(request.getOriginatorAccountName());
                nipheaders.add("OriginatorAccountName");

                nipvalues.add(request.getOriginatorAccountNumber());
                nipheaders.add("OriginatorAccountNumber");

                nipvalues.add(request.getOriginatorBankVerificationNumber());
                nipheaders.add("OriginatorBankVerificationNumber");

                nipvalues.add(request.getOriginatorKYCLevel());
                nipheaders.add("OriginatorKYCLevel");

                nipvalues.add(request.getTransactionLocation());
                nipheaders.add("TransactionLocation");

                nipvalues.add(request.getNameEnquiryRef());
                nipheaders.add("NameEnquiryRef");

                nipvalues.add(request.getNarration());
                nipheaders.add("Narration");

                nipvalues.add(request.getPaymentReference());
                nipheaders.add("PaymentReference");

                nipvalues.add("OUTWARD");
                nipheaders.add("TranDirection");

                nipvalues.add("fundtransfersingleitem_dc");
                nipheaders.add("MethodName");

                String datestr = sessionID.substring(6, 18);

                SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");

                Date date = sdf.parse(datestr);

                nipvalues.add(date);
                nipheaders.add("TransactionDate");

                SimpleDateFormat df = new SimpleDateFormat("MMMyyyy");

                monthlyTable = df.format(date) + "NIP_TRANSACTIONS";

                String createquery = options.getCreateNIPTableScript(monthlyTable);
                 db = new DBConnector(options.getDBserver(), options.getDBuser(), options.getDBpass(), "NIPLogs");

                try {
                    
                    db.Execute(createquery);
                } catch (Exception r) {
                        String hy = r.getMessage();
                        String hh = "";
                }

                db.insertData(nipheaders, nipvalues.toArray(), monthlyTable);

                String niprequeststr = options.ObjectToXML(niprequest);

                niprequeststr = nipssm.encrypt(niprequeststr);

                String nipresponse = nip.fundtransfersingleitemDc(niprequeststr);

                nipresponse = nipssm.decrypt(nipresponse);

                nipresponseobject = (FTSingleCreditResponse) options.XMLToObject(nipresponse, new FTSingleCreditResponse());

                sessionID = nipresponseobject.getSessionID();
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

                String query = "Update " + monthlyTable + " set ResponseCode='" + respcodes.getCode() + "', StatusMessage='" + respcodes.getMessage() + "' where SessionID='" + sessionID + "' and MethodName='fundtransfersingleitem_dc'";

                db.Execute(query);

                switch (respcodes.getCode()) {

                    case "97":
                    case "09":

                        query = "insert into NIPpendingFT_Outward select *,0 from " + monthlyTable + " where SessionID='" + sessionID + "' and MethodName='fundtransfersingleitem_dc'";

                        db.Execute(query);

                        break;

                    case "00":
                        break;

                    default:

                        InstitutionDetails details = this.getInstitutionDetails(response.getInstitutionCode());
                        ofsParam param = new ofsParam();
                        String[] credentials = new String[]{options.getOfsuser(), options.getOfspass(), details.getCompanyCode()};
                        param.setCredentials(credentials);
                        param.setOperation("FUNDS.TRANSFER");

                        param.setVersion("REV.WD");
                        String[] ofsoptions = new String[]{"", "R", "PROCESS", "2", "0"};
                        param.setOptions(ofsoptions);

                        param.setTransaction_id(nipresponseobject.getPaymentReference());

                        param.setDataItems(new ArrayList<>());

                        String ofstr = t24.generateOFSTransactString(param);

                        String result = t24.PostMsg(ofstr);

                        if (t24.IsSuccessful(result)) {

                        } else {

                            query = "insert into NIPpendingFT_Outward select *,1 from " + monthlyTable + " where SessionID='" + sessionID + "' and MethodName='fundtransfersingleitem_dc'";

                            db.Execute(query);

                        }
                        break;
                }

            } catch (Exception s) {

            }
        }

        return gson.toJson(response);
    }

    /**
     * Retrieves representation of an instance of nip.service.NIPInterface
     *
     * @param authenticationID
     * @param timeStamp
     * @param applicationID
     * @param payload
     * @return an instance of java.lang.String
     */
    @POST
    @Path("TransactionStatusQuery")
    @Produces(MediaType.APPLICATION_JSON)
    public String TransactionStatusQuery(@HeaderParam("authenticationID") String authenticationID, @HeaderParam("timeStamp") String timeStamp, @HeaderParam("applicationID") String applicationID, String payload) {
        Gson gson = new Gson();
        String monthlyTable = "";
        TransactionStatusQueryResponse response = new TransactionStatusQueryResponse();

        List<Object> values = new ArrayList<>();
        List<String> headers = new ArrayList<>();

        List<Object> nipvalues = new ArrayList<>();
        List<String> nipheaders = new ArrayList<>();

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
            
            
            TransactionStatusQueryRequest request = (TransactionStatusQueryRequest) gson.fromJson(payload, TransactionStatusQueryRequest.class);

            headers.add("applicationID");
            values.add(applicationID);

            headers.add("requestID");
            values.add(request.getRequestID());

            String stringtohash = request.getRequestID() + request.getNibssSessionID();

            String requesthash = request.getHash();

            String hash = options.get_SHA_512_Hash(stringtohash, apikey);

            headers.add("hash");
            values.add(hash);

            if (hash.equals(requesthash)) {

                TSQuerySingleRequest niprequest = new TSQuerySingleRequest();

                niprequest.setSessionID(request.getNibssSessionID());
                niprequest.setChannelCode(request.getChannelCode());
                niprequest.setSourceInstitutionCode(request.getInstitutionCode());

                nipvalues.add(request.getNibssSessionID());
                nipheaders.add("SessionID");

                nipvalues.add(request.getChannelCode());
                nipheaders.add("ChannelCode");

                nipvalues.add(request.getInstitutionCode());
                nipheaders.add("SourceInstitutionCode");

                nipvalues.add("OUTWARD");
                nipheaders.add("TranDirection");

                nipvalues.add("txnstatusquerysingleitem");
                nipheaders.add("MethodName");

                String datestr = request.getNibssSessionID().substring(6, 18);

                SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");

                Date date = sdf.parse(datestr);

                nipvalues.add(date);
                nipheaders.add("TransactionDate");

                SimpleDateFormat df = new SimpleDateFormat("MMMyyyy");

                monthlyTable = df.format(date) + "NIP_TRANSACTIONS";

                String createquery = options.getCreateNIPTableScript(monthlyTable);

                try {
                    db.Execute(createquery);
                } catch (Exception r) {

                }
  db = new DBConnector(options.getDBserver(), options.getDBuser(), options.getDBpass(), "NIPLogs");
                db.insertData(nipheaders, nipvalues.toArray(), monthlyTable);

                String niprequeststr = options.ObjectToXML(niprequest);

                niprequeststr = nipssm.encrypt(niprequeststr);
              String nipresponse ="";// niptsq.txnstatusquerysingleitem(niprequeststr);

                nipresponse = nipssm.decrypt(nipresponse);

                TSQuerySingleResponse nipresponseobject = (TSQuerySingleResponse) options.XMLToObject(nipresponse, new TSQuerySingleResponse());

                respcodes = options.getResponseObject(nipresponseobject.getResponseCode());

                response.setResponseCode(respcodes.getInlaksCode());
                response.setResponseDescription(respcodes.getMessage());
                response.setNibssSessionID(nipresponseobject.getSessionID());

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

                String query = "Update " + monthlyTable + " set ResponseCode='" + respcodes.getCode() + "', StatusMessage='" + respcodes.getMessage() + "' where SessionID='" + response.getNibssSessionID() + "'";

                db.Execute(query);

            } catch (Exception s) {

            }
        }

        return gson.toJson(response);
    }

    @POST
    @Path("getFIList")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFIList(@HeaderParam("authenticationID") String authenticationID, @HeaderParam("timeStamp") String timeStamp, @HeaderParam("applicationID") String applicationID, String payload) {
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
            
            
            
            

            getFIListRequest request = (getFIListRequest) gson.fromJson(payload, getFIListRequest.class);

            headers.add("applicationID");
            values.add(applicationID);

            headers.add("requestID");
            values.add(request.getRequestID());

            String stringtohash = request.getRequestID();

            String requesthash = request.getHash();

            String hash = options.get_SHA_512_Hash(stringtohash, apikey);

            headers.add("hash");
            values.add(hash);

            if (hash.equals(requesthash)) {
  db = new DBConnector(options.getDBserver(), options.getDBuser(), options.getDBpass(), "NIPLogs");
                 rs = db.getData("Select * from NIP_Institutions", conn);

                //response.setChannelCode(request.getChannelCode());

                List<Records> records = new LinkedList<>();
                while (rs.next()) {
                    Records record = new Records();

                    record.setInstitutionCode(rs.getString("InstitutionCode"));
                    record.setCategory(String.valueOf(rs.getInt("Category")));
                    record.setInstitutionName(rs.getString("InstitutionName"));
                    records.add(record);
                }

                response.setNumberOfRecords(String.valueOf(records.size()));
                response.setHash(hash);
                //response.setInstitutionCode(request.getInstitutionCode());
                response.setRecord(records.toArray(new Records[records.size()]));
                respcodes = NIBBsResponseCodes.SUCCESS;
                response.setResponseCode(respcodes.getInlaksCode());
                response.setResponseDescription(respcodes.getMessage());

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
            } catch (Exception s) {

            }
        }

        return gson.toJson(response);
    }

    private InstitutionDetails getInstitutionDetails(String InstCode) {

        try {

            ArrayList<List<String>> result = t24.getOfsData("GET.BANK.NIP.PARAM", options.getOfsuser(), options.getOfspass(), "INSTITUTION.CODE:EQ=" + InstCode, "");

            List<String> headers = result.get(0);

            if (headers.size() != result.get(1).size()) {
                return null;
            }

            String companycode = result.get(1).get(headers.indexOf("@ID")).replace("\"", "").trim();
            String payableacct = result.get(1).get(headers.indexOf("PAYABLE.ACCOUNT")).replace("\"", "").trim();
            String receivableacct = result.get(1).get(headers.indexOf("RECEIVABLE.ACCOUNT")).replace("\"", "").trim();

            InstitutionDetails details = new InstitutionDetails();
            details.setCompanyCode(companycode);
            details.setInstitutionCode(InstCode);
            details.setPayableAccount(payableacct);

            details.setReceivableAccount(receivableacct);

            return details;

        } catch (Exception d) {

            return null;
        }
    }

}
