package com.peterkisch.boxselector.beans;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class ItemDefinitionConfig {
    private String itemDefinitionList;

    public String getItemDefinitionList() {
        return itemDefinitionList;
    }

    public void setItemDefinitionList(String itemDefinitionList) {
        this.itemDefinitionList = itemDefinitionList;
    }
}