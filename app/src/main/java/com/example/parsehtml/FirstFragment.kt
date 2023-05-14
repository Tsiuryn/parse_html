package com.example.parsehtml

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.parsehtml.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    val html = """
        <p style="text-align:left;"><span style="color: rgb(255,0,0);font-size: 14px;"><strong>Имеется</strong></span><strong> </strong><span style="color: rgb(67,28,249);font-size: 18px;"><em>активный</em></span> <span style="color: rgb(76,255,63);font-size: 11px;"><strong><em>пакет</em></strong></span> <span style="color: rgb(188,13,212);font-size: 30px;"><strong><em><ins>услуг</ins></em></strong></span></p>
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