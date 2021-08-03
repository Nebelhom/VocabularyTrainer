package com.example.vocabularytrainer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlin.system.exitProcess


// TODO
// Choose random vocabulary item and populate lang1 and lang2 on press start/next
// Show the other side on question

// Filter by unit and word type
// allow switch between language directions
// make the arrow point in the fitting direction

public final class VocabularyTrainer(private val context: Context) {
    // import the baseline
    val basevocabulary: List<List<String>> = readVocabCSV()
    // working vocabulary ready to be filtered
    var vocablist: List<List<String>> = basevocabulary.drop(1)
    // The item displayed on the app
    var vocab: List<String> = vocablist[0]
    // Headings to be used to populate GUI
    val headers: List<String> = basevocabulary[0]

    fun createSet(index: Int): List<String>{
        // takes val basevocabulary, and based on index of the nested list item creates a set
        // of unique values
        val valueSet = mutableSetOf<String>("All")

        for (item in basevocabulary){
            valueSet.add(item[index])
        }
        // `it` selects to sort ascending
        return valueSet.sortedBy { it }
    }

    fun readVocabCSV(): List<List<String>> {
        return csvReader{delimiter = ';'}.readAll(context.assets.open("vocab.csv"))
    }

    fun switchVocab(){
        vocab = vocablist.random()
    }
}


class MainActivity : AppCompatActivity() {

    //val vc = VocabularyTrainer(applicationContext)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val vc = VocabularyTrainer(applicationContext)

        // Populate the GUI
        val lang1: TextView = findViewById(R.id.textViewLang1)
        lang1.text = vc.headers[0]
        val lang1a: TextView = findViewById(R.id.textViewLang1a)
        lang1a.text = vc.headers[0]

        val lang2: TextView = findViewById(R.id.textViewLang2)
        lang2.text = vc.headers[1]
        val lang2a: TextView = findViewById(R.id.textViewLang2a)
        lang2a.text = vc.headers[1]

        // Set the Vocabulary containers
        val vocab1: EditText = findViewById(R.id.Vocab1)
        val vocab2: EditText = findViewById(R.id.Vocab2)

        // Create set of all wordtypes
        var wordTypes: List<String> = vc.createSet(2)

        // https://tutorialwing.com/android-spinner-using-kotlin-with-example/
        // Populates the Spinner and ties a function to it.
        val spinword = findViewById<Spinner>(R.id.spinnerwordtype)
        if (spinword != null) {
            val wtarrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, wordTypes)
            spinword.adapter = wtarrayAdapter

            spinword.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    println(wordTypes[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    println("NOTHING!!!")
                }
            }
            spinword.setSelection(wordTypes.indexOf("All"))
        }

        // Create set of all units
        var units: List<String> = vc.createSet(3)

        // https://tutorialwing.com/android-spinner-using-kotlin-with-example/
        // Populates the Spinner and ties a function to it.
        val spinunit = findViewById<Spinner>(R.id.spinnerunit)
        if (spinunit != null) {
            val wtarrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
            spinunit.adapter = wtarrayAdapter

            spinunit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    println(units[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    println("NOTHING!!!")
                }
            }
            spinunit.setSelection(units.indexOf("All"))
        }

        println("================")
        println("================")
        println("================")
        println(vc.headers)
        println(wordTypes)
        println(units)
        println(vc.vocab) // how to obtain a random item --> link function to nextbtn
        vc.switchVocab()
        println(vc.vocab)
        println("================")
        println("================")
        println("================")

        val nextbtn: Button = findViewById(R.id.nextbtn)
        nextbtn.setOnClickListener {

            if (nextbtn.text == "Start"){
                nextbtn.setText("Next")
            }

            if (vocab2.text.toString() != "") {
                vocab2.setText("")
            }

            vc.switchVocab()
            vocab1.setText(vc.vocab[0])
        }

        val showbtn: Button = findViewById(R.id.showbtn)
        showbtn.setOnClickListener {
            vocab2.setText(vc.vocab[1])
        }
    }

    fun quitApp(view: View) {
        this@MainActivity.finish()
        exitProcess(0)
    }


}