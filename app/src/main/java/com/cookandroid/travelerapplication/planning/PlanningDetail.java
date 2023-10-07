package com.cookandroid.travelerapplication.planning;

import static androidx.core.util.PatternsCompat.IP_ADDRESS;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.recommend.PlaceScore;
import com.cookandroid.travelerapplication.task.Recommend_Place;

import java.util.ArrayList;

public class PlanningDetail extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_planning_detail_upload);



    }
}
