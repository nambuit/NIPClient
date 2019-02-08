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
public class BvnMultipleSearchRequest {

    private String requestID;
    private String BVNs;
    private String bankcode;
    private String institutioncode;
    private String hash;

}
