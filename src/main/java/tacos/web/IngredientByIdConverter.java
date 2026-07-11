package tacos.web;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tacos.Ingredient;
import tacos.data.IngredientRepository;

@Component
@RequiredArgsConstructor
public class IngredientByIdConverter implements Converter<String, Ingredient> {

    private final IngredientRepository ingredientRepo;

    @Override
    @Nullable
    public Ingredient convert(String id) {
        return ingredientRepo.findById(id).orElse(null);
    }
}
