package zenn.test.sample.testpedometer;

import java.util.List;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends Activity{

	/** Called when the activity is first created. */
	SensorAdapter ad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("MainActivity", "onCreate");

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
		SensorManager manager = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ALL);
		TextView comment = (TextView) findViewById(R.id.comment);
		String val = "サポートセンサー\n";
		for (Sensor s : sensors) {
			val += s.getName() + "\n";
		}
		comment.setText(val);

		ad = new SensorAdapter(findViewById(R.id.lvaluex),
				findViewById(R.id.lvaluey), findViewById(R.id.lvaluez));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	class SensorAdapter implements SensorEventListener{

		private SensorManager manager;
		private TextView vx;
		private TextView vy;
		private TextView vz;

		public SensorAdapter(View vx, View vy, View vz) {
			manager = (SensorManager) getSystemService(SENSOR_SERVICE);
			this.vx = (TextView) vx;
			this.vy = (TextView) vy;
			this.vz = (TextView) vz;
			List<Sensor> sensors = manager
					.getSensorList(Sensor.TYPE_ACCELEROMETER);
			if (sensors.size() > 0) {
				Sensor s = sensors.get(0);
				manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
			}
		}

		public void stopSensor() {
			manager.unregisterListener(this);
			this.vx = null;
			this.vy = null;
			this.vz = null;
		}

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

				vx.setText("" + event.values[0]);
				vy.setText("" + event.values[1]);
				vz.setText("" + event.values[2]);
			}
		}
	}
}
