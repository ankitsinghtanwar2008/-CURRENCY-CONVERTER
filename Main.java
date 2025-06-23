import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;  

public class Main {
 
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Available Currencies: USD, INR, EUR, JPY");
        System.out.print("Enter base currency (e.g., USD): ");
        String baseCurrency = sc.next().toUpperCase();

        System.out.print("Enter target currency (e.g., INR): ");
        String targetCurrency = sc.next().toUpperCase();

        System.out.print("Enter amount to convert: ");
        double amount = sc.nextDouble();

        double rate = fetchRate(baseCurrency, targetCurrency);

        if (rate != 0.0) {
            double converted = amount * rate;
            System.out.printf("Converted Amount: %.2f %s\n", converted, targetCurrency);
        } else {
            System.out.println("Currency not found or failed to fetch rate.");
        }

        sc.close();
    }

    public static double fetchRate(String base, String target) {
        try {
            String url = "https://api.exchangerate-api.com/v4/latest/" + base;
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("\"" + target + "\":")) {
                    // Example: "INR":83.75,
                    int start = line.indexOf("\"" + target + "\":") + target.length() + 3;
                    int end = line.indexOf(",", start);
                    if (end == -1) end = line.indexOf("}", start);
                    String rateStr = line.substring(start, end).trim();
                    return Double.parseDouble(rateStr);
                }
            }
            in.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0.0;
    }
}
