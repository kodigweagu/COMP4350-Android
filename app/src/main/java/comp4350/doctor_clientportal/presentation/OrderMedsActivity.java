package comp4350.doctor_clientportal.presentation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import comp4350.doctor_clientportal.R;

public class OrderMedsActivity extends AppCompatActivity {

    public final static String apiURL = "http://ec2-52-32-93-246.us-west-2.compute.amazonaws.com/api/";

    MaterialNumberPicker numberPicker;
    View dialogView;
    TextView name_text_view;
    TextView quantity_text_view;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_meds);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initt();
        addActionListener();
    }

    private void initt()
    {
        dialogView = getLayoutInflater().inflate(R.layout.number_picker_view, null);
        numberPicker = (MaterialNumberPicker)dialogView.findViewById(R.id.number_picker);
        name_text_view = (TextView)findViewById(R.id.med_name_edit);
        quantity_text_view = (TextView)findViewById(R.id.quantity_edit);
        submit = (Button)findViewById(R.id.med_submit);
    }

    private void addActionListener()
    {

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(attemptSave())
                {
                    //do stuff here
                    String name = name_text_view.getText().toString();
                    String quantity = quantity_text_view.getText().toString();
                    //Toast.makeText(OrderMedsActivity.this, name+" "+quantity, Toast.LENGTH_LONG).show();

                    final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    JSONObject postData = new JSONObject();
                    JSONObject data = new JSONObject();


                    try {
                        postData.put("name", name);
                        postData.put("quantity", quantity);

                        data.put("data", postData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.POST, apiURL + "medication", data, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray jsonArray = response.getJSONArray("data");

                                        //after saving data
                                        Toast.makeText(OrderMedsActivity.this, jsonArray.getString(0), Toast.LENGTH_LONG).show();
                                        finish();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO Auto-generated method stub
                                    //uiUpdate.setText("Response: " + error.toString());
                                    Toast.makeText(OrderMedsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                    // Add the request to the RequestQueue.
                    queue.add(jsObjRequest);
                }
            }
        });


    }

    private boolean attemptSave()
    {
        boolean attempt = true;
        if (name_text_view.getText().toString().isEmpty())
        {
            name_text_view.setError(getString(R.string.error_field_required));
            attempt = false;
        }
        if (quantity_text_view.getText().toString().isEmpty())
        {
            quantity_text_view.setError(getString(R.string.error_field_required));
            attempt = false;
        }

        return attempt;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
