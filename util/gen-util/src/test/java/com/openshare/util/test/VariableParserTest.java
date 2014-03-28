package com.openshare.util.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.openshare.util.parse.VariableParser;
/**
 * testing variable parser
 * @author james.mcilroy
 *
 */
public class VariableParserTest {

	@Test
	public void test() {
		String lineToParse = "this line contains ${variable-a} and ${variable-b} but also ${variable-c}";
		Set<String> variables = VariableParser.parseExpression(lineToParse);
		Assert.assertTrue(variables.size() == 3);
		Assert.assertTrue(variables.contains("variable-a"));
		Assert.assertTrue(variables.contains("variable-b"));
		Assert.assertTrue(variables.contains("variable-c"));
	}
	
	@Test
	public void test2() {
		String lineToParse = "this line contains {variable-a} and {variable-b} but also {variable-c} but none of them are variables to be parsed";
		Set<String> variables = VariableParser.parseExpression(lineToParse);
		Assert.assertTrue(variables.size() == 0);
		Assert.assertFalse(variables.contains("variable-a"));
		Assert.assertFalse(variables.contains("variable-b"));
		Assert.assertFalse(variables.contains("variable-c"));
	}
	
	@Test
	public void test3() {
		String lineToParse = "this line contains ${variable-a} and {variable-b} but also ${variable-c} but none of them are variables to be parsed";
		Set<String> variables = VariableParser.parseExpression(lineToParse);
		Assert.assertTrue(variables.size() == 2);
		Assert.assertTrue(variables.contains("variable-a"));
		Assert.assertFalse(variables.contains("variable-b"));
		Assert.assertTrue(variables.contains("variable-c"));
	}

	@Test
	public void test4() {
		String lineToParse = "this line contains ${variable-a} and ${variable-b} but also ${variable-c}";
		Map<String,Object> vars = new HashMap<String,Object>();
		vars.put("variable-a", "cat");
		vars.put("variable-b", "dog");
		vars.put("variable-c", "rat");
		String result = VariableParser.replaceVariablesInExpression(lineToParse, vars);
		Assert.assertTrue("this line contains cat and dog but also rat".equals(result));
	}
	
}
