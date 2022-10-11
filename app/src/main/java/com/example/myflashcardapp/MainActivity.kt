package com.example.myflashcardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    lateinit var flashcardDatabase: FlashcardDatabase
    var allFlashcard = mutableListOf<Flashcard>()

    var CurrCardDisplayedIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flashcardDatabase = FlashcardDatabase(this)
        allFlashcard = flashcardDatabase.getAllCards().toMutableList()

        val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        val flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)

        if (allFlashcard.size > 0) {
            flashcardQuestion.text = allFlashcard[0].question
            flashcardAnswer.text = allFlashcard[0].answer
        }

        flashcardQuestion.setOnClickListener {
            flashcardAnswer.visibility = View.VISIBLE
            flashcardQuestion.visibility = View.INVISIBLE

//            Toast.makeText(this, "Question button was clicked", Toast.LENGTH_SHORT).show()

            Snackbar.make(flashcardQuestion, "Question button was clicked",Snackbar.LENGTH_SHORT).show()

            Log.i("Van Hung", "Question button was clicked")
        }

        flashcardAnswer.setOnClickListener {
            flashcardAnswer.visibility = View.INVISIBLE
            flashcardQuestion.visibility = View.VISIBLE
        }

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result ->

            val data: Intent? = result.data

            if (data != null){
                val questionString = data.getStringExtra("QUESTION_KEY")
                val answerString = data.getStringExtra("ANSWER_KEY")

                flashcardQuestion.text = questionString
                flashcardAnswer.text = answerString

                Log.i("Van Hung: MainActivity", "question: $questionString")
                Log.i("Van Hung: MainActivity", "answer: $answerString")

                if (!questionString.isNullOrEmpty() && !answerString.isNullOrEmpty()) {
                    flashcardDatabase.insertCard(Flashcard(questionString, answerString))
                    allFlashcard = flashcardDatabase.getAllCards().toMutableList()
                }
            } else{
                Log.i("Van Hung: MainActivity", "Returned null data from AddCardActivity")
            }
        }

        val addQuestionButton = findViewById<ImageView>(R.id.add_question_button)
        addQuestionButton.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
        }

        val nextButton = findViewById<ImageView>(R.id.flashcard_next_button)
        nextButton.setOnClickListener {
            if (allFlashcard.isEmpty()){
                // early return so that the rest of the code doesn't execute
                return@setOnClickListener
            }
            CurrCardDisplayedIndex++

            if (CurrCardDisplayedIndex >= allFlashcard.size) {
                // go back to the beginning
                CurrCardDisplayedIndex = 0
            }

            allFlashcard = flashcardDatabase.getAllCards().toMutableList()

            val question = allFlashcard[CurrCardDisplayedIndex].question
            val answer = allFlashcard[CurrCardDisplayedIndex].answer

            flashcardQuestion.text = question
            flashcardAnswer.text = answer
        }
    }
}