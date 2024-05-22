package com.gadarts.parashoot.android;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import com.android.billingclient.api.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.AndroidGraphics;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.android.aidl.com.example.android.trivialdrivesample.util.IabHelper;
import com.gadarts.parashoot.model.GGSActionResolver;
import com.gadarts.parashoot.model.PurchaseItem;
import com.gadarts.parashoot.model.SavedGame;
import com.gadarts.parashoot.screens.MenuScreenImproved;
import com.gadarts.parashoot.utils.GameSettings;
import com.gadarts.parashoot.utils.Rules;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.*;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadata;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.tasks.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.android.billingclient.api.BillingClient.*;
import static com.gadarts.parashoot.android.AndroidLauncher.RC_SIGN_IN;
import static com.gadarts.parashoot.assets.Assets.Configs.*;
import static com.gadarts.parashoot.assets.Assets.Configs.Preferences.Settings.*;
import static com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN;

/**
 * Implementation of the GGS action resolver.
 */
class GGSActionResolverImplementation implements GGSActionResolver {
    private final BillingClient billingClient;
    private AchievementsClient mAchievementsClient;
    private LeaderboardsClient mLeaderboardsClient;
    private EventsClient mEventsClient;
    private PlayersClient mPlayersClient;

    /**
     * The game's android activity.
     */
    private AndroidLauncher context;

    /**
     * Handler for the IAB.
     */
    private final IabHelper iabHandler;
    private final GoogleSignInClient googleClient;
    private SnapshotsClient snapshotsClient;
//
    /**
     * Whether to save game. True on default.
     */
    private boolean shouldSaveGame = true;
    private MenuScreenImproved.CoinsButton onPurchaseSuccessSubscriber;

    public GGSActionResolverImplementation(AndroidLauncher androidLauncher, IabHelper iabHandler) {
        this.context = androidLauncher;
        this.iabHandler = iabHandler;
        GoogleSignInOptions signInOptions = new Builder(DEFAULT_GAMES_SIGN_IN).
                requestScopes(Drive.SCOPE_APPFOLDER).build();
        googleClient = GoogleSignIn.getClient(androidLauncher, signInOptions);
        billingClient = newBuilder(androidLauncher).
                setListener(createPurchaseListener()).build();
    }

    @NonNull
    private PurchasesUpdatedListener createPurchaseListener() {
        return new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
                if (responseCode == BillingResponse.OK) {
                    consume(purchases.get(0));
                }
            }
        };
    }

    @Override
    public boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(context) != null;
    }

    @Override
    public void logout() {
        Log.d(TAG, "signOut()");
        if (!isSignedIn()) {
            Log.w(TAG, "signOut() called, but was not signed in!");
            return;
        }
        googleClient.signOut().addOnCompleteListener(context, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                boolean successful = task.isSuccessful();
                Log.d(TAG, "signOut(): " + (successful ? "success" : "failed"));
                onDisconnected();
            }
        });
    }

    @Override
    public void rateGame() {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_URL)));
    }

    @Override
    public void login() {
        context.startActivityForResult(googleClient.getSignInIntent(), RC_SIGN_IN);
    }

    @Override
    public void submitScore(int score) {
        if (!GameSettings.SEND_SCORE && isSignedIn()) return;
        mLeaderboardsClient.submitScore(LEADERBOARDS_ID, score);
    }

    @Override
    public void unlockAchievement(String achievementId) {

    }

    @Override
    public void showLeaderBoard() {
        if (!isSignedIn()) {
            context.toast("Please log-in using Google account");
            return;
        }
        mLeaderboardsClient.getLeaderboardIntent(LEADERBOARDS_ID)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        context.startActivityForResult(intent, context.RC_UNUSED);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String text = "There was an issue communicating with leaderboards.";
                handleException(e, text);
                context.toast(text);
            }
        });
    }

    @Override
    public void getAchievements() {

    }

    @Override
    public void showSavedGamesUI() {

    }

    @Override
    public void saveSnapshot(final Image savingAnimation) {
        if (!Parastrike.getGGS().isSignedIn()) return;
        int policy = SnapshotsClient.RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED;
        Task<SnapshotsClient.DataOrConflict<Snapshot>> open = snapshotsClient.open(Rules.System.SAVED_GAME_NAME, true, policy);
        open.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to saving game to Google Games account!", e);
            }
        });
        open.addOnCompleteListener(new OnCompleteListener<SnapshotsClient.DataOrConflict<Snapshot>>() {
            @Override
            public void onComplete(@NonNull Task<SnapshotsClient.DataOrConflict<Snapshot>> task) {
                try {
                    Snapshot snapshot = task.getResult().getData();
                    Task<SnapshotMetadata> result = writeSnapshot(snapshot, SavedGame.serialize(Parastrike.getPlayerStatsHandler().getPlayerStats()));
                    result.addOnCompleteListener(new OnCompleteListener<SnapshotMetadata>() {
                        @Override
                        public void onComplete(@NonNull Task<SnapshotMetadata> task) {
                            if (savingAnimation != null) {
                                savingAnimation.setVisible(true);
                                savingAnimation.addAction(createDisappearAction(savingAnimation));
                            }
                            setShouldSaveGame(false);
                        }
                    });
                    result.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            context.toast("Failed to save game to Google Games account!");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void loadFromSnapshot() {
        int policy = SnapshotsClient.RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED;
        if (snapshotsClient != null) {
            Task<SnapshotsClient.DataOrConflict<Snapshot>> open = snapshotsClient.open(Rules.System.SAVED_GAME_NAME, true, policy);
            open.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Failed to load game to Google Games account!", e);
                }
            }).continueWith(new Continuation<SnapshotsClient.DataOrConflict<Snapshot>, byte[]>() {
                @Override
                public byte[] then(@NonNull Task<SnapshotsClient.DataOrConflict<Snapshot>> task) {
                    Snapshot snapshot = task.getResult().getData();
                    try {
                        return snapshot.getSnapshotContents().readFully();
                    } catch (IOException e) {
                        Log.e(TAG, "Error while reading Snapshot.", e);
                    }
                    return null;
                }
            }).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                @Override
                public void onComplete(@NonNull Task<byte[]> task) {
                    byte[] data = task.getResult();
                    if (data != null) {
                        try {
                            SavedGame savedGame = new SavedGame();
                            savedGame.setData(data);
                            Parastrike.getPlayerStatsHandler().applySavedGame(savedGame);
                            context.toast("Your progress has been restored from the cloud!");
                        } catch (Exception e) {
                            context.toast("Failed to restore progress from the cloud!");
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            context.toast("Failed to restore progress from the cloud! Try to login again...");
        }
    }

    @Override
    public boolean shouldSaveGame() {
        return shouldSaveGame;
    }

    private void onConnected(GoogleSignInAccount googleSignInAccount) {
        Log.d(TAG, "onConnected(): connected to Google APIs");
        GamesClient gamesClient = Games.getGamesClient(context, googleSignInAccount);
        gamesClient.setGravityForPopups(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        gamesClient.setViewForPopups(((AndroidGraphics) context.getGraphics()).getView());
        snapshotsClient = Games.getSnapshotsClient(context, googleSignInAccount);
        mAchievementsClient = Games.getAchievementsClient(context, googleSignInAccount);
        mLeaderboardsClient = Games.getLeaderboardsClient(context, googleSignInAccount);
        mEventsClient = Games.getEventsClient(context, googleSignInAccount);
        mPlayersClient = Games.getPlayersClient(context, googleSignInAccount);
        mPlayersClient.getCurrentPlayer().addOnCompleteListener(new OnCompleteListener<Player>() {
            @Override
            public void onComplete(@NonNull Task<Player> task) {
                if (task.isSuccessful()) {
                    String displayName = task.getResult().getDisplayName();
                    Parastrike.getPlayerStatsHandler().setPlayerName(displayName);
                } else {
                    String error = "There was an issue communicating with Google players.";
                    handleException(task.getException(), error);
                }
            }
        });

    }

    private void handleException(Exception e, String details) {
        int status = 0;
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            status = apiException.getStatusCode();
        }
        String message = String.format("%1$s (status %2$d). %3$s.", details, status, e);
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }

    public void onActivityResult(int requestCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                onConnected(account);
            } catch (ApiException apiException) {
                Preferences preferences = Gdx.app.getPreferences(PREF_SETTINGS);
                preferences.putBoolean(DONT_ASK_TO_LOGIN, true);
                preferences.flush();
                onDisconnected();
            }
        }
    }

    private void onDisconnected() {
        Log.d(TAG, "onDisconnected()");
        mAchievementsClient = null;
        mLeaderboardsClient = null;
        mPlayersClient = null;
    }

    public void signInSilently() {
        Log.d(TAG, "signInSilently()");
        googleClient.silentSignIn().addOnCompleteListener(context,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInSilently(): success");
                            onConnected(task.getResult());
                        } else {
                            Log.d(TAG, "signInSilently(): failure", task.getException());
                            onDisconnected();
                        }
                    }
                });
    }

    private DelayAction createDisappearAction(final Image savingImage) {
        return Actions.delay(4, new Action() {
            @Override
            public boolean act(float delta) {
                savingImage.setVisible(false);
                return true;
            }
        });
    }

    @Override
    public void setShouldSaveGame(boolean shouldSaveGame) {
        this.shouldSaveGame = shouldSaveGame;
    }

    @Override
    public void queryProducts(final ArrayList<String> list, final MenuScreenImproved.CoinsButton button) {
        onPurchaseSuccessSubscriber = button;
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(
                    @BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingResponse.OK) {
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(list).setType(SkuType.INAPP);
                    billingClient.querySkuDetailsAsync(params.build(), responseListener());
                }
            }

            @Override
            public void onBillingServiceDisconnected() {

            }

            @NonNull
            private SkuDetailsResponseListener responseListener() {
                return new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(int responseCode, List<SkuDetails> list) {
                        if (responseCode == BillingResponse.OK && list != null) {
                            HashMap<String, PurchaseItem> productsList = new HashMap<>();
                            for (SkuDetails details : list) {
                                productsList.put(details.getSku(), createPurchaseItem(details));
                            }
                            if (list.size() > 0)
                                button.createAndSetCoinsMonitorContent(productsList);
                        }
                    }

                    @NonNull
                    private PurchaseItem createPurchaseItem(SkuDetails skuDetails) {
                        return new PurchaseItem(skuDetails.getSku(),
                                skuDetails.getPrice(),
                                skuDetails.getPriceCurrencyCode(),
                                skuDetails.getTitle().replaceAll("\\(ParaStrike\\)", ""));

                    }
                };
            }

        });
    }

    @Override
    public void buy(String id) {
        BillingFlowParams flowParams = BillingFlowParams.newBuilder().setSku(id)
                .setType(SkuType.INAPP).build();
        int responseCode = billingClient.launchBillingFlow(context, flowParams);
        if (responseCode == BillingResponse.ITEM_ALREADY_OWNED) {
            handleIfAlreadyOwned(id);
        } else if (responseCode != BillingResponse.OK) {
            context.toast("An error occurred! No charges were made!");
        }
    }

    private void handleIfAlreadyOwned(String id) {
        List<Purchase> purchases = billingClient.queryPurchases(SkuType.INAPP).getPurchasesList();
        for (int i = 0; i < purchases.size(); i++) {
            Purchase purchase = purchases.get(i);
            if (purchase.getSku().equals(id)) {
                consume(purchase);
                return;
            }
        }
    }

    private void consume(final Purchase purchase) {
        billingClient.consumeAsync(purchase.getPurchaseToken(), new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(int responseCode, String purchaseToken) {
                if (responseCode == BillingResponse.OK) {
                    onPurchaseSuccessSubscriber.purchaseSuccess(purchase.getSku());
                    context.toast("You've purchased successfully!");
                }
            }
        });
    }


    /**
     * Generates metadata, takes a screenshot, and performs the write operation for saving a
     * snapshot.
     */
    private Task<SnapshotMetadata> writeSnapshot(Snapshot snapshot, byte[] savedGame) {
        snapshot.getSnapshotContents().writeBytes(savedGame);
        SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder().setDescription("Modified at: " + Calendar.getInstance().getTime()).build();
        SnapshotsClient snapshotsClient = Games.getSnapshotsClient(context, GoogleSignIn.getLastSignedInAccount(context));
        return snapshotsClient.commitAndClose(snapshot, metadataChange);
    }
}
