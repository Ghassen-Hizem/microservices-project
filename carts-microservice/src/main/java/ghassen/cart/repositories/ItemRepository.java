package ghassen.cart.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ghassen.cart.entities.Item;

@RepositoryRestResource
public interface ItemRepository extends MongoRepository<Item, String> {
}

