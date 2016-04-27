package com.example.dimitrios.NoteCards;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.dimitrios.swipe_testing.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    SharedPreferences mSettings;
    SharedPreferences.Editor mEditor;
    GsonBuilder gsonb;
    private Gson mGson;
    private final String setting_name = "my_cards_test";
    HashMap<String, ArrayList<NoteCard>> cards_collection = new HashMap<String, ArrayList<NoteCard>>();
    //ListView mListView;
    Button create_b;

    private AppAdapter mAdapter;
    private SwipeMenuListView mListView;
    private ArrayList<String> mAppList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mSettings = getSharedPreferences("my_cards_test", Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
        gsonb = new GsonBuilder();
        mGson = gsonb.create();

        create_b = (Button)findViewById(R.id.create_category);
        mAppList = new ArrayList<String>();

        mListView = (SwipeMenuListView) findViewById(R.id.listView);

        mAdapter = new AppAdapter();
        mListView.setAdapter(mAdapter);

        create_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Create_Category.class);
                //i.putExtra("value", );
                startActivityForResult(i, 0);

            }
        });
        populate();

            SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {
                    // create "open" item
                    SwipeMenuItem openItem = new SwipeMenuItem(
                            getApplicationContext());
                    // set item background
                    openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                            0xCE)));
                    // set item width
                    openItem.setWidth(dp2px(90));
                    // set item title
                    openItem.setTitle("Modify");
                    // set item title fontsize
                    openItem.setTitleSize(18);
                    // set item title font color
                    openItem.setTitleColor(Color.WHITE);
                    // add to menu
                    menu.addMenuItem(openItem);

                    // create "delete" item
                    SwipeMenuItem deleteItem = new SwipeMenuItem(
                            getApplicationContext());
                    // set item background
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                            0x3F, 0x25)));
                    // set item width
                    deleteItem.setWidth(dp2px(90));
                    // set a icon
                    deleteItem.setIcon(R.drawable.ic_delete);
                    // add to menu
                    menu.addMenuItem(deleteItem);
                }
            };
        // set creator
        mListView.setMenuCreator(creator);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = mAppList.get(position);
                Intent i = new Intent(getBaseContext(), Cards.class);
                i.putExtra("key", item);
                startActivity(i);
            }
        });

        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                String item = mAppList.get(position);
                switch (index) {
                    case 0:
                        // modify
                        Intent i = new Intent(getBaseContext(), Create_Category.class);
                        i.putExtra("key", item);
                        i.putExtra("modify", true);
                        startActivityForResult(i, 0);
                        break;
                    case 1:
                        // delete
                        mAppList.remove(position);
                        cards_collection.remove(item);
                        String writearray = mGson.toJson(cards_collection);
                        mEditor.putString(setting_name, writearray);
                        mEditor.apply();
                        mAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
    }
    private void populate(){
        String loadValue = mSettings.getString(setting_name, "");
        Type type = new TypeToken<HashMap<String,ArrayList<NoteCard>>>(){}.getType();
        //String[] myitems = {};
        mAppList.clear();

        if(!loadValue.isEmpty())
        {
            cards_collection = mGson.fromJson(loadValue,type);

        }

        //cards_collection = mGson.fromJson(loadValue,type);


        if(cards_collection.isEmpty() && !loadValue.isEmpty())
        {
            //my_listview.setVisibility(View.GONE);
            Toast.makeText(this, "No NoteCard categories found.", Toast.LENGTH_LONG).show();
        }
        else if(!cards_collection.isEmpty() && !loadValue.isEmpty())
        {

            for(String key: cards_collection.keySet())
            {
                mAppList.add(key);
            }

            /*ListView my_list = (ListView)findViewById(R.id.Cards);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_layout,myitems);

            my_list.setAdapter(adapter);*/

        }

    }

    class AppAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public String getItem(int position) {
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_list_app, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            String item = getItem(position);
            //holder.iv_icon.setImageDrawable(item.loadIcon(getPackageManager()));
            holder.tv_name.setText(item);
            /*holder.iv_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SimpleActivity.this, "iv_icon_click", Toast.LENGTH_SHORT).show();
                }
            });
            holder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SimpleActivity.this,"iv_icon_click",Toast.LENGTH_SHORT).show();
                }
            });*/
            return convertView;
        }

        class ViewHolder {
            //ImageView iv_icon;
            TextView tv_name;

            public ViewHolder(View view) {
                //iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(this);
            }
        }
        }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_left) {
            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
            return true;
        }
        if (id == R.id.action_right) {
            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Retrieve data in the intent
        mAdapter.notifyDataSetChanged();
        if(resultCode == RESULT_OK && requestCode == 0){
            populate();
        }

    }

    }


