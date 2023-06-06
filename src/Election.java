import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.text.DecimalFormat;

public class Election {
  private final String password;

  private boolean status;

  private int nullPresidentVotes;

  private int nullFederalDeputyVotes;

  private int nullStateDeputyVotes;

  private int nullVereadorVotes;

  private int presidentProtestVotes;

  private int federalDeputyProtestVotes;

  private int stateDeputyProtestVotes;
  
  private int vereadorProtestVotes;

  // Na prática guardaria uma hash do eleitor
  private Map<Voter, Integer> votersPresident = new HashMap<Voter, Integer>();

  // Na prática guardaria uma hash do eleitor
  private Map<Voter, Integer> votersFederalDeputy = new HashMap<Voter, Integer>();

  private Map<Voter, Integer> votersStateDeputy = new HashMap<Voter, Integer>();

    private Map<Voter, Integer> votersVereador = new HashMap<Voter, Integer>();

  private Map<Integer, President> presidentCandidates = new HashMap<Integer, President>();

  private Map<String, FederalDeputy> federalDeputyCandidates = new HashMap<String, FederalDeputy>();

  private Map<String, StateDeputy> stateDeputyCandidates = new HashMap<String, StateDeputy>();

  private Map<Integer, Vereador> vereadorCandidates = new HashMap<Integer, Vereador>();


  private Map<Voter, FederalDeputy> tempFDVote = new HashMap<Voter, FederalDeputy>();

  private Map<Voter, StateDeputy> tempSDVote = new HashMap<Voter, StateDeputy>();

    private Map<Voter, Vereador> tempVvote = new HashMap<Voter, Vereador>();


  public static class Builder {
    protected String password;

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Election build() {
      if (password == null)
        throw new IllegalArgumentException("password mustn't be null");

      if (password.isEmpty())
        throw new IllegalArgumentException("password mustn't be empty");

      return new Election(this.password);
    }
  }

  protected Election(
      String password) {
    this.password = password;
    this.status = false;
    this.nullFederalDeputyVotes = 0;
    this.nullPresidentVotes = 0;
    this.presidentProtestVotes = 0;
    this.federalDeputyProtestVotes = 0;
    this.nullStateDeputyVotes = 0;
    this.stateDeputyProtestVotes = 0;
    this.nullVereadorVotes = 0;
    this.vereadorProtestVotes = 0;
  }

  private Boolean isValid(String password) {
    return this.password.equals(password);
  }

  public void computeVote(Candidate candidate, Voter voter) {
    if (candidate instanceof President) {
      if (votersPresident.get(voter) != null && votersPresident.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para presidente");

      candidate.numVotes++;
      votersPresident.put(voter, 1);
    } else if (candidate instanceof FederalDeputy) {
      if (votersFederalDeputy.get(voter) != null && votersFederalDeputy.get(voter) >= 2)
        throw new StopTrap("Você não pode votar mais de uma vez para deputado federal");

      if (tempFDVote.get(voter) != null && tempFDVote.get(voter).equals(candidate))
        throw new Warning("Você não pode votar mais de uma vez em um mesmo candidato");

      candidate.numVotes++;
      if (votersFederalDeputy.get(voter) == null) {
        votersFederalDeputy.put(voter, 1);
        tempFDVote.put(voter, (FederalDeputy) candidate);
      } else {
        votersFederalDeputy.put(voter, this.votersFederalDeputy.get(voter) + 1);
        tempFDVote.remove(voter);
      }
    }
    else if (candidate instanceof StateDeputy) {
      if (votersStateDeputy.get(voter) != null && votersStateDeputy.get(voter) >= 2)
        throw new StopTrap("Você não pode votar mais de uma vez para deputado estadual");

      if (tempSDVote.get(voter) != null && tempSDVote.get(voter).equals(candidate))
        throw new Warning("Você não pode votar mais de uma vez em um mesmo candidato");

      candidate.numVotes++;
      if (votersStateDeputy.get(voter) == null) {
        votersStateDeputy.put(voter, 1);
        tempSDVote.put(voter, (StateDeputy) candidate);
      } else {
        votersStateDeputy.put(voter, this.votersStateDeputy.get(voter) + 1);
        tempSDVote.remove(voter);
      }
    }
    else if (candidate instanceof Vereador) {
      if (votersVereador.get(voter) != null && votersVereador.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para vereador");

      candidate.numVotes++;
      votersVereador.put(voter, 1);
    }
  };

  public void computeNullVote(String type, Voter voter) {
    if (type.equals("President")) {
      if (this.votersPresident.get(voter) != null && votersPresident.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para presidente");

      this.nullPresidentVotes++;
      votersPresident.put(voter, 1);
    } else if (type.equals("FederalDeputy")) {
      if (this.votersFederalDeputy.get(voter) != null && this.votersFederalDeputy.get(voter) >= 2)
        throw new StopTrap("Você não pode votar mais de uma vez para deputado federal");

      this.nullFederalDeputyVotes++;
      if (this.votersFederalDeputy.get(voter) == null)
        votersFederalDeputy.put(voter, 1);
      else
        votersFederalDeputy.put(voter, this.votersFederalDeputy.get(voter) + 1);
    }
    else if (type.equals("StateDeputy")) {
      if (this.votersStateDeputy.get(voter) != null && this.votersStateDeputy.get(voter) >= 2)
        throw new StopTrap("Você não pode votar mais de uma vez para deputado estadual");

      this.nullStateDeputyVotes++;
      if (this.votersStateDeputy.get(voter) == null)
        votersStateDeputy.put(voter, 1);
      else
        votersStateDeputy.put(voter, this.votersStateDeputy.get(voter) + 1);
    }
    else if (type.equals("Vereador")) {
      if (this.votersVereador.get(voter) != null && votersVereador.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para vereador");

      this.nullVereadorVotes++;
      votersVereador.put(voter, 1);
    }
  }

  public void computeProtestVote(String type, Voter voter) {
    if (type.equals("President")) {
      if (this.votersPresident.get(voter) != null && votersPresident.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para presidente");

      this.presidentProtestVotes++;
      votersPresident.put(voter, 1);
    } else if (type.equals("FederalDeputy")) {
      if (this.votersFederalDeputy.get(voter) != null && this.votersFederalDeputy.get(voter) >= 2)
        throw new StopTrap("Você não pode votar mais de uma vez para deputado federal");

      this.federalDeputyProtestVotes++;
      if (this.votersFederalDeputy.get(voter) == null)
        votersFederalDeputy.put(voter, 1);
      else
        votersFederalDeputy.put(voter, this.votersFederalDeputy.get(voter) + 1);
    }

    else if (type.equals("StateDeputy")) {
      if (this.votersStateDeputy.get(voter) != null && this.votersStateDeputy.get(voter) >= 2)
        throw new StopTrap("Você não pode votar mais de uma vez para deputado estadual");

      this.stateDeputyProtestVotes++;
      if (this.votersStateDeputy.get(voter) == null)
        votersStateDeputy.put(voter, 1);
      else
        votersStateDeputy.put(voter, this.votersStateDeputy.get(voter) + 1);
    }

     else if (type.equals("Vereador")) {
      if (this.votersVereador.get(voter) != null && votersVereador.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para vereador");

      this.vereadorProtestVotes++;
      votersVereador.put(voter, 1);
    }
  }

  public boolean getStatus() {
    return this.status;
  }

  public void start(String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.status = true;
  }

  public void finish(String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.status = false;
  }

  public President getPresidentByNumber(int number) {
    return this.presidentCandidates.get(number);
  }

  public void addPresidentCandidate(President candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (this.presidentCandidates.get(candidate.number) != null)
      throw new Warning("Numero de candidato indisponível");

    this.presidentCandidates.put(candidate.number, candidate);

  }

  public void removePresidentCandidate(President candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.presidentCandidates.remove(candidate.number);
  }

  public FederalDeputy getFederalDeputyByNumber(String state, int number) {
    return this.federalDeputyCandidates.get(state + number);
  }

  public void addFederalDeputyCandidate(FederalDeputy candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (this.federalDeputyCandidates.get(candidate.state + candidate.number) != null)
      throw new Warning("Numero de candidato indisponível");

    this.federalDeputyCandidates.put(candidate.state + candidate.number, candidate);
  }

  public void removeFederalDeputyCandidate(FederalDeputy candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.federalDeputyCandidates.remove(candidate.state + candidate.number);
  }

  public StateDeputy getStateDeputyByNumber(String state, int number) {
    return this.stateDeputyCandidates.get(state + number);
  }

  public void addStateDeputyCandidate(StateDeputy candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (this.stateDeputyCandidates.get(candidate.state + candidate.number) != null)
      throw new Warning("Numero de candidato indisponível");

    this.stateDeputyCandidates.put(candidate.state + candidate.number, candidate);
  }

  public void removeStateDeputyCandidate(StateDeputy candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.stateDeputyCandidates.remove(candidate.state + candidate.number);
  }

  public Vereador getVereadorByNumber( int number) {
    return this.vereadorCandidates.get( number);
  }

  public void addVereadorCandidate(Vereador candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (this.vereadorCandidates.get(candidate.number) != null)
      throw new Warning("Numero de candidato indisponível");

    this.vereadorCandidates.put( candidate.number, candidate);
  }

  public void removeVereadorCandidate(Vereador candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.vereadorCandidates.remove( candidate.number);
  }

  public String getResults(String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (this.status)
      throw new StopTrap("Eleição ainda não finalizou, não é possível gerar o resultado");

    var decimalFormater = new DecimalFormat("0.00");
    var presidentRank = new ArrayList<President>();
    var federalDeputyRank = new ArrayList<FederalDeputy>();
    var stateDeputyRank = new ArrayList<StateDeputy>();
    var vereadorRank = new ArrayList<Vereador>();

    var builder = new StringBuilder();

    builder.append("Resultado da eleicao:\n");

    int totalVotesP = presidentProtestVotes + nullPresidentVotes;
    for (Map.Entry<Integer, President> candidateEntry : presidentCandidates.entrySet()) {
      President candidate = candidateEntry.getValue();
      totalVotesP += candidate.numVotes;
      presidentRank.add(candidate);
    }

    int totalVotesFD = federalDeputyProtestVotes + nullFederalDeputyVotes;
    for (Map.Entry<String, FederalDeputy> candidateEntry : federalDeputyCandidates.entrySet()) {
      FederalDeputy candidate = candidateEntry.getValue();
      totalVotesFD += candidate.numVotes;
      federalDeputyRank.add(candidate);
    }

     int totalVotesSD = stateDeputyProtestVotes + nullStateDeputyVotes;
    for (Map.Entry<String, StateDeputy> candidateEntry : stateDeputyCandidates.entrySet()) {
      StateDeputy candidate = candidateEntry.getValue();
      totalVotesSD += candidate.numVotes;
      stateDeputyRank.add(candidate);
    }

    int totalVotesV = vereadorProtestVotes + nullVereadorVotes;
    for (Map.Entry<Integer, Vereador> candidateEntry : vereadorCandidates.entrySet()) {
      Vereador candidate = candidateEntry.getValue();
      totalVotesV += candidate.numVotes;
      vereadorRank.add(candidate);
    }

    var sortedFederalDeputyRank = federalDeputyRank.stream()
        .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
        .collect(Collectors.toList());

    var sortedPresidentRank = presidentRank.stream()
        .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
        .collect(Collectors.toList());

    var sortedStateDeputyRank = stateDeputyRank.stream()
        .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
        .collect(Collectors.toList());

    var sortedVereadorRank = vereadorRank.stream()
        .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
        .collect(Collectors.toList());     

    builder.append("  Votos presidente:\n");
    builder.append("  Total: " + totalVotesP + "\n");
    builder.append("  Votos nulos: " + nullPresidentVotes + " ("
        + decimalFormater.format((double) nullPresidentVotes / (double) totalVotesFD * 100) + "%)\n");
    builder.append("  Votos brancos: " + presidentProtestVotes + " ("
        + decimalFormater.format((double) presidentProtestVotes / (double) totalVotesFD * 100) + "%)\n");
    builder.append("\tNumero - Partido - Nome  - Votos  - % dos votos totais\n");
    for (President candidate : sortedPresidentRank) {
      builder.append("\t" + candidate.number + " - " + candidate.party + " - " + candidate.name + " - "
          + candidate.numVotes + " - "
          + decimalFormater.format((double) candidate.numVotes / (double) totalVotesP * 100)
          + "%\n");
    }

    President electPresident = sortedPresidentRank.get(0);
    builder.append("\n\n  Presidente eleito:\n");
    builder.append("  " + electPresident.name + " do " + electPresident.party + " com "
        + decimalFormater.format((double) electPresident.numVotes / (double) totalVotesP * 100) + "% dos votos\n");
    builder.append("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");

    builder.append("\n\n  Votos deputado federal:\n");
    builder.append("  Votos nulos: " + nullFederalDeputyVotes + " ("
        + decimalFormater.format((double) nullFederalDeputyVotes / (double) totalVotesFD * 100) + "%)\n");
    builder.append("  Votos brancos: " + federalDeputyProtestVotes + " ("
        + decimalFormater.format((double) federalDeputyProtestVotes / (double) totalVotesFD * 100) + "%)\n");
    builder.append("  Total: " + totalVotesFD + "\n");
    builder.append("\tNumero - Partido - Nome - Estado - Votos - % dos votos totais\n");
    for (FederalDeputy candidate : sortedFederalDeputyRank) {
      builder.append(
          "\t" + candidate.number + " - " + candidate.party + " - " + candidate.state + " - " + candidate.name + " - "
              + candidate.numVotes + " - "
              + decimalFormater.format((double) candidate.numVotes / (double) totalVotesFD * 100)
              + "%\n");
    }

    FederalDeputy firstDeputy = sortedFederalDeputyRank.get(0);
    FederalDeputy secondDeputy = sortedFederalDeputyRank.get(1);
    builder.append("\n\n  Deputados eleitos:\n");
    builder.append("  1º " + firstDeputy.name + " do " + firstDeputy.party + " com "
        + decimalFormater.format((double) firstDeputy.numVotes / (double) totalVotesFD * 100) + "% dos votos\n");
    builder.append("  2º " + secondDeputy.name + " do " + secondDeputy.party + " com "
        + decimalFormater.format((double) secondDeputy.numVotes / (double) totalVotesFD * 100) + "% dos votos\n");


    //RESULTADOS DEPUTADO ESTADUAL
    builder.append("\n\n  Votos deputado estadual:\n");
    builder.append("  Votos nulos: " + nullStateDeputyVotes + " ("
        + decimalFormater.format((double) nullStateDeputyVotes / (double) totalVotesSD * 100) + "%)\n");
    builder.append("  Votos brancos: " + stateDeputyProtestVotes + " ("
        + decimalFormater.format((double) stateDeputyProtestVotes / (double) totalVotesSD * 100) + "%)\n");
    builder.append("  Total: " + totalVotesSD + "\n");
    builder.append("\tNumero - Partido - Nome - Estado - Votos - % dos votos totais\n");
    for (StateDeputy candidate : sortedStateDeputyRank) {
      builder.append(
          "\t" + candidate.number + " - " + candidate.party + " - " + candidate.state + " - " + candidate.name + " - "
              + candidate.numVotes + " - "
              + decimalFormater.format((double) candidate.numVotes / (double) totalVotesSD * 100)
              + "%\n");
    }

    StateDeputy firstStateDeputy = sortedStateDeputyRank.get(0);
    StateDeputy secondStateDeputy = sortedStateDeputyRank.get(1);
    builder.append("\n\n  Deputados estaduais eleitos:\n");
    builder.append("  1º " + firstStateDeputy.name + " do " + firstStateDeputy.party + " com "
        + decimalFormater.format((double) firstStateDeputy.numVotes / (double) totalVotesSD * 100) + "% dos votos\n");
    builder.append("  2º " + secondStateDeputy.name + " do " + secondStateDeputy.party + " com "
        + decimalFormater.format((double) secondStateDeputy.numVotes / (double) totalVotesSD * 100) + "% dos votos\n");


    //RESULTADOS VEREADOR
    builder.append("  Votos vereador:\n");
    builder.append("  Total: " + totalVotesV + "\n");
    builder.append("  Votos nulos: " + nullVereadorVotes + " ("
        + decimalFormater.format((double) nullVereadorVotes / (double) totalVotesFD * 100) + "%)\n");
    builder.append("  Votos brancos: " + vereadorProtestVotes + " ("
        + decimalFormater.format((double) vereadorProtestVotes / (double) totalVotesFD * 100) + "%)\n");
    builder.append("\tNumero - Partido - Nome  - Votos  - % dos votos totais\n");
    for (Vereador candidate : sortedVereadorRank) {
      builder.append("\t" + candidate.number + " - " + candidate.party + " - " + candidate.name + " - "
          + candidate.numVotes + " - "
          + decimalFormater.format((double) candidate.numVotes / (double) totalVotesV * 100)
          + "%\n");
    }

    Vereador electVereador = sortedVereadorRank.get(0);
    builder.append("\n\n  Vereador eleito:\n");
    builder.append("  " + electVereador.name + " do " + electVereador.party + " com "
        + decimalFormater.format((double) electVereador.numVotes / (double) totalVotesV * 100) + "% dos votos\n");
    builder.append("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");



    return builder.toString();
  }
}
