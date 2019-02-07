/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mcash.service.objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;


/**
 *
 * @author tadekayero
 */
@Getter
@XmlRootElement(name = "Pre-PaymentResponse")
public class Pre_PaymentResponse {
    @XmlElement(name = "SessionID")
    private String SessionID;
    
    @XmlElement(name = "RequestorID")
    private String RequestorID;
    
    @XmlElement(name = "PayerBVN")
    private int PayerBVN;
    
    @XmlElement(name = "PayerPhoneNumber")
    private String PayerPhoneNumber;
    
    @XmlElement(name = "MerchantCode")
    private String MerchantCode;
    
    @XmlElement(name = "MerchantName")
    private int MerchantName;
    
    @XmlElement(name = "MerchantPhoneNumber")
    private String MerchantPhoneNumber;
    
    @XmlElement(name = "Amount")
    private String Amount;
    
    @XmlElement(name = "Fee")
    private String Fee;
    
    @XmlElement(name = "FinancialInstitutions")
    private FinancialInstitutions FinancialInstitutions;
    
    @XmlElement(name = "ResponseCode")
    private String ResponseCode;
    
    
}
