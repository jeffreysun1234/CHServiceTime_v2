package com.mycompany.chservicetime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.mycompany.chservicetime.databinding.ActivityMainBinding
import com.mycompany.chservicetime.utilities.DNDControllerHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        setSupportActionBar(toolbar)

        val navController: NavController = findNavController(R.id.nav_host)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        DNDControllerHelper.checkDndPermissionCompat(this, true)
    }
}
