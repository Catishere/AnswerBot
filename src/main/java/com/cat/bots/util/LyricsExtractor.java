package com.cat.bots.util;

import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;

public class LyricsExtractor {
    private String apiKey = "305e4de3d45a7db4cd721fbb0abd85f8";
    private MusixMatch musixMatch;

    public LyricsExtractor() {
        musixMatch = new MusixMatch(apiKey);
    }

    public String[] getLyrics(String artist, String song) throws MusixMatchException {
        System.out.println(artist + " " + song);
        Track track = musixMatch.getMatchingTrack(song, artist);
        TrackData trackdata = track.getTrack();
        Lyrics lyrics = musixMatch.getLyrics(trackdata.getTrackId());
        return lyrics.getLyricsBody().split("\n");
    }
}
