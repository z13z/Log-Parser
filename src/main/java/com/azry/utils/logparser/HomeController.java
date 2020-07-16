package com.azry.utils.logparser;

import com.azry.utils.logparser.dto.request.RegexParseRequest;
import com.azry.utils.logparser.parsers.regex.RegexParser;
import com.azry.utils.logparser.services.FilesDirectoryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    private static final String AJAX_HEADER_NAME = "X-Requested-With";

    private static final String AJAX_HEADER_VALUE = "XMLHttpRequest";

    private final RegexParser regexParser;

    private FilesDirectoryController directoryController;

    @Autowired
    public HomeController(RegexParser regexParser, FilesDirectoryController directoryController) {
        this.regexParser = regexParser;
        this.directoryController = directoryController;
    }

    @GetMapping("/list")
    public String sendForm(Model model) {
        model.addAttribute("request", new RegexParseRequest());
        model.addAttribute("files", directoryController.listFiles());
        return "listFiles";
    }

    @GetMapping("/result")
    public String result(Model model) {
        return "parseResult";
    }

    @PostMapping(path = "/parse")
    public String processForm(RegexParseRequest request) {
        regexParser.parse(request);
        return "parseResult";
    }

    @PostMapping(params = "addExcludeRegex", path = "/list")
    public String addExcludeRegexField(RegexParseRequest parseRequest, HttpServletRequest request) {
        parseRequest.getExcludeRegexList().add("");
        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME))) {
            // It is an Ajax request, render only #items fragment of the page.
            return "request::#excludeRegexList";
        } else {
            // It is a standard HTTP request, render whole page.
            return "request";
        }
    }

    // "removeItem" parameter contains index of a item that will be removed.
    @PostMapping(params = "removeExcludeRegex", path = "/list")
    public String removeOrder(RegexParseRequest parseRequest, @RequestParam("removeItem") int index, HttpServletRequest request) {
        parseRequest.getExcludeRegexList().remove(index);
        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME))) {
            return "request::#excludeRegexList";
        } else {
            return "request";
        }
    }
}
