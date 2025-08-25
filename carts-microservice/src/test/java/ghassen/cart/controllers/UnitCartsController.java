package ghassen.cart.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ghassen.cart.cart.CartDAO;
import ghassen.cart.entities.Cart;
import ghassen.cart.entities.Item;
import ghassen.cart.item.ItemDAO;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class UnitCartsController {

    @Autowired
    private ItemsController itemsController;

    @Autowired
    private CartDAO cartDAO;

    @Autowired
    private CartsController cartsController;


    @Test
    public void shouldGetCart() {
        String customerId = "customerIdGet";
        Cart cart = new Cart(customerId);
        cartDAO.save(cart);
        Cart gotCart = cartsController.get(customerId);
        assertThat(gotCart, is(equalTo(cart)));
        assertThat(cartDAO.findByCustomerId(customerId).get(0), is(equalTo(cart)));
    }

    /**
     * Verifies that deleting a cart by customerId removes it from persistent storage.
     *
     * Creates and saves a Cart for a customerId, invokes CartsController.delete(customerId),
     * and asserts the CartDAO no longer returns any carts for that customer.
     */
    @Test
    public void shouldDeleteCart() {
        String customerId = "customerIdGet";
        Cart cart = new Cart(customerId);
        cartDAO.save(cart);
        cartsController.delete(customerId);
        assertThat(cartDAO.findByCustomerId(customerId), is(empty()));
    }

    @Test
    public void shouldMergeItemsInCartsTogether() {
        String customerId1 = "customerId1";
        Cart cart1 = new Cart(customerId1);
        Item itemId1 = new Item("itemId1");
        cart1.add(itemId1);
        cartDAO.save(cart1);
        String customerId2 = "customerId2";
        Cart cart2 = new Cart(customerId2);
        Item itemId2 = new Item("itemId2");
        cart2.add(itemId2);
        cartDAO.save(cart2);

        cartsController.mergeCarts(customerId1, customerId2);
        assertThat(cartDAO.findByCustomerId(customerId1).get(0).contents(), is(hasSize(2)));
        assertThat(cartDAO.findByCustomerId(customerId1).get(0).contents(), is(containsInAnyOrder(itemId1, itemId2)));
        assertThat(cartDAO.findByCustomerId(customerId2), is(empty()));
    }

    @Configuration
    static class ItemsControllerTestConfiguration {
        /**
         * Spring test bean that provides a new ItemsController instance.
         *
         * <p>Used to wire an ItemsController into the test application context.
         *
         * @return a new ItemsController instance
         */
        @Bean
        public ItemsController itemsController() {
            return new ItemsController();
        }

        /**
         * Creates and exposes a CartsController bean for the test Spring context.
         *
         * Used by the nested test configuration to inject a standalone CartsController instance into tests.
         *
         * @return a new CartsController instance
         */
        @Bean
        public CartsController cartsController() {
            return new CartsController();
        }

        /**
         * Provides a test double implementation of ItemDAO for the Spring test context.
         *
         * @return an in-memory {@link ItemDAO} fake used by unit tests
         */
        @Bean
        public ItemDAO itemDAO() {
            return new ItemDAO.Fake();
        }

        /**
         * Provides a test {@code CartDAO} implementation backed by an in-memory fake.
         *
         * <p>Used to inject a deterministic, ephemeral cart data store for unit tests.
         *
         * @return a new instance of {@link CartDAO.Fake} suitable for testing
         */
        @Bean
        public CartDAO cartDAO() {
            return new CartDAO.Fake();
        }
    }
}
