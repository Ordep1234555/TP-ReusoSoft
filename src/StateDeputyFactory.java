import java.util.Set;

public class StateDeputyFactory {
    private StateDeputyFactory() {
        
    }

    public static StateDeputy createStateDeputy(String name, String party, int number, String state) {
        StateDeputy.Builder builder = new StateDeputy.Builder();
        builder.name(name)
                .party(party)
                .number(number)
                .state(state);
        return builder.build();
    }
}
