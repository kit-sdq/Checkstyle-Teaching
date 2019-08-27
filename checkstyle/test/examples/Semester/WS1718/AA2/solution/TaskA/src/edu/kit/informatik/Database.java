package edu.kit.informatik;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<Administrator> admins;
    private List<Venue> venues;
    private List<Country> countries;
    private List<Discipline> disciplines;
    private List<Athlete> athletes;
    
    public Database() {
        admins = new ArrayList<>();
        venues = new ArrayList<>();
        countries = new ArrayList<>();
        disciplines = new ArrayList<>();
        athletes = new ArrayList<>();
    }
    
    public Country getCountryByName(String name) {
        for (Country current : countries) {
            if (current.getName().equals(name)) {
                return current;
            }
        }
        
        return null;
    }

    public Athlete getAthleteByDummy(Athlete dummyAthlete) {
        for (Athlete current : athletes) {
            if (current.equals(dummyAthlete)) {
                return current;
            }
        }

        return null;
    }
    
    public Athlete getAthleteById(String id) {
        for (Athlete current : athletes) {
            if (current.getId().equals(id)) {
                return current;
            }
        }

        return null;
    }


    public List<Athlete> getAthletesByCountry(Country country) {
        List<Athlete> result = new ArrayList<>();
        for (Athlete current : athletes) {
            if (current.getCountry().equals(country)) {
                result.add(current);
            }
        }
        
        return result;
    }
    
    public boolean venueIdExists(String id) {
        for (Venue current : venues) {
            if (current.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }
    
    public boolean disciplineExists(Discipline discipline) {
        return disciplines.contains(discipline);
    }

    public boolean countryIdExists(String id) {
        for (Country current : countries) {
            if (current.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }
    
    public boolean countryCodeExists(String code) {
        for (Country current : countries) {
            if (current.getCode().equals(code)) {
                return true;
            }
        }

        return false;
    }

    public boolean athleteIdExists(String id) {
        for (Athlete current : athletes) {
            if (current.getId().equals(id)) {
                return true;
            }
        }

        return false;    
    }

    public void add(Administrator admin) {
        admins.add(admin);
    }

    public void add(Venue venue) {
        venues.add(venue);
    }

    public void add(Country country) {
        countries.add(country);
    }
    
    public void add(Discipline discipline) {
        disciplines.add(discipline);
    }
    
    public void add(Athlete athlete) {
        athletes.add(athlete);
    }
    
    public boolean contains(Administrator admin) {
        return admins.contains(admin);
    }

    public List<Administrator> getAdmins() {
        return admins;
    }

    public List<Venue> getVenues() {
        return venues;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }
    
    public List<Athlete> getAthletes() {
        return athletes;
    }
    
    public void reset() {
        venues = new ArrayList<>();
        countries = new ArrayList<>();
        disciplines = new ArrayList<>();
        athletes = new ArrayList<>();
    }
}
