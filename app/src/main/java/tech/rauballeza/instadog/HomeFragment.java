package tech.rauballeza.instadog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    FirebaseAuth firebaseAuth;

    //Para mostrar el Post
    RecyclerView recyclerView;
    List<ModelPost> postList;
    AdapterPosts adapterPosts;


    public HomeFragment() {
        //Constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Inicializar
        firebaseAuth = FirebaseAuth.getInstance();

        //Recycler view y sus propiedades
        recyclerView = view.findViewById(R.id.postsRecyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //Mostrando los primeros posts.
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        //Agregar layout para el recyclerview
        recyclerView.setLayoutManager(layoutManager);

        //Inicializando la lista de post
        postList = new ArrayList<>();
        loadPost();

        return view;
    }

    private void loadPost() {
        //Path de todos los posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        //Obteniendo todos los datos de esta referencia
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelPost modelPost = ds.getValue(ModelPost.class);
                    postList.add(modelPost);
                    //Adaptador
                    adapterPosts = new AdapterPosts(getActivity(), postList);
                    //Agregar adaptador para el recyclerview
                    recyclerView.setAdapter(adapterPosts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //En caso de error
                Toast.makeText(getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Falta términar la busqueda de posts
    private void searchPosts(final String searchQuery) {
        //Path de todos los posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        //Obteniendo todos los datos de esta referencia
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    if (modelPost.getpTime().toLowerCase().contains(searchQuery.toLowerCase()) ||
                            modelPost.getpDescr().toLowerCase().contains(searchQuery.toLowerCase())) {
                        postList.add(modelPost);
                    }

                    postList.add(modelPost);
                    //Adaptador
                    adapterPosts = new AdapterPosts(getActivity(), postList);
                    //Agregar adaptador para el recyclerview
                    recyclerView.setAdapter(adapterPosts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //En caso de error
                Toast.makeText(getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

        } else {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onStart() {
        checkUserStatus();
        super.onStart();
    }

/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        //Inflating menu
        inflater.inflate(R.menu.menu_main,menu);

        //Searchview para buscar post por titulo o descripción
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Llamado cuando el usuario presiona el boton buscar
                if(!TextUtils.isEmpty(s)){
                    searchPosts(s);
                }
                else
                {
                    loadPost();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //Llamado cuando el usuario presiona alguna carta
                if(!TextUtils.isEmpty(s)){
                    searchPosts(s);
                }
                else
                {
                    loadPost();
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
*/

}


