package com.aliyuneventbridge.httptarget.siginature;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class PublicKeyBuilder {

    private static HttpClient client = HttpClientBuilder.create()
        .build();

    private static Map<String, PublicKey> publicKeyMap = new ConcurrentHashMap<>();

    public static String downloadCertWithRetry(URI certUrl) {
        try {
            return downloadCert(certUrl);
        } catch (RuntimeException e) {
            if (e instanceof RuntimeException) {
                return downloadCert(certUrl);
            } else {
                throw e;
            }
        }
    }

    private static String downloadCert(URI certUrl) {
        //try {
            //verifyCertUrl(certUrl);
            //HttpResponse response = client.execute(new HttpGet(certUrl));
            //if (response.getStatusLine()
            //    .getStatusCode() / 100 == 2) {
            //    try {
                    String publicCerl = "-----BEGIN CERTIFICATE-----\n"
                        + "MIICxTCCAi4CCQCTtXSvs6sFWTANBgkqhkiG9w0BAQUFADCBpTELMAkGA1UEBhMC\n"
                        + "Q04xETAPBgNVBAgMCFpoZUppYW5nMREwDwYDVQQHDAhIYW5nWmhvdTEPMA0GA1UE\n"
                        + "CgwGQWxpeXVuMRMwEQYDVQQLDApNaWRkbGV3YXJlMR8wHQYDVQQDDBZldmVudGJy\n"
                        + "aWRnZS5hbGl5dW4uY29tMSkwJwYJKoZIhvcNAQkBFhpqaW5nbHVvLnNsQGFsaWJh\n"
                        + "YmEtaW5jLmNvbTAgFw0yMTAxMjUwOTIzMjZaGA8yMTIxMDEwMTA5MjMyNlowgaUx\n"
                        + "CzAJBgNVBAYTAkNOMREwDwYDVQQIDAhaaGVKaWFuZzERMA8GA1UEBwwISGFuZ1po\n"
                        + "b3UxDzANBgNVBAoMBkFsaXl1bjETMBEGA1UECwwKTWlkZGxld2FyZTEfMB0GA1UE\n"
                        + "AwwWZXZlbnRicmlkZ2UuYWxpeXVuLmNvbTEpMCcGCSqGSIb3DQEJARYaamluZ2x1\n"
                        + "by5zbEBhbGliYWJhLWluYy5jb20wgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGB\n"
                        + "AMtqUJ8GfVzgCiyUzWjpe3Y+7Ub/GERfqS1zAneVUEXJvRssovrqy4eGXwPBpACG\n"
                        + "MDf7iOK7sCk1lKDFDGjNk34SsR6zcpq8h3z2fHIzEwqSgo4tdRjAEeSBUTrGlVU/\n"
                        + "suyMlcPiXDAYGOiWc2CISpD/709Ni7xHZttfeh3sde1/AgMBAAEwDQYJKoZIhvcN\n"
                        + "AQEFBQADgYEADWYxzJAAtteTEcJVf5HUgIDbqwJwTS198HxLRhUSdwy9jBh7VpZo\n"
                        + "3UOsqW+oU9TdFkaHsmVQWbH6UzoOtCeZiU45z8G6nxFssg0ZR94U4pXI4od4GaH3\n"
                        + "485p83bOGV5jRmsS0winbtDQBrkTrsRB0hfPogoCwVo/p3MAYbCmnJY=\n" + "-----END CERTIFICATE-----";
                        //new String(toByteArray(response.getEntity()
                        //.getContent()), Charset.forName("UTF-8"));
                    return publicCerl;
                //} finally {
                //    response.getEntity()
                //        .getContent()
                //        .close();
                //}
            //} else {
            //    throw new RuntimeException("Could not download the certificate from EventBridge:" + certUrl);
            //}
        //} catch (IOException e) {
        //    throw new RuntimeException("Unable to download SNS certificate from " + certUrl.toString(), e);
        //}
    }

    public static byte[] toByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            byte[] b = new byte[4096];
            boolean var3 = false;

            int n;
            while ((n = is.read(b)) != -1) {
                output.write(b, 0, n);
            }

            byte[] var4 = output.toByteArray();
            return var4;
        } finally {
            output.close();
        }
    }

    /**
     * Verify the certUrl is valid.
     *
     * @param certUrl
     */
    private static void verifyCertUrl(URI certUrl) {
        return;
    }

    /**
     * Build public Key
     *
     * @param certurl
     *
     * @return
     */
    public static PublicKey buildPublicKey(String certurl) {
        if (publicKeyMap.containsKey(certurl)) {
            return publicKeyMap.get(certurl);
        }
        String cert = downloadCertWithRetry(URI.create(certurl));
        PublicKey publicKey = null;
        try {
            CertificateFactory fact = CertificateFactory.getInstance("X.509");
            InputStream stream = new ByteArrayInputStream(cert.getBytes(Charset.forName("UTF-8")));
            X509Certificate cer = (X509Certificate)fact.generateCertificate(stream);
            publicKey = cer.getPublicKey();
        } catch (Exception e) {
            throw new RuntimeException("Could not create public key from certificate", e);
        }
        publicKeyMap.put(certurl, publicKey);
        return publicKey;
    }
}
