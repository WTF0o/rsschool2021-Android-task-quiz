package com.rsschool.quiz

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.ListFragment
import com.rsschool.quiz.databinding.ActivityMainBinding
import layout.QuizFragment

class MainActivity : AppCompatActivity(), QuizFragment.ListenerFragment,
    QuizFinish.ListenerFinishFragment {
    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityMainBinding
    private var currentFragment: Int = 0
    private var userAnswers = arrayListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)

        if (savedInstanceState == null) {
            setTheme()
            fragmentManager = supportFragmentManager
            val fragment: QuizFragment = QuizFragment.newInstance(
                QuestionAnswer.getQuestion(currentFragment),
                QuestionAnswer.getAnswer(currentFragment),
                currentFragment
            )
            fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()
            ++currentFragment
        }
    }

    override fun nextQuiz(
        question: Array<String>,
        answer: Int,
        _currentFragment: Int,
        userAnswer: Int
    ) {
        currentFragment = _currentFragment
        if (currentFragment - 1 > userAnswers.size - 1)
            userAnswers.add(userAnswer)
        else userAnswers[currentFragment - 1] = userAnswer

        val fragment: QuizFragment = QuizFragment.newInstance(question, answer, currentFragment)
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null)
            .commit()

        setTheme()

    }

    override fun finishQuiz(_currentFragment: Int, userAnswer: Int) {

        currentFragment = _currentFragment

        if (currentFragment - 1 > userAnswers.size - 1)
            userAnswers.add(userAnswer)
        else userAnswers[currentFragment - 1] = userAnswer

        val fragment: QuizFinish = QuizFinish.newInstance(
            userAnswers.toIntArray(),
            QuestionAnswer.getAnswers().toIntArray()
        )
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null)
            .commit()
    }

    override fun previousQuiz() {
        fragmentManager.popBackStack()
        --currentFragment
    }

    override fun sendResult(textResult: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textResult)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun backQuiz() {

        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        currentFragment = 0
        val fragment: QuizFragment = QuizFragment.newInstance(
            QuestionAnswer.getQuestion(currentFragment),
            QuestionAnswer.getAnswer(currentFragment),
            currentFragment
        )
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()
        ++currentFragment
    }

    private fun setTheme() {

        when (currentFragment) {
            0 -> {
                setTheme(R.style.Theme_Quiz_First)
            }
            1 -> {
                setTheme(R.style.Theme_Quiz_Second)
            }
            2 -> {
                setTheme(R.style.Theme_Quiz_Third)
            }
            3 -> {
                setTheme(R.style.Theme_Quiz_Forth)
            }
            4 -> {
                setTheme(R.style.Theme_Quiz_Fifth)
            }
        }
        window.statusBarColor = getThemeColor(android.R.attr.statusBarColor)
    }

    fun Context.getThemeColor(@AttrRes attrRes: Int): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute (attrRes, typedValue, true)
        return typedValue.data
    }

    override fun closeQuiz() {
        finish()
    }

}