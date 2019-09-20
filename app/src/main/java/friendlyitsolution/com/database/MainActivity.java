package friendlyitsolution.com.database;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText etname,etcontact;
    Button btn;
    SQLiteDatabase mydb=null;
    String dbname="mydatabase.db";
    String dbpath="/data/data/friendlyitsolution.com.database/databases/";


    ListView lv;
    ArrayAdapter<String> adapter;
    List<String> numbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mysql my=new mysql(this);
        my.checkMydb();
        createTable();

        btn=findViewById(R.id.btn);
        etname=findViewById(R.id.etname);
        etcontact=findViewById(R.id.etcontact);


        lv=findViewById(R.id.lv);
        numbers=new ArrayList<>();
        adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,numbers);
        lv.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nm=etname.getText().toString();
                String num=etcontact.getText().toString();
                if(!nm.isEmpty() && !num.isEmpty() )
                {
                    addContacttoTable(nm,num);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter input",Toast.LENGTH_LONG).show();
                }

            }
        });


        getContacts();

    }

    void getContacts()
    {
        try{
            numbers.clear();
            String mypath=dbpath+dbname;
            mydb= SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);


            Cursor c=mydb.rawQuery("select * from mytab1",null);

            while(c.moveToNext())
            {
                String name=c.getString(c.getColumnIndex("name"));
                String num=c.getString(c.getColumnIndex("number"));
              //  Toast.makeText(getApplicationContext(),"Data :"+name,Toast.LENGTH_LONG).show();

                numbers.add(name+"\n"+num);
            }


            adapter.notifyDataSetChanged();





        }
        catch(Exception e)
        {

        }

    }

    void createTable()
    {  String mypath = dbpath + dbname;

        try {

            mydb = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);

            mydb.execSQL("CREATE TABLE " + "mytab1" + " (name Text,number TEXT)");
            Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_LONG).show();

        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    void addContacttoTable(String name,String cont)
    {
        String mypath = dbpath + dbname;

        try {

            mydb = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
            mydb.execSQL("insert into mytab1 values('"+name+"','"+cont+"')");

            mydb.close();
            Toast.makeText(getApplicationContext(),"Successfully Insert",Toast.LENGTH_SHORT).show();
            getContacts();


        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }



    }

    void saveContacts(String nm,String num)
    {

        try{
            String mypath=dbpath+dbname;
            mydb= SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);

            mydb.execSQL("insert into mytab1(name,number) values('"+nm+"','"+num+"')");

            Toast.makeText(getApplicationContext(),"Saved.",Toast.LENGTH_LONG).show();
            }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();

        }


    }

}
