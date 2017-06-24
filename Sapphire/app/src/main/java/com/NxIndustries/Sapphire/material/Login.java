package com.NxIndustries.Sapphire.material;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NxIndustries.Sapphire.R;
import com.NxIndustries.Sapphire.preferences.Features_Review;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusShare;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by ry on 2/14/17.
 */

public class Login extends AppCompatActivity {

    private static final int CONTENT_VIEW_ID = 10101010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        if (savedInstanceState == null) {
            Fragment newFragment = new FragmentFour();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(CONTENT_VIEW_ID, newFragment).commit();
        }
    }

    public class FragmentFour extends Fragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

        private static final String TAG = "SignInActivity";
        private static final int RC_SIGN_IN = 9001;

        private GoogleApiClient mGoogleApiClient;
        private TextView mStatusTextView;
        private RoundedImageView mProfileImageView;
        private ProgressDialog mProgressDialog;
        private Toolbar toolbar;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.google_main, container, false);
            setHasOptionsMenu(true);

            mStatusTextView = (TextView) v.findViewById(R.id.status);
            mProfileImageView = (RoundedImageView) v.findViewById(R.id.pro_image);
            toolbar = (Toolbar) v.findViewById(R.id.toolbar);

            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));

            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setTitle("Google Plus Login");
            activity.getSupportActionBar().setSubtitle("Google Services Enabling");

            v.findViewById(R.id.sign_in_button).setOnClickListener(this);
            v.findViewById(R.id.sign_out_button).setOnClickListener(this);
            v.findViewById(R.id.disconnect_button).setOnClickListener(this);

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .addApi(Plus.API)
                    .build();

            SignInButton signInButton = (SignInButton) v.findViewById(R.id.sign_in_button);
            signInButton.setSize(SignInButton.SIZE_STANDARD);
            signInButton.setScopes(gso.getScopeArray());

            return v;
        }

        @Override
        public void onStart() {
            super.onStart();
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                Log.d(TAG, "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                showProgressDialog();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(GoogleSignInResult googleSignInResult) {
                        hideProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });
            }
        }

        @Override
        public void onStop() {
            super.onStop();
            mGoogleApiClient.disconnect();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }

        private void handleSignInResult(GoogleSignInResult result) {
            Log.d(TAG, "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
                Uri uri = acct.getPhotoUrl();
                Picasso.with(getActivity())
                        .load(uri)
                        .placeholder(android.R.drawable.sym_def_app_icon)
                        .error(android.R.drawable.sym_def_app_icon)
                        .into(mProfileImageView);

                updateUI(true);
            } else {
                updateUI(false);
            }
        }

        private void signIn() {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }

        private void signOut() {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            updateUI(false);
                        }
                    });
        }

        private void revokeAccess() {
            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            updateUI(false);
                        }
                    });
        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.d(TAG, "onConnectionFailed:" + connectionResult);
        }

        private void showProgressDialog() {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setMessage(getString(R.string.loading));
                mProgressDialog.setIndeterminate(true);
            }

            mProgressDialog.show();
        }

        private void hideProgressDialog() {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.hide();
            }
        }

        private void updateUI(boolean signedIn) {
            if (signedIn) {
                getActivity().findViewById(R.id.sign_in_button).setVisibility(View.GONE);
                getActivity().findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
            } else {
                mStatusTextView.setText(R.string.signed_out);

                getActivity().findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sign_in_button:
                    signIn();
                    break;
                case R.id.sign_out_button:
                    signOut();
                    break;
                case R.id.disconnect_button:
                    revokeAccess();
                    break;
            }
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            menu.clear();
            inflater.inflate(R.menu.google_menu, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.rate:
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.NxIndustries.Sapphire"));
                    startActivity(intent);
                    Toast.makeText(getActivity(), "Your feedback is very much appreciated",
                            Toast.LENGTH_LONG).show();
                    return true;
                case R.id.share:
                    Intent shareIntent = new PlusShare.Builder(getActivity())
                            .setType("text/plain")
                            .setText("Sapphire Launcher by GocalDS, give it a shot!")
                            .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.NxIndustries.Sapphire"))
                            .getIntent();
                    startActivityForResult(shareIntent, 0);
                    return true;
                case R.id.plusOne:
                    Intent plusOne = new Intent(getActivity(), Features_Review.class);
                    startActivity(plusOne);
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
