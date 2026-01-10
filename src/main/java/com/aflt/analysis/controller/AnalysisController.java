package com.aflt.analysis.controller;

import com.aflt.analysis.dto.AnalysisResult;
import com.aflt.analysis.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/analysis")
public class AnalysisController {
    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping("/analyze-parts")
    public String analyzeParts(@RequestParam("partNumbers") String partNumbersInput,
                               Model model) {
        List<String> partNumbers = Arrays.stream(partNumbersInput.split("\\r?\\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        List<AnalysisResult> results = analysisService.analyzeByPartNumbers(partNumbers);

        model.addAttribute("results", results);
        model.addAttribute("analysisType", "Part Numbers Analysis");
        model.addAttribute("inputData", partNumbersInput);

        return "analysis-results";
    }

    @PostMapping("/analyze-list")
    public String analyzeList(@RequestParam("listName") String listName,
                              Model model) {
        List<AnalysisResult> results = analysisService.analyzeByList(listName);

        model.addAttribute("results", results);
        model.addAttribute("analysisType", listName + " List Analysis");
        model.addAttribute("listName", listName);

        return "analysis-results";
    }
}
