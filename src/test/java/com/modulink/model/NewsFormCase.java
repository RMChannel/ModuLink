package com.modulink.model;

import com.modulink.Controller.AdminModules.News.NewsForm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class NewsFormCase {
    @Test
    public void TestGetterAndSetter()
    {
        LocalDate now = LocalDate.now();
        NewsForm newsForm = new NewsForm();
        newsForm.setTitolo("Testo");
        newsForm.setTesto("testo");
        newsForm.setData(now);
        assertEquals(newsForm.getTitolo(), "Testo");
        assertEquals(newsForm.getTesto(), "testo");
        assertEquals(newsForm.getData(), now);
    }

    @Test
    public void TestConstructor() {
        LocalDate now = LocalDate.now();
        NewsForm newsForm = new NewsForm("Titolo", "Contenuto", now);
        assertEquals("Titolo", newsForm.getTitolo());
        assertEquals("Contenuto", newsForm.getTesto());
        assertEquals(now, newsForm.getData());
    }
}
