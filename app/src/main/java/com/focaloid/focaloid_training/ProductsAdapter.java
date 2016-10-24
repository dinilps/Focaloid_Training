package com.focaloid.focaloid_training;

import android.app.Activity;
import android.app.FragmentManager;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.provider.SyncStateContract;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.focaloid.focaloid_training.models.Allproducts;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by focaloid on 21/09/16.
 */
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {

    private Context mContext;
    private Activity activity;

    private List<Allproducts> Products;





    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView title, count;
        public ImageView thumbnail, overflow;

        private List<Allproducts> Products;
        Context mContext;
        public View view;
        ProductDetailFragment productDetailFragment=new ProductDetailFragment();

        private getProductDetail getproductdetail;

        public MyViewHolder(View v,Context mContext, List<Allproducts> Products ) {
            super(v);

            view=v;
            this.Products=Products;
            this.mContext=mContext;
            view.setOnClickListener(this);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }

        @Override
        public void onClick(View view) {

            int position= getAdapterPosition();
            Allproducts product=this.Products.get(position);

            Activity activity = (Activity) mContext;
            FragmentManager fragmentManager = activity.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


            //getproductdetail.getDetails(product);




            Bundle args = new Bundle();
            args.putString("ProPkId",  product.getProPkId());
            //args.putString("title",product.getProName());
            //args.getString("Price", product.getProPrice());
            productDetailFragment.setArguments(args);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.fragment,productDetailFragment).commit();



            //Inflate the fragment
            //FragmentTransaction fragmentTransaction=mContext.getApplicationContext().getSupportFragmentManager().beginTransaction();
            //activity.getFragmentManager().beginTransaction().replace(R.id.fragment, productfragment).commit();





            /*Intent intent=new Intent(this.mContext,Testingclass.class);
            intent.putExtra("img_id",album.getThumbnail());
            intent.putExtra("title",album.getName());
            intent.putExtra("Price",album.getPrice());
            this.mContext.startActivity(intent);*/

        }
    }



    public ProductsAdapter(Context mContext, List<Allproducts> Products) {
        this.mContext = mContext;
        this.Products = Products;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        MyViewHolder myViewHolder=new MyViewHolder(itemView,mContext,Products);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Allproducts product = Products.get(position);
        holder.title.setText(product.getProName());
        holder.count.setText(" Price :" + product.getProPrice() );




        if (product.getProductImages() != null) {
            // Picasso.with(mContext).load(Constant.imageUrlLargeResolution+ productSingleItem.getProductImageL0()).placeholder(R.drawable.product_image_default).fit().into(myViewHolder.productImage);


            String images=product.getProductImages();

            List<String> list = Lists.newArrayList(Splitter.on(",").splitToList(images));

            String img=list.get(0);

            // loading album cover using Glide library
            Glide.with(mContext).load(Constant.imageUrlLargeResolution+img).into(holder.thumbnail);

        }
        /*if (productSingleItem.getProductImageL0() != null) {
            Picasso.with(mContext).load(SyncStateContract.Constants.imageUrlLowResolution+ productSingleItem.getProductImageL0()).placeholder(R.drawable.product_image_default).fit().into(myViewHolder.productImage);
        }
*/
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });

    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_product, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_Wishlist:
                    Toast.makeText(mContext, "Add to Wishlist", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.add_to_cart:
                    Toast.makeText(mContext, "Add to Cart", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }


    public interface getProductDetail{
        void getDetails(Allproducts product);

    }


    @Override
    public int getItemCount() {
        return Products.size();
    }

}
