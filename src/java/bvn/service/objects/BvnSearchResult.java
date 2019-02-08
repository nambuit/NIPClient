/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bvn.service.objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

/**
 *
 * @author dogor-Igbosuah
 */

@Getter 
@XmlRootElement(name = "BvnSearchResult")
public class BvnSearchResult {
    
    @XmlElement(name = "Bvn")
    private String Bvn; 
    
    @XmlElement(name = "FirstName")
    private String FirstName; 
    
    @XmlElement(name = "MiddleName")
    private String MiddleName; 
    
    @XmlElement(name = "LastName")
    private String LastName; 
     
    @XmlElement(name = "DateOfBirth")
    private String DateOfBirth; 
    
    @XmlElement(name = "PhoneNumber")
    private String PhoneNumber; 
     
    @XmlElement(name = "RegistrationDate")
    private String RegistrationDate; 
    
    @XmlElement(name = "EnrollmentBank")
    private String EnrollmentBank; 
    
    @XmlElement(name = "EnrollmentBranch")
    private String EnrollmentBranch; 
    
    @XmlElement(name = "ImageBase64")
    private String ImageBase64; 
}
