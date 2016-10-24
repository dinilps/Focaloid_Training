package com.focaloid.focaloid_training;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.focaloid.focaloid_training.models.AllProductsResults;
import com.focaloid.focaloid_training.models.Allproducts;
import com.focaloid.focaloid_training.rest.APIPlug;
import com.focaloid.focaloid_training.rest.ApiClient;
import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.roger.catloadinglibrary.CatLoadingView;


import java.util.ArrayList;

import java.util.List;


import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    boolean setGrid=false;

    private RecyclerView recyclerView;
    private ProductsAdapter adapter;
    private ArrayList<Allproducts> productList;
    int flag=0;
    Button layoutChangerBtn;
    RecyclerView.LayoutManager linearLayoutManager;
    RecyclerView.LayoutManager gridLayoutManager;
    Context mcontext;
    RatingBar ratingBar;

    private List<Allproducts> data;
    CatLoadingView mView;




    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
        //prepareProducts();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.home, container, false);
        ButterKnife.inject(this, view);
        //animatedCircleLoadingView = (AnimatedCircleLoadingView) view.findViewById(R.id.circle_loading_view);
        //((MainActivity) getActivity()).setActionBarTitle("Home");

        mcontext=view.getContext();
        //((MainActivity) getActivity()).setActionBarTitle("Home");

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);












       // productList = new ArrayList<>();
        //adapter = new ProductsAdapter(view.getContext(), productList);

         gridLayoutManager = new GridLayoutManager(view.getContext(), 2);

        // gridLayoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        //gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        //   linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        //recyclerView = (RecyclerView) getActivity().findViewById(R.id.men_rv);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
       // adapter = new ProductsAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);

        layoutChangerBtn = (Button)view.findViewById(R.id.btnview);
        ratingBar=(RatingBar)view.findViewById(R.id.ratingBar);

        layoutChangerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag == 0) {

                    float deg = layoutChangerBtn.getRotation() + 180F;
                    layoutChangerBtn.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());


                    layoutChangerBtn.setBackgroundResource(R.drawable.ic_grid);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    ratingBar = new RatingBar(mcontext, null, android.R.attr.ratingBarStyleSmall);
                    ratingBar.setIsIndicator(false);
                    //adapter.setGrid(false);

                            flag = 1;

                } else {

                    float deg = layoutChangerBtn.getRotation() + 180F;
                    layoutChangerBtn.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

                    layoutChangerBtn.setBackgroundResource(R.drawable.ic_linear);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    //adapter.setGrid(true);
                    ratingBar = new RatingBar(mcontext, null, android.R.attr.ratingBarStyleIndicator);
                    ratingBar.setIsIndicator(false);

                    //ratingBar.setst

                    flag = 0;
                }

            }
        });

        /*RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);*/

        //prepareProducts();



        loadJSON();



        /*try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) view.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        return view;

    }






    private void loadJSON(){

        final ProgressDialog loading = ProgressDialog.show(mcontext,"Fetching Data","Please wait...",false,false);


        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.Base_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIPlug request = retrofit.create(APIPlug.class);*/

        APIPlug api = ApiClient.getApiService();

        Call<AllProductsResults> call = api.getProducts("100","0","0","0","1");

        call.enqueue(new Callback<AllProductsResults>() {
            @Override
            public void onResponse(Call<AllProductsResults> call, Response<AllProductsResults> response) {



                //AllProductsResults allproductsResults = response.body();
                //System.out.println("Response : " + response.body().getData());

                data= response.body().getData();

                //data = new ArrayList<Allproducts>((Collection<? extends Allproducts>) Arrays.asList(allproductsResults.getData()));
                adapter = new ProductsAdapter(mcontext,data);
                recyclerView.setAdapter(adapter);
                loading.dismiss();
            }

            @Override
            public void onFailure(Call<AllProductsResults> call, Throwable t) {
                Toast.makeText(mcontext,"json failure",Toast.LENGTH_LONG).show();
                Log.d("Error",t.getMessage());
            }
        });
    }
    


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
