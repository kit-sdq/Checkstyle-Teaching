package edu.kit.informatik;

import edu.kit.informatik.aggregation.Aggregation;
import edu.kit.informatik.aggregation.AggregationType;
import edu.kit.informatik.goal.Goal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Tracker {
    private Date currentDay;
    private Set<Type> types;
    private Map<Date, Map<Type, Aggregation>> storage;
    private Map<Type, Goal> goals;
    
    public Tracker() {
        Date firstDay = new Date(0);
        this.currentDay = firstDay;
        
        Type pulse = new Type("pulse", AggregationType.MINMAX);
        this.types = new HashSet<>();
        this.types.add(pulse);

        Map<Type, Aggregation> firstDayRow = new TreeMap<>();
        firstDayRow.put(pulse, pulse.createAggregation());
        this.storage = new TreeMap<>();
        this.storage.put(firstDay, firstDayRow);
        
        this.goals = new HashMap<>();
    }

    public Date getCurrentDay() {
        return currentDay;
    }
    
    public Type getTypeByName(String name) throws InteractionException {
        return types.stream()
                .filter(type -> type.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new InteractionException(name + " can't be found."));
    }

    public void addType(String name, AggregationType aggregationType) throws InteractionException {
        Type newType = new Type(name, aggregationType);
        
        if (!types.add(newType)) {
            throw new InteractionException(name + " already exists.");
        }
        
        for (Map<Type, Aggregation> row : storage.values()) {
            row.put(newType, newType.createAggregation());
        }
    }
    
    public void addGoal(Type type, Goal goal) {
        goals.put(type, goal);
    }
    
    public void removeGoal(String typeName) throws InteractionException {
        Goal removed = goals.remove(new Type(typeName, null));
        if (removed == null) {
            throw new InteractionException("type doesn't exist or has no goal set.");
        }
    }
    
    public String recordValue(Type type, double value) throws InteractionException {
        Aggregation aggregation = storage.get(currentDay).get(type);
        
        if (aggregation == null) {
            throw new InteractionException("type doesn't exist.");    
        }
        
        aggregation.addValue(value);
        return aggregation.toString(); 
    }

    public String stateToString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Type, Aggregation> row : storage.get(currentDay).entrySet()) {
            sb.append(row.getValue().toString());
            sb.append("\n");
        }
        
        return sb.toString().trim();
    }
    
    public String progressToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(currentDay.toString());
        sb.append(System.lineSeparator());
        for (Map.Entry<Type, Aggregation> row : storage.get(currentDay).entrySet()) {
            sb.append(row.getValue().toString());
            sb.append(" - ");
            Goal current = goals.get(row.getKey());
            if (current != null) { 
                if (current.evaluate(row.getValue())) {
                    sb.append("goal reached");
                } else {
                    sb.append("goal not reached");
                }
            } else {
                sb.append("no goal");
            }
            sb.append("\n");
        }
        return sb.toString().trim();
    }

    public SortedSet<Map.Entry<Integer, Double>> getTopDays(Type type) {
        Comparator<Map.Entry<Integer, Double>> comparator = new MapValueComparator<>();
        Map<Integer, Double> highestValues = new HashMap<>();
        SortedSet<Map.Entry<Integer, Double>> sortedHighestValues = new TreeSet<>(comparator); 
        
        for (Map.Entry<Date, Map<Type, Aggregation>> day : storage.entrySet()) {
            if (day.getValue().get(type).getHighestValue() != null) {
                highestValues.put(day.getKey().getDay(), day.getValue().get(type).getHighestValue());
            }
            
            if (currentDay.equals(day)) {
                break;
            }
        }

        sortedHighestValues.addAll(highestValues.entrySet());
        return sortedHighestValues;
    }
    
    public Map<Goal, Integer> goalsReachedInWeek(int week) {
        List<Map.Entry<Type, Aggregation>> data = storage.entrySet().stream()
                .filter(entry -> entry.getKey().getWeek() == week)
                .map(entry -> entry.getValue().entrySet())
                .flatMap(Set::stream)
                .filter(entry -> goals.get(entry.getKey()) != null)
                .collect(Collectors.toList());
        
        Map<Goal, Integer> timesGoalReached = new HashMap<>();
        goals.forEach((key, value) -> timesGoalReached.put(value, 0));
        for (Map.Entry<Type, Aggregation> current : data) {
            Type type = current.getKey();
            Goal goal = goals.get(type);
            if (goal.evaluate(current.getValue())) {
                timesGoalReached.put(goal, timesGoalReached.get(goal) + 1);
            }
        }
        
        return timesGoalReached;
    }
    
    public String nextDay() {
        StringBuilder missingEntriesOutput = new StringBuilder();
        for (Map.Entry<Type, Aggregation> row : storage.get(currentDay).entrySet()) {
            if (!row.getValue().hasValue()) {
                missingEntriesOutput.append("no entry for ");
                missingEntriesOutput.append(row.getKey().getName());
                missingEntriesOutput.append("\n");
            }
        }
        
        if (!missingEntriesOutput.toString().isEmpty()) {
             return missingEntriesOutput.toString().trim();   
        }
        
        int goalsReached = 0;
        for (Map.Entry<Type, Aggregation> row : storage.get(currentDay).entrySet()) {
            Goal current = goals.get(row.getKey());
            if (current != null) {
                if (current.evaluate(row.getValue())) {
                    goalsReached++;
                }
            }
        }
        return "Goals reached: " + goalsReached + "/" + getGoalCount() + "\n"
                + setDay(currentDay.getDay() + 1);
    }

    public String setDay(int newDayNumber) {
        Date newDay = new Date(newDayNumber);
        
        if (storage.get(newDay) == null) {
            Map<Type, Aggregation> newDayRow = new HashMap<>();
            types.forEach(type -> newDayRow.put(type, type.createAggregation()));
            storage.put(newDay, newDayRow);
        }        
        
        currentDay = newDay;
        return currentDay.toString();
    }

    public String listGoals() {
        return goals.values().stream().map(Goal::toString).collect(Collectors.joining(System.lineSeparator()));
    }
    
    public int getGoalCount() {
        return goals.size();
    }
    
    public List<Double> slidingWindow(List<Double> input, int windowSize) {
        int newSize = input.size() - windowSize + 1;
        List<Double> result = new ArrayList<>(newSize);
        for (int i = 0; i < newSize; i++) {
            double sum = 0.0;
            for (int j = 0; j < windowSize; j++) {
                sum += input.get(i + j);
            }
            result.add(sum / windowSize);
        }
        
        return result;
    }
    
    public List<Double> normalize(List<Double> input, int lower, int upper) throws InteractionException {
        if (lower >= upper) {
            throw new InteractionException("lower bound is equal to or greater than upper bound.");
        }
        
        Double min = input.stream().min(Helper::compare)
                .orElseThrow(() -> new IllegalArgumentException("input is empty."));
        Double max = input.stream().max(Helper::compare)
                .orElseThrow(() -> new IllegalArgumentException("input is empty."));
        
        List<Double> result = new ArrayList<>(input.size());
        for (Double current : input) {
            result.add((lower * max - lower * current - upper * min + upper * current) / (max - min));
        }
        
        return result;
    }
    
    public List<Integer> detectPeaks(List<Double> input, double threshold, int duration) {
        int exceedingCount = 0;
        int exceedingIndex = -1;
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            Double current = input.get(i);
            if (Helper.greaterThan(current, threshold)) {
                exceedingCount++;
                if (exceedingIndex == -1) {
                    exceedingIndex = i;
                }
            } else {
                if (exceedingCount >= duration) {
                    result.add(exceedingIndex);
                }
                exceedingCount = 0;
                exceedingIndex = -1;
            }
        }

        if (exceedingCount >= duration) {
            result.add(exceedingIndex);
        }

        return result;
    }
    
    public String recordPulse(List<Double> input) throws InteractionException {
        List<Double> temp = slidingWindow(input, 3);
        temp = normalize(temp, 0, 1);
        List<Integer> peaks = detectPeaks(temp, 0.7, 2);
        
        List<Integer> peakDistances = new ArrayList<>(peaks.size() - 1);
        for (int i = 0; i < peaks.size() - 1; i++) {
            peakDistances.add(peaks.get(i + 1) - peaks.get(i));
        }
        
        Double size = new Integer(peakDistances.size()).doubleValue();
        Double averagePeakDistance = peakDistances.stream().mapToInt(Integer::intValue).sum() / size;
        Double heartRate = 1.0 / (averagePeakDistance * (1.0 / 30.0)) * 60.0;
        
        return "pulse frequency: " + Helper.formatDouble(heartRate) + "\n"
                + recordValue(getTypeByName("pulse"), heartRate);
    }
}
