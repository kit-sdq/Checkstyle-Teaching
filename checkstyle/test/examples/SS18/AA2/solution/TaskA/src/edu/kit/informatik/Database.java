package edu.kit.informatik;

import java.util.List;

public class Database {
    private List<Trie> lectures;

    public Database(List<Trie> lectures) {
        this.lectures = lectures;
    }

    public String create(String identifier) throws InputException {
        containsOrThrowException(identifier, false);
        this.lectures.add(new Trie(identifier));
        return "OK";
    }

    public String reset(String identifier) throws InputException {
        containsOrThrowException(identifier, true);
        lectures.get(lectures.indexOf(new Trie(identifier))).clear();
        return "OK";
    }

    public String add(String identifier, String name, int points) throws InputException {
        containsOrThrowException(identifier, true);
        Trie lecture = lectures.get(lectures.indexOf(new Trie(identifier)));
        lecture.add(name, points);
        return "OK";
    }

    public String modify(String identifier, String name, int points) throws InputException {
        containsOrThrowException(identifier, true);
        Trie lecture = lectures.get(lectures.indexOf(new Trie(identifier)));
        lecture.modify(name, points);
        return "OK";
    }

    public String delete(String identifier, String name) throws InputException {
        containsOrThrowException(identifier, true);
        Trie lecture = lectures.get(lectures.indexOf(new Trie(identifier)));
        lecture.delete(name);
        return "OK";
    }

    public String credits(String identifier, String name) throws InputException {
        containsOrThrowException(identifier, true);
        Trie lecture = lectures.get(lectures.indexOf(new Trie(identifier)));
        return String.valueOf(lecture.credits(name));
    }

    public String print(String identifier) throws InputException {
        containsOrThrowException(identifier, true);
        Trie lecture = lectures.get(lectures.indexOf(new Trie(identifier)));
        return lecture.print();
    }

    public String average(String identifier) throws InputException {
        containsOrThrowException(identifier, true);
        Trie lecture = lectures.get(lectures.indexOf(new Trie(identifier)));
        return String.valueOf(lecture.average());
    }

    public String median(String identifier) throws InputException {
        containsOrThrowException(identifier, true);
        Trie lecture = lectures.get(lectures.indexOf(new Trie(identifier)));
        return String.valueOf(lecture.median());
    }

    private void containsOrThrowException(String identifier, boolean contains) throws
            InputException {
        if (contains) {
            if (!lectures.contains(new Trie(identifier))) {
                throw new InputException("no trie with this identifier is in use");
            }
        } else {
            if (lectures.contains(new Trie(identifier))) {
                throw new InputException("lecture name is already in use");
            }

        }
    }
}
