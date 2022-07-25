package com.nubank.model;

public enum OperationType {
    BUY("buy"),
    SELL("sell");

    private final String name;

    OperationType(String name){
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static OperationType from(String name){
        for(OperationType op : OperationType.values()){
            if(op.name().equalsIgnoreCase(name)){
                return op;
            }
        }
        return null;
    }


}
