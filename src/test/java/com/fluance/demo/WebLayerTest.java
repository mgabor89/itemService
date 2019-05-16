package com.fluance.demo;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(ItemController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class WebLayerTest {

	@Autowired
    private MockMvc mockMvc;

	@MockBean
	private ItemService itemService;
	
    @Test
    public void shouldReturnErrorForDefaultPath() throws Exception {
        this.mockMvc.perform(get("/"))
            .andExpect(status().isNotFound());
    }
    
    @Test
    public void findAllShouldReturnListOfItems() throws Exception {
        when(itemService.getAll()).thenReturn(Lists.newArrayList(
            new Item("Foo", "Dummy Item"),
            new Item("Bar", "Another Dummy Item")));
        mockMvc.perform(get("/item/getAll"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].name", is("Foo")))
            .andExpect(jsonPath("$.[0].description", is("Dummy Item")))
            .andExpect(jsonPath("$.[1].name", is("Bar")))
            .andExpect(jsonPath("$.[1].description", is("Another Dummy Item")))
            // For documentation
            .andDo(document("item-getAll-ok"))
            .andDo(document("{method-name}", 
            	        responseFields(
            	            fieldWithPath("[].id").description("The unique identifier of the item"),
            	            fieldWithPath("[].name").description("The name of the item"),
            	            fieldWithPath("[].description").description("The description of the item"))));
            ;
        verify(itemService).getAll();
    }
    
    @Test 
    public void findByIdShouldReturnAnItem() throws Exception {
    	 Long itemId = 123L;
		when(itemService.findById(itemId)).thenReturn(new Item("Foo", "Dummy Item"));
    	        mockMvc.perform(get("/item/" + itemId))
    	            .andExpect(status().isOk())
    	            .andExpect(jsonPath("$.name", is("Foo")))
    	            .andExpect(jsonPath("$.description", is("Dummy Item")))
    	            // For documentation
    	            .andDo(document("item-getById-ok"))
    	            .andDo(document("{method-name}", 
    	            	        responseFields(
    	            	            fieldWithPath("id").description("The unique identifier of the item"),
    	            	            fieldWithPath("name").description("The name of the item"),
    	            	            fieldWithPath("description").description("The description of the item"))));
    	            ;
    	        verify(itemService).findById(itemId);
    }
}
