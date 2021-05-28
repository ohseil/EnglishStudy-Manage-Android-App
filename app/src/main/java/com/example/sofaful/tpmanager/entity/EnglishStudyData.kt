package com.example.sofaful.tpmanager.entity

/**
 * Created by Seil on 2020-11-03.
 */
class EnglishStudyData {

    private var id: Long? = null

    private var categorycode: Long? = null

    private var question: String? = null

    private var answer: String? = null

    constructor (id: String, categorycode: String, question: String, answer: String) {
        this.id = id.toLong()
        this.categorycode = categorycode.toLong()
        this.question = question
        this.answer = answer
    }

    fun getId(): Long? {return id}
    fun getCategoryCode(): Long? {return categorycode}
    fun getQuestion(): String? {return question}
    fun getAnswer(): String? {return answer}
}