package tacos.data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.asm.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import tacos.Ingredient;
import tacos.Taco;
import tacos.TacoOrder;

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    @Autowired
    private final JdbcOperations jdbcOperations;

    public JdbcOrderRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public TacoOrder save(TacoOrder order) {
        var factory = new PreparedStatementCreatorFactory("""
                INSERT INTO Taco_Order (delivery_name, delivery_street, delivery_city, delivery_state, 
                    delivery_zip, cc_number, cc_expiration, cc_cvv, placed_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP);
        factory.setReturnGeneratedKeys(true);

        order.setPlacedAt(new Date());
        PreparedStatementCreator creator = factory.newPreparedStatementCreator(
                Arrays.asList(
                        order.getDeliveryName(),
                        order.getDeliveryStreet(),
                        order.getDeliveryCity(),
                        order.getDeliveryState(),
                        order.getDeliveryZip(),
                        order.getCcNumber(),
                        order.getCcExpiration(),
                        order.getCcCVV(),
                        order.getPlacedAt()
                )
        );
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(creator, keyHolder);
        long orderId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        order.setId(orderId);
        int i = 0;
        for (Taco taco : order.getTacos()) {
            saveTaco(orderId, i++, taco);
        }
        return order;
    }

    private long saveTaco(long orderId, int orderKey, Taco taco) {
       taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory("""
                INSERT INTO Taco (name, cratedAt, taco_order, taco_order_id)
                VALUES (?, ?, ?, ?)""",
                Types.VARCHAR, Types.TIMESTAMP, Type.LONG, Type.LONG
        );
        factory.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = factory.newPreparedStatementCreator(
                List.of(taco.getName(), taco.getCreatedAt(), orderId, orderKey)
        );
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        long tacoId = keyHolder.getKey().longValue();
        taco.setId(tacoId);

        saveIngredientRefs(tacoId, taco.getIngredients());
        return tacoId;
    }

    private void saveIngredientRefs(long tacoId,
                                    @NotNull @Size(min = 1, message = "You must choose at least 1 ingredient")
                                    List<Ingredient> ingredients) {

    }
}
