package com.kaltura.playkit.plugins;

import android.content.Context;

import com.kaltura.playkit.PKPlugin;
import com.kaltura.playkit.PlayKit;
import com.kaltura.playkit.Player;
import com.kaltura.playkit.PlayerConfig;
import com.kaltura.playkit.PlayerEvent;

/**
 * Created by zivilan on 02/11/2016.
 */

public class KalturaLiveStatisticsPlugin extends PKPlugin {
    private static final String TAG = "KalturaLiveStatisticsPlugin";

    public static final Factory factory = new Factory() {
        @Override
        public String getName() {
            return "KalturaLiveStatistics";
        }

        @Override
        public PKPlugin newInstance(PlayKit playKit) {
            return new KalturaLiveStatisticsPlugin();
        }
    };

    @Override
    protected void load(Player player, PlayerConfig playerConfig, Context context) {
        player.addEventListener(mEventListener);
    }

    @Override
    protected void update(Player player, PlayerConfig playerConfig, Context context) {

    }

    @Override
    public void release() {

    }

    private PlayerEvent.Listener mEventListener = new PlayerEvent.Listener() {
        @Override
        public void onPlayerEvent(Player player, PlayerEvent event) {
            switch (event){
                case CAN_PLAY:

                    break;
                case DURATION_CHANGE:

                    break;
                case ENDED:

                    break;
                case ERROR:

                    break;
                case LOADED_METADATA:

                    break;
                case PAUSE:

                    break;
                case PLAY:

                    break;
                case PLAYING:

                    break;
                case SEEKED:

                    break;
                case SEEKING:

                    break;
                default:

                    break;
            }
        }
    };
}
