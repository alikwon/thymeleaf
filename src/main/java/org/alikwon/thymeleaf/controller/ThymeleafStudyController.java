package org.alikwon.thymeleaf.controller;

import lombok.extern.log4j.Log4j2;
import org.alikwon.thymeleaf.dto.SampleDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/thymeleaf")
@Log4j2
public class ThymeleafStudyController {

    @GetMapping("/ex1")
    public void ex1() {
        log.info("ex1...........");
    }

    private List<SampleDTO> makeDummies() {
        List<SampleDTO> list = new ArrayList<>();

        for (long i = 1; i < 11; i++) {
            SampleDTO dto = SampleDTO.builder()
                    .sno(i)
                    .first("First - " + i)
                    .last("Lat - " + i)
                    .regTime(LocalDateTime.now())
                    .build();
            list.add(dto);
        }
        return list;
    }

    @GetMapping({"/loop", "/control", "/link"})
    public void loop(Model model) {
        model.addAttribute("list", makeDummies());
    }

    /**
     * 생성된 dto의 데이터를 심어서 전달
     */
    @GetMapping("/inline")
    public String inline(RedirectAttributes redirectAttributes) {
        log.info("inlineTest..................");
        long sno = 100L;
        SampleDTO dto = SampleDTO.builder()
                .sno(sno)
                .first("First - " + sno)
                .last("Lat - " + sno)
                .regTime(LocalDateTime.now())
                .build();
        redirectAttributes.addFlashAttribute("result", "success");
        redirectAttributes.addFlashAttribute("dto", dto);
        return "redirect:/thymeleaf/inlineView";
    }

    @GetMapping("/inlineView")
    public void inlineView() {
        log.info("inline view ...........");
    }

    @GetMapping({"/layout", "/layoutParam", "/template","/sidebar"})
    public void layout(Model model) {
        log.info("layout................");
        long sno = 100L;
        SampleDTO dto = SampleDTO.builder()
                .sno(sno)
                .first("First - " + sno)
                .last("Lat - " + sno)
                .regTime(LocalDateTime.now())
                .build();
        model.addAttribute("test",dto);
    }
}
