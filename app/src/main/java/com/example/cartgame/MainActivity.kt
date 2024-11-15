package com.example.cartgame

import Card
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var cards: List<Card>
    private var firstSelectedCard: Card? = null
    private var secondSelectedCard: Card? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setupGameBoard()
    }
    private fun setupGameBoard() {
        val numbers = (1..10).toList()
        val cardNumbers = (numbers + numbers).shuffled()

        cards = cardNumbers.mapIndexed { index, number -> Card(id = index, number = number) }

        val gameBoard = findViewById<GridLayout>(R.id.gameBoard)

        for (card in cards) {
            val cardView = createCardView(card)
            gameBoard.addView(cardView)
        }
    }

    private fun createCardView(card: Card): Button {
        val button = Button(this)
        button.layoutParams = GridLayout.LayoutParams().apply {
            width = 0
            height = GridLayout.LayoutParams.WRAP_CONTENT
            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        }

        button.text = "?"

        button.setOnClickListener {
            onCardClicked(card, button)
        }

        return button
    }

    private fun onCardClicked(card: Card, button: Button) {
        if (card.isMatched || card.isVisible) return

        if(firstSelectedCard != null && secondSelectedCard != null) return

        card.isVisible = true
        button.text = card.number.toString()

        if (firstSelectedCard == null) {
            firstSelectedCard = card
        } else {
            secondSelectedCard = card

            if (firstSelectedCard?.number == secondSelectedCard?.number) {
                firstSelectedCard?.isMatched = true
                firstSelectedCard?.let {
                    val btn = (findViewById<GridLayout>(R.id.gameBoard)).getChildAt(it.id) as Button
                    btn.setBackgroundColor(10)
                }

                secondSelectedCard?.isMatched = true
                secondSelectedCard?.let {
                    val btn = (findViewById<GridLayout>(R.id.gameBoard)).getChildAt(it.id) as Button
                    btn.setBackgroundColor(10)
                }
                firstSelectedCard = null
                secondSelectedCard = null
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    firstSelectedCard?.let {
                        it.isVisible = false
                        val firstButton = (findViewById<GridLayout>(R.id.gameBoard)).getChildAt(it.id) as Button
                        firstButton.text = "?"
                    }

                    secondSelectedCard?.let {
                        it.isVisible = false
                        val secondButton = (findViewById<GridLayout>(R.id.gameBoard)).getChildAt(it.id) as Button
                        secondButton.text = "?"
                    }

                    firstSelectedCard = null
                    secondSelectedCard = null
                }, 1000)
            }
        }

        if (cards.all { it.isMatched }) {
            Toast.makeText(this, "You Win!", Toast.LENGTH_SHORT).show()
        }
    }
}