package com.example.parsehtml

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.parsehtml.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

//    val html = """
//        <p style="text-align:left;"><span style="color: rgb(255,0,0);font-size: 14px;"><strong>Имеется</strong></span><strong> </strong><span style="color: rgb(67,28,249);font-size: 18px;"><em>активный</em></span> <span style="color: rgb(76,255,63);font-size: 11px;"><strong><em>пакет</em></strong></span> <span style="color: rgb(188,13,212);font-size: 30px;"><strong><em><ins>услуг</ins></em></strong></span></p>
//    """

//    val html = """
//        <p style="text-align:left;"><span style="color: rgb(255,0,0);font-size: 14px;">Имеется</span> <span style="color: rgb(67,28,249);font-size: 18px;"><em>активный</em></span> <span style="color: rgb(76,255,63);font-size: 11px;"><strong><em>пакет</em></strong></span> <span style="color: rgb(188,13,212);font-size: 30px;"><strong><em><ins>услуг</ins></em></strong></span></p>
//<p></p>
//<ol>
//<li><span style="color: #cd1fe6;font-size: 16px;">first</span></li>
//<li><span style="color: #cd1fe6;font-size: 16px;">second</span></li>
//<li><span style="color: #cd1fe6;font-size: 16px;">third</span></li>
//</ol>
//<p></p>
//    """

//    val html = """
//        <p style="text-align:left;"><span style="color: rgb(255,0,0);font-size: 14px;">Имеется</span> <span style="color: rgb(67,28,249);font-size: 18px;"><em>активный</em></span> <span style="color: rgb(76,255,63);font-size: 11px;"><strong><em>пакет</em></strong></span> <span style="color: rgb(188,13,212);font-size: 30px;"><strong><em><ins>услуг</ins></em></strong></span></p>
//<p></p>
//<ul>
//<li><span style="color: rgb(205,31,230);font-size: 18px;">first</span></li>
//<li><span style="color: rgb(205,31,230);font-size: 16px;">second</span></li>
//<li><span style="color: rgb(205,31,230);font-size: 16px;">third</span></li>
//</ul>
//<p></p>
//    """

//    val html = """
//    <p style="text-align:left;"><span style="color: rgb(255,0,0);font-size: 14px;">Имеется</span> <span style="color: rgb(67,28,249);font-size: 18px;"><em>активный</em></span> <span style="color: rgb(76,255,63);font-size: 11px;"><strong><em>пакет</em></strong></span> <span style="color: rgb(188,13,212);font-size: 30px;"><strong><em><ins>услуг</ins></em></strong></span></p>
//<p><a href="https://translate.google.by/?sl=ru&tl=en&text=%D0%BA%D0%B0%D1%80%D1%82%D0%B0%20%D0%BD%D0%B5%D0%BE%D0%B1%D1%80%D0%B0%D0%B1%D0%B0%D1%82%D1%8B%D0%B2%D0%B0%D0%B5%D0%BC%D1%8B%D1%85%20%D1%82%D1%8D%D0%B3%D0%BE%D0%B2&op=translate" target="_blank"><span style="color: rgb(205,31,230);font-size: 16px;">translator</span></a>&nbsp;</p>
//<p></p>
//    """
//
//    val html = """
//        <p style="text-align:right;"><span style="color: rgb(255,0,0);font-size: 14px;">Имеется</span> <span style="color: rgb(67,28,249);font-size: 18px;"><em>активный</em></span> <span style="color: rgb(76,255,63);font-size: 11px;"><strong><em>пакет</em></strong></span> <span style="color: rgb(188,13,212);font-size: 30px;"><strong><em><ins>услуг</ins></em></strong></span></p>
//    """

    val html = """
        <p><span style="color: rgb(47,241,50);">Редактор </span><span style="color: #f12f5a;"><strong>форматированного</strong></span><span style="color: rgb(47,241,50);"> текста</span></p>
    """

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textviewFirst.text = GenHtmlCompat.fromHtml(html)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}