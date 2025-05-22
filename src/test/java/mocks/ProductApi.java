public class ProductApi {
    /**
     * Мокированный метод для добавления товара в корзину.
     * Возвращает фиктивный JSON-ответ.
     */
    public String addToCartMock(int productId, int quantity) {
        return "{\n" +
                "  \"success\": true,\n" +
                "  \"id_product\": " + productId + ",\n" +
                "  \"id_product_attribute\": 1,\n" +
                "  \"id_customization\": 0,\n" +
                "  \"quantity\": " + quantity + ",\n" +
                "  \"cart\": {\n" +
                "    \"products\": [\n" +
                "      {\n" +
                "        \"add_to_cart_url\": \"https://proud-snails.demo.prestashop.com/en/cart?add=1&id_product=\" + productId + \"&id_product_attribute=1&token=d7e7cc81ffddba8e19ef2b668074a64e\",\n" +
                "        \"id\": " + productId + ",\n" +
                "        \"attributes\": {\n" +
                "          \"Size\": \"S \",\n" +
                "          \"Color\": \"White\"\n" +
                "        },\n" +
                "        \"show_price\": true,\n" +
                "        \"weight_unit\": \"kg\",\n" +
                "        \"url\": \"https://proud-snails.demo.prestashop.com/en/men/1-1-hummingbird-printed-t-shirt.html#/1-size-s/8-color-white\",\n" +
                "        \"canonical_url\": \"https://proud-snails.demo.prestashop.com/en/men/1-hummingbird-printed-t-shirt.html\"\n" +
                "      }\n]" +
                "}";

    }
}