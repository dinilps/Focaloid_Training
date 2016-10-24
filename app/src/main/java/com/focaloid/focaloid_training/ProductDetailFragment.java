package com.focaloid.focaloid_training;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.focaloid.focaloid_training.models.Allproducts;
import com.focaloid.focaloid_training.models.SingleProduct;
import com.focaloid.focaloid_training.rest.APIPlug;
import com.focaloid.focaloid_training.rest.ApiClient;
import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductDetailFragment extends Fragment implements ProductsAdapter.getProductDetail{

    Context mcontext;
    public ImageView thumbnail;
    public TextView txttitle,txtprice,txtdic;
    private Allproducts products;
    String productid;
    private AnimatedCircleLoadingView animatedCircleLoadingView;

    private SingleProduct data;

    private OnFragmentInteractionListener mListener;

    public ProductDetailFragment() {
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
        View view=inflater.inflate(R.layout.fragment_product_detail, container, false);

        ((MainActivity) getActivity()).setActionBarTitle("Product Detail");

        thumbnail=(ImageView)view.findViewById(R.id.singleproductImageview);
        txttitle=(TextView)view.findViewById(R.id.txtsingleTitle);
        txtprice=(TextView)view.findViewById(R.id.txtsinglePrice);
        txtdic=(TextView)view.findViewById(R.id.txtviewDisc);
        Bundle bundle = getArguments();
        productid = bundle.getString("ProPkId");
        //String price = bundle.getString("Price");
        //String img_id = bundle.getString("title");




        mcontext=view.getContext();

        loadJSON();



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

        Call<JsonObject> call = api.getProductDetail(productid);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful())
                {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        if(jsonObject.has("success"))
                        {
                            if(jsonObject.getString("success").equalsIgnoreCase("1"))
                            {
                                JSONObject dataobject=jsonObject.getJSONObject("data");
                                if(dataobject.isNull("1")) {

                                    JSONArray productArray = dataobject.getJSONArray("product_data");

                                    for (int i = 0; i < productArray.length(); i++) {

                                        String proName = productArray.getJSONObject(i).getString("pro_name");
                                        txttitle.setText(proName);
                                        txtprice.setText("Price : "+productArray.getJSONObject(i).getString("pro_price"));


                                        // get our html content
                                        String htmlAsString = productArray.getJSONObject(i).getString("pro_desc");      // used by WebView
                                        Spanned htmlAsSpanned = Html.fromHtml(htmlAsString); // used by TextView

                                        // set the html content on a TextView



                                        txtdic.setText(htmlAsSpanned);

                                        String images=productArray.getJSONObject(i).getString("product_images");



                                        List<String> list = Lists.newArrayList(Splitter.on(",").splitToList(images));

                                        String img=list.get(0);

                                        // loading album cover using Glide library
                                        Glide.with(mcontext).load(Constant.imageUrlLargeResolution+img).into(thumbnail);

                                        loading.dismiss();

                                    }
                                }
                            }

                        }else{

                        }









                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(mcontext,"json failure",Toast.LENGTH_LONG).show();
                Log.d("Error",t.getMessage());
            }
        });
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void getDetails(Allproducts product) {
        products=product;

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
