package com.aliyuneventbridge.httptarget.siginature;

import org.apache.http.Header;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aliyuneventbridge.httptarget.siginature.EBConstants.*;

public class SignatureVerify {

    private static Logger logger = LoggerFactory.getLogger(SignatureVerify.class);

    /**
     * Verify the request signature
     *
     * @param urlWithQueryString
     * @param headers
     * @param body
     *
     * @return
     */
    public static boolean verify(String urlWithQueryString, List<Header> headers, byte[] body) {
        Map<String, String> headerMap = toHeaderMap(headers);
        if (!checkParam(urlWithQueryString, headerMap)) {
            return Boolean.FALSE;
        }
        String sign = headerMap.get(HEADER_X_EVENTBRIDGE_SIGNATURE_V2);

        PublicKey publicKey = PublicKeyBuilder.buildPublicKey(headerMap.get(HEADER_X_EVENTBRIDGE_SIGNATURE_URL));
        String stringToSign = StringToSignBuilder.defaultStringToSign(urlWithQueryString, headerMap, body);
        return verifySignatureBySHA256withRSA(stringToSign, publicKey, sign);
    }

    private static boolean checkParam(String urlWithQueryString, Map<String, String> headerMap) {
        if (Strings.isBlank(urlWithQueryString)) {
            return Boolean.FALSE;
        }
        if (!headerMap.containsKey(HEADER_X_EVENTBRIDGE_SIGNATURE_TIMESTAMP) || !headerMap.containsKey(
            HEADER_X_EVENTBRIDGE_HASH_METHOD) || !headerMap.containsKey(HEADER_X_EVENTBRIDGE_SIGNATURE_VERSION)
            || !headerMap.containsKey(HEADER_X_EVENTBRIDGE_SIGNATURE_URL)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * convert header list to header map
     *
     * @param headers
     *
     * @return
     */
    public static Map<String, String> toHeaderMap(List<Header> headers) {
        Map<String, String> headerMap = new HashMap<>();
        if (headers == null || headers.isEmpty()) {
            return headerMap;
        }
        for (Header header : headers) {
            headerMap.put(header.getName(), header.getValue());
        }
        return headerMap;
    }

    /**
     * Decrypt the encryptStr with the publicKey (same with privatekey)
     *
     * @param publicKey
     * @param encryptStr
     *
     * @return
     *
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String decrypt(PublicKey publicKey, String encryptStr) {
        byte[] decryptCode = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] decodeByte = DatatypeConverter.parseBase64Binary(encryptStr);
            decryptCode = cipher.doFinal(decodeByte);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Decrypt encryptStr failed. which is " + encryptStr, e);
        }
        return new String(decryptCode);
    }

    public static Boolean verifySignatureBySHA256withRSA(String stringToSign, PublicKey publicKey, String signature) {
        try{
            Signature verify = Signature.getInstance("SHA256withRSA");
            verify.initVerify(publicKey);
            verify.update(stringToSign.getBytes());
            return verify.verify(Base64.getDecoder().decode(signature));
        } catch (Exception e){
            throw new RuntimeException("Verify signature failed. which stringToSign is " + stringToSign, e);
        }
    }
}
