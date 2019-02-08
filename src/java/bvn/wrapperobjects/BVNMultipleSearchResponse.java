/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bvn.wrapperobjects;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author emusa
 */
@Getter
@Setter
public class BVNMultipleSearchResponse {

    private String requestID;
    private String Searchresults;
    private String Searchresult;
    private String resultlist;
    private String resultstatus;
    private String bvn;
    private String firstname;
    private String middlename;
    private String lastname;
    private String dateofbirth;
    private String phonenumber;
    private String registrationdate;
    private String enrollmentbank;
    private String entrollmentbranch;
    private String imagebase64;
    private String institutioncode;
    private String hash;
    private String responsecode;
    private String responsedescription;

}
