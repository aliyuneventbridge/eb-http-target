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
        try {
            verifyCertUrl(certUrl);
            HttpResponse response = client.execute(new HttpGet(certUrl));
            if (response.getStatusLine()
                .getStatusCode() / 100 == 2) {
                try {
                    String publicCerl = "-----BEGIN CERTIFICATE-----\n"
                        + "MIICwzCCAiwCCQC++87W+yaNEDANBgkqhkiG9w0BAQUFADCBpTELMAkGA1UEBhMC\n"
                        + "Q04xETAPBgNVBAgMCFpoZUppYW5nMREwDwYDVQQHDAhIYW5nWmhvdTEPMA0GA1UE\n"
                        + "CgwGQWxpeXVuMRMwEQYDVQQLDApNaWRkbGV3YXJlMR8wHQYDVQQDDBZldmVudGJy\n"
                        + "aWRnZS5hbGl5dW4uY29tMSkwJwYJKoZIhvcNAQkBFhpqaW5nbHVvLnNsQGFsaWJh\n"
                        + "YmEtaW5jLmNvbTAeFw0yMTAxMjExMzIwMzdaFw0zMTAxMTkxMzIwMzdaMIGlMQsw\n"
                        + "CQYDVQQGEwJDTjERMA8GA1UECAwIWmhlSmlhbmcxETAPBgNVBAcMCEhhbmdaaG91\n"
                        + "MQ8wDQYDVQQKDAZBbGl5dW4xEzARBgNVBAsMCk1pZGRsZXdhcmUxHzAdBgNVBAMM\n"
                        + "FmV2ZW50YnJpZGdlLmFsaXl1bi5jb20xKTAnBgkqhkiG9w0BCQEWGmppbmdsdW8u\n"
                        + "c2xAYWxpYmFiYS1pbmMuY29tMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDG\n"
                        + "HZD42MuTIMsyAilDiLCq73emBwQ5eYWzY+lDG9JCwZPnI73hgq/JG1r+7gurn7mH\n"
                        + "oSgzmavRcJQzu8mAZHPBaneohetL5i4lmsPWV/Ef869OmBZi+j0rjUgJmTjAV9Vr\n"
                        + "qTppK/rO8gedBSIiymEWpR1rgzqYZvrMrFrcFy0oqwIDAQABMA0GCSqGSIb3DQEB\n"
                        + "BQUAA4GBAE8FQjH6SDM2CMU4RBWuI2/jqpDJtg7qc30aasVRyjlbxhSGBkzmAYnM\n"
                        + "xwyJSfQImDs/n35uuYE3pKH0bLRlOQu9Z0TlumSRlhin+YXBfPNScoyX8upNfLgr\n"
                        + "peHK7YGGpAbJH8Qh3GbOueSD1wT/MWA15QfQABHgtrYl5Am+VNNc\n" + "-----END CERTIFICATE-----";
                        //new String(toByteArray(response.getEntity()
                        //.getContent()), Charset.forName("UTF-8"));
                    System.out.println("publicCerl:" + publicCerl);
                    return publicCerl;
                } finally {
                    response.getEntity()
                        .getContent()
                        .close();
                }
            } else {
                throw new RuntimeException("Could not download the certificate from EventBridge:" + certUrl);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to download SNS certificate from " + certUrl.toString(), e);
        }
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
