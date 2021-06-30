package com.rsschool.quiz

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentResultBinding

class QuizFinish: Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private var listenerFragment: ListenerFinishFragment? = null
    private lateinit var userAnswer: IntArray
    private lateinit var answer: IntArray


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userAnswer = arguments?.getIntArray(USER_ANSWERS)!!
        answer = arguments?.getIntArray(ANSWERS)!!

        var countRightAnswer = 0
        for(i in userAnswer.indices){
            if(answer[i] == userAnswer[i]) ++countRightAnswer
        }
        val procent: Double = ((countRightAnswer * 100) / answer.size).toDouble()

        binding.textResult.text = "Your result: $procent %"


        binding.imgShare.setOnClickListener {
            var textResult = ""
            textResult = binding.textResult.text.toString() +"\n"
            for (i in userAnswer.indices){
                val answer = QuestionAnswer.getQuestion(i)
                val question = QuestionAnswer.getTitleQuestion(i)
                val textUserAnswer = userAnswer[i] - 1
                textResult += "${i+1}) $question \n" + "Your answer: ${answer[textUserAnswer]} \n"
            }
            listenerFragment?.sendResult(textResult)
        }
        binding.imgBack.setOnClickListener {
            listenerFragment?.backQuiz()
        }
        binding.imgClose.setOnClickListener {
            listenerFragment?.closeQuiz()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listenerFragment = activity as ListenerFinishFragment
    }

    interface ListenerFinishFragment {
        fun sendResult(textResult: String)
        fun backQuiz()
        fun closeQuiz()
    }

    companion object {

        @JvmStatic
        fun newInstance(userAnswer: IntArray, answer: IntArray): QuizFinish {
            val fragment = QuizFinish()
            val args = Bundle()
            args.putIntArray(USER_ANSWERS, userAnswer)
            args.putIntArray(ANSWERS, answer)
            fragment.arguments = args
            return fragment
        }

        private const val USER_ANSWERS = "USER_ANSWERS"
        private const val ANSWERS = "ANSWERS"
    }

}