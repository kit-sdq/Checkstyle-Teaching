public class Album {
    private static int albumCounter = 0;

    private final int id;
    private String name;
    private Track[] tracks;
    private Date releaseDate;
    private Genre genre;

    public Album(String name, Track[] tracks, Date releaseDate, Genre genre) {
        this.id = albumCounter++;
        this.name = name;
        this.tracks = tracks;
        this.releaseDate = releaseDate;
        this.genre = genre;
    }

    // getter to get the ID for e.g. database work
    public int getId() {
        return id;
    }

    // no setter for ID because it can't and shouldn't change

    // getter because you want to get the name for printing and stuff
    public String getName() {
        return name;
    }

    // no setter as the name can't change after release

    // getter to get the list of tracks for printing and stuff
    public Track[] getTracks() {
        return tracks;
    }

    // no setter as the tracklist can't change after release

    // getter for e.g. database work
    public Date getReleaseDate() {
        return releaseDate;
    }

    // no setter as the release date can't change after release

    // getter to get the genre for e.g. database work
    public Genre getGenre() {
        return genre;
    }

    // no setter as the genre can't change after release


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Album album = (Album) o;
        return id == album.id;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Album "
                + "id=" + id
                + ", name='" + name + "\'"
                + ", releaseDate=" + releaseDate
                + ", genre=" + genre
                + ", tracks: ");

        for (Track track : tracks) {
            result.append(track.toString()).append(" ");
        }

        return result.toString().substring(0, result.length() - 1);
    }
}
