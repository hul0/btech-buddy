package io.github.hul0.btechbuddy.data.model

data class FaqCategory(
    val category: String,
    val faqs: List<Faq>
)

data class Faq(
    val id: String,
    val question: String,
    val answer: String
)
