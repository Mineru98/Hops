package com.mineru.hops.Fragment;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mineru.hops.Function.AddGroup.AddGroupLatter1;
import com.mineru.hops.Function.Code_Scanner;
import com.mineru.hops.Function.Hopping2;
import com.mineru.hops.UserManage.Model.Group_model;
import com.mineru.hops.Function.Searching_friends;
import com.mineru.hops.R;

import java.util.ArrayList;
/**
 * Created by Mineru on 2017-09-08.
 */

public class List extends Fragment {
    private static final String TAG ="ListFragment";

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    public LinearLayout l_layout;
    private RecyclerView recyclerView;
    private BoardRecyclerViewAdapter mAdapter;

    private TestAdapter mAdapter2;

    private GridLayoutManager recyclerManager = new GridLayoutManager(getContext(),2);

    private FloatingActionMenu fam;
    private com.github.clans.fab.FloatingActionButton fabQr,fabHopping,fabGroup;

    private java.util.List<Group_model> group_models =new ArrayList<>();

    public String str_title;
    public int test=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.list_fragment_layout,container,false);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        recyclerView = (RecyclerView) view.findViewById(R.id.listView);
        recyclerManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){
            @Override
            public int getSpanSize(int position){
                if(position==0) return 2;
                return 1;
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        mAdapter = new BoardRecyclerViewAdapter();
        mAdapter2 = new TestAdapter();

        recyclerView.setAdapter(mAdapter);


        l_layout = (LinearLayout) view.findViewById(R.id.l_layout);

        l_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Searching_friends.class);
                startActivity(intent);
            }
        });
        fabQr = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.fab_qr);
        fabHopping = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.fab_hopping);
        fabGroup = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.fab_group);
        fam = (FloatingActionMenu) view.findViewById(R.id.fab_plus_list);

        fabQr.setOnClickListener(onButtonClick());
        fabGroup.setOnClickListener(onButtonClick());
        fabHopping.setOnClickListener(onButtonClick());
        fam.setClosedOnTouchOutside(true);

        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fam.isOpened()) {
                    fam.close(true);
                }
            }
        });

        database.getReference().child("Users/"+auth.getCurrentUser().getUid()+"/Group/"+str_title)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        group_models.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Group_model group_model = snapshot.getValue(Group_model.class);
                            group_models.add(group_model);
                        }
                        mAdapter2.notifyDataSetChanged();

                    }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        database.getReference().child("Users/"+auth.getCurrentUser().getUid()+"/Group")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                group_models.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Group_model group_model = snapshot.getValue(Group_model.class);
                    group_models.add(group_model);
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }

    private View.OnClickListener onButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == fabQr) {
                    Intent intent = new Intent(getActivity(), Code_Scanner.class);
                    startActivity(intent);
                }
                else if(view == fabHopping){
                    Intent intent = new Intent(getActivity(), Hopping2.class);
                    startActivity(intent);
                }
                else if(view == fabGroup){
                    Intent intent = new Intent(getActivity(),AddGroupLatter1.class);
                    startActivity(intent);
                }
                fam.close(true);
            }
        };
    }

    public class TestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item2, parent, false);

            return new TestAdapter.CustomViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            //((CustomViewHolder)holder).tv_name.setText(imageDTOs.get(position).inputName);
            //((CustomViewHolder)holder).tv_description.setText(imageDTOs.get(position).inputDescription);
            String tmp1 =group_models.get(position).friends.keySet().toString();
            String tmp2 =group_models.get(position).friends.values().toString();
            ((CustomViewHolder)holder).tv_name.setText(""+tmp1);
            ((CustomViewHolder)holder).tv_description.setText(""+tmp2);
            ((CustomViewHolder)holder).cardLayer.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    str_title = (String.valueOf(group_models.get(position).group_name));
                    if(test==1){
                        recyclerManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){
                            @Override
                            public int getSpanSize(int position){
                                if(position==0) return 2;
                                return 1;
                            }
                        });
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                        mAdapter = new BoardRecyclerViewAdapter();
                        recyclerView.setAdapter(mAdapter);
                        test=0;
                    }

                }
            });

            Glide.with(holder.itemView.getContext())
                    .load(group_models.get(position).imageUrl)
                    .into(((CustomViewHolder)holder).item_imageView);

        }

        @Override
        public int getItemCount() {
            return group_models.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            ImageView item_imageView;
            RelativeLayout cardLayer;
            GradientDrawable drawable;
            TextView tv_name;
            TextView tv_description;

            public CustomViewHolder(View view) {
                super(view);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                tv_description = (TextView) view.findViewById(R.id.tv_description);
                cardLayer = (RelativeLayout) view.findViewById(R.id.cardLayer);
                item_imageView = (ImageView) view.findViewById(R.id.item_imageView);
                drawable = (GradientDrawable) view.getContext().getDrawable(R.drawable.round_view);
                item_imageView.setBackground(drawable);
                item_imageView.setClipToOutline(true);
            }
        }
    }



     public class BoardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            ((CustomViewHolder)holder).group_title.setText(group_models.get(position).group_name);
            ((CustomViewHolder)holder).group_number.setText(String.valueOf(group_models.get(position).m_num)+"개의 Hops");

            ((CustomViewHolder)holder).list_layout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    /*Intent intent = new Intent(view.getContext(), MessageBoard.class);
                    intent.putExtra("group_name",group_models.get(position).group_name);
                    ActivityOptions activityOptions = null;
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
                        activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(),R.anim.fromright,R.anim.toleft);
                        startActivity(intent,activityOptions.toBundle());

                    }*/

                    str_title = (String.valueOf(group_models.get(position).group_name));

                    if(test==0){
                        /*recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        final BoardRecyclerViewAdapter boardRecyclerViewAdapter = new BoardRecyclerViewAdapter();
                        recyclerView.setAdapter(boardRecyclerViewAdapter);*/

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
                        mAdapter2 = new TestAdapter();
                        recyclerView.setAdapter(mAdapter2);
                        test=1;
                    }

                }
            });

            /*((CustomViewHolder)holder).list_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v){
                    mCardDialog = new CardDialog(getActivity(),imageDTOs.get(position).imageUrl,imageDTOs.get(position).inputName, imageDTOs.get(position).inputCompany,
                            imageDTOs.get(position).inputPosition,imageDTOs.get(position).inputDescription,imageDTOs.get(position).inputPhoneNumber,imageDTOs.get(position).uid);
                    mCardDialog.show();
                    return false;
                }
            });*/
            Glide.with(holder.itemView.getContext())
                    .load(group_models.get(position).imageUrl)
                    .into(((CustomViewHolder)holder).list_image);

        }

        @Override
        public int getItemCount() {
            return group_models.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            ImageView list_image;
            LinearLayout list_layout;
            TextView group_title;
            TextView group_number;
            GradientDrawable drawable;

            public CustomViewHolder(View view) {
                super(view);
                drawable = (GradientDrawable) view.getContext().getDrawable(R.drawable.round_view2);
                list_layout = (LinearLayout) view.findViewById(R.id.list_layout);
                list_image = (ImageView) view.findViewById(R.id.list_image);
                list_image.setBackground(drawable);
                list_image.setClipToOutline(true);
                group_title = (TextView) view.findViewById(R.id.group_title);
                group_number = (TextView) view.findViewById(R.id.group_number);
            }
        }
    }

}


