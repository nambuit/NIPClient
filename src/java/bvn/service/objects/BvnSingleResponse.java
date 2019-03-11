/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bvn.service.objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author dogor-Igbosuah
 */
@Getter 
@Setter
public class BvnSingleResponse {
    
    private String BVN; 
    
    private String FirstName; 
    
    private String MiddleName; 
    
    private String LastName;
    
    private String DateOfBirth; 
    
    private String PhoneNumber1; 
     
    private String RegistrationDate; 
    
    private String EnrollmentBank; 
    
    private String EnrollmentBranch;
    
    private String Base64Image; 
    
    private String Email;
    
    private String Gender; 
    
    private String PhoneNumber2; 
    
    private String LevelOfAccount; 
    
    private String LgaOfOrigin; 
    
    private String LgaOfResidence; 
    
    private String MaritalStatus; 
    
    private String NIN; 
    
    private String NameOnCard; 
    
    private String Nationality;
    
}
