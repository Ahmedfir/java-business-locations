import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.ComposedSchema;

public class DummyClassWithInstanceOf{
    static Model toModelClass(Schema<?> schema) {
        schema.getProperties().entrySet().forEach(entry -> {
            if (sch instanceof ComposedSchema) {
                Schema<?> sch = entry.getValue();
            }
        });
        return new Model();
    }
}