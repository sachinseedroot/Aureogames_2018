package com.globocom.aureogames_2018.Utilities;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rk on 13-03-2018.
 */

public class TrackingConstants {



/*          1.	INSTALL KPI -- level 1 score 100
            2.	MOBILE SUBMIT KPI – level 2 score 200
            3.	PIN SEND KPI – level 3 score 300
            4.	PIN READ KPI – level 4 score 400
            5.	PIN VERIFY KPI – level 5 score 500
            6.	PRODUCT PAGE KPI – level 6 score 600
*/

    public static Map<Integer, Object> eventServiceMap = new HashMap<Integer, Object>();
    static {
        eventServiceMap.put(1, 100);
        eventServiceMap.put(2, 100);
        eventServiceMap.put(3, 100);
        eventServiceMap.put(4, 100);
        eventServiceMap.put(5, 100);
        eventServiceMap.put(6, 100);
        eventServiceMap.put(7, 100);
        eventServiceMap.put(8, 100);
        eventServiceMap.put(9, 100);
        eventServiceMap.put(10, 100);
        eventServiceMap.put(11, 100);
        eventServiceMap.put(12, 100);
    }

    public static Map<Integer, String> eventValueMap = new HashMap<Integer, String>();

    static {
        // TODO : INSTALL_KPI
        eventServiceMap.put(1, "INSTALL_KPI");
        // TODO : MOBILE_SUBMIT_KPI
        eventServiceMap.put(2, "MOBILE_SUBMIT_KPI");
        // TODO : MOBILE_SUBMIT_KPI
        eventServiceMap.put(13, "VALID_MOBILE_KPI");
        // TODO : INVALID_MOBILE_KPI
        eventServiceMap.put(8, "INVALID_MOBILE_KPI");
        // TODO : INVALID_HLR_MOBILE_KPI
        eventServiceMap.put(11, "INVALID_HLR_MOBILE_KPI");
        // TODO: VALID_HLR_MOBILE_KPI
        eventServiceMap.put(12, "VALID_HLR_MOBILE_KPI");
        // TODO: PIN_SEND_KPI
        eventServiceMap.put(3, "PIN_SEND_KPI");
        // TODO: PIN_READ_KPI
        eventServiceMap.put(4, "PIN_READ_KPI");
        // TODO: PIN_READ_AND_SUBMIT_KPI
        eventServiceMap.put(5, "PIN_READ_AND_SUBMIT_KPI");
        // TODO: PIN_VERIFY_KPI
        eventServiceMap.put(6, "PIN_VERIFY_KPI");
        // TODO: PRODUCT_PAGE_KPI
        eventServiceMap.put(7, "PRODUCT_PAGE_KPI");
        // TODO: UNINSTALL_KPI
        eventServiceMap.put(9, "UNINSTALL_KPI");
        // TODO: PRODUCT_REPEAT_KPI
        eventServiceMap.put(10, "PRODUCT_REPEAT_KPI");
    }
}
