package com.browserstack.filewatcher;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TailFileReaderTest {
	
	public static void main(String[] args) throws IOException {
		
		File file = new File("File.txt");
		RandomAccessFile randomAccessFile = new RandomAccessFile(file,"r");
		System.out.println("File Length "+file.length());
		
		long k = file.length()-1;
		String str = "";
		while(k>=0) {
			randomAccessFile.seek(k);
			char ch = (char)randomAccessFile.read();
			if(ch=='\n') 
				break;
			str=ch+str;
			k--;
		}
		System.out.print(str);
	}
	
}
