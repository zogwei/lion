package com.alacoder.lion.tcc.spring;

import java.util.Properties;

public interface TccService {

    void start(String businessType, String businessId, Properties properties);
}
