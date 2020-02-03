package de.demmer.dennis.autopost.properties;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Log4j2
@Component
@Transactional
public abstract class TestContext {


    @PostConstruct
    public void init(){
//
    }



}
