package com.rmuti.cpe.rangsiman.busatkorat.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rmuti.cpe.rangsiman.busatkorat.R;

import java.util.Locale;


/**
 * Created by nuuneoi on 11/16/2014.
 */
public class SettingFragment extends Fragment {

    Button btn_language;
    Button btn_google_from;
    Button btn_facebook;
    Button btn_gmail;
    Button btn_credit;
    Button btn_conf;
    Button btn_cancel;
    TextView txt_language;
    TextView txt_setlanguage;

    public SettingFragment() {
        super();
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_setting, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    @SuppressWarnings("UnusedParameters")
    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        // Note: State of variable initialized here could not be saved
        //       in onSavedInstanceState

        btn_language = (Button) rootView.findViewById(R.id.btn_language);
        btn_google_from = (Button) rootView.findViewById(R.id.btn_googleform);
        btn_gmail = (Button) rootView.findViewById(R.id.btn_gmail);
        btn_facebook = (Button) rootView.findViewById(R.id.btn_facebook);
        btn_credit = (Button) rootView.findViewById(R.id.btn_credit);
        txt_language = (TextView) rootView.findViewById(R.id.txt_language);
        txt_setlanguage = (TextView) rootView.findViewById(R.id.txt_setlanguage);

       /* if(txt_setlanguage.getText().equals("Language")){
            Toast.makeText(getContext(), "True", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(), "False, " + txt_setlanguage.getText(), Toast.LENGTH_SHORT).show();
        }
*/
        btn_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.popup_confirm, null);
                btn_conf = (Button) mView.findViewById(R.id.btn_confirm);
                btn_cancel = (Button) mView.findViewById(R.id.btn_cancel);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                btn_conf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (txt_setlanguage.getText().equals("Language")) {
                            String languageToLoad = "th";
                            Locale locale = new Locale(languageToLoad);
                            Locale.setDefault(locale);
                            Configuration config = new Configuration();
                            config.locale = locale;
                            getContext().getResources().updateConfiguration(config, getParentFragment().getResources().getDisplayMetrics());
                            getActivity().recreate();
                            dialog.cancel();
                        }
                        else  {
                            String languageToLoad = "en_EN";
                            Locale locale = new Locale(languageToLoad);
                            Locale.setDefault(locale);
                            Configuration config = new Configuration();
                            config.locale = locale;
                            getContext().getResources().updateConfiguration(config, getParentFragment().getResources().getDisplayMetrics());
                            getActivity().recreate();
                            dialog.cancel();
                        }
                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });


            }
            });

        btn_google_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse("https://goo.gl/forms/NSLoM7vMyyWCJGl53");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        btn_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse("https://www.facebook.com/khun.rangsiman");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        btn_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","rangsiman.ch@hotmail.com", null));
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });

        btn_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse("https://www.facebook.com/pickiezilverlight");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });



    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance (Fragment level's variables) State here
    }

    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance (Fragment level's variables) State here
    }


}
