package com.example.vocabularytrainer

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

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // import the baseline
        val basevocabulary: List<List<String>> = readVocabCSV()
        // working vocabulary ready to be filtered
        var vocablist: List<List<String>> = basevocabulary.drop(1)
        // Headings to be used to populate GUI
        val headers: List<String> = basevocabulary[0]

        // Populate the GUI
        val lang1: TextView = findViewById(R.id.textViewLang1)
        lang1.text = headers[0]
        val lang1a: TextView = findViewById(R.id.textViewLang1a)
        lang1a.text = headers[0]

        val lang2: TextView = findViewById(R.id.textViewLang2)
        lang2.text = headers[1]
        val lang2a: TextView = findViewById(R.id.textViewLang2a)
        lang2a.text = headers[1]

        // Create set of all wordtypes
        var wordTypes: List<String> = create_set(vocablist, 2)

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
        var units: List<String> = create_set(vocablist, 3)

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
        println(headers)
        println(wordTypes)
        println(units)
        println("================")
        println("================")
        println("================")

        }

    fun create_set(base: List<List<String>>, index: Int): List<String>{
        // takes val basevocabulary, and based on index of the nested list item creates a set
        // of unique values
        val valueSet = mutableSetOf<String>("All")

        for (item in base){
            valueSet.add(item[index])
        }
        // `it` selects to sort ascending
        return valueSet.sortedBy { it }
    }

    fun quitApp(view: View) {
        this@MainActivity.finish()
        exitProcess(0)
    }

    fun readVocabCSV(): List<List<String>> {
        return csvReader{delimiter = ';'}.readAll(assets.open("vocab.csv"))
    }
}