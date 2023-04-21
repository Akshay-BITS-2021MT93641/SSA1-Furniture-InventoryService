package in.ac.bitspilani.wilp.scalableservices.assignment.furnitureinventoryservice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.ac.bitspilani.wilp.scalableservices.assignment.furnitureinventoryservice.dao.CatalogInventoryRepository;
import in.ac.bitspilani.wilp.scalableservices.assignment.furnitureinventoryservice.dao.FurnitureCatalogDao;
import reactor.core.publisher.Mono;

@RestController
public class FurnitureInventoryServiceController
{
    
    @Autowired
    private CatalogInventoryRepository catalogInventoryRepository;
    
    @Autowired
    private FurnitureCatalogDao furnitureCatalogDao;
    
    @GetMapping("/catalogItemsInventory/{catalogItemId}")
    public Mono<CatalogInventory> getStock(@PathVariable UUID catalogItemId)
    {
        return catalogInventoryRepository.findOptionalByCatalogItemId(catalogItemId);
    }

    @PostMapping("/catalogItemsInventory/{catalogItemId}")
    public Mono<CatalogInventory> addStock(@PathVariable UUID catalogItemId, @RequestParam String color, @RequestParam Integer stock)
    {
        Objects.requireNonNull(catalogItemId);
        Objects.requireNonNull(color);
        Objects.requireNonNull(stock);
        if(stock<=0) {
            throw new IllegalArgumentException("Invalid stock number. Must be a +ve integer");
        }
        
        return updateStock(catalogItemId, color, stock);
    }
    
    @DeleteMapping("/catalogItemsInventory/{catalogItemId}")
    public Mono<CatalogInventory> removeStock(@PathVariable UUID catalogItemId, @RequestParam String color, @RequestParam Integer stock)
    {
        Objects.requireNonNull(catalogItemId);
        Objects.requireNonNull(color);
        Objects.requireNonNull(stock);
        if(stock<=0) {
            throw new IllegalArgumentException("Invalid stock number. Must be a +ve integer");
        }
        
        return updateStock(catalogItemId, color, 0-stock.intValue());
    }
    
    private Mono<CatalogInventory> updateStock(UUID catalogItemId, String color, Integer stock)
    {
        return
                furnitureCatalogDao.getCatalogItemId(catalogItemId)
                .switchIfEmpty(Mono.error(()->new IllegalArgumentException("Catalog item id invalid. No catalog item exists with this id")))
                .flatMap(catalogItemIdValid->{
                    
                    return
                            catalogInventoryRepository.findOptionalByCatalogItemId(catalogItemIdValid)
                            .switchIfEmpty(Mono.just(CatalogInventory.builder().catalogItemId(catalogItemIdValid).build()))
                            .flatMap(inventoryRecord->{
                                
                                Map<String, Integer> colorWiseStock = inventoryRecord.getColorWiseStock();
                                if(Objects.isNull(colorWiseStock))
                                {
                                    colorWiseStock = new LinkedHashMap<>();
                                    inventoryRecord.setColorWiseStock(colorWiseStock);
                                }
                                
                                Integer existingStock = colorWiseStock.get(color);
                                Integer updatedStock = stock;
                                if(Objects.nonNull(existingStock)) 
                                {
                                    updatedStock = existingStock.intValue() + stock.intValue();
                                }
                                
                                if(updatedStock < 0) 
                                {
                                    throw new IllegalArgumentException("Invalid stock number. Cannot remove more than existing stock");
                                }
                                
                                colorWiseStock.put(color, updatedStock);
                                
                                return catalogInventoryRepository.save(inventoryRecord);
                            });
                });
    }
    
}
