package com.kaltura.playkit.backend.ovp.services;

import android.net.Uri;

import com.kaltura.playkit.connect.RequestBuilder;

import java.util.Date;

/**
 * Created by zivilan on 22/11/2016.
 */

public class StatsService {
    public static RequestBuilder sendStatsEvent(String baseUrl, int partnerId, int eventType, String clientVer, long duration,
                                                String sessionId, long position, String uiConfId, String entryId, String widgetId, String kalsig, boolean isSeek, String referrer) {
        return new RequestBuilder()
                .method("GET")
                .url(getOvpUrl(baseUrl, partnerId, eventType, clientVer, duration, sessionId, position, uiConfId, entryId, widgetId, kalsig, isSeek, referrer))
                .tag("stats-send");
    }

    private static String getOvpUrl(String baseUrl, int partnerId, int eventType, String clientVer, long duration,
                                    String sessionId, long position, String uiConfId, String entryId, String widgetId, String kalsig, boolean isSeek, String referrer) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority(baseUrl)
                .path("/api_v3/index.php")
                .appendQueryParameter("service", "stats")
                .appendQueryParameter("apiVersion", "3.1")
                .appendQueryParameter("expiry", "86400")
                .appendQueryParameter("clientTag", "kwidget:v" + clientVer)
                .appendQueryParameter("format", "1")
                .appendQueryParameter("ignoreNull", "1")
                .appendQueryParameter("action", "collect")
                .appendQueryParameter("event:eventType", Integer.toString(eventType))
                .appendQueryParameter("event:clientVer", clientVer)
                .appendQueryParameter("event:currentPoint", Long.toString(position))
                .appendQueryParameter("event:duration", Long.toString(duration))
                .appendQueryParameter("event:eventTimeStamp", Long.toString(new Date().getTime()))
                .appendQueryParameter("event:isFirstInSession", "false")
                .appendQueryParameter("event:objectType", "KalturaStatsEvent")
                .appendQueryParameter("event:partnerId", Integer.toString(partnerId))
                .appendQueryParameter("event:sessionId", sessionId)
                .appendQueryParameter("event:uiconfId", uiConfId)
                .appendQueryParameter("event:seek", Boolean.toString(isSeek))
                .appendQueryParameter("event:entryId", entryId)
                .appendQueryParameter("event:widgetId", widgetId)
                .appendQueryParameter("event:referrer", referrer)
                .appendQueryParameter("kalsig", kalsig);


        return builder.build().toString();
    }
}
