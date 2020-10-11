package binary404.mystica.common.core.util;

import java.util.List;

public class RandomItemChooser {

    public Item chooseOnWeight(List<Item> items) {
        double completeWeight = 0.0D;
        for (Item item : items)
            completeWeight += item.getWeight();
        double r = Math.random() * completeWeight;
        double countWeight = 0.0D;

        for (Item item : items) {
            countWeight += item.getWeight();
            if (countWeight >= r)
                return item;
        }
        throw new RuntimeException("Should Not Be Thrown");
    }

    public static interface Item {
        double getWeight();
    }

}
