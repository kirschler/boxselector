package com.peterkisch.boxselector;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.boot.test.context.SpringBootTest;

import com.peterkisch.boxselector.beans.Box;
import com.peterkisch.boxselector.beans.BoxPacker;
import com.peterkisch.boxselector.beans.Item;

@SpringBootTest
class BoxSelectorApplicationTests {

    @Test
    public void testCanPackItems() {
        Box box = new Box(1, 4, 5);
        List<Item> items = List.of(
            new Item(1, 1),
            new Item(2, 2),
            new Item(3, 4),
            new Item(3, 4),
            new Item(3, 4)
        );

        BoxPacker packer = new BoxPacker();
        boolean packResult = packer.packageItems(box, items);

        assertTrue(packResult);
    }
    
    @Test
    public void testCannotFitItemTooWide() {
        Box box = new Box(1, 4, 1);
        List<Item> items = List.of(new Item(3, 4));

        BoxPacker packer = new BoxPacker();
        boolean packResult = packer.packageItems(box, items);

        assertFalse(packResult);
    }    
    
    @Test
    public void testCannotPackItemsInAnyBox() {
        List<Box> availableBoxes = List.of(
		    new Box(1, 4, 5),
		    new Box(2, 8, 12),
		    new Box(3, 12, 20)
		);    
        
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < 100; i++)
        	itemList.add(new Item(1, 1));
        for (int i = 0; i < 12; i++)
        	itemList.add(new Item(7, 12));
        
        boolean packResult = false;
        BoxPacker boxPacker = new BoxPacker();
        for (Box box : availableBoxes) {            	
        	packResult = boxPacker.packageItems(box, itemList);
            if (packResult) {
            	break;
            }
        } 
        assertFalse(packResult);
    }      
}
