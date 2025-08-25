package ghassen.cart.cart;

import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Test;
import ghassen.cart.entities.Cart;
import ghassen.cart.entities.Item;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class UnitCartContentsResource {

    @Test
    public void shouldAddAndReturnContents() {
        String customerId = "testId";
        CartDAO.Fake fakeDAO = new CartDAO.Fake();
        Resource<Cart> fakeCartResource = new Resource.CartFake(customerId);
        CartContentsResource contentsResource = new CartContentsResource(fakeDAO, () -> fakeCartResource);
        Item item = new Item("item-1");
        contentsResource.add(() -> item).run();
        assertThat(contentsResource.contents().get(), IsCollectionWithSize.hasSize(1));
        assertThat(contentsResource.contents().get(), containsInAnyOrder(item));
    }

    @Test
    public void shouldStartEmpty() {
        String customerId = "cid-empty";
        CartDAO.Fake fakeDAO = new CartDAO.Fake();
        Resource<Cart> fakeCartResource = new Resource.CartFake(customerId);
        CartContentsResource contentsResource = new CartContentsResource(fakeDAO, () -> fakeCartResource);
        assertThat(contentsResource.contents().get(), IsCollectionWithSize.hasSize(0));
    }

    @Test
    public void shouldDeleteItemFromCart() {
        String customerId = "cid-delete";
        CartDAO.Fake fakeDAO = new CartDAO.Fake();
        Resource<Cart> fakeCartResource = new Resource.CartFake(customerId);
        CartContentsResource contentsResource = new CartContentsResource(fakeDAO, () -> fakeCartResource);
        Item item = new Item("testId");
        contentsResource.add(() -> item).run();
        assertThat(contentsResource.contents().get(), IsCollectionWithSize.hasSize(1));
        assertThat(contentsResource.contents().get(), containsInAnyOrder(item));
        Item item2 = new Item(item.itemId());
        contentsResource.delete(() -> item2).run();
        assertThat(contentsResource.contents().get(), IsCollectionWithSize.hasSize(0));
    }
}
