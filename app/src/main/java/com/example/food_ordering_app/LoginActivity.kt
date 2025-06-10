package com.example.food_ordering_app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.food_ordering_app.ModelClass.User
import com.example.food_ordering_app.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private var UserName : String? = null
    private lateinit var Email : String
    private lateinit var Password : String
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var database : DatabaseReference



    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        //Intialize Firebase Auth
        auth = Firebase.auth
        //Intialize Firebase database
        database = Firebase.database.reference

        //Initialize google sign in
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)


        binding.GoogleButton.setOnClickListener{
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }
        binding.Login.setOnClickListener{
            Email = binding.editTextTextEmailAddress.text.toString().trim()
            Password = binding.editTextTextPassword.text.toString().trim()

            if(Email.isBlank() || Password.isBlank()){
              Toast.makeText(this,"Please Fill All Details",Toast.LENGTH_SHORT).show()
            }else
            {
                createUserAccount(Email,Password)
            }
        }
        binding.textView11.setOnClickListener{
            val intent = Intent(this,SignUpActivity :: class.java)
            startActivity(intent)
        }

    }

    private fun createUserAccount(email: String, password: String) {
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{ task->
                if(task.isSuccessful)
                {
                    val user : FirebaseUser?= auth.currentUser
                    Toast.makeText(this,"Login Sucessfull",Toast.LENGTH_SHORT).show()
                    UpdateUI(user)
                }
                else{
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task->
                        if(task.isSuccessful)
                        {
                            val user : FirebaseUser ? = auth.currentUser
                            SaveUserDataInDatabase()
                            Toast.makeText(this,"User Registered & Login Sucessfull",Toast.LENGTH_SHORT).show()
                            UpdateUI(user)

                        }
                        else{
                            Toast.makeText(this,"Authentication Failed",Toast.LENGTH_SHORT).show()
                            Log.d("Account","Authentication Failed",task.exception)
                        }
                    }
                }
            }
    }

    private fun SaveUserDataInDatabase() {
        Email = binding.editTextTextEmailAddress.text.toString().trim()
        Password = binding.editTextTextPassword.text.toString().trim()
        UserName = UserName ?: "Username"
        val user = User(UserName!!, Email, Password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)
    }

    @SuppressLint("SuspiciousIndentation")
    private  val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
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
                        UpdateUI(authTask.result?.user)
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
    // Check if user already login
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser!=null)
        {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
    private fun UpdateUI(user: FirebaseUser?) {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}


