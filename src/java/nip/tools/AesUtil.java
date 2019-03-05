package nip.tools;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author TP
 */
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.json.JSONException;




public class AesUtil {
 private final int keySize;
    private final int iterationCount;
    private final Cipher cipher;
    
    public AesUtil(int keySize, int iterationCount) {
        this.keySize = keySize;
        this.iterationCount = iterationCount;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        }
        catch (NoSuchAlgorithmException a){ throw fail (a);}
         catch  (NoSuchPaddingException e) {
            throw fail(e);
        }
    }
    
    public String encrypt(String aeskey, String iv, String passphrase, String plaintext) {
        try {
            SecretKey key = new SecretKeySpec(aeskey.getBytes(),"AES");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));
            return this.bytesToHex(cipher.doFinal(plaintext.getBytes("UTF-8")));
        }
        catch (InvalidKeyException a) {throw fail (a);}
        catch  (InvalidAlgorithmParameterException b) {throw fail (b);}
        catch  (IllegalBlockSizeException c) {throw fail (c);}
        catch  (BadPaddingException d) {throw fail (d);}
        catch        (UnsupportedEncodingException e) {
            throw fail(e);
        }
    }
    
    public String decrypt(String aeskey, String iv, String passphrase, String ciphertext) throws JSONException {
        try {
            SecretKey key  = new SecretKeySpec(aeskey.getBytes(),"AES");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));
            return new String(cipher.doFinal(this.hexToBytes(ciphertext)), "UTF-8");
        }
        catch (InvalidKeyException a) {throw new JSONException(a);}
        catch  (InvalidAlgorithmParameterException b) {throw new JSONException (b);}
        catch  (IllegalBlockSizeException c) {throw new JSONException(c);}
        catch  (BadPaddingException d) {throw new JSONException (d);}
        catch        (UnsupportedEncodingException e) {
            throw new JSONException(e);
        }
    }
    
    private SecretKey generateKey(byte[] salt, String passphrase) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), salt, iterationCount, keySize);
            SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return key;
        }
        catch (NoSuchAlgorithmException a) {throw fail (a);}
        catch      (InvalidKeySpecException e) {
            throw fail(e);
        }
    }
    
    public static String random(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return hex(salt);
    }
    
    public static String base64(byte[] bytes) {
        return DatatypeConverter.printBase64Binary(bytes);
    }
    
    public static byte[] base64(String str) {
        return DatatypeConverter.parseBase64Binary(str);
    }
    
    public static String hex(byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes);
    }
    
    public static byte[] hex(String str) {
        return DatatypeConverter.parseHexBinary(str);
    }
    
    private IllegalStateException fail(Exception e) throws RuntimeException {
        return new IllegalStateException(e);
    }
    
    
    public String getsalt (){
        return "3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55";
    }
    public String getiv(){
        return "F27D5C9927726BCEFE7510B1BDD3D137";
    }
    public String getpassPhrase(){
        
        return "the quick brown fox jumps over the lazy dog";
    }
    
    public  String bytesToHex(byte[] data)
{
if (data==null) {
return null;
}
int len = data.length;
String str = "";
for (int i=0; i<len; i++) 
{
if ((data[i]&0xFF)<16) {
str = str + "0" + java.lang.Integer.toHexString(data[i]&0xFF);
}
else {
str = str + java.lang.Integer.toHexString(data[i]&0xFF);
}
}
return str;
}

public  byte[] hexToBytes(String str) {
if (str==null) {
return null;
}
else if (str.length() < 2) {
return null;
}
else {
int len = str.length() / 2;
byte[] buffer = new byte[len];
for (int i=0; i<len; i++) {
buffer[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
}
return buffer;
}
}
}
    
    
    
    
    

