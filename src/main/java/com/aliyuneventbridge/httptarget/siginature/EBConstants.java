package com.aliyuneventbridge.httptarget.siginature;

import java.nio.charset.Charset;

public class EBConstants {

    /**
     * CloudEvents attribute "datacontenttype" default value
     */
    public static final String DEFAULT_DATA_CONTENT_TYPE = "application/json;charset=utf-8";
    /**
     * CloudEvents attribute "specversion" default value
     */
    public static final String DEFAULT_SPEC_VERSION = "1.0";

    /**
     * CloudEvents extension attribute: aliyunpublishtime
     */
    public static final String EXTENSION_PUBLISHTIME = "aliyunpublishtime";
    /**
     * CloudEvents extension attribute: aliyuneventbus
     */
    public static final String EXTENSION_EVENTBUSNAME = "aliyuneventbusname";

    /**
     * Error code:ServiceNotEnabled
     */
    public static final String SERVICE_NOT_ENABLED_CODE = "ServiceNotEnabled";
    /**
     * Error Message:ServiceNotEnabled
     */
    public static final String SERVICE_NOT_ENABLED_MESSAGE
        = "The OwnerId that your Access Key Id associated to has not enabled.";

    /**
     * Default charset
     */
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");


    /**
     * the current timestamp, If the receiver exceeds 60 seconds, it is considered expired.
     */
    public static String HEADER_X_EVENTBRIDGE_SIGNATURE_TIMESTAMP = "x-eventbridge-signature-timestamp";
    /**
     * the algorithm of signature, default is HMAC-SHA1
     */
    public static String HEADER_X_EVENTBRIDGE_SIGNATURE_METHOD = "x-eventbridge-signature-method";
    /**
     * the version of signature
     */
    public static String HEADER_X_EVENTBRIDGE_SIGNATURE_VERSION = "x-eventbridge-signature-version";

    /**
     * the url of public pem
     */
    public static String HEADER_X_EVENTBRIDGE_SIGNATURE_URL = "x-eventbridge-signature-url";
    /**
     * it is used to  increase the security of  signatures
     */
    public static String HEADER_X_EVENTBRIDGE_SIGNATURE_TOKEN = "x-eventbridge-signature-token";
    /**
     * the temporary secret， which encrypted by EB with a private pem
     */
    public static String HEADER_X_EVENTBRIDGE_SIGNATURE_SECRET = "x-eventbridge-signature-secret";

    /**
     * the signature of eventbridge http push
     */
    public static String HEADER_X_EVENTBRIDGE_SIGNATURE = "x-eventbridge-signature";

    /**
     * the separator of stringToSign
     */
    public static String SEPARATOR_OF_STRING_TO_SIGN = "\n";

    /**
     * the algorithm of signature, default is HMAC-SHA1
     */
    public static String DEFAULT_SIGNATURE_METHOD = "HMAC-SHA1";

    /**
     * the version of signature
     */
    public static String DEFAULT_SIGNATURE_VERSION = "1.0";

    /**
     * the url of public pem
     */
    public static String DEFAULT_SIGNATURE_URL = "https://g.alicdn.com/eventbridge/certifacete/public.pem";

}
