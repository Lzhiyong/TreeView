/*
 * Copyright © 2022 Github Lzhiyong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.widget.treeview

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import com.widget.treeview.databinding.ActivityMainBinding

import java.io.File


public class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    private val TAG = this::class.simpleName
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate and get instance of binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        
        // set content view to binding's root
        setContentView(binding.root)
        
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        if(!hasPermission(permission)) {
            applyPermission(permission)
        }
        
        with(binding.recyclerView) {
            // your root directory, external or internal directory
            // android 10+ can't access the sdcard directory
            var root = getExternalFilesDir(null)!!
            
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                // sdcard root directory
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    root = Environment.getExternalStorageDirectory()
                }
            }
            
            setItemAnimator(null)
            
            val nodes = TreeViewAdapter.merge(root)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = TreeViewAdapter(this@MainActivity, nodes).apply {
                setOnItemClickListener(object: OnItemClickListener {
                    override fun onItemClick(v: View, position: Int) {
                        // TODO
                        Toast.makeText(this@MainActivity, "onItemClick", Toast.LENGTH_SHORT).show()
                    }
                
                    override fun onItemLongClick(v: View, position: Int) {
                        // TODO
                        Toast.makeText(this@MainActivity, "onItemLongClick", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
    
    fun hasPermission(permission: String): Boolean {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        else
            return true
    }

    fun applyPermission(permission: String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(shouldShowRequestPermissionRationale(permission)) {
                Toast.makeText(this@MainActivity, "request read sdcard permmission", Toast.LENGTH_SHORT).show()
            }
            requestPermissions(arrayOf(permission), 0)
        }
    }
}

