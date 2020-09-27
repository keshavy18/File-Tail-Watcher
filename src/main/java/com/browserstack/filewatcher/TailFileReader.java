package com.browserstack.filewatcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

public class TailFileReader extends Thread{
	
	KafkaTemplate kafkaTemplate;
	
	private boolean reading = false;;
	private boolean status = false;
	
	private String fileName;

	private Integer lines;

	private String[] lastLines;
	
	private Boolean init;
	
	public Boolean getInitalize() {
		return init;
	}


	public void setInitalize(Boolean initalize) {
		this.init = initalize;
	}


	@Value("file.modify.topic")
	private String modifyFileTopic;
	
	public TailFileReader(String fileName,Integer lines,KafkaTemplate kafkaTemplate) {
		this.fileName = fileName;
		this.lines = lines;
		this.lastLines = new String[lines];
		this.kafkaTemplate=kafkaTemplate;
	}
	
	
	public void setLines(String newLine) {

		String temp = lastLines[this.lines-1];
		for(int i=this.lines-1;i>=0;i--) {
			String temp2 = this.lastLines[i];
			this.lastLines[i]=temp;
			temp=temp2;
		}
		this.lastLines[4]=newLine;
	}
	
	
	public void run()  {

		File file = new File(this.fileName);
		long fileTime = file.lastModified();
		long fileLength = 0;
		long lastLine = 0;
		while (true) {

			if (fileTime != file.lastModified() || init) {

				RandomAccessFile filePointer;
				try {
					filePointer = new RandomAccessFile(fileName, "r");
					if (file.length() < fileLength)
						lastLine = 0;

					try {
						filePointer.seek(lastLine);
						while (true) {
							String str = filePointer.readLine();
							if (str == null)
								break;
							setLines(str);
							status = true;
							lastLine++;
						}
						
						fileLength = file.length();
						fileTime = file.lastModified();
						this.kafkaTemplate.send("update", Arrays.toString(this.lastLines));
						init = false;
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Out of Range");
					}
					
				} catch (FileNotFoundException e1) {
					System.out.println("Exception");
				}
			}else {
				this.status=false;
			}
		}

	}

	public boolean isReading() {
		return reading;
	}

	public void setReading(boolean reading) {
		this.reading = reading;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getLines() {
		return lines;
	}

	public void setLines(Integer lines) {
		this.lines = lines;
	}

	public String[] getLastLines() {
		return lastLines;
	}

	public void setLastLines(String[] lastLines) {
		this.lastLines = lastLines;
	}


	public boolean isStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}

	
}
