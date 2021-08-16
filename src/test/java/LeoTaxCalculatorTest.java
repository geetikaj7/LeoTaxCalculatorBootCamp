import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

public class LeoTaxCalculatorTest {

    private static final double BASIC_TAX = 10;
    private static final double IMPORTED_TAX = 5;

    @Test
    public void calculateBasicTaxWithExceptedProduct() throws ParseException {
        double costWithBasicTax = getCostWithBasicTax(124.99, ExemptedProduct.BOOK.name());
        Assertions.assertEquals(124.99, costWithBasicTax);
    }

    @Test
    public void calculateBasicTaxWithoutExceptedProduct() throws ParseException {
        double costWithBasicTax = getCostWithBasicTax(149.99, "music");
        Assertions.assertEquals(164.99, costWithBasicTax);
    }

    @Test
    public void calculateTaxOnImportedProd() throws ParseException {
        double costWithImportedTax = getCostWithTaxOnEachProduct(470.50, true);
        Assertions.assertEquals(494.02, costWithImportedTax);
    }

    @Test
    public void calculateTotalBill() throws ParseException {
        double costWithImportedTaxP1 = getCostWithTaxOnEachProduct(getCostWithBasicTax(270.99, "perfume"), true);
        double totalTaxP1 = getTaxOnEachProduct(270.99, costWithImportedTaxP1, true);

        double costWithImportedTaxP2 = getCostWithTaxOnEachProduct(getCostWithBasicTax(180.99, "perfume"), false);
        double totalTaxP2 = getTaxOnEachProduct(180.99, costWithImportedTaxP2, false);

        double costWithImportedTaxP3 = getCostWithTaxOnEachProduct(getCostWithBasicTax(19.75, ExemptedProduct.MEDICAL.name()), false);
        double totalTaxP3 = getTaxOnEachProduct(19.75, costWithImportedTaxP3, false);

        double costWithImportedTaxP4 = getCostWithTaxOnEachProduct(getCostWithBasicTax(210.25, ExemptedProduct.FOOD.name()), true);
        double totalTaxP4 = getTaxOnEachProduct(210.25, costWithImportedTaxP4, true);

        Assertions.assertEquals(752.59, costWithImportedTaxP1 + costWithImportedTaxP2 + costWithImportedTaxP3 + costWithImportedTaxP4);
        Assertions.assertEquals(94.89, totalTaxP1 + totalTaxP2 + totalTaxP3 + totalTaxP4);

    }

    private double getTaxOnEachProduct(double costWithBasicTax, double costWithImportedTax, boolean isImported) throws ParseException {
        double importedTax = 0;
        double basicTax = getTax(BASIC_TAX, costWithBasicTax);
        if (isImported) {
            importedTax = getTax(IMPORTED_TAX, costWithImportedTax);
        }

        return basicTax + importedTax;
    }

    private double getCostWithTaxOnEachProduct(double perfume, boolean isImported) throws ParseException {
        double costWithBasicTax = perfume;
        return getCostWithImportedTax(costWithBasicTax, isImported);
    }

    private double getTax(double tax, double taxCost) throws ParseException {
        LeoTaxCalculator service = getInstance(taxCost, "productType");
        return service.calculateTax(tax);
    }

    private double getCostWithImportedTax(double costWithBasicTax, boolean isImported) throws ParseException {
        if (isImported) {
            LeoTaxCalculator service = getInstance(costWithBasicTax, "");
            return service.calculateCost(IMPORTED_TAX);
        }
        return costWithBasicTax;
    }

    private double getCostWithBasicTax(double v, String productType) throws ParseException {
        double productCost = v;
        LeoTaxCalculator service = getInstance(productCost, productType);
        boolean isExempted = service.isProductExempted();
        double costWithBasicTax = productCost;
        if (!isExempted) {
            costWithBasicTax = service.calculateCost(BASIC_TAX);
        }
        return costWithBasicTax;
    }

    private LeoTaxCalculator getInstance(double productCost, String food) {
        return new LeoTaxCalculator(food, productCost);
    }
}
