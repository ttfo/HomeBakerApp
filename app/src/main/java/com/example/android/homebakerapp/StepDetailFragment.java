package com.example.android.homebakerapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.net.Uri;
import android.os.Bundle;

import com.example.android.homebakerapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.URI;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link StepListActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment implements Player.EventListener {

//    public static final String ARG_ITEM_ID = "item_id"; //

    private static final String TAG = StepDetailActivity.class.getSimpleName();
    private Step mStep;
    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private LinearLayout mLL;
    private NotificationManager mNotificationManager;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    public StepDetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);

        mLL = (LinearLayout) rootView.findViewById(R.id.step_detail_ll);
        mPlayerView = (PlayerView) rootView.findViewById(R.id.step_video);

        // Show the dummy content as text in a TextView.
        if (mStep != null) {
            ((TextView) rootView.findViewById(R.id.step_description)).setText(mStep.getDescription());
            // ((ImageView) rootView.findViewById(R.id.step_thumbnail)).setText(mStep.getThumbnailURL()); // @TODO need to fix
            //((PlayerView) rootView.findViewById(R.id.step_video)).setText(mStep.getVideoURL()); // @TODO need to fix
        }

        // Initialize the Media Session.
        initializeMediaSession();
        // Init Exo Player
        initializePlayer(Uri.parse(mStep.getVideoURL()));

        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(getContext().getResources().getString(R.string.ARG_ITEM_ID))) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            Bundle bundle = getArguments();
            Log.i("BUNDLE", "MY BUNDLE VALUE: " + getContext().getResources().getString(R.string.ARG_ITEM_ID));
            mStep = (Step) bundle.getSerializable(getContext().getResources().getString(R.string.ARG_ITEM_ID));

            Log.i("STEP FRAG", "STEP: " + mStep.getShortDescription());

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mStep.getShortDescription());
            }
        }
    }


    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(mLL.getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }


    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }


    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        mMediaSession.setActive(false);
    }

    /**
     * Initialize ExoPlayer.
     * REF. https://stackoverflow.com/questions/31048475/creating-a-simple-instance-of-exoplayer &&
     * https://exoplayer.dev/hello-world.html
     * @param mp4VideoUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mp4VideoUri) {
        if (mExoPlayer == null) {

            // Create an instance of the ExoPlayer.
            Activity activity = getActivity(); // if you are in a fragment
            // Or,   activity = YourActivity.this;      if you are in an Activity
            SimpleExoPlayer mExoPlayer = new SimpleExoPlayer.Builder(activity).build();
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);
            // For the rest of the code in this method, ref.
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mLL.getContext(),
                    Util.getUserAgent(mLL.getContext(), "Home Baker App"));
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource =
                    new ProgressiveMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(mp4VideoUri);
            // Prepare the player with the source.
            mExoPlayer.prepare(videoSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        mNotificationManager.cancelAll();
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }


    /**
     * Media player methods
     */
    @Override
    public void onTimelineChanged(Timeline timeline, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}