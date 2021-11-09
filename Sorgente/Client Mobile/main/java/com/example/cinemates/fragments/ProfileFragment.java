package com.example.cinemates.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.example.cinemates.activities.MainActivity;
import com.example.cinemates.control.AuthHandler;
import com.example.cinemates.control.Controller;
import com.example.cinemates.control.ListenerDispenser;
import com.example.cinemates.data.FirebaseStorageHandler;
import com.example.cinemates.GlideApp;
import com.example.cinemates.model.AppUser;
import com.example.cinemates.helper.InputChecker;
import com.example.cinemates.R;
import com.facebook.AccessToken;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.UserInfo;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    private static final String NAME_KEY = "name";
    private static final String SURNAME_KEY = "surname";

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Uri image_uri;


    @Override
    public void onCreate (Bundle savedInstanceState) {
        if (AuthHandler.getCurrentUserInstance() == null) {
            AuthHandler.setCurrentUserInstance(new AppUser());
        }
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu (@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_logout, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            AuthHandler.logout((MainActivity) getActivity());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if (AuthHandler.getCurrentUserInstance().isValid()) {
            setPermissions();
            initViews(view);
            ListenerDispenser.attachUserProfileListener(this);
        }

        return view;

    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        ListenerDispenser.detachUserProfileListener();
    }

    private void setPermissions () {
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }
    private void initViews (View inflatedView) {

        TextView nomeView = inflatedView.findViewById(R.id.nome_textView);
        TextView cognomeView = inflatedView.findViewById(R.id.cognome_textView);
        TextView emailView = inflatedView.findViewById(R.id.email_textView);

        nomeView.setText(AuthHandler.getCurrentUserInstance().getName());
        cognomeView.setText(AuthHandler.getCurrentUserInstance().getSurname());
        emailView.setText(AuthHandler.getCurrentUserInstance().getEmail());

        FloatingActionButton editButton = inflatedView.findViewById(R.id.edit_button);
        editButton.setOnClickListener(v -> showEditProfileDialog());

        ImageView imgView = inflatedView.findViewById(R.id.propic_imgView);
        initImgView(imgView);

    }
    private void initImgView (ImageView imgView) {

        String uri;

        if (!AuthHandler.getCurrentUserInstance().hasPropic()) {
            uri = uploadFacebookPropicIfAvailable();

            GlideApp
            .with(getContext())
            .load(uri)
            .apply(new RequestOptions().error(R.drawable.user_placeholder).override(130, 130))
            .into(imgView);
        }
        else {
            GlideApp
            .with(getContext())
            .load(AuthHandler.getCurrentUserInstance().getPropic())
            .apply(new RequestOptions().override(130, 130))
            .into(imgView);
        }
    }
    private String uploadFacebookPropicIfAvailable () {

        if (AuthHandler.auth.getCurrentUser() == null) {
            return "";
        }

        final String FACEBOOK_GRAPH_DOMAIN = "https://graph.facebook.com/";
        final String FACEBOOK_PROVIDER_ID = "facebook.com";

        List<? extends UserInfo> userProviderData = AuthHandler.auth.getCurrentUser().getProviderData();

        String currUserId = AuthHandler.getCurrentUserInstance().getUid();
        String currUserFacebookId, accessToken;
        String uri = null;

        for(UserInfo profile : userProviderData) {
            if (FACEBOOK_PROVIDER_ID.equals(profile.getProviderId())) {
                currUserFacebookId = profile.getUid();
                accessToken = AccessToken.getCurrentAccessToken().getToken();
                uri = FACEBOOK_GRAPH_DOMAIN + currUserFacebookId + "/picture?height=130&width=130&access_token=" + accessToken;
                FirebaseStorageHandler.uploadThenSetUserPictureFromFacebook(currUserId, uri);
                break;
            }
        }

        return uri;

    }


    @Override
    public void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        String uid = AuthHandler.getCurrentUserInstance().getUid();
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = data.getData();
                FirebaseStorageHandler.uploadThenSetUserPicture(uid, image_uri.toString());
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE)
                FirebaseStorageHandler.uploadThenSetUserPicture(uid, image_uri.toString());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestStoragePermission() {
        requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean storageGranted = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean cameraGranted = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        return storageGranted && cameraGranted;
    }
    private void requestCameraPermission() {
        requestPermissions(cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted)
                        pickFromCamera();
                    else
                        Toast.makeText(getActivity(), "Per proseguire, abilita l'accesso alla fotocamera e all'archivio", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted)
                        pickFromGallery();
                    else
                        Toast.makeText(getActivity(), "Per proseguire, abilita l'accesso all'archivio", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void showEditProfileDialog () {

        final String[] options = {"Modifica foto profilo", "Modifica nome", "Modifica cognome", "Modifica password"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        builder.setTitle("Scegli cosa modificare");
        builder.setItems(options, (dialog, which) -> {

            if (which == 0) {
                showImagePickDialog();
            }
            else if (which == 1) {
                showNameUpdateDialog(NAME_KEY);
            }
            else if (which == 2) {
                showNameUpdateDialog(SURNAME_KEY);
            }
            else if (which == 3) {
                showPasswordUpdateDialog();
            }

        });

        builder.create().show();

    }

    private void showImagePickDialog () {

        String[] options = {"Fotocamera", "Galleria"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);

        builder.setTitle("Prendi immagine da");
        builder.setItems(options, (dialog, which) -> {

            if (which == 0) {
                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else {
                    pickFromCamera();
                }
            }
            else if (which == 1) {
                if (!checkStoragePermission()) {
                    requestStoragePermission();
                }
                else {
                    pickFromGallery();
                }
            }

        });

        builder.create().show();

    }
    private void showNameUpdateDialog (String key) {

        EditText editText = new EditText(getActivity());
        editText.setTextColor(Color.BLACK);
        editText.setHintTextColor(Color.LTGRAY);

        if (NAME_KEY.equals(key)) {
            editText.setText(InputChecker.capitalizeNames(AuthHandler.getCurrentUserInstance().getName()));
        }
        else if (SURNAME_KEY.equals(key)){
            editText.setText(InputChecker.capitalizeNames(AuthHandler.getCurrentUserInstance().getSurname()));
        }
        else
            throw new IllegalArgumentException("Key must be either \"name\" or \"surname\"");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        builder.setTitle("Modifica il tuo " + (NAME_KEY.equals(key) ? "nome" : "cognome"));

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(editText);
        builder.setView(linearLayout);

        builder.setPositiveButton("Modifica", (dialog, which) -> {

            String newName = editText.getText().toString().trim();

            if (InputChecker.isName(editText, newName))
                Controller.getUserDataHandler().updateUserName(ProfileFragment.this, AuthHandler.getCurrentUserInstance().getUid(), key, newName);
            else
                Toast.makeText(getContext(), "Modifica annullata: nome non valido", Toast.LENGTH_SHORT).show();

        });
        builder.setNegativeButton("Annulla", (dialog, which) -> dialog.dismiss());

        builder.create().show();

    }
    private void showPasswordUpdateDialog () {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        builder.setTitle("Modifica la tua password");

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        EditText editText = new EditText(getActivity());
        editText.setHint("Inserisci la nuova password");

        editText.setTextColor(Color.BLACK);
        editText.setHintTextColor(Color.LTGRAY);

        linearLayout.addView(editText);
        builder.setView(linearLayout);

        builder.setPositiveButton("Modifica", (dialog, which) -> {

            String newPassword = editText.getText().toString().trim();
            if (InputChecker.isPassword(editText, newPassword)) {
                AuthHandler.changeUserPassword(ProfileFragment.this, newPassword);
            }
            else {
                Toast.makeText(getContext(), "Modifica annullata: password non valida", Toast.LENGTH_SHORT).show();
            }

        });
        builder.setNegativeButton("Annulla", (dialog, which) -> dialog.dismiss());

        builder.create().show();

    }


    private void pickFromCamera () {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");

        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

    }
    private void pickFromGallery () {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

}