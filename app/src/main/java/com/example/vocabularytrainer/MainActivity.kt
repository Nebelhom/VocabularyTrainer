package com.example.vocabularytrainer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlin.system.exitProcess

class VocabularyTrainer(private val context: Context) {
    // import the baseline
    private val basevocabulary: List<List<String>> = readVocabCSV()
    // working vocabulary ready to be filtered
    private var vocablist: List<List<String>> = basevocabulary.drop(1)
    // The item displayed on the app
    var vocab: List<String> = vocablist[0]
    // Headings to be used to populate GUI
    val headers: List<String> = basevocabulary[0]

    fun createSet(index: Int): List<String>{
        // takes val basevocabulary, and based on index of the nested list item creates a set
        // of unique values
        val valueSet = mutableSetOf<String>("All")

        for (item in basevocabulary.drop(1)){
            valueSet.add(item[index])
        }
        // `it` selects to sort ascending
        return valueSet.sortedBy { it }
    }

    fun filterVocabList(keywordtype: String, keywordlesson: String){
        /*
        Functions filters vocablist according to given keywords. It always filters first by type
        then by lesson number.

        "All" means all types or lessons

        Index: 2 = wordtypes
        Index: 3 = Lesson
         */

        // Filter for wordtype
        vocablist = basevocabulary.drop(1)

        if (keywordtype != "All") {
            vocablist = basevocabulary.drop(1).filter { it[2] == keywordtype }
        }

        // Filter for lesson
        if (keywordlesson != "All") {
            vocablist = vocablist.filter { it[3] == keywordlesson }
        }

        if (vocablist.isNullOrEmpty()) {
            val templist = listOf<String>("No Vocabulary Found", "Change Filters")
            vocablist = listOf(templist)
        }
    }

    private fun readVocabCSV(): List<List<String>> {
        return csvReader{delimiter = ';'}.readAll(context.assets.open("vocab.csv"))
    }

    fun switchVocab(){
        vocab = vocablist.random()
    }
}


class MainActivity : AppCompatActivity() {

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

        // Determines "direction" of language
        // lang1 shown, guess lang2 or vice versa
        var reverseLang: Boolean = false

        // Create set of all wordtypes
        val wordTypes: List<String> = vc.createSet(2)
        // Create set of all units
        val units: List<String> = vc.createSet(3)

        // https://tutorialwing.com/android-spinner-using-kotlin-with-example/
        // Populates the Spinner and ties a function to it.
        val spinword = findViewById<Spinner>(R.id.spinnerwordtype)
        val spinunit = findViewById<Spinner>(R.id.spinnerunit)

        // Spinner Action
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
                    vc.filterVocabList(
                        spinword.selectedItem.toString(),
                        spinunit.selectedItem.toString()
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    println("NOTHING!!!")
                }
            }
            spinword.setSelection(wordTypes.indexOf("All"))
        }

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
                    vc.filterVocabList(
                        spinword.selectedItem.toString(),
                        spinunit.selectedItem.toString()
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    println("NOTHING!!!")
                }
            }
            spinunit.setSelection(units.indexOf("All"))
        }

        val nextbtn: Button = findViewById(R.id.nextbtn)
        nextbtn.setOnClickListener {

            if (nextbtn.text == "Start") {
                nextbtn.setText(getString(R.string.nextbtn_string))
            }

            vc.switchVocab()

            if (reverseLang) {
                if (vocab1.text.toString() != "") {
                    vocab1.setText("")
                }
                vocab2.setText(vc.vocab[1])
            } else {
                if (vocab2.text.toString() != "") {
                    vocab2.setText("")
                }
                vocab1.setText(vc.vocab[0])
            }
        }

        val showbtn: Button = findViewById(R.id.showbtn)
        showbtn.setOnClickListener {
            if (reverseLang) {
                vocab1.setText(vc.vocab[0])
            } else {
                vocab2.setText(vc.vocab[1])
            }

        }

        val lswitchbtn: Button = findViewById(R.id.lswitchbutton)
        lswitchbtn.setOnClickListener {
            reverseLang = !reverseLang

            if (reverseLang) {
                lswitchbtn.setText("<-")
            } else {
                lswitchbtn.setText("->")
            }
        }

        val quitbtn: Button = findViewById(R.id.quitbtn)
        quitbtn.setOnClickListener {
            this@MainActivity.finish()
            exitProcess(0)
        }
    }
}