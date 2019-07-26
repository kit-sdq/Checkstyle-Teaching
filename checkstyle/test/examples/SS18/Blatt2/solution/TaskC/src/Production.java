public class Production {
    private final Track trackProduced;
    private RecordingStudio studio;
    private Person producer;
    // production plan is implicitely modulated in the production
    private final Date start;
    private Date end;
    private Person[] participatingPersons;

    public Production(Track trackProduced, RecordingStudio studio, Person producer, 
                      Date start, Date end, Person[] participatingPersons) {
        this.trackProduced = trackProduced;
        this.studio = studio;
        this.producer = producer;
        this.start = start;
        this.end = end;
        this.participatingPersons = participatingPersons;
    }

    // getter to work with the object in different program parts
    public Track getTrackProduced() {
        return trackProduced;
    }

    // no setter because the produced track can't change

    // getter to work with the object in different program parts
    public RecordingStudio getStudio() {
        return studio;
    }

    // setter because the studio can change while production if the previous one is bad
    public void setStudio(RecordingStudio studio) {
        this.studio = studio;
    }

    // getter to work with the object in different program parts
    public Person getProducer() {
        return producer;
    }

    // setter because the producer can change while production if the previous one is bad
    public void setProducer(Person producer) {
        this.producer = producer;
    }

    // getter to get information about the production start
    public Date getStart() {
        return start;
    }

    // no setter because the starting point can't really change

    // getter to get information about the production end
    public Date getEnd() {
        return end;
    }

    // setter because the deadline can shift
    public void setEnd(Date end) {
        this.end = end;
    }

    // getter to get information about participants
    public Person[] getParticipatingPersons() {
        return participatingPersons;
    }

    // setter because the participants can change ofer time
    public void setParticipatingPersons(Person[] participatingPersons) {
        this.participatingPersons = participatingPersons;
    }

    // own equals because one producer (person) can be participating in multiple productions so the person equals isn't
    // enough for uniqueness, but the track is, because each track is only produced once.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Production that = (Production) o;
        return trackProduced.equals(that.trackProduced);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Production "
                + "trackProduced=" + trackProduced
                + ", studio=" + studio
                + ", producer=" + producer
                + ", start=" + start
                + ", end=" + end
                + ", participatingPersons: ");

        for (Person person : participatingPersons) {
            result.append(person.toString()).append(" ");
        }

        return result.toString().substring(0, result.length() - 1);
    }
}
