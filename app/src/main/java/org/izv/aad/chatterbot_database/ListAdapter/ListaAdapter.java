package org.izv.aad.chatterbot_database.ListAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.izv.aad.chatterbot_database.MainActivity;
import org.izv.aad.chatterbot_database.POJO.Chat;
import org.izv.aad.chatterbot_database.R;
import org.izv.aad.chatterbot_database.SavedChat;

import java.util.List;

public class ListaAdapter extends ArrayAdapter<Chat> {

    private final List<Chat> list;
    private final Context context;

    public ListaAdapter(Context context, List<Chat> list) {
        super(context, R.layout.item, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder{
        protected TextView tvChatName;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tvChatName = view.findViewById(R.id.tvchatname);
            view.setTag(viewHolder);
        }else{
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.tvChatName.setText(list.get(position).getName());
        Log.v(MainActivity.TAG, list.get(position).getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SavedChat.class);
                intent.putExtra("name", list.get(position).getName());
                intent.putExtra("conversation", list.get(position).getConversation());
                context.startActivity(intent);
            }
        });

        return view;
    }


}
