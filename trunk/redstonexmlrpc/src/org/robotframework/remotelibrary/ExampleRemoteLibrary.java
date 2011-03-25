package org.robotframework.remotelibrary;

import java.io.*;

/**
 * This is an example remote library
 * for demonstration with the Java
 * generic remote server for use with
 * Robot Framework, per the RF spec
 * 
 * @author David Luu
 *
 */
public class ExampleRemoteLibrary {
	
	/**
	 * Get the number of files & directories in the given directory path
	 * @param path The directory path to get number of files & directories for.
	 * @return The number of files & directories in the given directory path.
	 */
	public static int count_items_in_directory(String path){
		File dir = new File(path);
		String[] dirList = dir.list();
		if(dirList != null)
			return dirList.length;
		else
			return 0;
	}
	
	/**
	 * Compares two strings for equality. Throws exception if they are not equal.
	 * Otherwise does not return anything, which assumes equality/success.
	 * @param str1 First string to compare
	 * @param str2 Second string to compare
	 * @throws Exception indicating string comparison failed, they are not equal. 
	 */
	public static void strings_should_be_equal(String str1, String str2) throws Exception{
		System.out.printf("Comparing '%s' to '%s'", str1, str2);
		if(!str1.equals(str2)){
			System.out.println("Somehow when executed by remote server as remote library,");
			System.out.println("we get null pointer exception here instead of what's below.");
			//or is my knowledge of Java incomplete and the string comparison is incorrect?
			Exception ex = new Exception("Given strings are not equal");
			throw ex;
		}
	}
}
