package com.modulink.Model.News;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NewsService {
    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<NewsEntity> findAll() {
        return newsRepository.findAll();
    }

    @Transactional
    public void save(NewsEntity news) {
        newsRepository.save(news);
    }

    @Transactional
    public void delete(int id) {
        newsRepository.deleteById(id);
    }
}
