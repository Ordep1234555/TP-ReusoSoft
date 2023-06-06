import java.util.Set;

public class VereadorFactory {
    private VereadorFactory() {
        // Impede a instanciação da classe factory
    }

    public static Vereador createVereador(String name, String party, int number, String city) {
        Vereador.Builder builder = new Vereador.Builder();
        builder.name(name)
                .party(party)
                .number(number)
                .city(city);
        return builder.build();
    }
}
