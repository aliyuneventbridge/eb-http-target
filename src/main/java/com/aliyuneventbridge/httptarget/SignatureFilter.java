//package com.aliyuneventbridge.httptarget;
//
//import com.amazonaws.services.sns.message.*;
//import org.apache.logging.log4j.util.Strings;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@Order(1)
//public class SignatureFilter implements Filter {
//
//    private SnsMessageManager snsMessageManager = new SnsMessageManager("us-east-2");
//
//    @Override
//    public void doFilter(
//        ServletRequest ServletRequest,
//        ServletResponse ServletResponse,
//        FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) ServletRequest;
//        try {
//            if (!Strings.isBlank(request.getHeader("x-amz-sns-message-type"))) {
//                SnsMessage snsMessage = snsMessageManager.parseMessage(request.getInputStream());
//                Map<String, Object> messageModel = new HashMap<>();
//                messageModel.put("messageId,", snsMessage.getMessageId());
//                if (snsMessage instanceof SnsNotification) {
//                    messageModel.put("message", ((SnsNotification) snsMessage).getMessage());
//                    messageModel.put("subject", ((SnsNotification) snsMessage).getSubject());
//                } else if (snsMessage instanceof SnsSubscriptionConfirmation) {
//                    messageModel.put("subscribeUrl", ((SnsSubscriptionConfirmation) snsMessage).getSubscribeUrl());
//                } else if (snsMessage instanceof SnsUnsubscribeConfirmation) {
//                    messageModel.put("unSubscribeUrl", ((SnsUnsubscribeConfirmation) snsMessage).getSubscribeUrl());
//                }
//                HttpTargetApplication.requestLists.add(Collections.singletonMap("SnsMessage", messageModel));
//            }
//        } catch (Throwable e) {
//            HttpTargetApplication.requestLists.add(Collections.singletonMap("Throwable", e.fillInStackTrace()));
//        }
//        chain.doFilter(ServletRequest, ServletResponse);
//
//    }
//
//}