package com.example.gohome

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.gohome.databinding.ActivityMainBinding

import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var navController: NavController
    private  var resultLauncher =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val response = IdpResponse.fromResultIntent(it.data)

        if (it.resultCode == RESULT_OK){
            val user = FirebaseAuth.getInstance()
            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
        }else{
            if(response == null){
                Toast.makeText(this, "Hasta pronto", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                response.error?.let {
                    if(it.errorCode == ErrorCodes.NO_NETWORK){
                        Toast.makeText(this, "Sin conexión a internet", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "Código de error ${it.errorCode}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    //Theme.MaterialComponents.DayNight.DarkActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GoHome)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configAuth()
        navegacion()
        myNavigation()

    }

    private fun navegacion() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun myNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)
        bottomNavigationView.setupWithNavController(navController)

    }

    //configuracion con FirebaseUI
    private fun configAuth() {
        firebaseAuth = FirebaseAuth.getInstance()
        authStateListener = FirebaseAuth.AuthStateListener { auth->
            if(auth.currentUser != null){
               // supportActionBar?.title = "Hola, ${auth.currentUser?.displayName}" //titulo del Barra

               // binding.llProgres.visibility = View.GONE

             //   binding.txtInicio.visibility = View.VISIBLE // ocultar contenedores en esta parte -> Space
            }else{
                val providers = arrayListOf(    //  PROVIDERS
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build())


                val loginView = AuthMethodPickerLayout.Builder(R.layout.login_view)
                    .setEmailButtonId(R.id.btnLoginEmail)
                    .setGoogleButtonId(R.id.btnGoogle)
                    .setTosAndPrivacyPolicyId(R.id.tv_TerminosCondiciones)
                    .build()

               resultLauncher.launch(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                   .setTosAndPrivacyPolicyUrls("https://sites.google.com/view/politica-de-privacidad-gohome/p%C3%A1gina-principal",
                    "https://sites.google.com/view/politica-de-privacidad-gohome/p%C3%A1gina-principal")
                   .setAuthMethodPickerLayout(loginView)
                   .setTheme(R.style.login_theme)
                   .build())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onPause() {
        super.onPause()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_sing_out -> {
                AuthUI.getInstance().signOut(this)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Sesión terminada", Toast.LENGTH_SHORT).show()
                    }
                    .addOnCompleteListener { 
                        if (it.isSuccessful){
                          //  binding.txtInicio.visibility = View.GONE
                          //  binding.llProgres.visibility = View.VISIBLE
                        }else
                            Toast.makeText(this, "No se pudo cerrar la sesión", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}