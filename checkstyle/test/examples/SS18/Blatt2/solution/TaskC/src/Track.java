public class Track {
    private String name;
    private Artist artist;
    private int duration;

    public Track(String name, Artist artist, int duration) {
        this.name = name;
        this.artist = artist;
        this.duration = duration;
    }

    // getter e.g. for showing stuff in a music player
    public String getName() {
        return name;
    }

    // no setter because the name can't change after release

    // getter e.g. for showing stuff in a music player
    public Artist getArtist() {
        return artist;
    }

    // no setter because the artist can't change after release

    // getter e.g. for showing stuff in a music player
    public int getDuration() {
        return duration;
    }

    // no setter because the duration can't change after release


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Track track = (Track) o;
        return name.equals(track.name) && artist.equals(track.artist);
    }

    @Override
    public String toString() {
        return "Track "
                + "name=" + name + '\''
                + ", artist=" + artist
                + ", duration=" + duration;
    }
}
