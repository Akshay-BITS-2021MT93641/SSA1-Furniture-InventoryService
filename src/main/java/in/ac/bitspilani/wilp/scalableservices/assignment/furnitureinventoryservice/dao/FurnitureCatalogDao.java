package in.ac.bitspilani.wilp.scalableservices.assignment.furnitureinventoryservice.dao;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Repository
public class FurnitureCatalogDao
{

    @Value("${ssa1.furniture.catalog.getCatalogItemUrl}")
    private String getCatalogItemUrl;
    
    public Mono<UUID> getCatalogItemId(UUID catalogItemId)
    {
        return
                WebClient.builder()
                .build()
                .get()
                .uri(getCatalogItemUrl, catalogItemId.toString())
                .retrieve()
                .bodyToMono(Map.class)
                .map(m->UUID.fromString(m.get("itemId").toString()));        
    }
}
