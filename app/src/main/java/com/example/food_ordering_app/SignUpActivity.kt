package com.example.food_ordering_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.food_ordering_app.ModelClass.User
import com.example.food_ordering_app.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var UserName : String
    private lateinit var Email : String
    private lateinit var Password : String
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivitySignUpBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase
        Firebase.initialize(this)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        // Initialize Firebase Authentication
        auth = Firebase.auth
        //Initialize database
        database = Firebase.database.reference
        //Initialize google sign in
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)

        binding.CreateAccount.setOnClickListener {
            UserName = binding.editTextName.text.toString().trim()
            Email = binding.editTextTextEmailAddress.text.toString().trim()
            Password = binding.editTextTextPassword.text.toString().trim()

            if (UserName.isBlank() || Email.isBlank() || Password.isBlank()) {
                binding.editTextName.error = "Name is required"
                binding.editTextTextEmailAddress.error = "Email is required"
                binding.editTextTextPassword.error =  "Password is required"
            } else {
                createAccount(UserName, Email, Password)
            }
        }

        binding.googleButton.setOnClickListener{
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
        binding.AlreadyHave.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

            result->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account : GoogleSignInAccount = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener{ authTask->
                    if(authTask.isSuccessful){
                        //Sucessfully sign in with google
                        Toast.makeText(this,"Sucessfully sign in with Google",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this,"Google-Sign in Failed",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Toast.makeText(this,"Google-Sign in Failed",Toast.LENGTH_SHORT).show()

            }
        }

    }
    private fun createAccount(UserName: String, Email: String, Password: String) {
        auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "User Registered Successfully", Toast.LENGTH_SHORT).show()
                SaveUserDataInDatabase(UserName,Email,Password)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "User Not Registered", Toast.LENGTH_SHORT).show()
                Log.e("Error", task.exception.toString())
            }
        }
    }

    private fun SaveUserDataInDatabase(userName: String, email: String, password: String) {

        val user =  User(userName,email,password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)
    }
}

