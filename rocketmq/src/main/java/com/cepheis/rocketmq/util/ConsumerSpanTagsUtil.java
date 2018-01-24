//package com.gale.rocketmq.util;
//
//import java.util.Map;
//
//public class ConsumerSpanTagsUtil {
//
//    public static final String HANDLER_KEY = "getMessageHandler";
//    public static final String HANDLER_DELIVERY_ERROR_VALUE = "delivery_error";
//    public static final String HANDLER_NOT_FOUND_VALUE = "not_found";
//
//    public static Map<String, String> tagDeliverErr(Map<String, String> extTags) {
//        if (extTags != null) {
//            extTags.put(HANDLER_KEY, HANDLER_DELIVERY_ERROR_VALUE);
//        }
//        return extTags;
//    }
//
//    public static Map<String, String> tagDeliverNotFound(Map<String, String> extTags) {
//        if (extTags != null) {
//            extTags.put(HANDLER_KEY, HANDLER_NOT_FOUND_VALUE);
//        }
//        return extTags;
//    }
//}
