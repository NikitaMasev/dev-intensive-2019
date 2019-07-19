package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.extensions.isKeyboardOpen
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity(), View.OnClickListener, TextView.OnEditorActionListener {

    private lateinit var llRootView : LinearLayout
    private lateinit var sendBtn: ImageView
    private lateinit var messageEt: EditText
    private lateinit var textTxt: TextView
    private lateinit var benderImage: ImageView

    private lateinit var benderObj: Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        sendBtn = iv_send
        llRootView = ll_root

        val status = savedInstanceState?.getString("STATUS") ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION") ?: Bender.Question.NAME.name

        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question))

        Log.d("MainActivity", "onCreate $status $question")
        val (r, g, b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)

        textTxt.text = benderObj.askQuestion()
        sendBtn.setOnClickListener(this)
        messageEt.setOnEditorActionListener(this)
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("MainActivity", "onRestart")
    }

    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("MainActivity", "onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("STATUS", benderObj.status.name)
        outState?.putString("QUESTION", benderObj.question.name)

        Log.d("MainActivity", "onSaveInstanceState ${benderObj.status.name} ${benderObj.question.name}")
        super.onSaveInstanceState(outState)
    }


    override fun onClick(v: View?) {
        if (isKeyboardOpen()) {
            Log.d("MainActivity","OPEN")
        } else {
            Log.d("MainActivity","CLOSED")
        }

        hideKeyboard()
        if (v?.id == R.id.iv_send) {
            updateBender()
        }
    }

    override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
        return when (p1) {
            EditorInfo.IME_ACTION_DONE -> {
                hideKeyboard()
                updateBender()
                return true
            }
            else -> false
        }
    }

    private fun updateBender() {
        val answer = messageEt.text.toString()

        if(answer.trim().isEmpty()) {
            messageEt.setText("")
            return
        }

        val validationAnswer = benderObj.validation(answer)

        if (validationAnswer.isEmpty()) {
            val (phrase, color) = benderObj.listenAnswer(answer.trim().toLowerCase())
            messageEt.setText("")

            val (r, g, b) = color
            benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)

            textTxt.text = phrase
        } else {
            messageEt.setText("")
            textTxt.text = validationAnswer
        }
    }
}
