package layout

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rsschool.quiz.QuestionAnswer
import com.rsschool.quiz.R
import com.rsschool.quiz.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private var answer: Byte? = null
    private var questionsArray: Array<String>? = null
    private var listenerFragment: ListenerFragment? = null
    private var currentFragment: Int = 0
    private var currentAnswer: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        questionsArray = arguments?.getStringArray(QUESTION_KEY)
        answer = arguments?.getByte(ANSWER_KEY)
        currentFragment = arguments?.getInt(COUNT_FRAGMENT_KEY) ?: 0

        binding.question.text = QuestionAnswer.getTitleQuestion(currentFragment)
        binding.previousButton.isEnabled = currentFragment != 0
        binding.nextButton.isEnabled = binding.radioGroup.checkedRadioButtonId != -1
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            binding.nextButton.isEnabled = group.checkedRadioButtonId != -1
            when (checkedId) {
                R.id.option_one -> currentAnswer = 1
                R.id.option_two -> currentAnswer = 2
                R.id.option_three -> currentAnswer = 3
                R.id.option_four -> currentAnswer = 4
                R.id.option_five -> currentAnswer = 5
            }
        }
        if (QuestionAnswer.getMaxSize() == currentFragment + 1) {
            binding.nextButton.text = "Submit"
        }

        binding.toolbar.title = "Question ${currentFragment + 1}"
        binding.toolbar.setNavigationOnClickListener {
            --currentFragment
            listenerFragment?.previousQuiz()
        }

        questionsArray?.forEachIndexed { index, s ->
            when (index) {
                0 -> binding.optionOne.text = s
                1 -> binding.optionTwo.text = s
                2 -> binding.optionThree.text = s
                3 -> binding.optionFour.text = s
                4 -> binding.optionFive.text = s
            }
        }

        binding.nextButton.setOnClickListener {
            ++currentFragment
            if(currentFragment == 5) listenerFragment?.finishQuiz(currentFragment, currentAnswer!!)
            else{
                currentAnswer?.let { it1 ->
                    listenerFragment?.nextQuiz(
                        QuestionAnswer.getQuestion(currentFragment),
                        QuestionAnswer.getAnswer(currentFragment),
                        currentFragment,
                        it1
                    )
                }
            }
        }

        binding.previousButton.setOnClickListener {
            --currentFragment
            listenerFragment?.previousQuiz()
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listenerFragment = activity as ListenerFragment
    }


    interface ListenerFragment {
        fun nextQuiz(question: Array<String>, answer: Int, countFragment: Int, currentAnswer: Int)
        fun previousQuiz()
        fun finishQuiz(countFragment: Int, currentAnswer: Int)
    }

    companion object {

        @JvmStatic
        fun newInstance(question: Array<String>, answer: Int, currentFragment: Int): QuizFragment {
            val fragment = QuizFragment()
            val args = Bundle()
            args.putStringArray(QUESTION_KEY, question)
            args.putInt(ANSWER_KEY, answer)
            args.putInt(COUNT_FRAGMENT_KEY, currentFragment)
            fragment.arguments = args
            return fragment
        }

        private const val QUESTION_KEY = "QUESTION_KEY"
        private const val ANSWER_KEY = "ANSWER_KEY"
        private const val COUNT_FRAGMENT_KEY = "COUNT_FRAGMENT_KEY"

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


}