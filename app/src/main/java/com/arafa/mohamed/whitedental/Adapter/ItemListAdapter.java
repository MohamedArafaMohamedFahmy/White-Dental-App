package com.arafa.mohamed.whitedental.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.arafa.mohamed.whitedental.R;

import java.util.ArrayList;

public class ItemListAdapter extends ArrayAdapter<String> {

    ArrayList<String> mData;
    Context mContext;
    LayoutInflater mInflater;
    int mListItemResourceID;

        public ItemListAdapter(Context context, int listItemResourceId, ArrayList<String> items) {
            super(context, listItemResourceId, items);
            mData = items;
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mListItemResourceID = listItemResourceId;
        }

        class ViewHolder {
            TextView mNameTextView;

        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final String item = mData.get(position);
            final ViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.inflate(mListItemResourceID, parent, false);

                holder = new ViewHolder();
                holder.mNameTextView =  convertView.findViewById(R.id.text_doctor);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (item != null) {
                holder.mNameTextView.setText(item);
            }
            return convertView;
        }
    }
