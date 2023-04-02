package tacos.web;

import java.util.HashMap;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;

import org.springframework.stereotype.Component;
import tacos.Ingredient;
import tacos.Ingredient.Type;

@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {

    private Map<String, Ingredient> map = new HashMap<>();

    public IngredientByIdConverter() {
        map.put("FLTO", new Ingredient("FLTO", "Flour Tortilla", Type.WRAP));
        map.put("COTO", new Ingredient("COTO", "Corn Tortilla", Type.WRAP));
        map.put("GRBF", new Ingredient("GRBF", "Ground Beef", Type.PROTEIN));
        map.put("CARN", new Ingredient("CARN", "Carnitas", Type.PROTEIN));
        map.put("TMTO", new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES));
        map.put("LETC", new Ingredient("LETC", "Lettuce", Type.VEGGIES));
        map.put("CHED", new Ingredient("CHED", "Cheddar", Type.CHEESE));
        map.put("JACK", new Ingredient("JACK", "Monterrey Jack", Type.CHEESE));
        map.put("SLSA", new Ingredient("SLSA", "Salsa", Type.SAUCE));
        map.put("SRCR", new Ingredient("SRCR", "Sour Cream", Type.SAUCE));
    }

    @Override
    public Ingredient convert(String id) {
        return map.get(id);
    }
}
