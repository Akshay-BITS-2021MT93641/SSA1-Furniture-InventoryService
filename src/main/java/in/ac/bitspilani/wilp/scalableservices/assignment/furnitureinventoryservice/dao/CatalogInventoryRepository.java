package in.ac.bitspilani.wilp.scalableservices.assignment.furnitureinventoryservice.dao;

import java.util.UUID;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import in.ac.bitspilani.wilp.scalableservices.assignment.furnitureinventoryservice.CatalogInventory;
import reactor.core.publisher.Mono;

public interface CatalogInventoryRepository extends ReactiveMongoRepository<CatalogInventory, UUID>
{
    public Mono<CatalogInventory> findOptionalByCatalogItemId(UUID catalogItemId);
}
