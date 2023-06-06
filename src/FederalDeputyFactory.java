import java.util.Set;

public class FederalDeputyFactory {
    private FederalDeputyFactory() {
        // Impede a instanciação da classe factory
    }

    public static FederalDeputy createFederalDeputy(String name, String party, int number, String state) {
        FederalDeputy.Builder builder = new FederalDeputy.Builder();
        builder.name(name)
                .party(party)
                .number(number)
                .state(state);
        return builder.build();
    }
}
