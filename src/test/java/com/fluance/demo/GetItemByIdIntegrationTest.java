package com.fluance.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = StandaloneDataConfiguration.class)
@ActiveProfiles("dev")
public class GetItemByIdIntegrationTest  {
    @LocalServerPort
    private int localPort;

    @Test
    @Sql("/createItems.sql")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void testExistingItemById() {
        // given
    	final long existingItemId = 1L;
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        // when
        ResponseEntity<Item> result = testRestTemplate.getForEntity("http://localhost:" + localPort + "/item/" + existingItemId, Item.class);
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        final Item item = result.getBody();
        assertThat(item).isNotNull();
        assertThat(item.getName()).isNotNull().isEqualTo("Item1");
        assertThat(item.getId()).isNotNull().isEqualTo(existingItemId);
    }

    @Test
    public void testNotExistingPersonByIdShouldReturn404() {
        // given
    	final long nonExistingItemId = 42L;
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        // when
        ResponseEntity<String> result = testRestTemplate.getForEntity("http://localhost:" + localPort + "/item/" + nonExistingItemId, String.class);
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).contains(new ItemNotFoundException(nonExistingItemId).getMessage());
    }
}
