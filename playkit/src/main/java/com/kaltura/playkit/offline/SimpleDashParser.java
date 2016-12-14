package com.kaltura.playkit.offline;

import android.net.Uri;
import android.util.Log;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.source.dash.manifest.AdaptationSet;
import com.google.android.exoplayer2.source.dash.manifest.Period;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by anton.afanasiev on 13/12/2016.
 *
 * A simple (limited) dash parser. Extracts Format and DrmInitData from the manifest and/or initialization chink.
 * Currently only reads the first Representation of the video AdaptationSet of the first Period.
 */

class SimpleDashParser {

    DrmInitData drmInitData;
    boolean hasContentProtection;
    Format format;  // format of the first Representation of the video AdaptationSet
    byte[] widevineInitData;

    SimpleDashParser parse(String localPath) throws IOException {

        InputStream inputStream = new BufferedInputStream(new FileInputStream(localPath));

        MediaPresentationDescriptionParser mpdParser = new MediaPresentationDescriptionParser();
        MediaPresentationDescription mpd = mpdParser.parse(localPath, inputStream);

        if (mpd.getPeriodCount() < 1) {
            throw new IOException("At least one period is required");
        }

        Period period = mpd.getPeriod(0);
        List<AdaptationSet> adaptationSets = period.adaptationSets;
        AdaptationSet videoAdaptation = adaptationSets.get(period.getAdaptationSetIndex(AdaptationSet.TYPE_VIDEO));

        List<Representation> representations = videoAdaptation.representations;

        if (representations == null || representations.isEmpty()) {
            throw new IOException("At least one video representation is required");
        }
        Representation representation = representations.get(0);

        format = representation.format;

        hasContentProtection = videoAdaptation.hasContentProtection();
        if (hasContentProtection) {
            loadDrmInitData(representation);
        }

        return this;
    }

    private void loadDrmInitData(Representation representation) throws IOException {
        Uri initFile = representation.getInitializationUri().getUri();

        FileDataSource initChunkSource = new FileDataSource();
        DataSpec initDataSpec = new DataSpec(initFile);
        int trigger = 2;
        ChunkExtractorWrapper extractorWrapper = new ChunkExtractorWrapper(new FragmentedMp4Extractor());
        InitializationChunk chunk = new InitializationChunk(initChunkSource, initDataSpec, trigger, format, extractorWrapper);
        try {
            chunk.load();
        } catch (InterruptedException e) {
            Log.d(TAG, "Interrupted!", e);
        }
        if (!chunk.isLoadCanceled()) {
            drmInitData = chunk.getDrmInitData();
        }

        if (drmInitData != null) {
            DrmInitData.SchemeInitData schemeInitData = OfflineDrmManager.getWidevineInitData(drmInitData);
            if (schemeInitData != null) {
                widevineInitData = schemeInitData.data;
            }
        }
    }
}
