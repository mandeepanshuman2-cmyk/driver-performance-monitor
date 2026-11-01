import java.util.Scanner;

// Main class: driving details lekar safety score aur feedback dikhati hai.
public class DriverPerformance {

    // Galat ya negative input par dobara number maangte hain.
    static int readNumber(Scanner input, String message) {
        while (true) {
            System.out.print(message);
            try {
                int value = Integer.parseInt(input.nextLine());
                if (value >= 0) return value;
            } catch (NumberFormatException e) {
                // Text ya bahut bada number aane par program crash nahi karega.
            }
            System.out.println("Please enter a non-negative whole number.");
        }
    }

    // Demo rules: overspeed par 20, har brake par 5, acceleration par 5 points minus.
    static int calculateScore(int speed, int brakes, int acceleration) {
        // long use karte hain taaki bade input par calculation overflow na ho.
        long penalty = (speed > 80 ? 20 : 0) + 5L * brakes + 5L * acceleration;
        return (int) Math.max(0, 100 - penalty); // Score zero se neeche nahi jayega.
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in); // Keyboard se input lene ka object.
        System.out.println("=== DRIVER PERFORMANCE MONITOR ===");

        // Har loop mein ek driver/trip ki report banegi.
        String again;
        do {
            String name;
            do {
                System.out.print("Driver name: ");
                name = input.nextLine().trim();
            } while (name.isEmpty()); // Blank naam accept nahi karte.

            int speed = readNumber(input, "Maximum speed (km/h): ");
            int brakes = readNumber(input, "Sudden braking count: ");
            int acceleration = readNumber(input, "Harsh acceleration count: ");
            int score = calculateScore(speed, brakes, acceleration);

            // if-else score ko performance category mein convert karta hai.
            String rating;
            if (score >= 80) rating = "Good";
            else if (score >= 50) rating = "Average";
            else rating = "Needs Improvement";

            System.out.println("\n--- DRIVER REPORT ---");
            System.out.println("Driver: " + name);
            System.out.println("Safety score: " + score + "/100");
            System.out.println("Performance: " + rating);
            if (speed > 80) System.out.println("Advice: Reduce speed.");
            if (brakes > 0) System.out.println("Advice: Keep distance and brake smoothly.");
            if (acceleration > 0) System.out.println("Advice: Accelerate gradually.");
            System.out.println("Demo scoring only; actual speed limits vary by road.");

            System.out.print("\nCheck another trip? (y/n): ");
            again = input.nextLine().trim();
        } while (again.equalsIgnoreCase("y"));

        input.close(); // Program finish hone par Scanner close karte hain.
    }
}
