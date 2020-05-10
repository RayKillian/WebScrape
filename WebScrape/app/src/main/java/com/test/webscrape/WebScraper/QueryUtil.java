package com.test.webscrape.WebScraper;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.test.webscrape.DataModel.Products;
import com.test.webscrape.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class QueryUtil {

    public static final String LOG_TAG = QueryUtil.class.getSimpleName();

    private static String url;
    //private static String lazadaImgUrl2;
    private static String lazadaPriceNew, lazadaPrieOld, lazadaDesc, lazadaUrlLink,
    lazadaImgUrl, lazadaImgLogoUrl, lazadaDiscPercent, lazadaContainer;

    private static String tokopedUrl, tokopedPricceNew, tokopedPriceOld, tokopedDesc, tokopedUrlLink,
    tokopedImgUrl, tokopedImgLogoUrl, tokopedDiscPercent, tokopedContainer;

    private static String shopeeUrl, shopeePriceNew, shopeePriceOld, shopeeDesc, shopeeUrlLink,
    shopeeImgUrl, shopeeImgLogoUrl, shopeeContainer;

    static DecimalFormat df;
    static FirebaseRemoteConfig mFirebaseRemoteConfig;


    public QueryUtil(){
        // Used to return website data
    }

    public static List<Products> fetchWebsiteData(/*String requestUrl*/ String tokoUrl){

        //url = requestUrl;
        //shopeeUrl = shopUrl;
        tokopedUrl = tokoUrl;

        return extractShoppingData();
    }

    private static ArrayList<Products> extractShoppingData() {
        ArrayList<Products> products = new ArrayList<>();

        //Document doc = null;
        //Document docShop = null;
        Document docToko = null;

        fetchScrappingConfig();

        try {

            docToko = Jsoup.connect(tokopedUrl).sslSocketFactory(socketFactory())
                    .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                    .timeout(60000)
                    .get();

            // tokopedia web scraping content
            for (Element row:docToko.select(tokopedContainer)){
                Products pro1;

                if(row.select(tokopedDesc).text().equals("")) {
                    continue;
                } else {
                    String imageurl = row.select(tokopedImgUrl).attr("src");
                    String productLink = row.select(tokopedUrlLink).attr("href");
                    String priceOld = row.select(tokopedPriceOld).text();
                    String productdesc = row.select(tokopedDesc).text();
                    String newPrice = row.select(tokopedPricceNew).text();
                    String imglogo = tokopedImgLogoUrl;
                    String percentageOff = row.select(tokopedDiscPercent).text();
                    newPrice = newPrice.replace(priceOld, "");

                    pro1 = new Products(productdesc, priceOld, imageurl, productLink, imglogo, newPrice, percentageOff, "Tokopedia");
                }
                products.add(pro1);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        /*try {
            docShop = Jsoup.connect(shopeeUrl).sslSocketFactory(socketFactory()).get();

            //shopee web scraping content
            for (Element row:docShop.select(shopeeContainer)) {
                Products pro2;

                if (row.select(shopeeUrlLink).attr("href").equals("")) {
                    continue;
                } else {

                    String imageurl = row.select(shopeeImgUrl).attr("abs:src");
                    String productLink = row.select(shopeeUrlLink).attr("href");
                    String priceOld = row.select(shopeePriceOld).text();
                    String productdescription = row.select(shopeePriceNew).text();
                    String newPrice = row.select(shopeePriceNew).text();
                    String imglogo = shopeeImgLogoUrl;
                    String percentageOff = "";
                    if (!priceOld.isEmpty() && priceOld.indexOf('-') == -1) {
                        try {
                            float dOld = Float.valueOf(priceOld.replace("Rp","").replace(".",""));
                            float dNew = Float.valueOf(newPrice.replace("Rp", "").replace(".", ""));
                            percentageOff = "-";
                            percentageOff += df.format(100-((dNew/dOld)*100));
                            percentageOff += "%";
                        } catch (NumberFormatException e){
                            Log.d(LOG_TAG,"Error in calculation " + e.getStackTrace().toString());
                        }
                    }

                    pro2 = new Products(productdescription,priceOld,imageurl,productLink,imglogo,newPrice,percentageOff,"Shopee");
                }
                products.add(pro2);
            }
        } catch (IOException e){
            e.printStackTrace();
        } */

        /*try {
            doc = Jsoup.connect(url).sslSocketFactory(socketFactory()).get();

            //lazada web scraping content
            for (Element row:doc.select(lazadaContainer)) {
                Products pro3;

                //if (row.select(lazadaUrlLink).attr("href").equals("")){
                 //   continue;
                //} else {
                    String imageurl = row.select(lazadaImgUrl).attr("src");
                    String productLink = row.select(lazadaUrlLink).attr("href");
                    String priceOld = row.select(lazadaPrieOld).text();
                    String productdesc = row.select(lazadaDesc).text();
                    String priceNew = row.select(lazadaPriceNew).text();
                    String imglogo = lazadaImgLogoUrl;
                    String percentageOff = row.select(lazadaDiscPercent).text();

                    pro3 = new Products(productdesc,priceOld,imageurl,productLink,imglogo,priceNew,percentageOff,"Lazada");
                //}

                products.add(pro3);
            }
        } catch (IOException e){
            e.printStackTrace();
        }*/

        //return list of products
        return products;
    }

    private static SSLSocketFactory socketFactory() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Failed to create a SSL socket factory", e);
        }
    }

    private static void fetchScrappingConfig(){

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        long cacheExpiration=10200;
        Task<Void> voidTask = mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activate();
                        }
                    }
                });

        String scrapConfig=mFirebaseRemoteConfig.getString("webscraping_css");

        initializeScrappingConfig(scrapConfig);
    }

    private static void initializeScrappingConfig(String scrapJSON){
        try {
            JSONObject baseJSONresponse = new JSONObject(scrapJSON);
            JSONObject websites=baseJSONresponse.getJSONObject("websites");

            /*JSONObject lazadaCss = websites.getJSONObject("lazada");
            lazadaPriceNew = lazadaCss.getString("priceNew");
            lazadaPrieOld = lazadaCss.getString("priceOld");
            lazadaDesc = lazadaCss.getString("description");
            lazadaUrlLink = lazadaCss.getString("urlLink");
            lazadaImgUrl = lazadaCss.getString("imgUrl");
            lazadaImgLogoUrl = lazadaCss.getString("imgLogoUrl");
            lazadaDiscPercent = lazadaCss.getString("discountPercent");
            lazadaContainer = lazadaCss.getString("container");*/

            /*JSONObject shopeeCss = websites.getJSONObject("shopee");
            shopeePriceNew = shopeeCss.getString("priceNew");
            shopeePriceOld = shopeeCss.getString("priceOld");
            shopeeDesc = shopeeCss.getString("description");
            shopeeUrlLink = shopeeCss.getString("urlLink");
            shopeeImgUrl = shopeeCss.getString("imgUrl");
            shopeeImgLogoUrl = shopeeCss.getString("imgLogoUrl");
            shopeeContainer = shopeeCss.getString("container");*/

            JSONObject tokopediaCss = websites.getJSONObject("tokopedia");
            tokopedPricceNew = tokopediaCss.getString("priceNew");
            tokopedPriceOld = tokopediaCss.getString("priceOld");
            tokopedDesc = tokopediaCss.getString("description");
            tokopedUrlLink = tokopediaCss.getString("urlLink");
            tokopedImgUrl = tokopediaCss.getString("imgUrl");
            tokopedImgLogoUrl = tokopediaCss.getString("imgLogoUrl");
            tokopedDiscPercent = tokopediaCss.getString("discountPercent");
            tokopedContainer = tokopediaCss.getString("container");
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
