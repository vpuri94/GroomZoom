package fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.groomzoom.Browse;
import com.example.groomzoom.BrowseAdapter;
import com.example.groomzoom.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class BrowseFragment extends Fragment {


    private RecyclerView rvUsers;
    private BrowseAdapter browseAdapter;
    private List<Browse> allBrowse;
    private String TAG = "HI";

    public BrowseFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvUsers = view.findViewById(R.id.rvUsers);
        allBrowse = new ArrayList<>();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        rvUsers.addItemDecoration(dividerItemDecoration);
        browseAdapter = new BrowseAdapter(getContext(), allBrowse);


        rvUsers.setAdapter(browseAdapter);
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        queryBrowse();
    }
    private void queryBrowse() {
        ParseQuery<Browse> query = ParseQuery.getQuery(Browse.class);
        query.include(Browse.KEY_USERNAME);
        query.findInBackground(new FindCallback<Browse>() {
            @Override
            public void done(List<Browse> objects, ParseException e) {
                if(e != null){
                    return;
                }
                for(Browse browse: objects){
                    Log.i(TAG, "Barbers: " + browse.getName());
                }
                allBrowse.addAll(objects);
                browseAdapter.notifyDataSetChanged();
            }
        });
    }
}