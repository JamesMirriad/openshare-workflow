package com.mirriad.util.convert;

import org.apache.log4j.Logger;


public class Convert {

	private static final Logger logger = Logger.getLogger(Convert.class);
	
	/**
	 * 
	 * @param integer
	 * @return
	 */
	public static Integer convertToInteger(String integer) {
        try {
            return Integer.parseInt(integer);
        } catch (NumberFormatException e) {
        	logger.error("Could not parse string",e);
            return 0;
        }
    }
	
	/**
	 * 
	 * @param doubleString
	 * @return
	 */
	public static double convertToDouble(String doubleString) {
        try {
            return Double.parseDouble(doubleString);
        } catch (NumberFormatException e) {
        	logger.error("Could not parse string",e);
           	return 0;
        }
    }
    
    
}

