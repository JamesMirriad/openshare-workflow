package com.openshare.util.parse;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/**
 * Util class for parsing variables
 * @author james.mcilroy
 *
 */
public class VariableParser {

	/**
	 * parse a line for all variables within them
	 * @param line
	 * @return
	 */
	public static Set<String> parseExpression(String expression){
		//we need to find "variables" delimited by ${} for replacements and get them.
		String splits [] = expression.split("\\$\\{");
		//ok, now if we have any splits, we need to parse untill the end to find the variable name.
		Set<String> replaces = new HashSet<String>();
		boolean ignoreFirstSplit = true;
		if(expression.startsWith("${")){
			ignoreFirstSplit = false;
		}
		//go through the splits
		for(String split : splits){
			if(!ignoreFirstSplit){
				String [] variableNameSplit = split.split("\\}");
				if(variableNameSplit!=null && variableNameSplit.length>0){
					replaces.add(variableNameSplit[0]);
				}
			}
			//set to false from now on
			ignoreFirstSplit = false;
		}
		return replaces;
	}
	
	/**
	 * DO a replacement given a map. Objects in map will use the value of their 
	 * toString method to complete this
	 * @param expression
	 * @param replacementMap
	 * @return
	 */
	public static String replaceVariablesInExpression(String expression, Map<String,Object> replacementMap){
		if(expression==null || expression.isEmpty()){
			return expression;
		}
		//check map replacements
		if(expression!=null && !expression.isEmpty() && replacementMap!=null){
			for(Map.Entry<String, Object> entry : replacementMap.entrySet()){
				String replaceString = "${"+entry.getKey()+"}";
				String replacement = entry.getValue().toString();
				expression = expression.replace(replaceString, replacement);
			}
		}
		expression = doFixedReplacements(expression);
		return expression;
	}
	
	/**
	 * does fixed replacements only
	 * @param expression
	 * @return
	 */
	public static String doFixedReplacements(String expression){
		//do fixed variable expressions
		//setup the fixed values
		Date date = new Date();
		DateFormat dfDate = new SimpleDateFormat("yyyyMMdd");
		DateFormat dfTime = new SimpleDateFormat("HHmmss");
		DateFormat dfYear = new SimpleDateFormat("yyyy");
		DateFormat dfMonth = new SimpleDateFormat("mm");
		DateFormat dfDay = new SimpleDateFormat("dd");
		DateFormat dfHour = new SimpleDateFormat("HH");
		DateFormat dfMin = new SimpleDateFormat("mm");
		DateFormat dfSec = new SimpleDateFormat("ss");
		
		InetAddress addr;
		String host;
		String ip;
		try {
			addr = InetAddress.getLocalHost();
			host = addr.getHostName();
		} 
		catch (UnknownHostException e) {
			host = "unknown_host";
		}
		try {
			addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress();
		} 
		catch (UnknownHostException e) {
			ip = "unknown_ip";
		}
		//time replacement
		expression = expression.replace("#{date}", dfDate.format(date));
		expression = expression.replace("#{time}", dfTime.format(date));
		expression = expression.replace("#{year}", dfYear.format(date));
		expression = expression.replace("#{month}", dfMonth.format(date));
		expression = expression.replace("#{day}", dfDay.format(date));
		expression = expression.replace("#{hour}", dfHour.format(date));
		expression = expression.replace("#{minute}", dfMin.format(date));
		expression = expression.replace("#{second}", dfSec.format(date));
		//host replacement
		expression = expression.replace("#{host}", host);
		expression = expression.replace("#{ip}", ip);
		
		return expression;
	}
}
