package com.company.sample.service;


import java.math.BigDecimal;
import java.util.Map;

public interface DataProcessingService {
    String NAME = "sample_DataProcessingService";

    Map<String, BigDecimal> process1();

    Map<String, BigDecimal> process2();
}