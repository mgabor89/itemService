package com.fluance.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = StandaloneDataConfiguration.class)
@ActiveProfiles("dev")
public class GetAllItemsIntegrationTest {
    @LocalServerPort
    private int localPort;

    @Test
    @Sql("/createItems.sql")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void testGetAllItems() {

    	TestRestTemplate testRestTemplate = new TestRestTemplate();
    	
        ResponseEntity<Item[]> result = testRestTemplate.getForEntity("http://localhost:" + localPort + "/item/getAll", Item[].class);

        List<Item> resultList = Arrays.asList(result.getBody());

        assertEquals(4, resultList.size());
        assertTrue(resultList.stream().map(p -> p.getName()).collect(Collectors.toList()).containsAll(
        		Arrays.asList("Item1", "Item2", "Item3", "Item4")));
    }
}
