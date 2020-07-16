package com.azry.utils.logparser.dto.request;

import java.util.ArrayList;
import java.util.List;

public class RegexParseRequest {

	private String fileName;

	private String regex;

	private List<String> excludeRegexList = new ArrayList<String>(){
		{
			add("");
		}
	};

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public List<String> getExcludeRegexList() {
		return excludeRegexList;
	}

	public void setExcludeRegexList(List<String> excludeRegexList) {
		this.excludeRegexList = excludeRegexList;
	}
}
