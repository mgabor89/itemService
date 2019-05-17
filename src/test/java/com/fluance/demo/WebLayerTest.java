package com.fluance.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ItemController.class)
@AutoConfigureRestDocs("target/generated-snippets")
public class WebLayerTest {
    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldGetErrorForNotFoundItem() throws Exception {
        // given
        long nonExistingItemId = 123L;
        when(itemService.findById(nonExistingItemId)).thenThrow(ItemNotFoundException.class);
        // when
        mockMvc.perform(get("/item/{id}", nonExistingItemId))
            // then
            .andExpect(status().isNotFound())
            // document
            .andDo(
                document("errorOnFindById",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("id").description("Item Identifier")
                    )
            ));
    }

    @Test
    public void shouldFindItemForExistingId() throws Exception {
        // given
        Long existingItemId = 123L;
        Item item = new Item("foo", "dummy item");
        item.setId(existingItemId);
        when(itemService.findById(existingItemId)).thenReturn(item);
        // when
        mockMvc.perform(get("/item/{id}", existingItemId))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("id", is(existingItemId.intValue())))
            .andExpect(jsonPath("name", is("foo")))
            .andExpect(jsonPath("description", is("dummy item")))
            // document
            .andDo(
                document("findById",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("id").description("Item Identifier. This must be a positive long value.")
                    ),
                    responseFields(
                        fieldWithPath("id").description("Item Identifier"),
                        fieldWithPath("name").description("Item Name"),
                        fieldWithPath("description").description("Item Description")
                    )
                ));
    }

    @Test
    public void shouldFindAllItems() throws Exception {
        // given
        List<Item> items = Arrays.asList(
            new Item(123L, "foo", "dummy item"),
            new Item(456L, "bar", "another dummy item")
        );
        when(itemService.getAll()).thenReturn(items);
        // when
        mockMvc.perform(get("/item/getAll"))
            // then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.[0].id", is(new Long(123L).intValue())))
            .andExpect(jsonPath("$.[0].name", is("foo")))
            .andExpect(jsonPath("$.[0]description", is("dummy item"))).
            andExpect(jsonPath("$.[1].id", is(new Long(456L).intValue()))).
            andExpect(jsonPath("$.[1].name", is("bar")))
            .andExpect(jsonPath("$.[1]description", is("another dummy item")))
            // document
            .andDo(
                document("getAll",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("[].id").description("Item Identifier"),
                        fieldWithPath("[].name").description("Item Name"),
                        fieldWithPath("[].description").description("Item Description")
                    )
                ));
    }

    @Test
    public void shouldGetErrorOnStoreWhenMaximumLimitReached() throws Exception {
        // given
        Item newItem = new Item("newItem", "Item to store...");
        when(itemService.store(newItem)).thenThrow(TooManyItemsException.class);
        // when
        mockMvc.perform(post("/item/store"))
            // then
            .andExpect(status().isBadRequest())
            // document
            .andDo(
                document("errorStoreMaximumReached",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())
                ));
    }

    @Test
    public void shouldStoreItem() throws Exception {
        // given
        Item newItem = new Item("newItem", "Item to store...");
        final Long itemId = 456L;
        newItem.setId(itemId);
        // when
        when(itemService.store(newItem)).thenReturn(newItem);
        mockMvc.perform(post("/item/store")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(new ObjectMapper().writeValueAsString(newItem)))
            //then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("id", is(itemId.intValue())))
            .andExpect(jsonPath("name", is("newItem")))
            .andExpect(jsonPath("description", is("Item to store...")))
            // document
            .andDo(
                document("storeItem",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("id").description("Item Identifier"),
                        fieldWithPath("name").description("Item Name"),
                        fieldWithPath("description").description("Item Description")
                    )
                ));
    }

    @Test
    public void shouldGetErrorOnStoringInvalidItem() throws Exception {
        // given
        Item newItem = new Item(null, "Item to store...");
        when(itemService.store(newItem)).thenThrow(TooManyItemsException.class);
        // when
        mockMvc.perform(post("/item/store"))
            // then
            .andExpect(status().isBadRequest())
            // document
            .andDo(
                document("errorStoreInvalidItem",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())
                ));
    }
}
