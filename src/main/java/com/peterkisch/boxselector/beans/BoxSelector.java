package com.peterkisch.boxselector.beans;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class BoxSelector implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(BoxSelector.class);
    private final ItemDefinitionConfig itemDefinitionConfig;
    private final ItemListConfig itemListConfig;

    public BoxSelector(ItemDefinitionConfig itemDefinitionConfig, ItemListConfig itemListConfig) {
        this.itemDefinitionConfig = itemDefinitionConfig;
    	this.itemListConfig = itemListConfig;        
    }

    @Override
    public void run(String... args) {

    	// Setup available boxes
        List<Box> availableBoxes = List.of(
		    new Box(1, 4, 5),
		    new Box(2, 8, 12),
		    new Box(3, 12, 20)
		);        

    	// Get item definition config
    	String confItemDefinition = itemDefinitionConfig.getItemDefinitionList();
        if (confItemDefinition == null || confItemDefinition.isBlank()) {
        	logger.debug("No item definitions found in application.properties");
            return;
        }
        String[] itemDefinitions = confItemDefinition.split("\\,");
        Map<Integer, Integer> itemWidthMap = parseItemDefinitions(itemDefinitions);
        
        // Get item list config
        String confItemList = itemListConfig.getItemList();
        if (confItemList == null || confItemList.isBlank()) {
        	logger.debug("No item lists found in application.properties");
            return;
        }
        String[] itemGroups = confItemList.split("\\|");

        // Package each item list
        BoxPacker boxPacker = new BoxPacker();
        for (String itemGroup : itemGroups) {        	
            List<Item> itemList = parseItemGroup(itemGroup, itemWidthMap);
            
            // Sort the items by descending width
            itemList.sort((item1, item2) -> Integer.compare(item2.getWidth(), item1.getWidth()));
            
            // Try to pack the items in a box
            boolean boxResult = false;            
            for (Box box : availableBoxes) {            	
            	boxResult = boxPacker.packageItems(box, itemList);
                if (boxResult) {
                	System.out.printf(getItemListSummary(itemList));
                	System.out.printf(" => kartong nr %d ", box.getBoxNumber());
                	System.out.println();
                	break;
                }
                logger.debug("Result for packing into boxnr " + box.getBoxNumber() + ": " + boxResult);
            } 
            if (!boxResult) {
            	System.out.printf(getItemListSummary(itemList));
            	System.out.printf(" => \"Upphämtning krävs \"");
            	System.out.println();
            }
        }
    }
        
    private String getItemListSummary(List<Item> itemList) {
        Map<Integer, Long> itemCounts = itemList.stream()
            .collect(Collectors.groupingBy(Item::getItemNumber, TreeMap::new, Collectors.counting()));

        return itemCounts.entrySet().stream()
            .map(entry -> entry.getValue() + " st artikel " + entry.getKey())
            .collect(Collectors.joining(", "));
    }
    
    private List<Item> parseItemGroup(String itemGroup, Map<Integer, Integer> itemWidthMap) {
        List<Item> result = new ArrayList<>();
        String[] parts = itemGroup.split(",");
        for (String part : parts) {
            String[] split = part.split("x");
            int itemCount = Integer.parseInt(split[0].trim());
            int itemNumber = Integer.parseInt(split[1].trim());
            int itemWidth = itemWidthMap.getOrDefault(itemNumber, -1);
            
            for (int i = 0; i < itemCount; i++) {
                result.add(new Item(itemNumber, itemWidth));
            }
        }
        return result;
    }
    
    private Map<Integer, Integer> parseItemDefinitions(String[] itemDefinitions) {
    	Map<Integer, Integer> itemWidthMap = new HashMap<>();        	    	
    	for (String itemDefinition : itemDefinitions) {        	
	        String[] split = itemDefinition.split("x");
	        if (split.length == 2) {
	            try {
	                int itemNumber = Integer.parseInt(split[0].trim());
	                int itemWidth = Integer.parseInt(split[1].trim());
	                itemWidthMap.put(itemNumber, itemWidth);
	            } catch (NumberFormatException e) {
	                logger.debug("Invalid item definition: " + itemDefinition);
	            }
	        } else {
	            logger.debug("Invalid format of item definition: " + itemDefinition);
	        }
    	}
        return itemWidthMap;
    }    
    
}
