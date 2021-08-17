import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.stream.Stream;

public class LeoTaxCalculator {
    private final String productType;
    private final double cost;

    public LeoTaxCalculator(String productType, double cost) {
        this.productType = productType;
        this.cost = cost;
    }

    public boolean isProductExempted() {
        if (Stream.of(ExemptedProduct.values()).anyMatch(v -> v.name().equals(productType))) {
            return true;
        }
        return false;
    }

    public Double calculateCost(double tax) throws ParseException {
        return getValue(cost + calculateTax(tax));
    }

    private Double getValue(double cost) throws ParseException {
        DecimalFormat df = new DecimalFormat("0.00");
        String format = df.format(cost);
        return df.parse(format).doubleValue();
    }

    public double calculateTax(double tax) throws ParseException {
        return getValue(cost * tax / 100);
    }

}
