package unicam.pi.mqespol.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import unicam.pi.mqespol.model.LocalBroker;
import unicam.pi.mqespol.R;
import unicam.pi.mqespol.databinding.ActivityMainBinding;
import unicam.pi.mqespol.util.NetworkUtil;
//implements NavigationView.OnNavigationItemSelectedListener
import android.view.Menu;
import android.view.MenuItem;
public class MainActivity extends AppCompatActivity  {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LocalBroker l = new LocalBroker();
        setSupportActionBar(binding.toolbar);
        init();

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        System.out.println("FAFAFSASFASDFSASDFASFDA******"+NetworkUtil.getInstance().getIP());
    }
    private void init(){
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
//AQUI VAN LOS METODOS PARA LAS FUNCIONES.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_dispositivo) {
            Navigation.findNavController(this,R.id.nav_host_fragment_content_main).navigate(R.id.navegarFragment_addDevice);
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch ((item.getItemId())){
//            case R.id.add_dispositivo:{
//                Navigation.findNavController(this,R.id.nav_host_fragment_content_main).navigate(R.id.action_FirstFragment_to_SecondFragment);
//                break;
//            }
//        }
//        return false;
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}