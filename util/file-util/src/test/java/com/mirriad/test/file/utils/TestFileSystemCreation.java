package com.mirriad.test.file.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import com.openshare.file.utils.FileSystemManagerImpl;

public class TestFileSystemCreation {

	@Test
	public void test() {
		//try to instantiate
		try{
			FileSystemManagerImpl manager = FileSystemManagerImpl.getInstance();
		}
		catch(Exception e){
			fail("instantiation failed:"+e.getMessage());
		}
	}

}
