package ch.epfl.swissteam.services;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;


/**
 * A fragment to create a new spontaneous post.
 *
 * @author Adrian Baudat
 */
public class CreatePostFragment extends Fragment implements View.OnClickListener{

    public CreatePostFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new {@link CreatePostFragment}.
     * @return new instance of <code>CreatePostFragment</code>
     */
    public static CreatePostFragment newInstance() {
        CreatePostFragment fragment = new CreatePostFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View frag = inflater.inflate(R.layout.fragment_create_post, container, false);
        ((Button)frag.findViewById(R.id.button_createpostfragment_send)).setOnClickListener(this);
        return frag;
    }

    @Override
    public void onClick(View v) {
        EditText titleField = ((EditText)getView().findViewById(R.id.plaintext_createpostfragment_title));
        EditText bodyField = ((EditText)getView().findViewById(R.id.plaintext_createpostfragment_body));
        if(TextUtils.isEmpty(titleField.getText())) {
            Toast.makeText(getActivity(), R.string.createpostfragment_titleempty, Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(bodyField.getText())) {
            Toast.makeText(getActivity(), R.string.createpostfragment_bodyempty, Toast.LENGTH_SHORT).show();
        }
        else {
            String title = titleField.getText().toString();
            String body = bodyField.getText().toString();

            DBUtility.get().getUser(GoogleSignInSingleton.get().getClientUniqueID(), user -> {
                Post post = new Post(title, user.getName_(), body, (new Date()).getTime());

                post.addToDB(DBUtility.get().getDb_());
            });

            ((MainActivity) getActivity()).showHomeFragment();
        }
    }
}
