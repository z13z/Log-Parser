package com.azry.utils.logparser.services;

import com.azry.utils.logparser.dto.LogFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class FilesDirectoryControllerBean implements FilesDirectoryController {

	@Value("${defaultLogFilesDirectory:.}")
	private String defaultLogFilesDirectory;

	private static final String OUTPUT_FILE_PREFIX = "out_";

	@Override
	public List<LogFile> listFiles() {
		File currentDir = new File(defaultLogFilesDirectory);
		if (currentDir.exists()) {
			File[] files = currentDir.listFiles();
			if (files != null) {
				List<LogFile> resultList = new ArrayList<>();
				for (File file : files) {
					if (file.isFile() && !file.getName().startsWith(OUTPUT_FILE_PREFIX)) {
						LogFile logFile = new LogFile();
						logFile.setName(file.getName());
						logFile.setLastModifiedDate(new Date(file.lastModified()));
						resultList.add(logFile);
					}
				}
				return resultList;
			}
		}
		return Collections.emptyList();
	}

	@Override
	public File createOutputFile(String inputFileName) {
		File out = new File(defaultLogFilesDirectory + File.separator + OUTPUT_FILE_PREFIX + inputFileName);
		try {
			//noinspection ResultOfMethodCallIgnored
			out.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}

	@Override
	public File getLogFile(String logName) {
		return new File(defaultLogFilesDirectory + File.separator + logName);
	}
}
