package ch.epfl.swissteam.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileDisplayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileDisplayFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public ProfileDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileDisplayFragment.
     */
    public static ProfileDisplayFragment newInstance() {
        ProfileDisplayFragment fragment = new ProfileDisplayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_profile_display, container, false);

        Button button = (Button) thisView.findViewById(R.id.button_profiledisplay_modify);
        button.setOnClickListener(new View.OnClickListener()
        {
            Intent intent = new Intent(getActivity(), ProfileSettings.class);
            @Override
            public void onClick(View v)
            {
                startActivity(intent);

            }
        });

        String clientUniqueID = GoogleSignInSingleton.get().getClientUniqueID();
        
        loadAndShowUser(clientUniqueID);


        // Inflate the layout for this fragment
        return thisView;
    }

    private void loadAndShowUser(String clientUniqueID){
        //for now we use the username
        DBUtility.get().getUser(clientUniqueID, (user)->{
            TextView nameView = (TextView) getActivity().findViewById(R.id.textview_profiledisplay_name);
            nameView.setText(user.getName_());

            TextView surnameView =  (TextView) getActivity().findViewById(R.id.textview_displayprofile_surname);
            surnameView.setText(user.getSurname_());

            TextView emailView =  (TextView) getActivity().findViewById(R.id.textview_profiledisplay_email);
            emailView.setText(user.getEmail_());

            TextView descrView =  (TextView) getActivity().findViewById(R.id.textview_profiledisplay_description);
            descrView.setText(user.getDescription_());

        });
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
