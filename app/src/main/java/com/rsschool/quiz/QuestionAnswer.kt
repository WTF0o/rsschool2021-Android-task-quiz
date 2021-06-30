package com.rsschool.quiz

class QuestionAnswer(){
    companion object{
        private val answer = arrayOf("Тест1","Тест2","Тест3","Тест4","Тест5","Тест6","Тест7","Тест8","Тест9",
            "Тест10","Тест11","Тест12","Тест13","Тест14","Тест15","Тест16","Тест17","Тест18","Тест19",
            "Тест20","Тест21","Тест22","Тест23","Тест24","Тест25")
        private val currentAnswer = arrayOf(1, 2, 3, 4, 5)
        private val question = arrayOf("Тест1", "Тест2", "Тест3", "Тест4", "Тест5")

        fun getQuestion(countFragment: Int) = answer.copyOfRange(countFragment * 5, countFragment * 5 + 5)
        fun getAnswer(countFragment: Int) = currentAnswer[countFragment]
        fun getMaxSize() = answer.size / 5
        fun getAnswers() = currentAnswer
        fun getTitleQuestion(countFragment: Int) = question[countFragment]
    }
}