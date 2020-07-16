package com.azry.utils.logparser.services;

import com.azry.utils.logparser.dto.LogFile;

import java.io.File;
import java.util.List;

public interface FilesDirectoryController {

	List<LogFile> listFiles();

	File createOutputFile(String inputFileName);
}
