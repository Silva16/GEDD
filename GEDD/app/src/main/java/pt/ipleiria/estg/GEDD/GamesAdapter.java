package pt.ipleiria.estg.GEDD;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

import pt.ipleiria.estg.GEDD.Models.Game;

/**
 * Created by Andre on 18/05/2015.
 */
public class GamesAdapter extends BaseAdapter {
    Context context;
    ArrayList<Game> games;
    private static LayoutInflater inflater = null;

    public GamesAdapter(Context context, ArrayList<Game> games) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.games = games;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return games.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return games.get(games.size()-1-position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return games.size()-1-position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.custom_row_view, null);
        TextView textName = (TextView) vi.findViewById(R.id.labelName);
        textName.setText(games.get(games.size()-1-position).getName());

        TextView textDate = (TextView) vi.findViewById(R.id.labelDate);
        textDate.setText(games.get(games.size()-1-position).getDate());

        TextView textHour = (TextView) vi.findViewById(R.id.labelHour);
        textHour.setText(games.get(games.size()-1-position).getTime());

        return vi;
    }
}
