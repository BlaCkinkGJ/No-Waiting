package kr.ac.pusan.cs.sinbaram.nolinerforadmin.RSA;


import android.util.Base64;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/*
 * [ 메소드 리스트 ]
 * String encryption();
 * String decryption(String encryptedData);
 *
 * void init();
 *
 * String getBuffer();
 * String getPublicKey();
 * String getPrivateKey();
 *
 * String setBuffer(String data);
 * String setPublicKey(String publicKey);
 * String setPrivateKey(String privateKey);
 */
public class RSA {

    private String buffer;

    private PublicKey  publicKey;
    private PrivateKey privateKey;

    public RSA() throws Exception {
        this.publicKey = null;
        this.privateKey = null;
        this.buffer = null;
    }

    /*
     * 주의 > 제한적으로 사용하도록 할 것!!!!
     *
     * 관리자가 리스트를 만들면 실행되도록 합니다.
     *
     * [ 실행 후 ]
     * public key는 firebase에 반드시 들어가야 합니다.
     * 이 때, getPublicKey()를 사용하도록 합니다.
     */
    public void init() throws Exception {
        KeyPair keyPair = initKeyPair(); // this from my class
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }

    public String encryption() throws Exception {
        if(this.buffer == null || this.publicKey == null) throw new Exception();
        PublicKey publicKey = this.publicKey;
        String    buffer    = this.buffer;
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(buffer.getBytes());
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
    }



    // 주의 > 제한적으로 사용하도록 할 것!!!!
    public String decryption(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.decode(encryptedData, Base64.DEFAULT));
        return new String(decryptedBytes);
    }

    public String getBuffer(){
        return this.buffer;
    }

    public String getPublicKey(){
        return Base64.encodeToString(this.publicKey.getEncoded(), Base64.DEFAULT);
    }

    public String getPrivateKey(){
        return Base64.encodeToString(this.privateKey.getEncoded(), Base64.DEFAULT);
    }

    public void setBuffer(String data){
        this.buffer = data;
    }

    public void setPublicKey(String publicKey) throws
            NoSuchAlgorithmException,
            InvalidKeySpecException {
        byte[] keyBytes = Base64.decode(publicKey,Base64.DEFAULT);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.publicKey = keyFactory.generatePublic(spec);
    }

    public void setPrivateKey(String privateKey) throws
            NoSuchAlgorithmException,
            InvalidKeySpecException {
        byte[] keyBytes = Base64.decode(privateKey,Base64.DEFAULT);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.privateKey = keyFactory.generatePrivate(spec);
    }


    private KeyPair initKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        final int size = 512;
        keyGen.initialize(size);
        return keyGen.genKeyPair();
    }
}
