package com.aliyuneventbridge.httptarget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.aliyuneventbridge.httptarget.siginature.SignatureVerify;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class SignatureFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(ServletRequest ServletRequest, ServletResponse ServletResponse, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)ServletRequest;
        //try {
        //    byte[] body = IOUtils.toByteArray((request.getReader()));
        //    boolean isLegal = SignatureVerify.verify(buildUrlWithQueryString(request), getHeaders(request), body);
        //    if (!isLegal) {
        //        System.out.println("Verify failed.");
        //    }else{
        //        System.out.println("Verify success.");
        //    }
        //    HttpTargetApplication.requestLists.add(Collections.singletonMap("SnsMessage", new String(body, "UTF-8")));
        //} catch (Throwable e) {
        //    System.out.println(e.fillInStackTrace());
        //    HttpTargetApplication.requestLists.add(Collections.singletonMap("Throwable", e.fillInStackTrace()));
        //}
        logger.error("eb access");
        System.out.println("eb access");
        try {
            chain.doFilter(ServletRequest, ServletResponse);
        } catch (Throwable e) {
            logger.error("SignatureFilter", e);
            System.out.println(e.fillInStackTrace());
        }
    }

    public String buildUrlWithQueryString(HttpServletRequest request) {
        if (Strings.isBlank(request.getQueryString())) {
            return request.getRequestURL()
                .toString();
        } else {
            return request.getRequestURL() + "?" + request.getQueryString();
        }
    }

    public List<Header> getHeaders(HttpServletRequest request) {
        List<Header> headerList = new ArrayList<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String)headerNames.nextElement();
            String value = request.getHeader(key);
            headerList.add(new BasicHeader(key, value));
        }
        return headerList;
    }
}