package com.modulink.Model.SupportForm;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupportFormService {
    private final SupportFormRepository supportFormRepository;

    public SupportFormService(SupportFormRepository supportFormRepository) {
        this.supportFormRepository = supportFormRepository;
    }

    public void save(SupportFormEntity supportFormEntity) {
        supportFormRepository.save(supportFormEntity);
    }

    public void delete(int id) {
        supportFormRepository.deleteById(id);
    }

    public List<SupportFormEntity> findAll() {
        return supportFormRepository.findAll();
    }
}
