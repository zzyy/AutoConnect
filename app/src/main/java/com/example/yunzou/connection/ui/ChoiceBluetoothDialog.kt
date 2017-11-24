package com.example.yunzou.connection.ui

import android.app.Dialog
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.example.yunzou.connection.util.BluetoothUtils

/**
 * Created by Simon on 17/11/14.
 */
class ChoiceBluetoothDialog(context: Context, val callback: (device: BluetoothDevice) -> Unit) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("choice bluetooth")
        val listView = ListView(context)
        val adapter = Adapter(context)
        listView.adapter = adapter
        listView.setOnItemClickListener({ parent, view, position, id ->
            callback(adapter.getItem(position))
            this.dismiss()
        })
        setContentView(listView)
    }


    class Adapter(val context: Context) : BaseAdapter() {
        var inflate: LayoutInflater? = null
        val boundedDevices = BluetoothUtils.getBoundedDevices()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            if (inflate == null) {
                inflate = LayoutInflater.from(context)
            }
            val itemData = getItem(position)
            val view = inflate!!.inflate(android.R.layout.simple_list_item_1, parent, false)
            view.findViewById<TextView>(android.R.id.text1).text = "${itemData.name} \n${itemData.address}"
            return view
        }

        override fun getItem(position: Int): BluetoothDevice {
            return boundedDevices.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return boundedDevices.size
        }

    }

}