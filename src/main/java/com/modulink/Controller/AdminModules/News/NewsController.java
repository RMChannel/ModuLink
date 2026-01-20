package com.modulink.Controller.AdminModules.News;

import com.modulink.Alert;
import com.modulink.Controller.ModuloController;
import com.modulink.Model.Modulo.ModuloService;
import com.modulink.Model.News.NewsEntity;
import com.modulink.Model.News.NewsService;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class NewsController extends ModuloController {
    private final NewsService newsService;
    private final CustomUserDetailsService customUserDetailsService;

    public NewsController(ModuloService moduloService, NewsService newsService, CustomUserDetailsService customUserDetailsService) {
        super(moduloService, -3);
        this.newsService = newsService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/news")
    public String news(Model model, Principal principal) {
        model.addAttribute("news",newsService.findAll());
        return "homepage/news";
    }

    @GetMapping({"/dashboard/news","/dashboard/news/"})
    public String getNews(Model model, Principal principal) {
        Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            model.addAttribute("news",newsService.findAll());
            model.addAttribute("newsForm", new NewsForm());
            return "admin/news/newsPage";
        }
        else return "redirect:/";
    }

    @PostMapping({"/dashboard/news","/dashboard/news/"})
    public String addNews(Model model, Principal principal, @Valid @ModelAttribute("newsForm") NewsForm newsForm, BindingResult bindingResult) {
        Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            if(newsForm.getData().isBefore(LocalDate.now().minusDays(1))) bindingResult.rejectValue("data","datanelpassato.error","La data inserita è nel passato");
            if(bindingResult.hasErrors()) {
                model.addAttribute("news", newsService.findAll());
                return "admin/news/newsPage";
            }
            NewsEntity news=new NewsEntity(newsForm.getTitolo(),newsForm.getTesto(),newsForm.getData());
            newsService.save(news);
            return "redirect:/dashboard/news"+ Alert.success("News aggiunta con successo");
        }
        else return "redirect:/";
    }

    @PostMapping({"/dashboard/remove-news","/dashboard/remove-news/"})
    public String removeNews(Principal principal, Model model, @RequestParam int idNews) {
        Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(principal.getName());
        if(isAccessibleModulo(utenteOpt)) {
            newsService.delete(idNews);
            return "redirect:/dashboard/news"+ Alert.success("News cancellata con successo");
        }
        else return "redirect:/";
    }
}
