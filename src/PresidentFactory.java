public class PresidentFactory {
    private PresidentFactory() {
        // Impede a instanciação da classe factory
    }

    public static President createPresident(String name, String party, int number) {
        President.Builder builder = new President.Builder();
        builder.name(name)
                .party(party)
                .number(number);
        return builder.build();
    }
}
