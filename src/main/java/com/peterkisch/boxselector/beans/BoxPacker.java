package com.peterkisch.boxselector.beans;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoxPacker {

    private static final Logger logger = LoggerFactory.getLogger(BoxPacker.class);

    public boolean packageItems(Box box, List<Item> items) {
        List<Integer> listfreeRowSpace = new ArrayList<>(); // A list of free space for each row in the box
        int rowNumber = 0;
        
        for (Item item : items) {
            boolean itemPlaced = false;

            // Try to place the item on existing rows
            for (int i = 0; i < listfreeRowSpace.size(); i++) {
                if (listfreeRowSpace.get(i) >= item.getWidth()) {
                    listfreeRowSpace.set(i, listfreeRowSpace.get(i) - item.getWidth());
                    logger.debug("Place item (width {}) on row {}. Space left on row: {}", item.getWidth(), i + 1, listfreeRowSpace.get(i));
                    itemPlaced = true;
                    break;
                }
            }

            // If the item couldn't be placed try to add a new row
            if (!itemPlaced) {
                if (listfreeRowSpace.size() >= box.getHeight()) {
                	logger.debug("No place left for item (width {}). The height of the box is {} rows.", item.getWidth(), box.getHeight());
                    return false; // No more rows available in the box
                }
                if (item.getWidth() > box.getWidth()) {
                	logger.debug("The item (width {}) is too wide for the box (width {}).", item.getWidth(), box.getWidth());
                    return false; // The item is too wide for the box
                }
                listfreeRowSpace.add(box.getWidth() - item.getWidth()); // Add a new row
                
                rowNumber = listfreeRowSpace.size();
                logger.debug("Placing item (width {}) on a new row {}. Space left on row: {}", item.getWidth(), rowNumber, listfreeRowSpace.get(rowNumber - 1));                
                
            }
        }
        logger.debug("All items placed in the box.");
        return true;
    }

}
