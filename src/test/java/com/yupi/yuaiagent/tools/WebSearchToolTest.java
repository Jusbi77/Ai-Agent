package com.yupi.yuaiagent.tools;

import cn.hutool.json.JSONArray;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebSearchToolTest {

    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Test
    void searchWeb() {
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        String query = "Justin";
        String result = webSearchTool.searchWeb(query);
        System.out.println( result);
        Assertions.assertNotNull(result);
    }
}
