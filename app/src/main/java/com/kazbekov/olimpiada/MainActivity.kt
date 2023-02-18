package com.kazbekov.olimpiada

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    companion object {
        const val WARNING_TYPE_1 = 1 //Не удалось скачать файл
        const val WARNING_TYPE_2 = 2 //Внешнее хранилище не доступно
    }
}