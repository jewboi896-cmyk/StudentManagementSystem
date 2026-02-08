/*
Manages everything to do with weighted calculations and such
 */

package CategoryWeight;

import Course.Course;
import Main.Entry;
import java.util.Scanner;

public class CategoryWeightManager {
    private final Scanner scanner;

    public CategoryWeightManager(Scanner scanner, Entry entryApp) {
        this.scanner = scanner;
    }
    
    public void addCategoryWeights(Course course) {
        // Add category weights to the course
        System.out.println("\nAdd category weights (must total 1.0 or 100%):");
        double totalWeight = 0.0;

        // Loop to add multiple categories
        while (true) {
            System.out.print("Enter category name (or 'done' to finish): ");
            String category = scanner.nextLine().trim();
            // Check for completion
            if (category.equalsIgnoreCase("done")) {
                break;
            }

            // Get user weight for the category
            System.out.print("Enter weight for " + category + " (as decimal, e.g., 0.2 for 20%): ");
            double catWeight;

            // Validate weight input
            try {
                catWeight = Double.parseDouble(scanner.nextLine().trim());
                if (catWeight < 0) {
                    System.out.println("Error: Assignment weight cannot be negative");
                    continue;
                }
            }
            // if weight isnt a decimal, trigger error
            catch (NumberFormatException e) {
                System.out.println("Error: Invalid weight. Please enter a decimal number.");
                continue;
            }

            // Check weight range
            if (catWeight < 0 || catWeight > 1) {
                System.out.println("Error: Weight must be between 0 and 1.");
                continue;
            }

            // Add category and weight to the course
            course.addCategoryWeight(category, catWeight);
            totalWeight += catWeight;

            System.out.println("Added: " + category + " = " + (catWeight * 100) + "%");
            System.out.println("Total weight so far: " + (totalWeight * 100) + "%");
        }
        // Warn if total weight is not 100%
        if (Math.abs(totalWeight - 1.0) > 0.001) {
            System.out.println("Warning: Category weights total " + (totalWeight * 100) + "%, not 100%.");
        }
    }
}
