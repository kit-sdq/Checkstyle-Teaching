package edu.kit.informatik;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Athlete extends Person {
    private String id;
    private Country country;
    private List<Discipline> disciplines;
    private Map<Discipline, Medals> medalsPerDiscipline;
    private boolean hasParticipations;

    // Only for dummy creation!
    public Athlete(String firstName, String lastName, String id, Country country) {
        super(firstName, lastName);
        this.id = id;
        this.country = country;
        this.hasParticipations = false;
    }

    public Athlete(String firstName, String lastName, String id, Country country, Discipline discipline) {
        super(firstName, lastName);
        this.id = id;
        this.country = country;
        this.disciplines = new ArrayList<>();
        this.disciplines.add(discipline);
        this.medalsPerDiscipline = new HashMap<>();
        addSport(discipline);
    }

    public String getId() {
        return id;
    }

    public Country getCountry() {
        return country;
    }

    public void addSport(Discipline discipline) {
        disciplines.add(discipline);
        medalsPerDiscipline.put(discipline, new Medals());
    }
    
    public boolean addParticipation(Discipline discipline, int year, int gold, int silver, int bronze) {
        Medals medals = medalsPerDiscipline.get(discipline);
        if (medals.hasParticipated(year)) {
            return false;
        } else {
            medals.addMedal(year, gold, silver, bronze);
            hasParticipations = true;
            return true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Athlete athlete = (Athlete) o;
        return Objects.equals(id, athlete.id) 
                && Objects.equals(country, athlete.country)
                && Objects.equals(getFirstName(), athlete.getFirstName())
                && Objects.equals(getLastName(), athlete.getLastName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, country);
    }

    public boolean doesDiscipline(Discipline discipline) {
        return disciplines.contains(discipline);
    }
    
    public boolean hasParticipations() {
        return hasParticipations;
    }
    
    public boolean hasParticipations(Discipline discipline) {
        return medalsPerDiscipline.get(discipline).hasParticipations();
    }

    public int getNumberOfMedals(Discipline discipline) {
        return medalsPerDiscipline.get(discipline).sum();
    }

    public String getCountryName() {
        return country.getName();
    }

    public int[] getNumberPerMedal() {
        int[] result = new int[4];
        for (Medals entry : medalsPerDiscipline.values()) {
            result[0] += entry.getGoldMedals();
            result[1] += entry.getSilverMedals();
            result[2] += entry.getBronzeMedals();
            result[3] += entry.sum();
        }
        
        return result;
    }

    private final class Medals {
        private int goldMedals;
        private int silverMedals;
        private int bronzeMedals;
        private List<Integer> participatedYears;
        
        private Medals() {
            this.participatedYears = new ArrayList<>();
        }
        
        private void addMedal(int year, int gold, int silver, int bronze) {
            participatedYears.add(year);
            goldMedals += gold;
            silverMedals += silver;
            bronzeMedals += bronze;
        }
        
        private boolean hasParticipated(int year) {
            return participatedYears.contains(year);
        }
        
        private boolean hasParticipations() {
            return !participatedYears.isEmpty();
        }

        public int getGoldMedals() {
            return goldMedals;
        }

        public int getSilverMedals() {
            return silverMedals;
        }

        public int getBronzeMedals() {
            return bronzeMedals;
        }

        private int sum() {
            return goldMedals + silverMedals + bronzeMedals;
        }        
    }
}
