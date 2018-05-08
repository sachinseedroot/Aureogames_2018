package com.globocom.aureogames_2018.Utilities;

public class Constants {

    public static final String getHEUrl ="http://wap.globocom.co.in/du_header.jsp";

    public static final String invalidMobileUrl ="http://wap.globocom.co.in/mglobopay/invalidMobileApi?msisdn=";

    public static final String sentDUOTP ="http://wap.globocom.co.in/mglobopay/duPinGenerateApi?partner=Globocom&msisdn=";

    public static final String verifyDUOTP= "http://wap.globocom.co.in/mglobopay/duPinValidateApi?partner=Globocom&msisdn=";

    public static final String sentEtislatOTP ="http://wap.globocom.co.in/mglobopay/etisalatPinGenerateApi?msisdn=";

    public static final String verifyEtislatOTP= "http://wap.globocom.co.in/mglobopay/etisalatPinValidateApi?msisdn=";

    public static String INSTALL_KPI ="http://wap.globocom.co.in/mglobopay/mobileAppKpi?kpi=INSTALL_KPI&partnerid=1001&msisdn=@msisdn";

    public static String OPEN_APP_KPI ="http://wap.globocom.co.in/mglobopay/mobileAppKpi?kpi=OPEN_APP_KPI&partnerid=1001&msisdn=@msisdn";

    public static String MOBILE_SUBMIT_KPI ="http://wap.globocom.co.in/mglobopay/mobileAppKpi?kpi=MOBILE_SUBMIT_KPI&partnerid=1001&msisdn=@msisdn";

    public static String PIN_READ_KPI ="http://wap.globocom.co.in/mglobopay/mobileAppKpi?kpi=PIN_READ_KPI&partnerid=1001&msisdn=@msisdn&msg=@msg";

    public static String PIN_READ_AND_SUBMIT_KPI ="http://wap.globocom.co.in/mglobopay/mobileAppKpi?kpi=PIN_READ_AND_SUBMIT_KPI&partnerid=1001&msisdn=@msisdn&msg=@msg";

    public static String VERIFY_PIN_SUBMIT_KPI ="http://wap.globocom.co.in/mglobopay/mobileAppKpi?kpi=VERIFY_PIN_SUBMIT_KPI&partnerid=1001&msisdn=@msisdn&msg=@msg";

    public static String PRODUCT_PAGE_KPI ="http://wap.globocom.co.in/mglobopay/mobileAppKpi?kpi=PRODUCT_PAGE_KPI&partnerid=1001&msisdn=@msisdn";

    public static String PIN_READ_INBOX_KPI ="http://wap.globocom.co.in/mglobopay/mobileAppKpi?kpi=PIN_READ_INBOX_KPI&partnerid=1001&msisdn=@msisdn&msg=@msg";

    public static String GAMEZINE_PAGE ="http://gamezine.in/gamezine/home";

    public static String GET_OPERATOR="http://wap.globocom.co.in/mglobopay/getHlrLookUpByMdn/@msisdn";

    public static String GET_SUBSCRIBER_STATUS="http://wap.globocom.co.in/mglobopay/getSubscriberStatus/@msisdn";

    public static String SEND_OTP_SMS="http://globobill.com/globobill/sendSmsToUser/@msisdn/@message";

    public static final String SMS_URL="http://sms.hspsms.com/sendSMS?username=GLOBOCOM&message=@message&sendername=GLOBCM&smstype=TRANS"
            + "&numbers=@msisdn&apikey=f705e776-6328-411d-ac07-cfed97f1875b";

}
