package ghassen.cart.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ghassen.cart.cart.CartDAO;
import ghassen.cart.entities.Item;
import ghassen.cart.item.ItemDAO;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class UnitItemsController {

    @Autowired
    private ItemsController itemsController;

    @Autowired
    private ItemDAO itemDAO;

    @Autowired
    private CartsController cartsController;

    @Test
    public void whenNewItemAdd() {
        Item item = new Item("id", "itemId", 1, 0F);
        String customerId = "customerIdAdd";
        itemsController.addToCart(customerId, item);
        assertThat(itemsController.getItems(customerId), is(hasSize(1)));
        assertThat(itemsController.getItems(customerId), is(org.hamcrest.CoreMatchers.hasItem(item)));
    }

    @Test
    public void whenExistIncrementQuantity() {
        Item item = new Item("id", "itemId", 1, 0F);
        String customerId = "customerIdIncrement";
        itemsController.addToCart(customerId, item);
        itemsController.addToCart(customerId, item);
        assertThat(itemsController.getItems(customerId), is(hasSize(1)));
        assertThat(itemsController.getItems(customerId), is(org.hamcrest.CoreMatchers.hasItem(item)));
        assertThat(itemDAO.findOne(item.id()).quantity(), is(equalTo(2)));
    }

    @Test
    public void shouldRemoveItemFromCart() {
        Item item = new Item("id", "itemId", 1, 0F);
        String customerId = "customerIdRemove";
        itemsController.addToCart(customerId, item);
        assertThat(itemsController.getItems(customerId), is(hasSize(1)));
        itemsController.removeItem(customerId, item.itemId());
        assertThat(itemsController.getItems(customerId), is(hasSize(0)));
    }

    @Test
    public void shouldSetQuantity() {
        Item item = new Item("id", "itemId", 1, 0F);
        String customerId = "customerIdQuantity";
        itemsController.addToCart(customerId, item);
        assertThat(itemsController.getItems(customerId).get(0).quantity(), is(equalTo(item.quantity())));
        Item anotherItem = new Item(item, 15);
        itemsController.updateItem(customerId, anotherItem);
        assertThat(itemDAO.findOne(item.id()).quantity(), is(equalTo(anotherItem.quantity())));
    }

    @Configuration
    static class ItemsControllerTestConfiguration {
        @Bean
        public ItemsController itemsController() {
            return new ItemsController();
        }

        @Bean
        public CartsController cartsController() {
            return new CartsController();
        }

        @Bean
        public ItemDAO itemDAO() {
            return new ItemDAO.Fake();
        }

        @Bean
        public CartDAO cartDAO() {
            return new CartDAO.Fake();
        }
    }
}
