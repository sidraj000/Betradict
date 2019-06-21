package com.vincis.betradict;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class commentryfrag extends Fragment {
    TextView commentry;
    String[] response;
    public JSONObject jsonObj,jsonObj2,jsonObject3;

    JSONObject balls;

    public commentryfrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        List<String> ballLists=new ArrayList<>();
        List<String>comments=new ArrayList<>();
        response=getArguments().getStringArray("data");
        View view= inflater.inflate(R.layout.fragment_commentryfrag, container, false);
        commentry=view.findViewById(R.id.commentry);

        try {
            jsonObj=new JSONObject(response[0]);

            jsonObj2=jsonObj.getJSONObject("data");
            jsonObject3=jsonObj2.getJSONObject("card");
            JSONArray overs=jsonObject3.getJSONObject("now").getJSONArray("recent_overs");
            for(int i=0;i<overs.length();i++)
            {
                JSONArray arr=overs.getJSONArray(i).getJSONArray(1);
                for(int k=arr.length()-1;k>=0;k--)
                {
                    ballLists.add(arr.getString(k));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
           balls=jsonObject3.getJSONObject("balls");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String comm="";
        for(int i=0;i<ballLists.size();i++)
        {
            try {
                String s=balls.getJSONObject(ballLists.get(i)).getString("comment");
                comm=comm+Html.fromHtml(s).toString()+"\n"+"\n";
                comments.add(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        commentry.setText(comm);


        return view;
    }

}
