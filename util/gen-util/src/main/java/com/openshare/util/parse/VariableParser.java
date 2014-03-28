package com.openshare.util.parse;

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
		for(Map.Entry<String, Object> entry : replacementMap.entrySet()){
			String replaceString = "${"+entry.getKey()+"}";
			String replacement = entry.getValue().toString();
			expression = expression.replace(replaceString, replacement);
		}
		return expression;
	}
}
