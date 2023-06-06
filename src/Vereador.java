import java.util.Set;

public class Vereador extends Candidate {
  protected final String city;

  public static class Builder {
    protected String name;
    protected String party;
    protected int number;
    protected String city;

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder party(String party) {
      this.party = party;
      return this;
    }

    public Builder number(int number) {
      this.number = number;
      return this;
    }

    public Builder city(String city) {
      this.city = city;
      return this;
    }

    public Vereador build() {
      if (number <= 0)
        throw new IllegalArgumentException("number mustn't be less than or equal to 0");

      if (name == null)
        throw new IllegalArgumentException("name mustn't be null");

      if (name.isEmpty())
        throw new IllegalArgumentException("name mustn't be empty");

      if (party == null)
        throw new IllegalArgumentException("party mustn't be null");

      if (party.isEmpty())
        throw new IllegalArgumentException("party mustn't be empty");

      if (city == null)
        throw new IllegalArgumentException("city mustn't be null");

      if (city.isEmpty())
        throw new IllegalArgumentException("city mustn't be empty");

      Set<String> validCities = Set.of("BH");

      if (!validCities.contains(city))
        throw new IllegalArgumentException("city is invalid");

      return new Vereador(
          this.name,
          this.party,
          this.number,
          this.city);
    }
  }

  protected Vereador(
      String name,
      String party,
      int number,
      String city) {
    super(name, party, number);
    this.city = city;
  }

  @Override
  public String toString() {
    return super.name + super.party + super.number + new String(this.city);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Vereador))
      return false;

    var fd = (Vereador) obj;

    return this.toString().equals(fd.toString());
  }

  public String getCity(){
    return this.city;
  }
}