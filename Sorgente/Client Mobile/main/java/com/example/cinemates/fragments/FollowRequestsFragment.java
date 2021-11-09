package com.example.cinemates.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.control.Controller;
import com.example.cinemates.GlideApp;
import com.example.cinemates.control.ListenerDispenser;
import com.example.cinemates.model.AppUser;
import com.example.cinemates.R;
import com.example.cinemates.helper.InputChecker;

import java.util.ArrayList;
import java.util.List;


public class FollowRequestsFragment extends Fragment {

    private final List<AppUser> requests = new ArrayList<>();

    private RequestsAdapter requestsAdapter;

    private TextView noRequestsMessageTextView;


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_requests, container, false);
        noRequestsMessageTextView = view.findViewById(R.id.nessuna_richiesta_txtview);

        initRecyclerView(view);
        ListenerDispenser.attachRequestsListener(this);

        return view;
    }

    @Override
    public void onDestroy () {
        ListenerDispenser.detachRequestsListener();
        super.onDestroy();
    }



    private void initRecyclerView (View inflatedView) {
        RecyclerView recyclerView = inflatedView.findViewById(R.id.richieste_recyclerview);
        requestsAdapter = new RequestsAdapter(getContext(), requests);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(requestsAdapter);
    }


    public void removeRequest (String id) {
        for (AppUser r : requests) {
            if (r.getUid().equals(id)) {
                requests.remove(r);
                break;
            }
        }
    }


    public List<AppUser> getRequests () {
        return requests;
    }

    public TextView getNoRequestsMessageTextView () {
        return noRequestsMessageTextView;
    }

    public RequestsAdapter getRequestsAdapter () {
        return requestsAdapter;
    }

//////////////////////////////////////////////////RequestsAdapter//////////////////////////////////////////////////

    public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestHolder> {

        final Context context;
        final List<AppUser> requests;

        public RequestsAdapter (Context context, List<AppUser> requests) {
            this.context = context;
            this.requests = requests;
        }

        @NonNull
        @Override
        public RequestHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_request, parent, false);
            return new RequestHolder(view);
        }

        @Override
        public void onBindViewHolder (@NonNull RequestHolder holder, int position) {

            String name = requests.get(position).getName();
            String surname = requests.get(position).getSurname();
            String propic = requests.get(position).getPropic();
            String uid = requests.get(position).getUid();

            holder.requestNameTextView.setText(InputChecker.capitalizeNames(name));
            holder.requestSurnameTextView.setText(InputChecker.capitalizeNames(surname));
            GlideApp.with(getContext()).load(propic).placeholder(R.drawable.user_placeholder).into(holder.requestPropicView);

            holder.requestAcceptBtn.setOnClickListener(v -> Controller.getUserFollowingsDataHandler().acceptFriendRequest(getContext(), uid));
            holder.requestRefuseBtn.setOnClickListener(v -> Controller.getUserFollowingsDataHandler().refuseFriendRequest(getContext(), uid));

        }

        @Override
        public int getItemCount () {
            return requests.size();
        }



        private class RequestHolder extends RecyclerView.ViewHolder {
            private final ImageView requestPropicView;
            private final TextView requestNameTextView;
            private final TextView requestSurnameTextView;
            private final ImageButton requestAcceptBtn;
            private final ImageButton requestRefuseBtn;

            public RequestHolder (@NonNull View itemView) {
                super(itemView);

                requestPropicView = itemView.findViewById(R.id.propic_richiesta_imgview);
                requestNameTextView = itemView.findViewById(R.id.nome_richiesta_txtview);
                requestSurnameTextView = itemView.findViewById(R.id.cognome_richiesta_txtview);
                requestAcceptBtn = itemView.findViewById(R.id.accept_btn);
                requestRefuseBtn = itemView.findViewById(R.id.refuse_btn);
            }
        }

    }

}