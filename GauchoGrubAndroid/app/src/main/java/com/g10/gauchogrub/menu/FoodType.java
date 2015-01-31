package com.g10.gauchogrub.menu;

public enum FoodType {
    REGULAR, VEGAN, VEGETARIAN;

    /**
     *
     * @return
     */
    public String toString(){
        switch (this) {
            case VEGAN:
                return "Vegan";
            case VEGETARIAN:
                return "Vegetarian";
        }
        return "Regular";
    }
}
