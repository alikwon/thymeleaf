package org.alikwon.thymeleaf.controller;

import lombok.extern.log4j.Log4j2;
import org.alikwon.thymeleaf.dto.SampleDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/loop")
    public void loop(Model model) {
        List<SampleDTO> list = new ArrayList<>();

        for (long i = 1; i < 21; i++) {
            SampleDTO dto = SampleDTO.builder()
                    .sno(i)
                    .first("First - " + i)
                    .last("Lat - " + i)
                    .regTime(LocalDateTime.now())
                    .build();
            list.add(dto);
        }
        model.addAttribute("list", list);
    }
}
