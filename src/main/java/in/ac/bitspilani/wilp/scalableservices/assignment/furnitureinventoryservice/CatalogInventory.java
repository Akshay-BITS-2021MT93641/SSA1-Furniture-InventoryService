package in.ac.bitspilani.wilp.scalableservices.assignment.furnitureinventoryservice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Document
public class CatalogInventory
{
    @Id
    @Builder.Default
    private UUID catalogInventoryId = UUID.randomUUID();
    private UUID catalogItemId;
    @Builder.Default
    private Map<String, Integer> colorWiseStock = new LinkedHashMap<>();
}
