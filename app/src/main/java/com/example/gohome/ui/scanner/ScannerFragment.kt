package com.example.gohome.ui.scanner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.gohome.databinding.FragmentScannerBinding
import com.google.android.material.transition.MaterialSharedAxis
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator

class ScannerFragment : Fragment() {
    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, /* forward= */ true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, /* forward= */ false)
        _binding = FragmentScannerBinding.inflate(inflater, container, false)
        //  binding.btnScanner.setOnClickListener {
        //    initScanner()
        //  }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = FirebaseFirestore.getInstance()
        binding.btnSearch.setOnClickListener {

            try {

                idFirestore(db)


            } catch (e: Exception) {
                Toast.makeText(
                    this@ScannerFragment.requireActivity(),
                    "Error, no se encontró el registro",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun dial(phone: String) {
        val callIntent = Intent().apply {
            action = Intent.ACTION_DIAL
            data = Uri.parse("tel: $phone")
        }
        startActivity(callIntent)

    }

    private fun initScanner() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Escanea el código QR grande ♥ GoHome")
        integrator.setBeepEnabled(false)
            .initiateScan()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val dataId = binding.txtId.toString().trim()
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(
                    this@ScannerFragment.requireActivity(),
                    "No funciona",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@ScannerFragment.requireActivity(),
                    "Ahora presiona Buscar",
                    Toast.LENGTH_SHORT
                ).show()
                binding.txtId.text = result.contents
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun idFirestore(db: FirebaseFirestore) {
        db.collection("GoHomeCollection").document(binding.txtId.text.toString()).get()
            .addOnSuccessListener { document ->

                if (document.exists()) {

                    val arrayAdapter: ArrayAdapter<*> //indicar que el tipo de dato es cualquiera
                    val datos = mutableListOf(
                        document.getString("nombre"),
                        document.getString("edad"),
                        document.getString("genero"),
                        document.getString("direccion1"),
                        document.getString("direccion2"),
                        document.getString("telefono1"),
                        document.getString("telefono2"),
                        document.getString("alergia"),
                        document.getString("sangre"),
                        document.getString("discapacidad"),
                        document.getString("otros")
                    )

                    arrayAdapter = ArrayAdapter(
                        this@ScannerFragment.requireActivity(),
                        android.R.layout.simple_list_item_1, datos
                    )
                    //traer datos del documento
                    val nombre: String? = document.getString("nombre")
                    val edad: String? = document.getString("edad")
                    val sangre: String? = document.getString("sangre")
                    val genero: String? = document.getString("genero")
                    val direccion1: String? = document.getString("direccion1")
                    val direccion2: String? = document.getString("direccion2")
                    val telefono1: String? = document.getString("telefono1")
                    //  binding.btnLlamar.setOnClickListener {
                    //    val call = Intent().apply {
                    //      action = Intent.ACTION_DIAL
                    //    data = Uri.parse("tel: $telefono1")
                    //  }
                    //  startActivity(call)
                }
                val telefono2: String? = document.getString("telefono2")
                //   binding.btnllamar2.setOnClickListener {
                //     val calltwo = Intent().apply {
                //       action = Intent.ACTION_DIAL
                //     data = Uri.parse("tel: $telefono2")
                // }
                //   startActivity(calltwo)
            }
        //    val discapacidad: String? = document.getString("discapacidad")
        // val alergia: String? = document.getString("alergia")
        //  val otros: String? = document.getString("otros")

        //} else {
        Toast.makeText(
            this@ScannerFragment.requireActivity(),
            "No existe el documento",
            Toast.LENGTH_SHORT
        ).show()
    }
    //}

}
