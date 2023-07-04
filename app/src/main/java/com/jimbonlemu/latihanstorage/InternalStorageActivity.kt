package com.jimbonlemu.latihanstorage

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import java.io.FileInputStream
import java.io.FileOutputStream

class InternalStorageActivity : AppCompatActivity() {

    private lateinit var btnCreate: MaterialButton
    private lateinit var btnEdit: MaterialButton
    private lateinit var btnRead: MaterialButton
    private lateinit var btnDel: MaterialButton
    private lateinit var textCol: EditText
    private val fileName = "my_file.txt"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_internal)
        supportActionBar?.title = "Latihan Storage"

        btnCreate = findViewById(R.id.createBtn)
        btnEdit = findViewById(R.id.editBtn)
        btnRead = findViewById(R.id.readBtn)
        btnDel = findViewById(R.id.delBtn)
        textCol = findViewById(R.id.textCol)

        btnCreate.setOnClickListener {
            createFile()
        }

        btnEdit.setOnClickListener {
            editFile()
        }

        btnRead.setOnClickListener {
            readFile()
        }
        btnDel.setOnClickListener {
            deleteFile()
        }

        textCol.isEnabled = false

    }

    private fun createFile() {

        val text = textCol.text.toString()
        textCol.isEnabled = true
        val fileStream: FileOutputStream

        try {
            if (text.isNullOrEmpty()) {
                toaster("Kolom Teks tidak boleh kosong")
                textCol.isEnabled = true
            } else {
                fileStream = openFileOutput(fileName, Context.MODE_PRIVATE)
                fileStream.write(text.toByteArray())
                fileStream.close()
                toaster("File berhasil tersimpan")
                textCol.setText("")
                textCol.isEnabled = false
            }
            
        } catch (e: Exception) {
            e.printStackTrace()
            toaster("File Gagal Tersimpan")
        }

    }

    private fun editFile() {
        val fileStream: FileInputStream
        val textBuilder = StringBuilder()

        try {
            fileStream = openFileInput(fileName)
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (fileStream.read(buffer).also { bytesRead = it } != -1) {
                val text = String(buffer, 0, bytesRead)
                textBuilder.append(text)
            }
            fileStream.close()

            val fileContent = textBuilder.toString()
            textCol.setText(fileContent)
            textCol.isEnabled = true
            toaster("Teks berhasil diubah")
        } catch (e: Exception) {
            e.printStackTrace()
            toaster("File Gagal dibaca")
        }

    }

    private fun readFile() {
        val textBuilder = StringBuilder()


        try {
            val fileStream = openFileInput(fileName)
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (fileStream.read(buffer).also { bytesRead = it } != -1) {
                val text = String(buffer, 0, bytesRead)
                textBuilder.append(text)
            }
            fileStream.close()

            val fileContent = textBuilder.toString()
            textCol.setText(fileContent)
            textCol.isEnabled = false
            toaster("Anda sedang membaca file")

        } catch (e: Exception) {
            e.printStackTrace()
            toaster("File Gagal dibaca")

        }

    }

    private fun deleteFile() {

        val file = getFileStreamPath(fileName)
        if (file.exists()) {
            file.delete()
            toaster("File Berhasil dihapus")
            textCol.setText("")
            textCol.isEnabled = false
        } else {
            toaster("File Tidak ditemukan")
        }

    }

    private fun toaster(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}