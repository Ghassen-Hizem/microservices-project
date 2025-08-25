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

    /**
     * Verifies that adding a new Item to a customer's cart creates a single cart entry containing that item.
     *
     * Creates an Item and adds it via ItemsController.addToCart, then asserts the customer's cart
     * contains exactly one element and that element equals the added Item.
     */
    @Test
    public void whenNewItemAdd() {
        Item item = new Item("idAdd", "itemId", 1, 0F);
        String customerId = "customerIdAdd";
        itemsController.addToCart(customerId, item);
        assertThat(itemsController.getItems(customerId), is(hasSize(1)));
        assertThat(itemsController.getItems(customerId), is(org.hamcrest.CoreMatchers.hasItem(item)));
    }

    /**
     * Verifies that adding the same item twice to a customer's cart results in a single cart entry
     * and increments the stored item quantity.
     *
     * The test adds the same Item twice for a given customer, then asserts the cart contains exactly
     * one entry for that item and that the persisted item's quantity equals 2.
     */
    @Test
    public void whenExistIncrementQuantity() {
        Item item = new Item("idInc", "itemId", 1, 0F);
        String customerId = "customerIdIncrement";
        itemsController.addToCart(customerId, item);
        itemsController.addToCart(customerId, item);
        assertThat(itemsController.getItems(customerId), is(hasSize(1)));
        assertThat(itemsController.getItems(customerId), is(org.hamcrest.CoreMatchers.hasItem(item)));
        assertThat(itemDAO.findOne(item.id()).quantity(), is(equalTo(2)));
    }

    /**
     * Verifies that removing an item by its itemId deletes it from the customer's cart.
     *
     * Adds a single item to a cart, asserts the cart contains one entry, removes the item,
     * and then asserts the cart is empty.
     */
    @Test
    public void shouldRemoveItemFromCart() {
        Item item = new Item("idRemove", "itemId", 1, 0F);
        String customerId = "customerIdRemove";
        itemsController.addToCart(customerId, item);
        assertThat(itemsController.getItems(customerId), is(hasSize(1)));
        itemsController.removeItem(customerId, item.itemId());
        assertThat(itemsController.getItems(customerId), is(hasSize(0)));
    }

    /**
     * Verifies that updating an item's quantity via ItemsController changes the stored item's quantity.
     *
     * Creates an item, adds it to a customer's cart, confirms the initial quantity, updates the item with a
     * new quantity using ItemsController.updateItem, and asserts the ItemDAO reflects the updated quantity.
     */
    @Test
    public void shouldSetQuantity() {
        Item item = new Item("idQty", "itemId", 1, 0F);
        String customerId = "customerIdQuantity";
        itemsController.addToCart(customerId, item);
        assertThat(itemsController.getItems(customerId).get(0).quantity(), is(equalTo(item.quantity())));
        Item anotherItem = new Item(item, 15);
        itemsController.updateItem(customerId, anotherItem);
        assertThat(itemDAO.findOne(item.id()).quantity(), is(equalTo(anotherItem.quantity())));
    }

    @Configuration
    static class ItemsControllerTestConfiguration {
        /**
         * Test bean that instantiates and exposes a fresh ItemsController for the Spring test context.
         *
         * @return a new ItemsController instance used by tests
         */
        @Bean
        public ItemsController itemsController() {
            return new ItemsController();
        }

        /**
         * Creates a CartsController bean for the test application context.
         *
         * @return a new CartsController instance
         */
        @Bean
        public CartsController cartsController() {
            return new CartsController();
        }

        /**
         * Provides an in-memory fake ItemDAO for tests.
         *
         * Returns a new ItemDAO.Fake instance that simulates item persistence for unit tests without external dependencies.
         *
         * @return a fresh ItemDAO.Fake used by the test context
         */
        @Bean
        public ItemDAO itemDAO() {
            return new ItemDAO.Fake();
        }

        /**
         * Provides a test double implementation of CartDAO as a Spring bean.
         *
         * <p>Used by the test application context to supply an in-memory/fake cart repository.
         *
         * @return a new instance of {@code CartDAO.Fake}
         */
        @Bean
        public CartDAO cartDAO() {
            return new CartDAO.Fake();
        }
    }
}
