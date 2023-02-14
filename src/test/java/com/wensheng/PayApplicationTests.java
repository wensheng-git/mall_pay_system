package com.wensheng;

import com.wensheng.config.WxParamConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PayApplicationTests {
    @Autowired
    WxParamConfig wxParamConfig;
    @Test
    void contextLoads() {
        System.out.println(wxParamConfig);
    }

}
