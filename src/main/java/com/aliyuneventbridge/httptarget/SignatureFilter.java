package com.aliyuneventbridge.httptarget;

import com.amazonaws.services.sns.message.SnsMessage;
import com.amazonaws.services.sns.message.SnsMessageManager;
import com.google.gson.Gson;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;

@Component
@Order(1)
public class SignatureFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(SignatureFilter.class);

    SnsMessageManager snsMessageManager = new SnsMessageManager("us-east-2");

    @Override
    public void doFilter(
            ServletRequest ServletRequest,
            ServletResponse ServletResponse,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) ServletRequest;
        if (!Strings.isBlank(request.getHeader("x-amz-sns-message-type"))) {
            SnsMessage snsMessage = snsMessageManager.parseMessage(request.getInputStream());
            HttpTargetApplication.requestLists.add(Collections.singletonMap("SnsMessage", snsMessage.getMessageId()));
        }
        chain.doFilter(ServletRequest, ServletResponse);

    }

}