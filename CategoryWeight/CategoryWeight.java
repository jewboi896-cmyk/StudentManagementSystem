/*
Not gonna lie, idek if i use this but it exists just in case
 */

package CategoryWeight;

public class CategoryWeight {
    private final String categoryName;
    private final double weight;

    public CategoryWeight(String categoryName, double weight) {
        this.categoryName = categoryName;
        this.weight = weight;
    }

    public String getCategoryName() { return categoryName; }
    public double getWeight() { return weight; }
}
