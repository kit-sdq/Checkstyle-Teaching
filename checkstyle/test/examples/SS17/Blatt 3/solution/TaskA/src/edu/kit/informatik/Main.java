package edu.kit.informatik;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        Main main = new Main();

        if (!main.checkArgs(args)) {
            return;
        }

        List<Job> jobs = main.parseFile(args[0]);
        WaitingArea area = main.determineWaitingArea(args);

        int timeSlice = 0;
        if (args.length == 3) {
            timeSlice = main.parseTimeSlice(args[2]);
        }

        if (jobs != null && area != null && timeSlice != -1) {
            //Sort by time of arrival that it is only necessary to check the head of the list.
            jobs.sort(Comparator.comparingInt(Job::getTimeOfArrival));
            String[] output = new OperatingUnit(jobs, area, timeSlice).simulate();

            for (String line : output) {
                Terminal.printLine(line);
            }
        }
    }

    private boolean checkArgs(String[] args) {
        if (args.length < 1 || args.length > 3) {
            Terminal.printError("unexpected number of parameters.");
        } else if (args.length >= 2 && !args[1].startsWith("waitingarea=")) {
            Terminal.printError("expected waitingarea as second parameter.");
        } else if (args.length >= 2 && args[1].equals("waitingarea=rr")) {
            if (args.length != 3) {
                Terminal.printError("timeslice parameter missing.");
            } else if (!args[2].startsWith("timeSlice=")) {
                Terminal.printError("expected timeSlice as third parameter.");
            } else {
                return true;
            }
        } else if (args.length == 3) {
            Terminal.printError("expected no third parameter.");
        } else {
            return true;
        }

        return false;
    }

    private WaitingArea determineWaitingArea(String[] args) {
        if (args.length == 1) {
            return new FIFOWaitingArea();
        }

        String type = args[1].replace("waitingarea=", "");
        switch (type) {
            case "fifo":
                return new FIFOWaitingArea();
            case "lifo":
                return new LIFOWaitingArea();
            case "sjf":
                return new SJFWaitingArea();
            case "rr":
                return new FIFOWaitingArea();
            default:
                Terminal.printError("unknown type of waiting area.");
                return null;
        }
    }

    private int parseTimeSlice(String timeSliceParam) {
        int timeSlice = 0;

        try {
            timeSlice = Integer.parseInt(timeSliceParam.replace("timeSlice=", ""));
        } catch (NumberFormatException e) {
            Terminal.printError("timeslice is not a number.");
            return -1;
        }

        if (timeSlice < 1) {
            Terminal.printError("timeslice can't be zero or less.");
            return -1;
        }

        return timeSlice;
    }

    private List<Job> parseFile(String path) {
        String[] lines = readFile(path);
        if (lines == null) {
            return null;
        }

        if (lines.length == 0) {
            Terminal.printError("file is empty.");
            return null;
        }

        List<Job> jobs = new ArrayList<Job>();
        for (int i = 0; i < lines.length; i++) {
            String[] params = lines[i].split(",");
            if (params.length != 4) {
                printLineError(i, "illegally formatted.");
                return null;
            }

            String name = params[0];
            int arrivalTime = -1;
            int complexity = 0;

            if (name.isEmpty()) {
                printLineError(i, "the name can't be empty.");
                return null;
            }

            try {
                arrivalTime = Integer.parseInt(params[2]);
            } catch (NumberFormatException e) {
                printLineError(i, "the arrival time has to be a number.");
                return null;
            }

            if (arrivalTime < 0) {
                printLineError(i, "the arrival time can't be less than zero.");
                return null;
            }

            try {
                complexity = Integer.parseInt(params[3]);
            } catch (NumberFormatException e) {
                printLineError(i, "the complexity has to be a number.");
                return null;
            }

            if (complexity < 1) {
                printLineError(i, "the complexity can't be zero or negative");
                return null;
            }

            switch (params[1]) {
                case "simple":
                    jobs.add(new SimpleJob(name, arrivalTime, complexity));
                    break;
                case "complex":
                    jobs.add(new ComplexJob(name, arrivalTime, complexity));
                    break;
                default:
                    printLineError(i, "the job type is not valid.");
                    return null;
            }
        }

        return jobs;
    }

    private void printLineError(int lineNumber, String message) {
        Terminal.printError("line " + (lineNumber + 1) + ": " + message);
    }

    private String[] readFile(String path) {
        FileReader fr;
        try {
            fr = new FileReader(path);
        } catch (FileNotFoundException e) {
            Terminal.printError("the file couldn't be found.");
            return null;
        }

        BufferedReader reader = new BufferedReader(fr);
        List<String> lines = new ArrayList<>();
        try {
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            Terminal.printError("the file couldn't be read correctly.");
            return null;
        }

        return lines.toArray(new String[lines.size()]);
    }
}
