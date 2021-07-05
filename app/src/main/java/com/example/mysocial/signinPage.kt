package com.example.mysocial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.mysocial.model.User
import com.example.mysocial.model.UserDao
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.squareup.okhttp.Dispatcher
import kotlinx.android.synthetic.main.activity_signin_page.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

//change in manifest to start it first


class signinPage : AppCompatActivity() {
    lateinit var auth:FirebaseAuth
    val sign_in_code=123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_page)

        auth= FirebaseAuth.getInstance()


        googlesignin.setOnClickListener {
            val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val signinClient= GoogleSignIn.getClient(this,options)     //client also contain intent
            signinClient.signInIntent.also {
                startActivityForResult(it,sign_in_code)
            }

        }

    }

    override fun onStart() {
        super.onStart()
        val user=auth.currentUser       //firebase user in auth data
        if(user!=null){
            updateUI(user)
        }
    }



    private fun googleAuthForFirebase(account:GoogleSignInAccount){  //for adding these account to firebase
        val credentials=GoogleAuthProvider.getCredential(account.idToken,null)  //
        /*progress bar handling*/
        googlesignin.visibility=View.GONE
        progressbar.visibility=View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithCredential(credentials).await()           //couroties support await,will wait until data is stored in firebase
                val FirebaseUser=auth.currentUser      //current user data in firebase auth
                withContext(Dispatchers.Main){

                    updateUI(FirebaseUser!!)                    //null check
                    /*
                    val intent=Intent(this@signinPage,MainActivity::class.java)
                     startActivity(intent)
                    finish()
                    //Toast.makeText(this@signinPage,"Login Successful", Toast.LENGTH_SHORT).show()
                    */
                }
            } catch (e:Exception){
                withContext(Dispatchers.Main){                      //main activity context because we cannot access ui element from another thread
                    googlesignin.visibility=View.VISIBLE
                    progressbar.visibility=View.GONE

                    Toast.makeText(this@signinPage, e.message, Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun updateUI(firebaseuser:FirebaseUser){
        val user1=User(firebaseuser.uid,firebaseuser.displayName.toString(),firebaseuser.photoUrl.toString())
        val dao=UserDao()                 //dao object
        dao.addUser(user1)

        Intent(this@signinPage,MainActivity::class.java).also {
            startActivity(it)
        }
        finish()
        //Toast.makeText(this@signinPage,"Login Successful", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==sign_in_code){   //we will not check result code
            val account=GoogleSignIn.getSignedInAccountFromIntent(data).result     //Google account we get after login
            account?.let {
                googleAuthForFirebase(it)
            }

        }
    }



}