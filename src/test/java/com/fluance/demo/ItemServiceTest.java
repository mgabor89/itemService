package com.fluance.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@ContextConfiguration
@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTest {
    private static final long MORE_THAN_MAXIMUM_ALLOWED = 10L;
    private static final int MAXIMUM_ALLOWED_NR_OF_ITEMS = 3;

    @Mock
    private ItemApplicationConfiguration serviceConfig;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test(expected = TooManyItemsException.class)
    public void shouldNotStoreMoreThanMaximumAllowed() {
        // given
        Item newItem = new Item("myItem", "dummy item");
        // when
        when(itemRepository.count()).thenReturn(MORE_THAN_MAXIMUM_ALLOWED);
        when(serviceConfig.getMaximumNumber()).thenReturn(MAXIMUM_ALLOWED_NR_OF_ITEMS);
        itemService.store(newItem);
        // then
        fail("You should not see this messages: we are expecting an exception.");
    }

    @Test
    public void shouldStoreItemWhenLimitNotReached() {
        // given
        Item newItem = new Item("myItem", "dummy item");
        // when
        when(itemRepository.save(newItem)).thenReturn(newItem);
        when(itemRepository.count()).thenReturn(MORE_THAN_MAXIMUM_ALLOWED);
        when(serviceConfig.getMaximumNumber()).thenReturn((int)MORE_THAN_MAXIMUM_ALLOWED + 1);
        Item savedItem = itemService.store(newItem);
        // then
        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getName()).isNotNull().isEqualTo(newItem.getName());
        assertThat(savedItem.getDescription()).isEqualTo(newItem.getDescription());
    }

    @Test(expected = ItemNotFoundException.class)
    public void shouldThrowExceptionIfItemNotFound() {
        // given
        final long itemId = 12345L;
        // when
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        itemService.findById(itemId);
        // then
        fail("You should not see this messages: we are expecting an exception.");
    }

    @Test
    public void shouldFindItemForKnownId() {
        // given
        final long itemId = 12345L;
        Item item = new Item("myItem", "existing item");
        item.setId(itemId);
        // when
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Item foundItem = itemService.findById(itemId);
        // then
        assertThat(foundItem).isNotNull();
        assertThat(foundItem.getId()).isEqualTo(itemId);
        assertThat(foundItem.getName()).isNotNull().isEqualTo(item.getName());
        assertThat(foundItem.getDescription()).isEqualTo(item.getDescription());
    }

    @Test
    public void shouldFindAllItems() {
        List<Item> myItems = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            myItems.add(new Item("name" + i, "New Item number " + i));
        }
        // when
        when(itemRepository.findAll()).thenReturn(myItems);
        final List<Item> itemServiceAll = itemService.getAll();
        // then
        assertThat(myItems.equals(itemServiceAll)).isTrue();
    }
}
