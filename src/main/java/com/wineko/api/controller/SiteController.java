package com.wineko.api.controller;
import com.wineko.api.model.Article;
import com.wineko.api.service.ArticleService;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class SiteController {

    @GetMapping("/")
    public String respond(){
        return "Welcome on my project backend!! :)";
    }


}
