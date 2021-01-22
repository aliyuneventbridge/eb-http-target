package com.aliyuneventbridge.httptarget.siginature;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

public class StringToSignBuilder {

    /**
     * Build the string to be signed.
     *
     * @param urlWithQueryString
     * @param headerMap
     * @param body
     *
     * @return
     *
     * @see <a href="https://yuque.antfin-inc.com/aone709911/lb5o1x/pklssa">HTTP/S 事件签名</a>
     */

    public static String defaultStringToSign(String urlWithQueryString, Map<String, String> headerMap, byte[] body) {
        return buildStringToSign(urlWithQueryString, officalHeaders(headerMap), body);
    }

    /**
     * Build office headers
     *
     * @return
     */
    public static List<Header> officalHeaders(Map<String, String> headerMap) {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader(EBConstants.HEADER_X_EVENTBRIDGE_SIGNATURE_TIMESTAMP,
            headerMap.get(EBConstants.HEADER_X_EVENTBRIDGE_SIGNATURE_TIMESTAMP)));
        headers.add(new BasicHeader(EBConstants.HEADER_X_EVENTBRIDGE_SIGNATURE_METHOD,
            headerMap.get(EBConstants.HEADER_X_EVENTBRIDGE_SIGNATURE_METHOD)));
        headers.add(new BasicHeader(EBConstants.HEADER_X_EVENTBRIDGE_SIGNATURE_VERSION,
            headerMap.get(EBConstants.HEADER_X_EVENTBRIDGE_SIGNATURE_VERSION)));
        headers.add(new BasicHeader(EBConstants.HEADER_X_EVENTBRIDGE_SIGNATURE_URL,
            headerMap.get(EBConstants.HEADER_X_EVENTBRIDGE_SIGNATURE_URL)));
        headers.add(new BasicHeader(EBConstants.HEADER_X_EVENTBRIDGE_SIGNATURE_TOKEN,
            headerMap.get(EBConstants.HEADER_X_EVENTBRIDGE_SIGNATURE_TOKEN)));
        return headers;
    }

    /**
     * Build the string to be signed.
     *
     * @param url
     * @param headers
     * @param body
     *
     * @return
     */
    private static String buildStringToSign(String url, List<Header> headers, byte[] body) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(url)
            .append(EBConstants.SEPARATOR_OF_STRING_TO_SIGN);
        if (headers != null && !headers.isEmpty()) {
            for (Header header : headers) {
                buffer.append(header.toString())
                    .append(EBConstants.SEPARATOR_OF_STRING_TO_SIGN);
            }
        }
        buffer.append(new String(body, Charset.forName("UTF-8")))
            .append(EBConstants.SEPARATOR_OF_STRING_TO_SIGN);
        return buffer.toString();
    }

}
