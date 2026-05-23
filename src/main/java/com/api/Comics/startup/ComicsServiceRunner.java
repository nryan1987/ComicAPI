package com.api.Comics.startup;

import com.api.Comics.helper.CacheHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ComicsServiceRunner implements ApplicationRunner {
    private final CacheHelper cacheHelper;

    @Autowired
    public ComicsServiceRunner(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
    }


    @Override
    public void run(ApplicationArguments args) {
        log.info("Application started");
        cacheHelper.loadComicsPageCache();
    }
}
