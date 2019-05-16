package com.fluance.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = StandaloneDataConfiguration.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
@ActiveProfiles("dev")
public class ItemControllerIT {
    @LocalServerPort
    private int localPort;

    @Test
    @Sql("/createItems.sql")
    public void testExistingItemById() {
        // given
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        // when
        ResponseEntity<Item> result = testRestTemplate.getForEntity("http://localhost:" + localPort + "/item/1", Item.class);
        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        final Item item = result.getBody();
        assertNotNull("There's one item with the given ID.");
        assertEquals("Item1", item.getName());
        assertEquals(1L, item.getId().longValue());
    }

    @Test
    public void testNotExistingPersonByIdShouldReturn404() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        long itemId = 42L;
        
        ResponseEntity<String> result = testRestTemplate.getForEntity("http://localhost:" + localPort + "/item/" + itemId, String.class);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        ;
		assertTrue(result.getBody().contains(new ItemNotFoundException(itemId).getMessage()));
    }
}
