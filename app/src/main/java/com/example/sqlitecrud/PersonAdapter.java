package com.example.sqlitecrud;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonAdapter extends ArrayAdapter<Person> {

    private Activity context;
    private List<Person> personList;
    private DbHandler dbHandler;
    private int listLayout;

    public PersonAdapter(@NonNull Activity context, int resource, @NonNull List<Person> object, DbHandler dbHandler) {
        super(context, resource,object);
        this.context=context;
        this.dbHandler=dbHandler;
        this.personList=object;
        this.listLayout=resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final LayoutInflater inflater=LayoutInflater.from(context);
        final View view=inflater.inflate(listLayout,null);

        final  Person person=personList.get(position);

        TextView txtName=view.findViewById(R.id.txt_name_raw);
        TextView txtCity=view.findViewById(R.id.txt_city_raw);
        TextView txtAge=view.findViewById(R.id.txt_Age_raw);
        Button btnEdit=view.findViewById(R.id.btn_edit);
        Button btnDelete=view.findViewById(R.id.btn_del);
        CircleImageView img_prof= view.findViewById(R.id.img_prof);
        
        txtName.setText(person.getName());
        txtCity.setText(person.getCity());
        txtAge.setText(String.valueOf(person.getAge()));
        
        final TypedArray imgs =context.getResources().obtainTypedArray(R.array.apptour);
        final Random rand = new Random();
        final int rndInt = rand.nextInt(imgs.length());
        final int resID = imgs.getResourceId(rndInt, 0);
        img_prof.setImageResource(resID);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder  builder=new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.ic_trash);
                builder.setTitle("Are You Sure?");
                builder.setMessage("your information will delete...");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dbHandler.deletePerson(person.getId());
                        reloadPerson();
                        Toast.makeText(context, "Person deleted...", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                LayoutInflater inflater1=LayoutInflater.from(context);
                @SuppressLint("ViewHolder") View view1=inflater1.inflate( R.layout.dialog_update,null);
                builder.setView(view1);
                final AlertDialog editDialog=builder.create();
                editDialog.show();

                final EditText edt_name_update,edt_city_update,edt_age_update;
                Button btn_update_dialog,btn_cancel_dialog;

                edt_name_update=editDialog.findViewById(R.id.edt_name_update);
                edt_city_update=editDialog.findViewById(R.id.edt_city_update);
                edt_age_update=editDialog.findViewById(R.id.edt_age_update);

                edt_name_update.setText(person.getName());
                edt_city_update.setText(person.getCity());
                edt_age_update.setText(String.valueOf(person.getAge()));

                btn_update_dialog=editDialog.findViewById(R.id.btn_update_dialog);
                btn_update_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name=edt_name_update.getText().toString();
                        String city = edt_city_update.getText().toString();
                        int age = Integer.parseInt(edt_age_update.getText().toString());

                        try {
                            boolean res= dbHandler.updatePerson(person.getId(),name,city,age);
                            reloadPerson();
                            editDialog.dismiss();

                            if (res){
                                Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "not updated", Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            Toast.makeText(context, e.toString()+"", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                btn_cancel_dialog=editDialog.findViewById(R.id.btn_cancel_dialog);

                btn_cancel_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editDialog.dismiss();
                    }
                });





            }
        });
        
        
        return view;
    }


    void reloadPerson(){
        personList.clear();
        personList=dbHandler.getPersonList();
        notifyDataSetChanged();
    }
}
