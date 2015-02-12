package com.libopenmw.openmw;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import com.libopenmw.openmw.ParseJson.FilesData;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PluginsView extends Activity {

	private Context context;
	public static List<FilesData> Plugins;
	public boolean check = false;
	String name;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		try {

			Plugins = ParseJson.loadFile();
			File yourDir = new File(MainActivity.dataPath);

			checkFilesDeleted(yourDir);
			
			for (File f : yourDir.listFiles()) {

				boolean newPlugin = true;
				for (FilesData data : Plugins) {
					if (f.isFile() && f.getName().contains(data.name)) {

						newPlugin=false;
						break;

					} else
						newPlugin = true;
				         
				}
				   if (newPlugin ){
					   FilesData pluginData = new FilesData();
					      
					   pluginData.name = f.getName();
						pluginData.nameBsa = f.getName().split("\\.")[0] + ".bsa";
						if (f.getName().contains("Morrowind.esm"))
								Plugins.add(0, pluginData);
						else
						
						if (f.getName().contains("Bloodmoon.esm"))
								Plugins.add(1, pluginData);
						else
						if (f.getName().contains("Tribunal.esm"))
							Plugins.add(2, pluginData);
						
			       
			       }
			}
				
			Plugins = null;

			try {
				Plugins = ParseJson.loadFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			context = this;

			ListView listView = (ListView) findViewById(R.id.listView1);

			listView.setAdapter(new Adapter());
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "no data files found",
					Toast.LENGTH_LONG).show();
			finish();
		}

	}

	public void savePlugins(View v) throws IOException {
		List<FilesData> plugins = null;

		try {
			plugins = ParseJson.loadFile();

			FileWriter writer = new FileWriter(MainActivity.configsPath
					+ "/openmw/openmw.cfg");

			int i = 0;
			while (i < plugins.size()) {

				if (plugins.get(i).enabled == 1) {
					writer.write("content= " + plugins.get(i).name + "\n");
					writer.write("fallback-archive= " + plugins.get(i).nameBsa
							+ "\n");

					writer.flush();
				}
				i++;

			}
			writer.close();
			Toast toast = Toast.makeText(getApplicationContext(),
					"Saving done", Toast.LENGTH_LONG);
			toast.show();

		} catch (Exception e) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"config file openmw.cfg not found", Toast.LENGTH_LONG);
			toast.show();
			e.printStackTrace();
		}
	}
	
	
	private void checkFilesDeleted (File yourDir) throws JSONException, IOException{
		int index = 0;
		for (FilesData data : Plugins) {
			boolean fileDeleted = true;

			for (File f : yourDir.listFiles()) {

				if (f.isFile() && f.getName().contains(data.name)) {

					fileDeleted = false;
					break;

				} else
					fileDeleted = true;

			}

			if (fileDeleted) {
				Plugins.remove(index);
			}
			index++;
		}
		if (Plugins.size() < index)
			ParseJson.saveFile(Plugins);

	}

	public class Adapter implements ListAdapter

	{

		public View rowView;

		@Override
		public boolean isEmpty() {

			// TODO Auto-generated method stub

			return false;

		}

		@Override
		public boolean hasStableIds() {

			// TODO Auto-generated method stub

			return false;

		}

		@Override
		public int getViewTypeCount() {

			// TODO Auto-generated method stub

			return 1;

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			rowView = inflater.inflate(R.layout.rowlistview, parent, false);

			TextView data = (TextView) rowView.findViewById(R.id.textView1);
			TextView bsa = (TextView) rowView.findViewById(R.id.textViewBsa);
			TextView enabled = (TextView) rowView
					.findViewById(R.id.textViewenabled);

			final CheckBox Box = (CheckBox) rowView
					.findViewById(R.id.checkBoxenable);
			enabled.setText(String.valueOf(Plugins.get(position).enabled));

			if (Plugins.get(position).enabled == 1)
				Box.setChecked(true);

			Box.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (Box.isChecked()) {
						FilesData data = new FilesData();
						data.enabled = 1;
						ParseJson.pos = position;
						try {
							ParseJson.updatetofile(data);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {

						FilesData data = new FilesData();
						data.enabled = 0;
						ParseJson.pos = position;
						try {
							ParseJson.updatetofile(data);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}

			});

			data.setText(Plugins.get(position).name);
			return rowView;

		}

		@Override
		public int getItemViewType(int position) {

			// TODO Auto-generated method stub

			return 0;

		}

		@Override
		public long getItemId(int position) {

			// TODO Auto-generated method stub

			return 0;

		}

		@Override
		public Object getItem(int position) {

			// TODO Auto-generated method stub

			return null;

		}

		@Override
		public int getCount() {

			return Plugins.size();

		}

		@Override
		public boolean isEnabled(int position) {

			// TODO Auto-generated method stub

			return false;

		}

		@Override
		public boolean areAllItemsEnabled() {

			// TODO Auto-generated method stub

			return false;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver arg0) {
			// TODO Auto-generated method stub

		}

	}

}
