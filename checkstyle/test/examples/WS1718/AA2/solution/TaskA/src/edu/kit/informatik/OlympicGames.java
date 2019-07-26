package edu.kit.informatik;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class OlympicGames {
    private Database db;
    private boolean adminLoggedIn;
    /**
     * Constructs a new instance of the game with two players.
     */
    public OlympicGames() {
        db = new Database();
        adminLoggedIn = false;
    }
    
    public void addAdmin(String firstName, String lastName, String username, String password) throws InputException {
        if (adminLoggedIn) {
            throw new InputException("there is an administrator logged in, please log out first.");    
        }
        
        Administrator admin = new Administrator(firstName, lastName, username, password);
        if (db.contains(admin)) {
            throw new InputException("an administrator with this username already exists.");
        } else {
            db.add(admin);
        }
    }
    
    public void loginAdmin(String username, String password) throws InputException {
        if (adminLoggedIn) {
            throw new InputException("there is an administrator logged in, please log out first.");
        }
        
        Administrator adminToLogin = db.getAdmins().stream()
                .filter(admin -> username.equals(admin.getUsername()))
                .findFirst()
                .orElseThrow(() -> new InputException("no admin with this username found."));
        
        if (!adminToLogin.getPassword().equals(password)) {
            throw new InputException("the password is wrong, please try again.");
        } else {
            adminLoggedIn = true;
        }
    }
    
    public void logoutAdmin() throws InputException {
        if (!adminLoggedIn) {
            throw new InputException("there is no administrator logged in."); 
        } else {
            adminLoggedIn = false;
        }
    }

    public void addSportsVenue(String id, String country, String location, String name, int yearOfOpening, int numberOfSeats) throws InputException {
        if (!adminLoggedIn) {
            throw new InputException("there is no administrator logged in.");
        }
        
        if (db.venueIdExists(id)) {
            throw new InputException("a venue with this ID already exists.");
        }
        
        Country countryInfo = db.getCountryByName(country);
        if (countryInfo == null) {
            throw new InputException("the country of the venue is not part of the IOC.");
        }
        
        db.add(new Venue(id, countryInfo, location, name, yearOfOpening, numberOfSeats));
    }
    
    public List<Venue> getVenues(String countryName) throws InputException {
        if (!adminLoggedIn) {
            throw new InputException("there is no administrator logged in.");
        }
        
        Country country = db.getCountryByName(countryName);
        if (country == null) {
            throw new InputException("the country is not part of the IOC.");
        }
        
        return db.getVenues().stream().filter(venue -> venue.getCountry().equals(country)).collect(Collectors.toList());
    }

    public void addSport(String sport, String disciplineName) throws InputException {
        if (!adminLoggedIn) {
            throw new InputException("there is no administrator logged in.");
        }
        
        Discipline discipline = new Discipline(sport, disciplineName);
        if (db.disciplineExists(discipline)) {
            throw new InputException("this combination of sport and discipline already exists.");
        }
        
        db.add(discipline);
    }
    
    public List<Discipline> getDisciplines() throws InputException {
        if (!adminLoggedIn) {
            throw new InputException("there is no administrator logged in.");
        }
        
        return new ArrayList<>(db.getDisciplines());
    }

    public void addCountry(String id, String code, String name, String yearOfDetermination) throws InputException {
        if (!adminLoggedIn) {
            throw new InputException("there is no administrator logged in.");
        }
        
        if (db.countryIdExists(id)) {
            throw new InputException("there is already a country with this IOC-ID.");
        } else if (db.countryCodeExists(code)) {
            throw new InputException("there is already a country with this IOC-Code.");
        }
        
        db.add(new Country(id, code, name, yearOfDetermination));
    }

    public List<Country> getCountries() throws InputException {
        if (!adminLoggedIn) {
            throw new InputException("there is no administrator logged in.");
        }
        
        return new ArrayList<>(db.getCountries());
    }

    public void addAthlete(String id, String firstName, String lastName, String countryName, String sport, String disciplineName) throws InputException {
        if (!adminLoggedIn) {
            throw new InputException("there is no administrator logged in.");
        }
        
        Discipline discipline = new Discipline(sport, disciplineName);
        Country country = db.getCountryByName(countryName);
        if (country == null) {
            throw new InputException("the country is not registered with the IOC."); 
        } else if (!db.disciplineExists(discipline)) {
            throw new InputException("this combination of sport and discipline doesn't exist");
        }
        
        Athlete dummyAthlete = new Athlete(firstName, lastName, id, country);
        if (db.athleteIdExists(id)) {
            Athlete athlete = db.getAthleteByDummy(dummyAthlete);
            if (athlete != null) {
                athlete.addSport(discipline);
            } else {
                throw new InputException("there is already an athlete with this id.");
            }
        } else {
            db.add(new Athlete(firstName, lastName, id, country, discipline));
        }
    }

    public String printAthleteRaking(String sport, String disciplineName) throws InputException {
        if (!adminLoggedIn) {
            throw new InputException("there is no administrator logged in.");
        }
        
        List<SummaryAthlete> summaryAthletes = new ArrayList<>();
        Discipline discipline = new Discipline(sport, disciplineName);
        
        if (!db.disciplineExists(discipline)) {
            throw new InputException("discipline doesn't exist.");    
        }
        
        for (Athlete current : db.getAthletes()) {
            if (current.doesDiscipline(discipline) && current.hasParticipations(discipline)) {
                summaryAthletes.add(new SummaryAthlete(current, discipline));
            }
        }

        Collections.sort(summaryAthletes);
        return summaryAthletes.stream().map(SummaryAthlete::toString).collect(Collectors.joining("\n"));
    }

    public void addCompetition(String id, int year, String countryName, String sport, String disciplineName, int gold, int silver, int bronze) throws InputException {
        if (!adminLoggedIn) {
            throw new InputException("there is no administrator logged in.");
        }
        
        Athlete athlete = db.getAthleteById(id);
        Discipline discipline = new Discipline(sport, disciplineName);
        if (athlete == null) {
            throw new InputException("there is no athlete with this id.");
        } else if (!athlete.getCountryName().equals(countryName)) {
            throw new InputException("the athlete isn't from this country.");
        } else if (!db.disciplineExists(discipline)) {
            throw new InputException("the discipline doesn't exist.");
        } else if (!athlete.doesDiscipline(discipline)) {
            throw new InputException("the athlete isn't listed for this combination of sport and discipline.");
        } else if (!athlete.addParticipation(discipline, year, gold, silver, bronze)) {
            throw new InputException("the athlete has already participated for this combination of sport and discipline in the given year.");
        }
    }

    public String printTable() throws InputException {
        if (!adminLoggedIn) {
            throw new InputException("there is no administrator logged in.");
        }
        
        List<TableEntry> result = new ArrayList<>();
        for (Country country : db.getCountries()) {
            TableEntry entryForCountry = new TableEntry(country);
            boolean add = false;
            for (Athlete athlete : db.getAthletesByCountry(country)) {
                if (athlete.hasParticipations()) {
                    entryForCountry.addMedals(athlete.getNumberPerMedal());
                    add = true;
                }
            }
            if (add) {
                result.add(entryForCountry);
            }
        }
        
        Collections.sort(result);
        StringJoiner joiner = new StringJoiner("\n");
        for (int i = 0; i < result.size(); i++) {
            joiner.add("(" + (i + 1) + "," + result.get(i).toString() + ")");
        }
        
        return joiner.toString();
    }

    public void reset() throws InputException {
        if (!adminLoggedIn) {
            throw new InputException("there is no administrator logged in.");
        }
        
        db.reset();
    }

    private final class SummaryAthlete extends Person implements Comparable<SummaryAthlete> {
        private String id;
        private int numberOfMedals;
        
        private SummaryAthlete(Athlete athlete, Discipline discipline) {
            super(athlete.getFirstName(), athlete.getLastName());
            this.id = athlete.getId();
            this.numberOfMedals = athlete.getNumberOfMedals(discipline);
        }

        @Override
        public int compareTo(SummaryAthlete o) {
            if (numberOfMedals == o.numberOfMedals) {
                return id.compareTo(o.id);
            } else {
                return Integer.compare(numberOfMedals, o.numberOfMedals) * -1;
            }
        }

        @Override
        public String toString() {
            return id + " " + getFirstName() + " " + getLastName() + " " + numberOfMedals;
        }
    }
    
    private final class TableEntry implements Comparable<TableEntry> {
        private Country country;
        private int gold;
        private int silver;
        private int bronze;
        private int total;

        private TableEntry(Country country) {
            this.country = country;
        }
        
        private void addMedals(int[] medals) {
            gold += medals[0];
            silver += medals[1];
            bronze += medals[2];
            total += medals[3];
        }

        @Override
        public int compareTo(TableEntry o) {
            if (gold == o.gold) {
                if (silver == o.silver) {
                    if (bronze == o.bronze) {
                        return country.getId().compareTo(o.country.getId());
                    } else {
                        return Integer.compare(bronze, o.bronze) * -1;
                    }
                } else {
                    return Integer.compare(silver, o.silver) * -1;
                }
            } else {
                return Integer.compare(gold, o.gold) * -1;
            }
        }
        
        @Override
        public String toString() {
            return country.getId() + "," + country.getCode() + "," + country.getName() + "," + gold + "," + silver + "," + bronze + "," + total;
        }
    }
}