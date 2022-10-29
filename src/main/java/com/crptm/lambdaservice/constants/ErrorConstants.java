package com.crptm.lambdaservice.constants;

public class ErrorConstants {
    public static final String SQS_MESSAGING_ERROR = "Error occur while sending message to SQS queue : %s, due to reason : %s.";
    public static final String INVALID_URI = "URI is null or empty or does not start with http.";
    public static final String INVALID_ENDPOINT = "Endpoint can not be null.";
    public static final String HTTP_REQ_FAILURE = "Exception occurred during making HTTP %s request : %s.";
    public static final String HTTP_RES_FAILURE = "HTTP response status is not successful. ResponseCode : %s, ResponseBody : %s.";
    public static final String HTTP_RES_PARSE_FAILURE = "Error in parsing HTTP response : %s.";
    public static final String SYNC_ACCOUNT_TIMEOUT = "Sync is not processed within time for user : %d and account : %s.";
    public static final String SYNCING_USER_ACCOUNT_FAILURE = "Sync user : %d account : %s with status : %s is failed due to : %s.";
    public static final String TRIGGER_SYNC_ACCOUNT_FAILURE = "Trigger sync user : %d account : %s with status : %s is failed due to : %s.";
    public static final String PROPERTY_INITIALIZE_FAILURE = "Fail to initialize property for environment : %s.";
    public final static String PAYMENT_METHOD_NOT_FOUND = "Invalid payment method %s or not mentioned in enum";
    public static final String ERROR_OPENING_HIBERNATE_SESSION = "Error opening session of mysql in hibernate";
}
