/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bvn.service.objects;

/**
 *
 * @author inlaks-root
 */


import lombok.Getter;
import lombok.Setter;

 @Setter @Getter
public class BVNCredentials {
   private String IV;
   private String aesKey;
   private String password;
   private String userid;
}
