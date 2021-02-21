package com.zelyder.stocksapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zelyder.stocksapp.R
import com.zelyder.stocksapp.presentation.stockslist.StocksListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.main_container, StocksListFragment())
                    .commit()
        }
    }
}