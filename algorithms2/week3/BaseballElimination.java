/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseballElimination {

    private static final int TARGET = 1;
    private static final int SOURCE = 0;
    private final Team[] teams;
    private final Map<String, Integer> teamNames = new HashMap<>();

    private boolean certificateCalculated;

    public BaseballElimination(String filename) {
        String[] strings = new In(filename).readAllLines();
        teams = new Team[Integer.parseInt(strings[0])];
        for (int i = 1; i < strings.length; i++) {
            teams[i - 1] = new Team(strings[i]);
            teamNames.put(teams[i - 1].name, i - 1);
        }
    }              // create a baseball division from given filename in format specified below

    private void calculateCertificates() {
        for (int i = 0; i < teams.length; i++) {
            for (int j = 0; j < teams.length; j++) {
                if (i != j) {
                    if (teams[i].wins + teams[i].remaining < teams[j].wins) {
                        teams[i].certificates.add(teams[j].name);
                        teams[i].eliminated = true;
                    }
                }
            }
        }
        for (int i = 0; i < teams.length; i++) {
            if (teams[i].eliminated) {
                continue;
            }
            FordFulkerson fordFulkerson = createMinCut(teams[i].name);
            for (Team other : teams) {
                if (other.name.equals(teams[i].name) || other.eliminated) {
                    continue;
                }
                if (fordFulkerson.inCut(teamNames.get(other.name) + 2)) {
                    teams[i].certificates.add(other.name);
                }
            }
        }
    }

    public int numberOfTeams() {
        return teams.length;
    }            // number of teams

    public Iterable<String> teams() {
        return teamNames.keySet();
    }          // all teams

    public int wins(String team) {
        validate(team);
        return teams[teamNames.get(team)].wins;
    }          // number of wins for given team

    public int losses(String team) {
        validate(team);
        return teams[teamNames.get(team)].losses;
    }        // number of losses for given team

    public int remaining(String team) {
        validate(team);
        return teams[teamNames.get(team)].remaining;
    }      // number of remaining games for given team

    public int against(String team1, String team2) {
        validate(team1);
        validate(team2);
        return teams[teamNames.get(team1)].againsts.get(teamNames.get(team2));
    }   // number of remaining games between team1 and team2

    public boolean isEliminated(String team) {
        validate(team);
        if (!certificateCalculated) {
            certificateCalculated = true;
            calculateCertificates();
        }
        return certificateOfElimination(team) != null;
    }  // is given team eliminated?

    private void validate(String team) {
        if (!teamNames.containsKey(team)) {
            throw new IllegalArgumentException();
        }
    }

    private FordFulkerson createMinCut(String forTeam) {
        int potentialWin = wins(forTeam) + remaining(forTeam);
        List<FlowEdge> flowEdges = new ArrayList<>();
        int x = teams.length + 2;
        for (int i = 0; i < teams.length; i++) {
            if (teams[i].name.equals(forTeam) || teams[i].eliminated) {
                continue;
            }
            int capacity = potentialWin - teams[i].wins;
            flowEdges.add(new FlowEdge(i + 2, TARGET, capacity));

            List<Integer> againsts = teams[i].againsts;
            for (int j = i; j < againsts.size(); j++) {
                if (teams[j].eliminated || i == j || j == teamNames.get(forTeam)) {
                    continue;
                }
                flowEdges.add(new FlowEdge(SOURCE, x, againsts.get(j)));
                flowEdges.add(new FlowEdge(x, j + 2, Double.POSITIVE_INFINITY));
                flowEdges.add(new FlowEdge(x, i + 2, Double.POSITIVE_INFINITY));
                x++;
            }
        }
        FlowNetwork flowNetwork = new FlowNetwork(x);
        flowEdges.forEach(flowNetwork::addEdge);
        // System.out.println(flowNetwork);
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, SOURCE, TARGET);
        return fordFulkerson;
    }

    public Iterable<String> certificateOfElimination(String team) {
        validate(team);
        if (!certificateCalculated) {
            certificateCalculated = true;
            calculateCertificates();
        }
        List<String> certificates = teams[teamNames.get(team)].certificates;
        return certificates.isEmpty() ? null : certificates;
    } // subset R of teams that eliminates given team; null if not eliminated


    private static class Team {
        private final String name;
        private final int wins;
        private final int losses;
        private final int remaining;
        private boolean eliminated;
        private final List<Integer> againsts = new ArrayList<>();
        private final List<String> certificates = new ArrayList<>();

        private Team(String line) {
            String[] split = line.trim().split("\\s+");
            name = split[0];
            wins = Integer.parseInt(split[1]);
            losses = Integer.parseInt(split[2]);
            remaining = Integer.parseInt(split[3]);
            for (int i = 4; i < split.length; i++) {
                againsts.add(Integer.parseInt(split[i]));
            }
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
